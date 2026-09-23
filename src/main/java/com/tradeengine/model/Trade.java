package com.tradeengine.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record Trade(
        String tradeId,
        String buyOrderId,
        String sellOrderId,
        Asset asset,
        BigDecimal quantity,
        Money price,
        Money buyerFee,
        Money sellerFee,
        Instant executedAt) {
    public Trade {
        Objects.requireNonNull(tradeId, "Trade ID cannot be null");
        Objects.requireNonNull(buyOrderId, "Bur order ID cannot be null");
        Objects.requireNonNull(sellOrderId, "Sell order ID cannot be null");
        Objects.requireNonNull(asset, "Asset cannot be null");
        Objects.requireNonNull(quantity, "Quantity cannot be null");
        Objects.requireNonNull(price, "Price cannot be null");
        Objects.requireNonNull(buyerFee, "Buyer fee cannot be null");
        Objects.requireNonNull(sellerFee, "Seller fee cannot be null");
        Objects.requireNonNull(executedAt, "ExecutedAt cannot be null");
    }

    public static Trade of(Order buyOrder, Order sellOrder, BigDecimal matchQuantity, Money executionPrice,
            Money buyerFee, Money sellerFee) {
        return new Trade(
                UUID.randomUUID().toString().substring(0, 8),
                buyOrder.getId(),
                sellOrder.getId(),
                buyOrder.getAsset(),
                matchQuantity,
                executionPrice,
                buyerFee,
                sellerFee,
                Instant.now());
    }

}
