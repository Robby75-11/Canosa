package tourism.canosa.model;

import jakarta.persistence.*;
import lombok.Data;
import tourism.canosa.enumeration.Ruolo;

import java.util.List;

@Entity
@Data
@Table(name="users")
public class User {
    @Id
    @GeneratedValue
    private  Long id;
    private String nome;
    private String cognome;
    private String username;
    private String email;
    private String password;

@Enumerated(EnumType.STRING)
    private Ruolo ruolo;

    // 🔹 Relazione con Prenotazioni
    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prenotazione> prenotazioni;

}
