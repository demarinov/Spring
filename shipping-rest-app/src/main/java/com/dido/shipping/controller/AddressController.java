package com.dido.shipping.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.dido.shipping.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/{zip-code}")
    public String getMinDistanceZipCode(@PathVariable("zip-code") String zipCode) throws JsonProcessingException {
        return addressService.getMinDistanceZipCode(zipCode);
    }

    @GetMapping("/")
    public String test() {
        return "Test";
    }
}
