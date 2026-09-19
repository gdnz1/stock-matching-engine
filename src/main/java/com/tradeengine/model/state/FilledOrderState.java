package com.tradeengine.model.state;

import com.tradeengine.model.enums.OrderStatus;

public class FilledOrderState implements OrderState {

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.FILLED;
    }
}
