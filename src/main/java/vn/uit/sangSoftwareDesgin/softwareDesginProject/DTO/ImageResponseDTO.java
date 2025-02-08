package vn.uit.sangSoftwareDesgin.softwareDesginProject.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponseDTO {

    private UUID id;

    private String url;

    private String name;
}
