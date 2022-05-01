package com.dido.shipping.services;

import com.dido.shipping.service.AddressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.dido.shipping.service.AddressServiceImpl.*;
import static org.junit.Assert.assertEquals;

@SpringBootTest
public class AddressServiceImplTest {

    @Autowired
    private AddressService addressService;

    private final String BASE_MESSAGE = "Shortest distance shipping address zip code: ";
    public static final String SOURCE_ZIP_CODE_ONE = "70785";
    public static final String SOURCE_ZIP_CODE_TWO = "70533";
    public static final String SOURCE_ZIP_CODE_THREE = "39859";

    @Test
    public void findMinDistanceZipCodeTest()
            throws Exception {

        String actualZipCode = addressService.getMinDistanceZipCode(SOURCE_ZIP_CODE_ONE);
        assertEquals(BASE_MESSAGE+SHIPPING_ZIP_CODE_THREE,actualZipCode);
        actualZipCode = addressService.getMinDistanceZipCode(SOURCE_ZIP_CODE_TWO);
        assertEquals(BASE_MESSAGE+SHIPPING_ZIP_CODE_TWO,actualZipCode);
        actualZipCode = addressService.getMinDistanceZipCode(SOURCE_ZIP_CODE_THREE);
        assertEquals(BASE_MESSAGE+SHIPPING_ZIP_CODE_ONE, actualZipCode);
    }
}
