
package tourism.canosa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourism.canosa.model.Prenotazione;
import tourism.canosa.repository.PrenotazioneRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PrenotazioneService {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    // Tutte le prenotazioni
    public List<Prenotazione> getAll() {
        return prenotazioneRepository.findAll();
    }

    // Singola prenotazione
    public Optional<Prenotazione> getById(Long id) {
        return prenotazioneRepository.findById(id);
    }

    // Creazione prenotazione
    public Prenotazione create(Prenotazione prenotazione) {
        return prenotazioneRepository.save(prenotazione);
    }

    // Aggiorna prenotazione
    public Prenotazione update(Prenotazione prenotazione) {
        return prenotazioneRepository.save(prenotazione);
    }

    // Elimina prenotazione
    public void delete(Long id) {
        prenotazioneRepository.deleteById(id);
    }

    // Prenotazioni di un tour specifico
    public List<Prenotazione> getByItinerarioId(Long ItinerarioId) {
        return prenotazioneRepository.findByItinerarioId(ItinerarioId);
    }
}
