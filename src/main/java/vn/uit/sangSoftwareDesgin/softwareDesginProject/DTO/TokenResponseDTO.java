package vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenResponseDTO {
    private String refreshToken;
    private String accessToken;

    public TokenResponseDTO(String accessToken, String refreshToken) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }
}
