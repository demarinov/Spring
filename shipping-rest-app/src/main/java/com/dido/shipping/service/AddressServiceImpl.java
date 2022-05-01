package com.dido.shipping.service;

import com.dido.shipping.utils.Utils;
import com.dido.shipping.configuration.AppConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

@Service
@Slf4j
public class AddressServiceImpl implements AddressService{

    private static final String BASE_URL = "https://api.zippopotam.us/us/";
    public static final String SHIPPING_ZIP_CODE_ONE = "70115";
    public static final String SHIPPING_ZIP_CODE_TWO = "70508";
    public static final String SHIPPING_ZIP_CODE_THREE = "70811";

    @Override
    public String getMinDistanceZipCode(String zipCode) throws JsonProcessingException {

        ObjectMapper mapper = AppConfiguration.getObjectMapper();

        String inputAddress = getAddress(zipCode);

        JsonNode inputAddressNode = mapper.readTree(inputAddress);
        log.info(inputAddressNode.toString());

        Double sourceLongitude = Double.parseDouble(inputAddressNode.
                get("places").
                get(0).
                get("longitude").asText());

        Double sourceLatitude = Double.parseDouble(inputAddressNode.
                get("places").
                get(0).
                get("latitude").asText());

        String targetAddressOne = getAddress(SHIPPING_ZIP_CODE_ONE);

        Map<String, Double> addressDistanceMap = new TreeMap<>();

        extractAddress(mapper, sourceLongitude, sourceLatitude, addressDistanceMap, targetAddressOne);

        String targetAddressTwo = getAddress(SHIPPING_ZIP_CODE_TWO);

        extractAddress(mapper, sourceLongitude, sourceLatitude, addressDistanceMap, targetAddressTwo);

        String targetAddressThree = getAddress(SHIPPING_ZIP_CODE_THREE);

        extractAddress(mapper, sourceLongitude, sourceLatitude, addressDistanceMap, targetAddressThree);

        String minDistanceAddress = addressDistanceMap
                .entrySet()
                .stream()
                .min(Comparator.comparing(Map.Entry::getValue))
                .map(address -> address.getKey()).orElse(null);

        System.out.println(minDistanceAddress);

        return String.format("Shortest distance shipping address zip code: %s", minDistanceAddress);
    }

    private void extractAddress(ObjectMapper mapper, Double sourceLongitude, Double sourceLatitude,
                                Map<String, Double> addressDistanceMap,
                                String targetAddress) throws JsonProcessingException {
        Double targetLongitude;
        Double targetLatitude;
        JsonNode targetAddressNodeThree = mapper.readTree(targetAddress);

        targetLongitude = Double.parseDouble(targetAddressNodeThree.
                get("places").
                get(0).
                get("longitude").asText());

        targetLatitude = Double.parseDouble(targetAddressNodeThree.
                get("places").
                get(0).
                get("latitude").asText());

        Double distance = Utils
                .distance(sourceLatitude, targetLatitude, sourceLongitude, targetLongitude);
        log.info(targetAddressNodeThree.get("post code")+" -> "+distance);

        addressDistanceMap.putIfAbsent(targetAddressNodeThree.get("post code").asText(), distance);
    }

    private String getAddress(String zipCode) {

        // get elements data - blocking
        WebClient.UriSpec<WebClient.RequestBodySpec> addressRequest
                = prepareWebClientRequest();
        WebClient.RequestBodySpec requestUri =
                addressRequest.uri(BASE_URL+zipCode);

        return requestUri.retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private WebClient.UriSpec<WebClient.RequestBodySpec> prepareWebClientRequest() {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024)).build();
        WebClient webClient = WebClient.builder().exchangeStrategies(exchangeStrategies).build();

        return (WebClient.UriSpec<WebClient.RequestBodySpec>) webClient.get();
    }

}
