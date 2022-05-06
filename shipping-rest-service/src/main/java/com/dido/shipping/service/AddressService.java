package com.dido.shipping.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface AddressService {
    String getMinAddressDistanceZipCode(String zipCode) throws JsonProcessingException;
}
