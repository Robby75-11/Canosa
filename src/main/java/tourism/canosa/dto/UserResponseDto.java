package tourism.canosa.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import tourism.canosa.enumeration.Ruolo;

@Data
public class UserResponseDto {
    private Long id;
    private String nome;
    private String cognome;
    private String username;
    private String email;
    private Ruolo ruolo;
}
