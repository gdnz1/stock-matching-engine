package com.tradeengine.engine;

import com.tradeengine.model.Money;
import com.tradeengine.model.Order;
import com.tradeengine.model.Stock;
import com.tradeengine.model.Trade;
import com.tradeengine.model.enums.Currency;
import com.tradeengine.model.enums.OrderSide;
import com.tradeengine.model.enums.OrderStatus;
import com.tradeengine.strategy.FeeStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class MatchingEngineTest {

    @Mock
    private FeeStrategy mockFeeStrategy;

    private MatchingEngine matchingEngine;
    private Stock appleStock;
    private OrderBook orderBook;

    @BeforeEach
    void setUp() {
        matchingEngine = new MatchingEngine(mockFeeStrategy);
        appleStock = new Stock("APPL", "Apple Inc.", Currency.USD);
        orderBook = new OrderBook(appleStock);
    }

    @Test
    @DisplayName("When buyers and sellers prices align, a match must occur and a trade must be generated.")
    void shouldMatchOrdersWhenPricesAreCompitable() {

        when(mockFeeStrategy.calculateFee(any())).thenReturn(Money.of(2.50, Currency.USD));

        Order sellOrder = new Order(appleStock, OrderSide.SELL, new BigDecimal("50"), Money.of(150.00, Currency.USD));

        Order buyOrder = new Order(appleStock, OrderSide.BUY, new BigDecimal("50"), Money.of(150.00, Currency.USD));

        orderBook.addOrder(sellOrder);
        orderBook.addOrder(buyOrder);

        List<Trade> trades = matchingEngine.process(orderBook);

        assertThat(trades).hasSize(1);
        Trade trade = trades.get(0);

        assertThat(trade.quantity()).isEqualByComparingTo("50");
        assertThat(trade.price()).isEqualTo(Money.of(150.00, Currency.USD));
        assertThat(trade.buyerFee()).isEqualTo(Money.of(2.50, Currency.USD));

        assertThat(sellOrder.getStatus()).isEqualTo(OrderStatus.FILLED);
        assertThat(buyOrder.getStatus()).isEqualTo(OrderStatus.FILLED);

        assertThat(orderBook.getBuyOrderCount()).isZero();
        assertThat(orderBook.getSellOrderCount()).isZero();

        verify(mockFeeStrategy, times(2)).calculateFee(any());
    }
}
