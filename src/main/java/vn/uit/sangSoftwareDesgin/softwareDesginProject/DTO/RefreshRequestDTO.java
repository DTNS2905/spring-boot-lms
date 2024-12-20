package vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor // Ensures a no-args constructor is generated
public class RefreshRequestDTO {
    private String refreshToken;

}
