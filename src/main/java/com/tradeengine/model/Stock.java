package com.tradeengine.model;

import com.tradeengine.model.enums.AssetType;
import com.tradeengine.model.enums.Currency;


public class Stock extends Asset {
    public Stock(String symbol, String name, Currency currency) {
        super(symbol, name, currency, AssetType.STOCK);
    }

    // standart hisse senedini küsüratlı olarak alamadığımız için false veriyoruz
    @Override
    public boolean isFractionalAllowed() {
        return false;
    }
}