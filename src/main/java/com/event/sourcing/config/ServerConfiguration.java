package com.event.sourcing.config;


import com.beust.jcommander.Parameter;

public class ServerConfiguration {

    @Parameter(names = "-log-type", description = "Log type", required = true)
    private LogType logType;


    enum LogType {
        FILE
    }


}
