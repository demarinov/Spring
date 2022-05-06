package com.dido.shipping.service;

import com.dido.shipping.configuration.AddressProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class AddressClientServiceImpl implements AddressClientService {

    private final AddressProperties addressProperties;

    public String getAddress(String urlSuffix) {

        // get elements data - blocking
        WebClient.UriSpec<WebClient.RequestBodySpec> addressRequest
                = prepareWebClientRequest();
        WebClient.RequestBodySpec requestUri =
                addressRequest.uri(addressProperties.getAddressUrl() + urlSuffix);

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
