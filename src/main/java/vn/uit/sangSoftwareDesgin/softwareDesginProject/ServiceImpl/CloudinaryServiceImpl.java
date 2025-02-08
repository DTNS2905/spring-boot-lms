package vn.uit.sangSoftwareDesgin.softwareDesginProject.ServiceImpl;

import com.cloudinary.Cloudinary;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.CloudinaryService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    @Resource
    private Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file, String folderName) {
        try {
            HashMap<Object, Object> options = new HashMap<>();
            options.put("folder", folderName);
            Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);
            String publicId = (String) uploadedFile.get("public_id");
            return cloudinary.url().secure(true).generate(publicId);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteFile(String imageUrl) {
        try {
            String publicId = extractPublicId(imageUrl);
            if (publicId == null) {
                return false;
            }
            Map result = cloudinary.uploader().destroy(publicId, Map.of());
            return "ok".equals(result.get("result"));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String extractPublicId(String imageUrl) {
        try {
            String[] parts = imageUrl.split("/");
            String publicIdWithExtension = parts[parts.length - 1]; // Get last part of URL
            return publicIdWithExtension.replaceAll("\\.[^.]+$", ""); // Remove file extension
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
