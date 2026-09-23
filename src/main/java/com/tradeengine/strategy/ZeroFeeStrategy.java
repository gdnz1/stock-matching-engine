package com.tradeengine.strategy;

import com.tradeengine.model.Money;
import java.math.BigDecimal;
import java.util.Objects;

public class ZeroFeeStrategy implements FeeStrategy {

    @Override
    public Money calculateFee(Money tradeTotal) {
        Objects.requireNonNull(tradeTotal, "Trade total cannot be null");

        return new Money(BigDecimal.ZERO, tradeTotal.currency());
    }

    @Override
    public String getStrategyName() {
        return "Zero fee (Promotional)";
    }
}
