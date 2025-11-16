package tourism.canosa.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="prodotto_tipico")
public class ProdottoTipico {
    @Id
    @GeneratedValue
    private Long id;
    private String nome;
    private String categoria; // es. vino, olio, dolci, formaggi

    @Column(length = 1000)
    private String descrizione;

    private Double prezzo;

    @ElementCollection
    private List<String> immagini = new ArrayList<>();

    private String origine; // es. "Canosa di Puglia"


}
