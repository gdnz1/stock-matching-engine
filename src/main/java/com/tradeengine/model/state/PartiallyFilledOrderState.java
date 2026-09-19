package com.tradeengine.model.state;

import java.math.BigDecimal;

import com.tradeengine.model.Order;
import com.tradeengine.model.enums.OrderStatus;

public class PartiallyFilledOrderState implements OrderState {

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.PARTIALLY_FILLED;
    }

    @Override
    public void fill(Order order, BigDecimal filledQuantity) {
        order.reduceRemainingQuantity(filledQuantity);

        if (order.getRemainingQuantity().compareTo(BigDecimal.ZERO) == 0) {
            order.setState(new FilledOrderState());
        }
    }

    @Override
    public void cancel(Order order) {
        order.setState(new CancelledOrderState());
    }
}
