package tourism.canosa.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "itinerari")
@Data
public class Itinerario {
    @Id
    @GeneratedValue
    private  Long id;
    private String titolo;
    private String descrizione;
    private String percorso;
    private String tipo;

    @ElementCollection
    private List<String> immagini = new ArrayList<>();

    @OneToMany(mappedBy = "itinerario", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<Prenotazione> prenotazioni;
}
