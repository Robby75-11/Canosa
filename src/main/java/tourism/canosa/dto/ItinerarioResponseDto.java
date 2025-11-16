package tourism.canosa.dto;

import lombok.Data;

import java.util.List;

@Data
public class ItinerarioResponseDto {
    private  Long id;
    private String titolo;
    private String descrizione;
    private String percorso;
    private String Tipo;
    private List<String> immagini;
}
