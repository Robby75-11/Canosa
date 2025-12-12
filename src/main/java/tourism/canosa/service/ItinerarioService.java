package tourism.canosa.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tourism.canosa.dto.ItinerarioRequestDto;
import tourism.canosa.dto.ItinerarioResponseDto;
import tourism.canosa.model.Itinerario;
import tourism.canosa.repository.ItinerarioRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ItinerarioService {

    @Autowired
    private ItinerarioRepository itinerarioRepository;

    @Autowired
    private Cloudinary cloudinary;

    // Tutti gli itinerari
    public List<ItinerarioResponseDto> getAllItinerari() {
        return itinerarioRepository.findAll().stream().map(this::toResponseDto).collect(Collectors.toList());
    }

    public ItinerarioResponseDto getItinerarioById(Long id) {
        Itinerario it = itinerarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Itinerario non trovato"));
        return toResponseDto(it);
    }

    // Creazione solo JSON
    public ItinerarioResponseDto create(ItinerarioRequestDto request) {
        Itinerario it = new Itinerario();
        it.setTitolo(request.getTitolo());
        it.setDescrizione(request.getDescrizione());
        it.setPercorso(request.getPercorso());
        it.setTipo(request.getTipo());
        it.setImmagini(new ArrayList<>()); // inizializza vuoto051175

        Itinerario saved = itinerarioRepository.save(it);
        return toResponseDto(saved);
    }

    // Upload immagini (PATCH)
    public ItinerarioResponseDto addImages(Long id, List<MultipartFile> immagini) {
        if (immagini == null || immagini.isEmpty()) throw new RuntimeException("Nessuna immagine fornita");

        Itinerario it = itinerarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Itinerario non trovato"));

        List<String> newImages = uploadImages(immagini);
        if (it.getImmagini() == null) it.setImmagini(new ArrayList<>());
        it.getImmagini().addAll(newImages);

        Itinerario saved = itinerarioRepository.save(it);
        return toResponseDto(saved);
    }

    public void delete(Long id) {
        itinerarioRepository.deleteById(id);
    }

    private ItinerarioResponseDto toResponseDto(Itinerario it) {
        ItinerarioResponseDto dto = new ItinerarioResponseDto();
        dto.setId(it.getId());
        dto.setTitolo(it.getTitolo());
        dto.setDescrizione(it.getDescrizione());
        dto.setPercorso(it.getPercorso());
        dto.setTipo(it.getTipo());
        dto.setImmagini(it.getImmagini());
        return dto;
    }

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
