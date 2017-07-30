package com.event.sourcing.service.event.file;

import com.event.sourcing.event.Event;
import com.event.sourcing.exception.EventProcessingException;
import com.event.sourcing.service.event.EventHandler;
import com.event.sourcing.service.event.EventReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileLogReader extends EventReader<String> {
    private static final Logger L = Logger.getLogger(FileLogReader.class);


    private final ObjectMapper objectMapper;
    private final File file;

    public FileLogReader(final ObjectMapper objectMapper, final File file) {
        this.objectMapper = objectMapper;
        this.file = file;
    }

    @Override
    public void read(final EventHandler eventHandler) {

        try (FileReader fileReader = new FileReader(file)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            bufferedReader.lines()
                    .map(this::marshallEvent)
                    .forEach(eventHandler::handle);

        } catch (IOException e) {
            throw new EventProcessingException("Error reading from file " + file.getAbsolutePath(), e);
        }
    }

    @Override
    protected Event marshallEvent(final String s) {
        try {
            return objectMapper.readValue(s, Event.class);
        } catch (IOException e) {
            throw  new EventProcessingException("Could not marshall event " + s, e);
        }
    }


}
