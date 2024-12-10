package com.radtimes.rad_times_server.controllers;

import com.radtimes.rad_times_server.model.footage.ClipModel;
import com.radtimes.rad_times_server.service.FootageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/footage")
public class FootageController {
    private final FootageService footageService;

    public FootageController(FootageService footageService) {
        this.footageService = footageService;
    }

    @GetMapping
    public Set<ClipModel> loadClipSet(@RequestParam Long startDateTime, @RequestParam(required = false, defaultValue="5") Integer count, @RequestParam(required = false) List<String> filters) {
        return footageService.loadClipSet(startDateTime, count, filters);
    }
}
