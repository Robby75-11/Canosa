package tourism.canosa.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tourism.canosa.dto.ProdottoTipicoRequestDto;
import tourism.canosa.dto.ProdottoTipicoResponseDto;
import tourism.canosa.model.ProdottoTipico;
import tourism.canosa.repository.ProdottoTipicoRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProdottoTipicoService {

    @Autowired
    private ProdottoTipicoRepository prodottoTipicoRepository;

    @Autowired
    private Cloudinary cloudinary; // configurato come bean

    // 🔹 Tutti i prodotti
    public List<ProdottoTipicoResponseDto> getAll() {
        return prodottoTipicoRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public ProdottoTipicoResponseDto getById(Long id) {
        ProdottoTipico p = prodottoTipicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prodotto tipico non trovato"));
        return toResponseDto(p);
    }

    // 🔹 Creazione prodotto (JSON + eventuali immagini)
    public ProdottoTipicoResponseDto create(ProdottoTipicoRequestDto request, List<MultipartFile> immagini) {
        ProdottoTipico p = new ProdottoTipico();
        p.setNome(request.getNome());
        p.setCategoria(request.getCategoria());
        p.setDescrizione(request.getDescrizione());
        p.setPrezzo(request.getPrezzo());
        p.setOrigine(request.getOrigine());

        // upload immagini (gestione null)
        List<String> immaginiUrl = uploadImages(immagini);
        p.setImmagini(immaginiUrl);

        ProdottoTipico saved = prodottoTipicoRepository.save(p);
        return toResponseDto(saved);
    }

    // 🔹 Aggiornamento prodotto (JSON + eventuali nuove immagini)
    public ProdottoTipicoResponseDto update(Long id, ProdottoTipicoRequestDto request, List<MultipartFile> immagini) {
        ProdottoTipico p = prodottoTipicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prodotto tipico non trovato"));

        p.setNome(request.getNome());
        p.setCategoria(request.getCategoria());
        p.setDescrizione(request.getDescrizione());
        p.setPrezzo(request.getPrezzo());
        p.setOrigine(request.getOrigine());

        // aggiungi immagini se presenti
        if (immagini != null && !immagini.isEmpty()) {
            List<String> immaginiUrl = uploadImages(immagini);
            p.getImmagini().addAll(immaginiUrl);
        }

        ProdottoTipico saved = prodottoTipicoRepository.save(p);
        return toResponseDto(saved);
    }

    // 🔹 Aggiunta immagini a prodotto esistente (PATCH)
    public ProdottoTipicoResponseDto addImages(Long id, List<MultipartFile> immagini) {
        if (immagini == null || immagini.isEmpty()) {
            return getById(id); // niente da aggiungere
        }

        ProdottoTipico p = prodottoTipicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prodotto tipico non trovato"));

        List<String> immaginiUrl = uploadImages(immagini);
        p.getImmagini().addAll(immaginiUrl);

        ProdottoTipico saved = prodottoTipicoRepository.save(p);
        return toResponseDto(saved);
    }

    public void delete(Long id) {
        prodottoTipicoRepository.deleteById(id);
    }

    // 🔹 Converti entity in ResponseDto
    private ProdottoTipicoResponseDto toResponseDto(ProdottoTipico p) {
        ProdottoTipicoResponseDto dto = new ProdottoTipicoResponseDto();
        dto.setId(p.getId());
        dto.setNome(p.getNome());
        dto.setCategoria(p.getCategoria());
        dto.setDescrizione(p.getDescrizione());
        dto.setPrezzo(p.getPrezzo());
        dto.setOrigine(p.getOrigine());
        dto.setImmagini(p.getImmagini());
        return dto;
    }

    // 🔹 Upload immagini su Cloudinary (gestione null)
    private List<String> uploadImages(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                urls.add(uploadResult.get("secure_url").toString());
            } catch (IOException e) {
                throw new RuntimeException("Errore caricamento immagine: " + file.getOriginalFilename(), e);
            }
        }
        return urls;
    }
}
