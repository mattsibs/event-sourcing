package com.event.sourcing.service.event.file;

import com.event.sourcing.event.Event;
import com.event.sourcing.event.EventPayload;
import com.event.sourcing.exception.EventProcessingException;
import com.event.sourcing.proxy.EventfulBeanPostProcessor;
import com.event.sourcing.service.event.EventLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

public class FileLogger implements EventLogger {
    private static final Logger L = Logger.getLogger(EventfulBeanPostProcessor.class);
    private static final String LOG_SEPARATOR = "\n";

    private final ObjectMapper objectMapper;
    private final File file;

    public FileLogger(final ObjectMapper objectMapper, final File file) {
        this.objectMapper = objectMapper;
        this.file = file;
    }

    @Override
    public void logEvent(final EventPayload object) {
        try {
            String valueAsString = objectMapper.writeValueAsString(Event.of(object, LocalDateTime.now()));
            writeLine(valueAsString + LOG_SEPARATOR);
        } catch (JsonProcessingException e) {
            throw new EventProcessingException(String.format("Error writing %s to JSON", object), e);
        }
    }

    private synchronized void writeLine(final String line) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file, true)) {
            fileOutputStream.write(line.getBytes());
        } catch (IOException e) {
            throw new EventProcessingException(String.format("Error writing %s to file", line), e);
        }
    }
}
