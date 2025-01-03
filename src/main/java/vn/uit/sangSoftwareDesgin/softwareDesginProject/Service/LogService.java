package vn.uit.sangSoftwareDesgin.softwareDesginProject.Service;

import jakarta.validation.constraints.NotBlank;

public interface LogService {
    void info(String message);
    void warn(String message);
    void error(String message, Throwable throwable);

}
