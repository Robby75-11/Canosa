package tourism.canosa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tourism.canosa.model.Prenotazione;

import java.util.List;

public interface PrenotazioneRepository extends JpaRepository <Prenotazione, Long> {
    // Prenotazione di un Tour specifico5551
    List<Prenotazione> findByItinerarioId(Long tourId);

    // Prenotazioni di uno stato specifico
    List<Prenotazione> findByStato(String stato);
}
