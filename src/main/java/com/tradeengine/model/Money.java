package com.tradeengine.model;

import com.tradeengine.model.enums.Currency;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal amount, Currency currency) {

    public Money {
        Objects.requireNonNull(amount, "Amount cannot be null");
        Objects.requireNonNull(currency, "Currency cannot be null");
        amount = amount.setScale(4, RoundingMode.HALF_UP);
    }
    

    public static Money of(double amount, Currency currency){
        return new Money(BigDecimal.valueOf(amount), currency);
    }

    public static Money of(String amount, Currency currency) {
        return new Money(new BigDecimal(amount), currency);
    }

    public Money add(Money other) {
        validateSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        validateSameCurrency(other);
        BigDecimal newAmount = this.amount.subtract(other.amount);
        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Resulting balance cannot be negative");
        }
        return new Money(newAmount, this.currency);
    }

    public Money multiply(long factor) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(factor)), this.currency);
    }

    public boolean isGreaterThanOrEqual(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) >= 0;
    }

    private void validateSameCurrency(Money other) {
        Objects.requireNonNull(other, "Other money cannot be null");
        if (this.currency != other.currency) {
            throw new IllegalArgumentException("Cannot operate on different currencies: "
            + this.currency + " and " + other.currency);
        }
    }
}
