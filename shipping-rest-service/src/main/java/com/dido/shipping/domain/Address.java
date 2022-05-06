package com.dido.shipping.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Address {
    String postCode;
    List<Object> places;
}
