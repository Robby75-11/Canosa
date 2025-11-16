package tourism.canosa.dto;

import lombok.Data;
import tourism.canosa.enumeration.Ruolo;

@Data
public class UserRequestDto {
    private String nome;
    private String cognome;
    private String username;
    private String email;
    private String password;
    private Ruolo ruolo;
}
