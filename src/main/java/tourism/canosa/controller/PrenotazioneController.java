package tourism.canosa.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import tourism.canosa.model.Prenotazione;
import tourism.canosa.service.PrenotazioneService;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final String TOPIC = "prenotazioni";

    // --- Tutte le prenotazioni ---
    @GetMapping
    public List<Prenotazione> getAll() {
        return prenotazioneService.getAll();
    }

    // --- Singola prenotazione ---
    @GetMapping("/{id}")
    public EntityModel<Prenotazione> getById(@PathVariable Long id) {
        Prenotazione p = prenotazioneService.getById(id)
                .orElseThrow(() -> new RuntimeException("Prenotazione non trovata"));

        // HATEOAS: aggiunge link alla lista e aggiornamento
        return EntityModel.of(p,
                linkTo(methodOn(PrenotazioneController.class).getAll()).withRel("tuttePrenotazioni"),
                linkTo(methodOn(PrenotazioneController.class).update(id, p)).withRel("aggiornaPrenotazione"));
    }

    // --- Crea prenotazione ---
    @PostMapping
    public EntityModel<Prenotazione> create(@RequestBody Prenotazione prenotazione) {
        Prenotazione saved = prenotazioneService.create(prenotazione);

        // Notifica Kafka
        kafkaTemplate.send(TOPIC, "Nuova prenotazione ID: " + saved.getId());

        // HATEOAS: aggiunge link alla prenotazione appena creata
        return EntityModel.of(saved,
                linkTo(methodOn(PrenotazioneController.class).getById(saved.getId())).withSelfRel(),
                linkTo(methodOn(PrenotazioneController.class).getAll()).withRel("tuttePrenotazioni"));
    }

    // --- Aggiorna prenotazione ---
    @PutMapping("/{id}")
    public Prenotazione update(@PathVariable Long id, @RequestBody Prenotazione prenotazione) {
        prenotazione.setId(id); // assicura che sia lo stesso ID
        return prenotazioneService.update(prenotazione);
    }

    // --- Elimina prenotazione ---
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        prenotazioneService.delete(id);
        return "Prenotazione eliminata ID: " + id;
    }
}
