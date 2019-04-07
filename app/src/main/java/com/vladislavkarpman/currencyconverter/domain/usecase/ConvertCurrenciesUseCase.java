package com.vladislavkarpman.currencyconverter.domain.usecase;

import android.support.annotation.NonNull;

import com.vladislavkarpman.currencyconverter.domain.entity.Currency;
import com.vladislavkarpman.currencyconverter.domain.entity.CurrencyConverter;

public class ConvertCurrenciesUseCase {

    @NonNull
    private final CurrencyConverter currencyConverter;

    public ConvertCurrenciesUseCase(@NonNull CurrencyConverter currencyConverter) {
        this.currencyConverter = currencyConverter;
    }

    public double execute(double amount, @NonNull Currency from, @NonNull Currency to) {
        return currencyConverter.convertCurrency(amount, from, to);
    }
}
