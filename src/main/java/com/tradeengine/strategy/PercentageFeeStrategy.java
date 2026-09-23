package com.tradeengine.strategy;

import com.tradeengine.model.Money;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class PercentageFeeStrategy implements FeeStrategy {
    private final BigDecimal percentageRate;

    private final Money minimumFee;

    public PercentageFeeStrategy(double percentageRate, Money minimuFee) {
        if (percentageRate < 0) {
            throw new IllegalArgumentException("Fee rate cannot be negative");
        }
        this.percentageRate = BigDecimal.valueOf(percentageRate);
        this.minimumFee = minimuFee;
    }

    public PercentageFeeStrategy(double percentageRate) {
        this(percentageRate, null);
    }

    @Override
    public Money calculateFee(Money tradeTotal) {
        Objects.requireNonNull(tradeTotal, "Trade total cannot be null");

        BigDecimal calculatedAmount = tradeTotal.amount().multiply(this.percentageRate).setScale(4,
                RoundingMode.HALF_UP);

        Money fee = new Money(calculatedAmount, tradeTotal.currency());

        if (minimumFee != null && minimumFee.isGreaterThanOrEqual(fee)) {
            return minimumFee;
        }
        return fee;
    }

    @Override
    public String getStrategyName() {
        return "Percentage Fee (" + percentageRate.multiply(BigDecimal.valueOf(100)) + "%)";
    }
}
