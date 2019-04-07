package com.vladislavkarpman.currencyconverter.presentation;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.vladislavkarpman.currencyconverter.presentation.currencyconverter.CurrencyConverterView;
import com.vladislavkarpman.currencyconverter.presentation.currencyconverter.CurrencyConverterViewImpl;

public class ViewFactory {

    private final LayoutInflater layoutInflater;

    public ViewFactory(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    public CurrencyConverterView createCurrencyConverterView(@Nullable ViewGroup parent) {
        return new CurrencyConverterViewImpl(layoutInflater, parent);
    }
}
