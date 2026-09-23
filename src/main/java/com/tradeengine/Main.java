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
    }
}