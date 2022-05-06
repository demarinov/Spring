package com.dido.shipping.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.dido.shipping.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/{zip-code}")
    public String getMinAddressDistanceZipCode(@PathVariable("zip-code") String zipCode) throws JsonProcessingException {
        log.info("getMinAddressDistanceZipCode() - start. Zip code received: [{}]", zipCode);
        return addressService.getMinAddressDistanceZipCode(zipCode);
    }

    @GetMapping("/")
    public String getDefaultAddressMessage() {
        return "Use a zip code and try again!";
    }
}
