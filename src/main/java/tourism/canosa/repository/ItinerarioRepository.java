package tourism.canosa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tourism.canosa.model.Itinerario;

public interface ItinerarioRepository extends JpaRepository <Itinerario, Long> {

}
