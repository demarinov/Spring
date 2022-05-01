package com.dido.shipping.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface AddressService {
    String getMinDistanceZipCode(String zipCode) throws JsonProcessingException;
}
