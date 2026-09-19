package com.tradeengine.model.state;

import com.tradeengine.model.Order;
import com.tradeengine.model.enums.OrderStatus;
import java.math.BigDecimal;

public interface OrderState {

    OrderStatus getStatus();

    default void accept(Order order) {
        throw new IllegalStateException("Cannot accept order in " + getStatus() + " state");
    }

    default void fill(Order order, BigDecimal filledQuantity) {
        throw new IllegalStateException("Cannot fill order in " + getStatus() + " state");
    }

    default void cancel(Order order) {
        throw new IllegalStateException("Cannot cancel order in " + getStatus() + " state");
    }

    default void reject(Order order, String reason) {
        throw new IllegalStateException("Cannot reject order in " + getStatus() + " state");
    }
}
