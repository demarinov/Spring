package com.dido.shipping.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AddressControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getMinAddressDistanceOkTest() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/address/70785",
                String.class)).contains("Shortest distance shipping address zip code: 70811");
    }
    @Test
    void getMinAddressDistanceErrorTest() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/address/",
                String.class)).contains("Use a zip code and try again!");
    }

}
