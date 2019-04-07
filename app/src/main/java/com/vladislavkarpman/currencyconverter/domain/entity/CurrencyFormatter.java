package com.vladislavkarpman.currencyconverter.domain.entity;

import android.support.annotation.Nullable;

public interface CurrencyFormatter {

    String format(double currencyAmount, @Nullable Currency currency);
}
