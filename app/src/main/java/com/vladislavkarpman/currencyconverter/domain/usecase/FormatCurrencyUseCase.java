package com.vladislavkarpman.currencyconverter.domain.usecase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vladislavkarpman.currencyconverter.domain.entity.Currency;
import com.vladislavkarpman.currencyconverter.domain.entity.CurrencyFormatter;

public class FormatCurrencyUseCase {

    private final CurrencyFormatter currencyFormatter;

    public FormatCurrencyUseCase(@NonNull CurrencyFormatter currencyFormatter) {
        this.currencyFormatter = currencyFormatter;
    }

    @NonNull
    public String execute(double value, @Nullable Currency currency) {
        return currencyFormatter.format(value, currency);
    }
}
