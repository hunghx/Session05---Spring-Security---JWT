package api.example.com.springsecurityjwt.model.dto.response;

import api.example.com.springsecurityjwt.model.entity.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class JWTResponse {
    private final String type = "Bearer";
    private String accessToken ;
    private String fullName;
    private String email;
    private Collection<? extends GrantedAuthority> roleSet;
    private boolean status;
}

