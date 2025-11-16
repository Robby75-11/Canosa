package tourism.canosa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tourism.canosa.dto.RegisterRequestDto;
import tourism.canosa.enumeration.Ruolo;
import tourism.canosa.model.User;
import tourism.canosa.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUserById(Long id) {
        return  userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User non trovato con ID: " + id));
    }

    // 🔹 Tutti gli utenti
    public List<User> getAllUtenti() {
        return userRepository.findAll();
    }

    // 🔹 Utente per email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User non trovato con email: " + email));
    }
public User registerUser(RegisterRequestDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()){
            throw  new RuntimeException("Email già registrata");
        }
        User user = new User();
        user.setNome(request.getNome());
        user.setCognome(request.getCognome());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRuolo(request.getRuolo() != null ? request.getRuolo() : Ruolo.USER);
        return userRepository.save(user);
}

public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
}

}
