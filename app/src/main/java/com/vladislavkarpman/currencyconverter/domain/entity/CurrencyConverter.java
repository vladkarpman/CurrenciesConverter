package com.vladislavkarpman.currencyconverter.domain.entity;

import android.support.annotation.NonNull;

public interface CurrencyConverter {
    double convertCurrency(double amount, @NonNull Currency from, @NonNull Currency to);
}
