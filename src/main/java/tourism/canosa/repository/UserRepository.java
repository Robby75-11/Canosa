package tourism.canosa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tourism.canosa.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long> {
    Optional<User> findByEmail(String Email);; // per login
}
