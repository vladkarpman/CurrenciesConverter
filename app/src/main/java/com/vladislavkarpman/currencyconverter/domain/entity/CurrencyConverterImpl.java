package com.vladislavkarpman.currencyconverter.domain.entity;

import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.math.MathContext;

public class CurrencyConverterImpl implements CurrencyConverter {

    private static final MathContext ROUNDING_MODE = new MathContext(6);

    public double convertCurrency(double amount, @NonNull Currency from, @NonNull Currency to) {
        BigDecimal normalizedValueFrom = BigDecimal.valueOf(from.getValue())
                .divide(BigDecimal.valueOf(from.getNominal()), ROUNDING_MODE);

        BigDecimal normalizedValueTo = BigDecimal.valueOf(to.getValue())
                .divide(BigDecimal.valueOf(to.getNominal()), ROUNDING_MODE);

        BigDecimal divide = normalizedValueFrom.divide(normalizedValueTo, ROUNDING_MODE);

        return BigDecimal.valueOf(amount).multiply(divide).doubleValue();
    }
}
