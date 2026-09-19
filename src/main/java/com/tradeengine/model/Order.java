package com.tradeengine.model;

import com.tradeengine.model.enums.OrderSide;
import com.tradeengine.model.enums.OrderStatus;
import com.tradeengine.model.state.NewOrderState;
import com.tradeengine.model.state.OrderState;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Order {
    private final String id;
    private final Asset asset;
    private final OrderSide side;
    private final Money price;
    private final BigDecimal initialQuantity;
    private BigDecimal remainingQuantity;
    private OrderState state;
    private final Instant createdAt;

    public Order(Asset asset, OrderSide side, BigDecimal quantity, Money price) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.asset = Objects.requireNonNull(asset, "Asset cannot be null");
        this.side = Objects.requireNonNull(side, "OrderSide cannot be null");
        this.price = Objects.requireNonNull(price, "Price cannot be null");

        this.asset.validateQuantity((quantity));
        this.initialQuantity = quantity;
        this.remainingQuantity = quantity;

        this.state = new NewOrderState();
        this.createdAt = Instant.now();
    }

    public void setState(OrderState state) {
        this.state = Objects.requireNonNull(state, "State cannot be null");
    }

    public void accept() {
        this.state.accept(this);
    }

    public void fill(BigDecimal filledQuantity) {
        this.state.fill(this, filledQuantity);
    }

    public void cancel() {
        this.state.cancel(this);
    }

    public void reject(String reason) {
        this.state.reject(this, reason);
    }

    public void reduceRemainingQuantity(BigDecimal quantity) {
        if (quantity.compareTo(BigDecimal.ZERO) <= 0 || quantity.compareTo(remainingQuantity) > 0) {
            throw new IllegalArgumentException("Invalid fill quantity: " + quantity);
        }
        this.remainingQuantity = this.remainingQuantity.subtract(quantity);
    }

    // getter metodları

    public String getId() {
        return id;
    }

    public Asset getAsset() {
        return asset;
    }

    public OrderSide getSide() {
        return side;
    }

    public Money getPrice() {
        return price;
    }

    public BigDecimal getInitialQuantity() {
        return initialQuantity;
    }

    public BigDecimal getRemainingQuantity() {
        return remainingQuantity;
    }

    public OrderState getState() {
        return state;
    }

    public OrderStatus getStatus() {
        return state.getStatus();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Order #" + id + " [" + side + " " + remainingQuantity + "/" + initialQuantity + " " + asset.getSymbol()
                + " @" + price + " | " + getStatus() + "]";
    }
}
