package tourism.canosa.dto;

import lombok.Data;

@Data
public class LoginResponseDto {
    private String token;
    private String email;
    private String username;
}
