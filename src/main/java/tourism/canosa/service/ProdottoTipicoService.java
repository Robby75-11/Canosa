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

    // Tutti i prodotti
    public List<ProdottoTipicoResponseDto> getAll() {
        return prodottoTipicoRepository.findAll().stream().map(this::toResponseDto).collect(Collectors.toList());
    }

    public ProdottoTipicoResponseDto getById(Long id) {
        ProdottoTipico p = prodottoTipicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prodotto tipico non trovato"));
        return toResponseDto(p);
    }

    // Creazione con upload immagini
    public ProdottoTipicoResponseDto create(ProdottoTipicoRequestDto request, List<MultipartFile> immagini) {
        ProdottoTipico p = new ProdottoTipico();
        p.setNome(request.getNome());
        p.setCategoria(request.getCategoria());
        p.setDescrizione(request.getDescrizione());
        p.setPrezzo(request.getPrezzo());
        p.setOrigine(request.getOrigine());

        // upload immagini
        List<String> immaginiUrl = uploadImages(immagini);
        p.setImmagini(immaginiUrl);

        ProdottoTipico saved = prodottoTipicoRepository.save(p);
        return toResponseDto(saved);
    }

    // Aggiornamento con eventuale nuove immagini
    public ProdottoTipicoResponseDto update(Long id, ProdottoTipicoRequestDto request, List<MultipartFile> immagini) {
        ProdottoTipico p = prodottoTipicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prodotto tipico non trovato"));

        p.setNome(request.getNome());
        p.setCategoria(request.getCategoria());
        p.setDescrizione(request.getDescrizione());
        p.setPrezzo(request.getPrezzo());
        p.setOrigine(request.getOrigine());

        if (immagini != null && !immagini.isEmpty()) {
            List<String> immaginiUrl = uploadImages(immagini);
            p.getImmagini().addAll(immaginiUrl);
        }

        ProdottoTipico saved = prodottoTipicoRepository.save(p);
        return toResponseDto(saved);
    }

    public void delete(Long id) {
        prodottoTipicoRepository.deleteById(id);
    }

    // 🔹 Utility: converte entity in ResponseDto
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

    // 🔹 Upload immagini su Cloudinary
    private List<String> uploadImages(List<MultipartFile> files) {
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
