package com.event.sourcing.test.config;

import com.event.sourcing.config.ServerConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.io.File;

@Configuration
@Profile("logFilePresent")
public class PopulatedLogFileTestConfig {
    private static final String pathToFile = "src/test/resources/populatedFile.log";

    @Bean
    @Primary
    public ServerConfiguration serverConfiguration () {
        return new ServerConfiguration() {
            @Override
            public File getLogFile() {
                return new File(pathToFile);
            }
        };
    }
}
