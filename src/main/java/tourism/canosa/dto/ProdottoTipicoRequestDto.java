package tourism.canosa.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProdottoTipicoRequestDto {
    private String nome;
    private String categoria;
    private String descrizione;
    private Double prezzo;
    private List<String> immagini;
    private String origine;
}
