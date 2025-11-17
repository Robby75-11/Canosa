package tourism.canosa.dto;

import lombok.Data;
import tourism.canosa.enumeration.StatoPrenotazione;

import java.time.LocalDate;

@Data
public class PrenotazioneResponseDto {
    private Long id;

    private String nomeCliente; // aggiungere un campo per il cliente
    private LocalDate dataPrenotazione;
    private int numeroPersone;
    private StatoPrenotazione stato ;
    private Long userId;
    private Long itinerarioId;

}
