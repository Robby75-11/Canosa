package tourism.canosa.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tourism.canosa.service.CustomUserDetailsService;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;

    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    // Configurazione CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // frontend Vite
        configuration.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // se JWT in cookie
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // ✅ nuovo modo per disabilitare CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        // --- PRENOTAZIONI (QUI MANCAVA) ---
                        .requestMatchers(HttpMethod.GET, "/prenotazioni/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/prenotazioni/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/prenotazioni/**").hasRole("ADMIN")

                        // 🔒 Limita le operazioni di modifica (POST, PATCH, DELETE) solo a ADMIN
                        .requestMatchers(HttpMethod.GET, "/itinerari", "/itinerari/**").permitAll() // Accesso in lettura
                        .requestMatchers(HttpMethod.POST, "/itinerari/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/itinerari/**").hasAuthority("ADMIN") // <-- CORREZIONE CHIAVE
                        .requestMatchers(HttpMethod.DELETE, "/itinerari/**").hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/prodotti", "/prodotti/**").permitAll() // Accesso in lettura
                        .requestMatchers(HttpMethod.POST, "/prodotti/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/prodotti/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/prodotti/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                ) // ✅ nuovo modo per le autorizzazioni
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ); // ✅ nuovo modo per la gestione delle sessioni


        // Aggiungiamo il filtro JWT
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
