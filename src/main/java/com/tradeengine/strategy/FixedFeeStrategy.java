package com.tradeengine.strategy;

import com.tradeengine.model.Money;
import java.util.Objects;

public class FixedFeeStrategy implements FeeStrategy {
    private final Money fixedFee;

    public FixedFeeStrategy(Money fixedFee) {
        this.fixedFee = Objects.requireNonNull(fixedFee, "Fixed fee cannot be null");
    }

    @Override
    public Money calculateFee(Money tradeTotal) {
        Objects.requireNonNull(tradeTotal, "Trade total cannot be null");
        return fixedFee;
    }

    @Override
    public String getStrategyName() {
        return "Fixed Fee (" + fixedFee + ")";
    }
}
