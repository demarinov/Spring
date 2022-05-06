package com.dido.shipping.service;

import com.dido.shipping.configuration.AddressProperties;
import com.dido.shipping.utils.DistanceCalculationUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.TreeMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private static final String PLACES_FIELD = "places";
    private static final String LATITUDE_FIELD = "latitude";
    private static final String LONGITUDE_FIELD = "longitude";
    private static final String ERROR_PROCESSING_MSG = "Error processing request with zip code: ";
    private static final String ERROR_PARSING_MSG_FORMAT = "Error parsing address with zip code: {}";
    private static final String OUTPUT_MSG_FORMAT = "Shortest distance shipping address zip code: %s";

    private final Map<String, String> shippingAddressMap;
    private final ObjectMapper mapper;
    private final AddressProperties addressProperties;
    private final AddressClientService addressClientService;

    @PostConstruct
    public void initAddressService() {
        log.info("initAddressService() - start");

        addressProperties.getShippingZipCodes().forEach(shippingZipCode ->
                shippingAddressMap.putIfAbsent(shippingZipCode,
                        addressClientService.getAddress(shippingZipCode)));

        log.info("initAddressService() - end");
    }

    @Override
    public String getMinAddressDistanceZipCode(String zipCode) {

        StringBuilder msg = new StringBuilder();

        try {
            String inputAddress = addressClientService.getAddress(zipCode);
            Map<String, Double> addressDistanceMap = new TreeMap<>();

            shippingAddressMap.forEach((shippingZipCode, shippingAddress) -> {
                try {
                    addressDistanceMap.putIfAbsent(shippingZipCode,
                            findAddressDistance(mapper, inputAddress, shippingAddress));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    log.error(ERROR_PARSING_MSG_FORMAT, zipCode);
                }
            });

            String minDistanceAddress = addressDistanceMap
                    .entrySet()
                    .stream()
                    .min(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey).orElse(null);

            log.debug(String.format(OUTPUT_MSG_FORMAT, minDistanceAddress));
            msg.append(String.format(OUTPUT_MSG_FORMAT, minDistanceAddress));

        } catch (WebClientResponseException exception) {
            msg.append(ERROR_PROCESSING_MSG).append(zipCode);
            log.error(msg + exception.getLocalizedMessage());
        }

        return msg.toString();
    }

    private Double findAddressDistance(ObjectMapper mapper, String sourceAddress,
                                       String targetAddress) throws JsonProcessingException {
        double targetLongitude;
        double targetLatitude;

        JsonNode inputAddressNode = mapper.readTree(sourceAddress);
        double sourceLongitude = getAddressCoordinate(inputAddressNode, LONGITUDE_FIELD);
        double sourceLatitude = getAddressCoordinate(inputAddressNode, LATITUDE_FIELD);

        JsonNode targetAddressNode = mapper.readTree(targetAddress);
        targetLongitude = getAddressCoordinate(targetAddressNode, LONGITUDE_FIELD);
        targetLatitude = getAddressCoordinate(targetAddressNode, LATITUDE_FIELD);

        return DistanceCalculationUtils
                .distance(sourceLatitude, targetLatitude, sourceLongitude, targetLongitude,
                        DistanceCalculationUtils.DistanceType.KM);
    }

    private double getAddressCoordinate(JsonNode addressNode, String coordinateName) {

        return Double.parseDouble(addressNode.
                get(PLACES_FIELD).
                get(0).
                get(coordinateName).asText());
    }

}
