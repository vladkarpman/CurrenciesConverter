package com.vladislavkarpman.currencyconverter.common.dependencyinjection;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import com.vladislavkarpman.currencyconverter.domain.usecase.ConvertCurrenciesUseCase;
import com.vladislavkarpman.currencyconverter.domain.usecase.FormatCurrencyUseCase;
import com.vladislavkarpman.currencyconverter.presentation.ViewFactory;
import com.vladislavkarpman.currencyconverter.presentation.currencyconverter.CurrencyConverterViewModel;

public class PresentationCompositionRoot {

    private final CompositionRoot compositionRoot;
    private final AppCompatActivity activity;

    public PresentationCompositionRoot(CompositionRoot compositionRoot, AppCompatActivity activity) {
        this.compositionRoot = compositionRoot;
        this.activity = activity;
    }

    public ViewFactory getViewMvcFactory() {
        return new ViewFactory(LayoutInflater.from(getContext()));
    }

    public CurrencyConverterViewModel getCurrencyConverterViewModel() {
        return compositionRoot.provideCurrencyConverterViewModel();
    }

    private Context getContext() {
        return activity;
    }

    public FormatCurrencyUseCase getFormatCurrencyUseCase() {
        return compositionRoot.provideFormatCurrencyUseCase();
    }

    public ConvertCurrenciesUseCase getConvertCurrenciesUseCase() {
        return compositionRoot.provideConvertCurrenciesUseCase();
    }
}
