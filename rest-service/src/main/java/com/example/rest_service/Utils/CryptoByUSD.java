package com.example.rest_service.Utils;

import java.math.BigDecimal;

public class CryptoByUSD {

    private final String convertedUsd;
    private final BigDecimal quantity;

    public CryptoByUSD(BigDecimal price , BigDecimal quantity) {
        this.convertedUsd = price.multiply(quantity) + "$";
        this.quantity = quantity;
    }

    public String getResult(){
        return quantity + ",  " + convertedUsd;
    }
}