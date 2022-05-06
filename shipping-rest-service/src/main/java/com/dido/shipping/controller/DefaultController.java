package com.dido.shipping.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {

    @GetMapping("/")
    public String test() {
        return "This is default endpoint. Use /address/{zip_code} to get actual data.";
    }
}
