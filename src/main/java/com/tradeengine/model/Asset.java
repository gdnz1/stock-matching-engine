package com.tradeengine.model;

import com.tradeengine.model.enums.AssetType;
import com.tradeengine.model.enums.Currency;
import java.math.BigDecimal;
import java.util.Objects;

public abstract class Asset {
    private final String symbol;
    private final String name;
    private final Currency currency;
    private final AssetType assetType;

    protected Asset(String symbol, String name, Currency currency, AssetType assetType) {
        this.symbol = Objects.requireNonNull(symbol, "Symbol cannot be null").toUpperCase().trim();
        this.name = Objects.requireNonNull(name, "Name cannot be null").trim();
        this.currency = Objects.requireNonNull(currency, "Currency cannot be null");
        this.assetType = Objects.requireNonNull(assetType, "AssetType cannot be null");
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public Currency getCurrency() {
        return currency;
    }

    public AssetType getAssetType() {
        return assetType;
    }

    public abstract boolean isFractionalAllowed();

    public void validateQuantity(BigDecimal quantity) {
        Objects.requireNonNull(quantity, "Quantity cannot be null");
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (!isFractionalAllowed() && quantity.stripTrailingZeros().scale() > 0) {
            throw new IllegalArgumentException(
                    "Asset " + symbol + " does not support fractional quantities: " + quantity);
        }
    }

    @Override
    public String toString() {
        return assetType + " [" + symbol + " - " + name + " (" + currency + ")]";
    }
}