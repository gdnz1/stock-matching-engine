package com.tradeengine.model;

import com.tradeengine.model.enums.AssetType;
import com.tradeengine.model.enums.Currency;

public class Crypto extends Asset {
    public Crypto(String symbol, String name, Currency currency) {
        super(symbol, name, currency, AssetType.CRYPTO);
    }

    // kripto paralarda küsüratlı alımlar serbest olduğu için true veriyoruz
    @Override
    public boolean isFractionalAllowed() {
        return true;
    }
}

