package com.tradeengine.model;

import com.tradeengine.model.enums.Currency;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyTest {

    @Test
    @DisplayName("Two money object with the same currency must be succesfully added together")
    void shouldAddMoneyWhenCurrenciesMatch() {

        Money wallet = Money.of(100.50, Currency.USD);
        Money salary = Money.of(50.25, Currency.USD);

        Money total = wallet.add(salary);

        assertThat(total.amount()).isEqualByComparingTo("150.7500");
        assertThat(total.currency()).isEqualTo(Currency.USD);
    }

    @Test
    @DisplayName("It should throw an IllegalArgumentException when attempting to add different currencies")
    void shouldThrowExceptionWhenAddingDifferentCurrencies() {

        Money usd = Money.of(100.0, Currency.USD);
        Money tryMoney = Money.of(100.0, Currency.TRY);

        assertThatThrownBy(() -> usd.add(tryMoney))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContainingAll("Cannot operate on different currencies");
    }

    @Test
    @DisplayName("An error should be thrown if a subtraction causes tje balance to drop below zero")
    void shouldThrowExceptionWhenResultingBalanceIsNegative() {
        Money wallet = Money.of(50.0, Currency.USD);
        Money expensiveItem = Money.of(100.0, Currency.USD);

        assertThatThrownBy(() -> wallet.subtract(expensiveItem))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Resulting balance cannot be negative");
    }
}
