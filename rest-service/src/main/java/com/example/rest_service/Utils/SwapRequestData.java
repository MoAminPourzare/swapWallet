package com.example.rest_service.Utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SwapRequestData {
    private final String fromCoin;
    private final String toCoin;
    private final BigDecimal fromAmount;
    private final BigDecimal toAmount;
    private final LocalDateTime timestamp;
}