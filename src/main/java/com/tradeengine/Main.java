package com.tradeengine;

import java.math.BigDecimal;

import com.tradeengine.model.Crypto;
import com.tradeengine.model.Money;
import com.tradeengine.model.Stock;
import com.tradeengine.model.enums.Currency;

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
    }
}