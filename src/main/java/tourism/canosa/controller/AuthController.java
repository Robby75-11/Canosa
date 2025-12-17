package tourism.canosa.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tourism.canosa.dto.RegisterRequestDto;
import tourism.canosa.enumeration.Ruolo;
import tourism.canosa.model.User;
import tourism.canosa.repository.UserRepository;
import tourism.canosa.security.JwtUtil;
import tourism.canosa.service.CustomUserDetailsService;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authManager,
                          CustomUserDetailsService userDetailsService,
                          JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    // 🔹 Registrazione utente
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequestDto request) {

        Optional<User> existing = userRepository.findByEmail(request.getEmail());
        if (existing.isPresent()) {
            return "Email già registrata";
        }

        User user = new User();
        user.setNome(request.getNome());
        user.setCognome(request.getCognome());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // assegna ruolo di default se non specificato
        if (request.getRuolo() != null) {
            user.setRuolo(request.getRuolo());
        } else {
            user.setRuolo(Ruolo.USER);  // ✅ ruolo di default
        }
        userRepository.save(user);
        return "Registrazione avvenuta con successo";
    }

    // 🔹 Login con generazione JWT
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        return jwt;
    }
}
