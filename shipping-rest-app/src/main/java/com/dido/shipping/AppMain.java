package com.dido.shipping;

import com.dido.shipping.service.AddressService;
import com.dido.shipping.service.AddressServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@Slf4j
public class AppMain {

    private static AddressService addressService = new AddressServiceImpl();

    // given the shipping company offices :70115,70508,70811 ->
    // find the zip code of the office within min distance
    // source locations to test: 70785; 70533; 39059
    public static void main(String[] args) throws JsonProcessingException {
        SpringApplication.run(AppMain.class, args);
    }
}
