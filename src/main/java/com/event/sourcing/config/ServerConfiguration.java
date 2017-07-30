package com.event.sourcing.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class ServerConfiguration {

    @Value("${event.logger.file}")
    private String logFile;

    public File getLogFile() {
        return new File(logFile);
    }
}
