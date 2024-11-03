package com.radtimes.rad_times_server.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController implements ErrorController{

    public IndexController(){}

    @RequestMapping("/error")
    @ResponseBody
    public String getErrorPath() {
        return "Error for this route";
    }

    @GetMapping("/api/secured")
    public String secured(OAuth2AuthenticationToken token) {
        return token.getPrincipal().toString();
    }
}
