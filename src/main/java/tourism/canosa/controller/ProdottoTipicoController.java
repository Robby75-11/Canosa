package tourism.canosa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tourism.canosa.dto.ProdottoTipicoRequestDto;
import tourism.canosa.dto.ProdottoTipicoResponseDto;
import tourism.canosa.service.ProdottoTipicoService;

import java.util.List;

@RestController
@RequestMapping("/prodotti")
public class ProdottoTipicoController {

    @Autowired
    private ProdottoTipicoService prodottoTipicoService;

    @GetMapping
    public List<ProdottoTipicoResponseDto> getAll() {
        return prodottoTipicoService.getAll();
    }

    @GetMapping("/{id}")
    public ProdottoTipicoResponseDto getById(@PathVariable Long id) {
        return prodottoTipicoService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ProdottoTipicoResponseDto create(@RequestPart("request") ProdottoTipicoRequestDto request,
                                            @RequestPart("immagini") List<MultipartFile> immagini) {
        return prodottoTipicoService.create(request, immagini);
    }

    @PutMapping("/{id}")
    public ProdottoTipicoResponseDto update(@PathVariable Long id,
                                            @RequestPart("request") ProdottoTipicoRequestDto request,
                                            @RequestPart(value = "immagini", required = false) List<MultipartFile> immagini) {
        return prodottoTipicoService.update(id, request, immagini);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        prodottoTipicoService.delete(id);
        return "Prodotto tipico eliminato ID: " + id;
    }
}
