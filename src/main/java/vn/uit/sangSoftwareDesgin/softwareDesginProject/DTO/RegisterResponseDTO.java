package vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterResponseDTO {
    private RegisterRequestDTO userInfo;

    public RegisterResponseDTO(RegisterRequestDTO userInfo) {
        this.userInfo = userInfo;
    }
}
