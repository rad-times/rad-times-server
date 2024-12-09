package com.radtimes.rad_times_server.controllers;

import com.radtimes.rad_times_server.service.FootageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/footage")
public class FootageController {
    private final FootageService footageService;

    public FootageController(FootageService footageService) {
        this.footageService = footageService;
    }

}
