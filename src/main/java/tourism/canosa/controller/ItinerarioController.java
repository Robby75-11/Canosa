package tourism.canosa.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    public ItinerarioResponseDto create(@RequestPart("request") ItinerarioRequestDto request,
                                        @RequestPart("immagini") List<MultipartFile> immagini) {
        return itinerarioService.create(request, immagini);
    }

    @PutMapping("/{id}")
    public ItinerarioResponseDto update(@PathVariable Long id,
                                        @RequestPart("request") ItinerarioRequestDto request,
                                        @RequestPart(value = "immagini", required = false) List<MultipartFile> immagini) {
        return itinerarioService.update(id, request, immagini);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        itinerarioService.delete(id);
        return "Itinerario eliminato ID: " + id;
    }
}
