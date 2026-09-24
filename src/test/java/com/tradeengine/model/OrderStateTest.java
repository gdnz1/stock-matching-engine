package com.tradeengine.model;

import com.tradeengine.model.enums.Currency;
import com.tradeengine.model.enums.OrderSide;
import com.tradeengine.model.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderStateTest {

    private Stock appleStock;
    private Order order;

    @BeforeEach
    void setUp() {
        appleStock = new Stock("AAPL", "Apple Inc.", Currency.USD);
        order = new Order(appleStock, OrderSide.BUY, new BigDecimal("100"), Money.of(150.0, Currency.USD));
    }

    @Test
    @DisplayName("A new created order must start in the NEW state and become PENDING once accepted")
    void shouldTransitionFromNewToPendingWhenAccepted() {

        assertThat(order.getStatus()).isEqualTo(OrderStatus.NEW);

        order.accept();

        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    @DisplayName("It should transition to the PARTIALLY_FILLED state upon partial fulfillment and to the FILLED state upon full fulfillment")
    void shouldTransitionToPartiallyFilledAndThenFilled() {
        order.accept();

        order.fill(new BigDecimal("40"));
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PARTIALLY_FILLED);
        assertThat(order.getRemainingQuantity()).isEqualByComparingTo("60");

        order.fill(new BigDecimal("60"));
        assertThat(order.getStatus()).isEqualTo(OrderStatus.FILLED);
        assertThat(order.getRemainingQuantity()).isEqualByComparingTo("0");
    }

    @Test
    @DisplayName("An error should be thrown when attempting to cancel a filled order")
    void shouldThrowExceptionWhenCancellingFilledOrder() {
        order.accept();
        order.fill(new BigDecimal("100"));

        assertThatThrownBy(() -> order.cancel())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot cancel order in FILLED state");
    }
}
