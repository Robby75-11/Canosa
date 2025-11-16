package tourism.canosa.dto;

import lombok.Data;
import tourism.canosa.enumeration.StatoPrenotazione;

import java.time.LocalDate;

@Data
public class PrenotazioneRequestDto {

    private String nomeCliente; // aggiungere un campo per il cliente
    private LocalDate dataPrenotazione;
    private int numeroPersone;
    private Long utenteId;
    private Long itinerarioId;
}
