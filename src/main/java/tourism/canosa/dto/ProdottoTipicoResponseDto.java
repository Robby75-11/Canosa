package tourism.canosa.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProdottoTipicoResponseDto {
    private Long id;
    private String nome;
    private String categoria;
    private String descrizione;
    private Double prezzo;
    private List<String> immagini;
    private String origine;
}
