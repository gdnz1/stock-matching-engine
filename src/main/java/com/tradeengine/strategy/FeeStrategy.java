package com.tradeengine.strategy;

import com.tradeengine.model.Money;

public interface FeeStrategy {

    Money calculateFee(Money tradeTotal);

    String getStrategyName();
}
