package com.tradeengine.model.state;

import com.tradeengine.model.enums.OrderStatus;

public class RejectedOrderState implements OrderState {
    private final String reason;

    public RejectedOrderState(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.REJECTED;
    }
}
