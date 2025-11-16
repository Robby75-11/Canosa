package tourism.canosa.dto;

import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;

@Data
public class LoginDto {
    private String email;
    private String password;
}
