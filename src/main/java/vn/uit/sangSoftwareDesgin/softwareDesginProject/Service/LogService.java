package vn.uit.sangSoftwareDesgin.softwareDesginProject.Service;

public interface LogService {
    void info(String message);
    void warn(String message);
    void error(String message, Throwable throwable);
}
