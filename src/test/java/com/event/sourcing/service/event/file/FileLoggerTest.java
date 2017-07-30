package com.event.sourcing.service.event.file;

import com.event.sourcing.event.EventPayload;
import com.event.sourcing.exception.EventProcessingException;
import com.event.sourcing.model.DataManager;
import com.event.sourcing.model.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class FileLoggerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private FileLogger fileLogger;

    private File outputFile;

    @Before
    public void setUp() throws Exception {
        outputFile = temporaryFolder.newFile();
        fileLogger = new FileLogger(objectMapper, outputFile);
    }

    @Test
    public void logEvent_eventMappedToString_writesStringToFile() throws Exception {
        given(objectMapper.writeValueAsString(any()))
                .willReturn("Event As A String");

        fileLogger.logEvent(new TestEvent(User.class, EventPayload.Type.CREATE));

        assertThat(outputFile).hasContent("Event As A String");
    }

    @Test
    public void logEvent_objectMapperThrowsException_throwsEventProcessingException() throws Exception {
        given(objectMapper.writeValueAsString(any()))
                .willThrow(JsonProcessingException.class);

        assertThatThrownBy(() -> fileLogger.logEvent(new TestEvent(User.class, EventPayload.Type.CREATE)))
                .isInstanceOf(EventProcessingException.class)
                .hasMessage("Error writing TestEvent to JSON");
    }

    private static class TestEvent extends EventPayload {

        protected TestEvent(final Class domainObject, final Type type) {
            super(domainObject, type);
        }

        @Override
        public Object process(final DataManager dataManager) {
            return null;
        }

        @Override
        public String toString() {
            return "TestEvent";
        }
    }
}