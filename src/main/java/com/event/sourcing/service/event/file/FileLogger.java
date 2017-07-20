package com.event.sourcing.service.event.file;

import com.event.sourcing.event.Event;
import com.event.sourcing.event.EventPayload;
import com.event.sourcing.service.event.EventLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

public class FileLogger implements EventLogger {
    private static final String LOG_SEPARATOR = "\n";

    private final ObjectMapper objectMapper;
    private final File file;

    public FileLogger(final ObjectMapper objectMapper, final String pathToFile) {
        this.objectMapper = objectMapper;
        this.file = new File(pathToFile);
    }

    @Override
    public void logEvent(final EventPayload object) {
        try {
            String valueAsString = objectMapper.writeValueAsString(Event.of(object, LocalDateTime.now()));
            writeLine(valueAsString + LOG_SEPARATOR);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private synchronized void writeLine(final String line) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file, true)) {
            fileOutputStream.write(line.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
