package vn.uit.sangSoftwareDesgin.softwareDesginProject.ServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vn.uit.sangSoftwareDesgin.softwareDesginProject.Service.LogService;

@Service
public class LogServiceImpl implements LogService {

    private final Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void warn(String message) {
        logger.warn(message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }
}
