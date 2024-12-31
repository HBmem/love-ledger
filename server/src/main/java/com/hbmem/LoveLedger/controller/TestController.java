package com.hbmem.LoveLedger.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/main")
    public String mainTest() {
        return "Hello World";
    }

    @GetMapping("/hide")
    public String hideTest() {
        return "You must be logged in to see this!";
    }
}
