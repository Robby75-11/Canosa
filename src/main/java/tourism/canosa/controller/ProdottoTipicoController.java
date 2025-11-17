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

    // CREAZIONE solo JSON (senza immagini)
    @PostMapping(consumes = "application/json")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ProdottoTipicoResponseDto create(@RequestBody ProdottoTipicoRequestDto prodotto) {
        return prodottoTipicoService.create(prodotto, null);
    }

    // AGGIUNTA immagini a prodotto esistente
    @PatchMapping(path = "/{id}/immagini", consumes = {"multipart/form-data"})
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ProdottoTipicoResponseDto uploadImages(
            @PathVariable Long id,
            @RequestPart("immagini") List<MultipartFile> immagini) {
        // Recuperiamo il prodotto senza modificare altri campi
        ProdottoTipicoResponseDto prodotto = prodottoTipicoService.getById(id);

        // Creiamo un DTO "vuoto" con valori correnti, utile per update del service
        ProdottoTipicoRequestDto dto = new ProdottoTipicoRequestDto();
        dto.setNome(prodotto.getNome());
        dto.setCategoria(prodotto.getCategoria());
        dto.setDescrizione(prodotto.getDescrizione());
        dto.setPrezzo(prodotto.getPrezzo());
        dto.setOrigine(prodotto.getOrigine());

        return prodottoTipicoService.update(id, dto, immagini);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String delete(@PathVariable Long id) {
        prodottoTipicoService.delete(id);
        return "Prodotto tipico eliminato ID: " + id;
    }
}
