package com.dido.shipping.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Data
public class AddressProperties {

    @Value("#{'${shipping.zip.codes}'.split(',')}")
    public List<String> shippingZipCodes;

    @Value("${address.url}")
    public String addressUrl;
}
