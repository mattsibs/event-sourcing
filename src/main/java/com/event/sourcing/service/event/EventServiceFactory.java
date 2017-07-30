package com.event.sourcing.service.event;

import com.event.sourcing.service.event.file.FileLogReader;
import com.event.sourcing.service.event.file.FileLogger;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class EventServiceFactory {

    private EventServiceFactory() {

    }

    public static EventService createFileBasedEventService(final File file, final EventHandler eventHandler) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_OBJECT);

        FileLogger fileLogger = new FileLogger(objectMapper, file);
        FileLogReader fileLogReader = new FileLogReader(objectMapper, file);

        return new EventService(fileLogger, fileLogReader, eventHandler);
    }
}
