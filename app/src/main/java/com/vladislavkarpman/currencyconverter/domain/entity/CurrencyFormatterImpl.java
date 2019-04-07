package com.vladislavkarpman.currencyconverter.domain.entity;

import java.text.NumberFormat;

public class CurrencyFormatterImpl implements CurrencyFormatter {

    @Override
    public String format(double currencyAmount, Currency currency) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        numberFormat.setCurrency(java.util.Currency.getInstance(currency.getCharCode()));
        numberFormat.setGroupingUsed(true);
        numberFormat.setMaximumFractionDigits(4);
        return numberFormat.format(currencyAmount);
    }
}
