package com.tradeengine;

import java.math.BigDecimal;
import java.util.List;
import com.tradeengine.model.Order;
import com.tradeengine.model.Crypto;
import com.tradeengine.model.Money;
import com.tradeengine.model.Stock;
import com.tradeengine.model.enums.Currency;
import com.tradeengine.model.enums.OrderSide;
import com.tradeengine.strategy.FeeStrategy;
import com.tradeengine.strategy.FixedFeeStrategy;
import com.tradeengine.strategy.PercentageFeeStrategy;
import com.tradeengine.strategy.ZeroFeeStrategy;
import com.tradeengine.engine.MatchingEngine;
import com.tradeengine.engine.OrderBook;
import com.tradeengine.model.Trade;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Trade Engine Test");

        Money wallet = Money.of(100.50, Currency.USD);
        Money salary = Money.of(50.25, Currency.USD);

        System.out.println("Initial Wallet: " + wallet);
        System.out.println("Incoming Salary: " + salary);

        Money sum = wallet.add(salary);
        System.out.println("Total balance: " + sum);

        Money sharePrice = Money.of(30.00, Currency.USD);
        Money totalCost = sharePrice.multiply(2);
        System.out.println("The cost of 2 share is: " + totalCost);

        Money remainingBalance = sum.subtract(totalCost);
        System.out.println("Remaining after share purchase: " + remainingBalance);

        try {
            Money tlMoney = Money.of(200, Currency.TRY);
            System.out.println("\n An attempt is being made to sum different currency...");
            remainingBalance.add(tlMoney);
        } catch (IllegalArgumentException e) {
            System.out.println("Cought security error: " + e.getMessage());
        }

        // -------------------------------------------------------------------

        System.out.println("1. money test");
        Money money = wallet;
        System.out.println("Wallet: " + money);

        System.out.println("\n2. asset hierarchy test");
        Stock appleStock = new Stock("AAPL", "Apple Inc.", Currency.USD);
        Crypto bitcoin = new Crypto("BTC", "Bitcoin", Currency.USD);

        System.out.println("Created Stock: " + appleStock);
        System.out.println("Created Crypto: " + bitcoin);

        BigDecimal cryptoQty = new BigDecimal("0.005");
        bitcoin.validateQuantity(cryptoQty);
        System.out.println("Valid quantity for BTC: " + cryptoQty);

        try {
            BigDecimal stockQty = new BigDecimal("1.5");
            System.out.println("Trying to validate 1.5 quantity for AAPL...");
            appleStock.validateQuantity(stockQty);
        } catch (IllegalArgumentException e) {
            System.out.println("Cought Expected Error: " + e.getMessage());
        }

        // ---------------------------------------------------------------

        System.out.println("\nState pattern order test");

        Stock apple = new Stock("AAPL", "Apple Inc.", Currency.USD);
        Order myOrder = new Order(apple, OrderSide.BUY, new BigDecimal("100"), Money.of(150.00, Currency.USD));

        System.out.println("1- Order Was Created: " + myOrder);

        myOrder.accept();
        System.out.println("2- Order Has Been Accepted By The Exchange: " + myOrder);

        System.out.println("\n--- 40 Shares Are Being Purchased ---");
        myOrder.fill(new BigDecimal("40"));
        System.out.println("3- Partial Fill: " + myOrder);

        System.out.println("\n---The Remaining 60 Shares Are Also Being Purchased---");
        myOrder.fill(new BigDecimal("60"));
        System.out.println("4- All Filled");

        System.out.println("\n--- Trying To Cancel Completed Order---");
        try {
            myOrder.cancel();
        } catch (IllegalStateException e) {
            System.out.println("Security Triggered: " + e.getMessage());
        }

        // -----------------------------------------------------------------
        System.out.println("\n Strategy Pattern Fee Test");

        Money tradeTotal = Money.of(10_100.00, Currency.USD);
        System.out.println("Trade Total Amount: " + tradeTotal + "\n");

        List<FeeStrategy> strategies = List.of(
                new PercentageFeeStrategy(0.002),
                new PercentageFeeStrategy(0.0005),
                new PercentageFeeStrategy(0.0001, Money.of(5.00, Currency.USD)),
                new FixedFeeStrategy(Money.of(2.50, Currency.USD)),
                new ZeroFeeStrategy());

        for (FeeStrategy strategy : strategies) {
            Money fee = strategy.calculateFee(tradeTotal);
            System.out.println("Strategy: " + strategy.getStrategyName() + "-> Calculated Fee: " + fee);
        }

        // ------------------------------------------------------------------
        // matching engine & order book test
        FeeStrategy feeStrategy = new PercentageFeeStrategy(0.001);
        System.out.println("\n live stock exchange simulation (aapl)");

        OrderBook appleBook = new OrderBook(apple);
        MatchingEngine engine = new MatchingEngine(feeStrategy);

        Order bobSell = new Order(apple, OrderSide.SELL, new BigDecimal("50"), Money.of(150.00, Currency.USD));

        Order charlieSell = new Order(apple, OrderSide.SELL, new BigDecimal("30"), Money.of(148.00, Currency.USD));

        appleBook.addOrder(bobSell);
        appleBook.addOrder(charlieSell);

        System.out.println("2 Sell Order Added To The Board");
        System.out.println("Bob: " + bobSell);
        System.out.println("Charlie" + charlieSell);
        System.out.println("Best Seller On The Board (Cheapest Price: " + appleBook.peekBestSell());

        System.out.println("\nAlice is in: 70 share @ 152.00$");
        Order aliceBuy = new Order(apple, OrderSide.BUY, new BigDecimal("70"), Money.of(152.00, Currency.USD));
        appleBook.addOrder(aliceBuy);

        // motoru çalıştıruyoruz artık
        System.out.println("Maching engine processing");
        List<Trade> trades = engine.process(appleBook);

        System.out.println("Trades count: " + trades.size());
        for (Trade trade : trades) {
            System.out.println("TRADE: " + trade.quantity() + trade.asset().getSymbol()
                    + " @ " + trade.price()
                    + " | Buyer Fee: " + trade.buyerFee()
                    + " | Seller Fee: " + trade.sellerFee()
                    + " | Time: " + trade.executedAt());
        }

        System.out.println("\nState Of The Board After Session");
        System.out.println("Remaining Buy Order Count: " + appleBook.getBuyOrderCount());
        System.out.println("Remaining Sell Order Count: " + appleBook.getSellOrderCount());
        System.out.println("Remaining Seller On The Board(Bob)" + appleBook.peekBestSell());
    }
}