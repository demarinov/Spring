package com.dido.shipping.services;

import com.dido.shipping.configuration.AddressProperties;
import com.dido.shipping.service.AddressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;

@SpringBootTest
class AddressServiceImplTest {

    @Autowired
    private AddressProperties addressProperties;

    @Autowired
    private AddressService addressService;

    private final String SUCCESS_MESSAGE = "Shortest distance shipping address zip code: ";
    private final String ERROR_MESSAGE = "Error processing request with zip code: ";
    public static final String SOURCE_ZIP_CODE_ONE = "70785";
    public static final String SOURCE_ZIP_CODE_TWO = "70533";
    public static final String SOURCE_ZIP_CODE_THREE = "39859";
    public static final String UNKNOWN_ZIP_CODE = "35099";

    @Test
    void findMinDistanceZipCodeTest()
            throws Exception {

        String actualZipCode = addressService.getMinAddressDistanceZipCode(SOURCE_ZIP_CODE_ONE);
        assertEquals(SUCCESS_MESSAGE + addressProperties.getShippingZipCodes().get(2), actualZipCode);
        actualZipCode = addressService.getMinAddressDistanceZipCode(SOURCE_ZIP_CODE_TWO);
        assertEquals(SUCCESS_MESSAGE + addressProperties.getShippingZipCodes().get(1), actualZipCode);
        actualZipCode = addressService.getMinAddressDistanceZipCode(SOURCE_ZIP_CODE_THREE);
        assertEquals(SUCCESS_MESSAGE + addressProperties.getShippingZipCodes().get(0), actualZipCode);
    }

    @Test
    void unknownZipCodeTest()
            throws Exception {

        String actualZipCode = addressService.getMinAddressDistanceZipCode(UNKNOWN_ZIP_CODE);
        assertEquals(ERROR_MESSAGE + UNKNOWN_ZIP_CODE, actualZipCode);
    }
}
