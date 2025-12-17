package tourism.canosa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tourism.canosa.dto.ItinerarioRequestDto;
import tourism.canosa.dto.ItinerarioResponseDto;
import tourism.canosa.service.ItinerarioService;

import java.util.List;

@RestController
@RequestMapping("/itinerari")
public class ItinerarioController {

    @Autowired
    private ItinerarioService itinerarioService;

    @GetMapping
    public List<ItinerarioResponseDto> getAll() {
        return itinerarioService.getAllItinerari();
    }

    @GetMapping("/{id}")
    public ItinerarioResponseDto getById(@PathVariable Long id) {
        return itinerarioService.getItinerarioById(id);
    }

    // Creazione solo JSON
    @PostMapping(consumes = "application/json")
    public ItinerarioResponseDto createJson(@RequestBody ItinerarioRequestDto request) {
        return itinerarioService.create(request);
    }

    // Aggiunta immagini (PATCH)
    @PatchMapping(path = "/{id}/immagini", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ItinerarioResponseDto uploadImages(@PathVariable Long id,
                                              @RequestPart("immagini") List<MultipartFile> immagini) {
        return itinerarioService.addImages(id, immagini);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        itinerarioService.delete(id);
        return "Itinerario eliminato ID: " + id;
    }
}
