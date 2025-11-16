package tourism.canosa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tourism.canosa.dto.RegisterRequestDto;
import tourism.canosa.model.User;
import tourism.canosa.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 🔹 Lista di tutti gli utenti (solo ADMIN)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUtenti();
    }

    // 🔹 Utente per ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // 🔹 Registrazione nuovo utente
    @PostMapping("/register")
    public User registerUser(@RequestBody RegisterRequestDto request) {
        return userService.registerUser(request);
    }

    // 🔹 Eliminazione utente (solo ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
