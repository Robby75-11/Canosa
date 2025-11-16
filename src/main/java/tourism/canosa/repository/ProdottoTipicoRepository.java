package tourism.canosa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tourism.canosa.model.ProdottoTipico;

import java.util.List;

public interface ProdottoTipicoRepository extends JpaRepository<ProdottoTipico, Long> {
    // Ricerca per categoria
    List<ProdottoTipico> findByCategoria(String categoria);

    }
