package com.radtimes.rad_times_server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HealthAndLiveness {
    @Autowired
    private BuildProperties buildProperties;

    @GetMapping("/info")
    public String getServerInfo() {


        class ServerStateInfo {
            private final String version = buildProperties.getVersion();
            private final String name = "Rad Times server";
            ServerStateInfo() {}
        }

        ServerStateInfo serverStateInfo = new ServerStateInfo();
        return "{ " +
                "name: " + serverStateInfo.name + " " +
                "version: " + serverStateInfo.version +
                " }";
    }
}
