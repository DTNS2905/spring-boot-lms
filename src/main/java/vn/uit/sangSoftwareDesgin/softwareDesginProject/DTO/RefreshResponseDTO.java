package vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RefreshResponseDTO {
        private String accessToken;

        public RefreshResponseDTO(String refreshToken) {
            this.accessToken = refreshToken;
        }
}
