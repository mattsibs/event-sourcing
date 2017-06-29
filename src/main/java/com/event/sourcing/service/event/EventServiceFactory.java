package com.event.sourcing.service.event;

import com.event.sourcing.service.event.file.FileLogReader;
import com.event.sourcing.service.event.file.FileLogger;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EventServiceFactory {

    private EventServiceFactory() {

    }

    public static EventService createFileBasedEventService(final String pathToFile, final EventHandler eventHandler) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_OBJECT);

        FileLogger fileLogger = new FileLogger(objectMapper, pathToFile);
        FileLogReader fileLogReader = new FileLogReader(objectMapper, pathToFile);

        return new EventService(fileLogger, fileLogReader, eventHandler);
    }

}
