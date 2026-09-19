package com.tradeengine.model.state;

import com.tradeengine.model.enums.OrderStatus;

public class CancelledOrderState implements OrderState {

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.CANCELLED;
    }
}
