package com.tradeengine.engine;

import com.tradeengine.model.Money;
import com.tradeengine.model.Order;
import com.tradeengine.model.Trade;
import com.tradeengine.strategy.FeeStrategy;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MatchingEngine {
    private final FeeStrategy feeStrategy;
    private final List<Trade> tradeHistory;

    public MatchingEngine(FeeStrategy feeStrategy) {
        this.feeStrategy = Objects.requireNonNull(feeStrategy, "FeeStrategy cannot be null");
        this.tradeHistory = new ArrayList<>();
    }

    public List<Trade> process(OrderBook orderBook) {
        Objects.requireNonNull(orderBook, "OrderBook cannot be null");
        List<Trade> executedTrades = new ArrayList<>();

        while (true) {
            Order bestBuy = orderBook.peekBestBuy();
            Order bestSell = orderBook.peekBestSell();

            if (bestBuy == null || bestSell == null) {
                break;
            }

            // alıcının teklifi satıcının istediğinden az ise anlaşma olmaz
            if (bestBuy.getPrice().amount().compareTo(bestSell.getPrice().amount()) < 0) {
                break;
            }

            Money executionPrice = bestBuy.getCreatedAt().isBefore(bestSell.getCreatedAt())
                    ? bestBuy.getPrice()
                    : bestSell.getPrice();

            BigDecimal matchQuantity = bestBuy.getRemainingQuantity().min(bestSell.getRemainingQuantity());

            bestBuy.fill(matchQuantity);
            bestSell.fill(matchQuantity);

            BigDecimal totalAmount = executionPrice.amount()
                    .multiply(matchQuantity)
                    .setScale(4, RoundingMode.HALF_UP);

            Money tradeTotal = new Money(totalAmount, executionPrice.currency());

            Money buyerFee = feeStrategy.calculateFee(tradeTotal);
            Money sellerFee = feeStrategy.calculateFee(tradeTotal);

            Trade trade = Trade.of(bestBuy, bestSell, matchQuantity, executionPrice, buyerFee, sellerFee);
            executedTrades.add(trade);
            this.tradeHistory.add(trade);

            if (bestBuy.getRemainingQuantity().compareTo(BigDecimal.ZERO) == 0) {
                orderBook.pollBestBuy();
            }
            if (bestSell.getRemainingQuantity().compareTo(BigDecimal.ZERO) == 0) {
                orderBook.pollBestSell();
            }
        }
        return executedTrades;
    }

    public List<Trade> getTradeHistory() {
        return Collections.unmodifiableList(tradeHistory);
    }

    public FeeStrategy getFeeStrategy() {
        return feeStrategy;
    }
}
