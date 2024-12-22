package vn.uit.sangSoftwareDesgin.softwareDesginProject.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


public interface CloudinaryService {
    String uploadFile(MultipartFile file, String folderName);

}
