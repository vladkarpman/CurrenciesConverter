package com.vladislavkarpman.currencyconverter;

import com.vladislavkarpman.currencyconverter.domain.entity.Currency;
import com.vladislavkarpman.currencyconverter.repository.currencies.CurrenciesSchema;
import com.vladislavkarpman.currencyconverter.repository.currencies.CurrencySchema;

import java.util.Collections;
import java.util.List;

public class TestConstants {
    private static final String NAME = "Russian Ruble";
    private static final String CHAR_CODE = "RUB";
    private static final int VALUE = 1;
    private static final int NOMINAL = 100;
    public static final Currency CURRENCY = new Currency()
            .setName(NAME)
            .setValue(VALUE)
            .setCharCode(CHAR_CODE)
            .setNominal(NOMINAL);

    public static final List<Currency> CURRENCIES_LIST = Collections.singletonList(CURRENCY);

    private static final CurrencySchema CURRENCY_SCHEMA = new CurrencySchema()
            .setCharCode(CHAR_CODE)
            .setNominal(NOMINAL)
            .setName(NAME)
            .setValue(String.valueOf(VALUE));

    public static final CurrenciesSchema CURRENCIES_SCHEMA = new CurrenciesSchema()
            .setCurrenciesSchemaList(Collections.singletonList(CURRENCY_SCHEMA))
            .setDate("Date")
            .setName("Name");
}
