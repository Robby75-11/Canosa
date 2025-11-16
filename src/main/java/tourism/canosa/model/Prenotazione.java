package tourism.canosa.model;

import jakarta.persistence.*;
import lombok.Data;
import tourism.canosa.enumeration.StatoPrenotazione;

import java.time.LocalDate;

@Data
@Entity
@Table(name="prenotazione")
public class Prenotazione {
    @Id
    @GeneratedValue
    private Long id;

    private String nomeCliente; // aggiungere un campo per il cliente
    private LocalDate dataPrenotazione;
    private int numeroPersone;

    @Enumerated(EnumType.STRING)
    private StatoPrenotazione stato = StatoPrenotazione.IN_ATTESA;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private User utente;

    // 🔹 Relazione con Itinerario
    @ManyToOne
    @JoinColumn(name = "itinerario_id")
    private Itinerario itinerario;

}
