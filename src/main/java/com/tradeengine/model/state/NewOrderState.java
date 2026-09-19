package com.tradeengine.model.state;

import com.tradeengine.model.Order;
import com.tradeengine.model.enums.OrderStatus;

public class NewOrderState implements OrderState {

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.NEW;
    }

    @Override
    public void accept(Order order) {
        order.setState(new PendingOrderState());
    }

    @Override
    public void reject(Order order, String reason) {
        order.setState(new RejectedOrderState(reason));
    }
}
