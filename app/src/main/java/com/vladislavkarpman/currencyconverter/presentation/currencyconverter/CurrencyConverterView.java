package com.vladislavkarpman.currencyconverter.presentation.currencyconverter;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

import com.vladislavkarpman.currencyconverter.common.viewholder.ObservableViewHolder;
import com.vladislavkarpman.currencyconverter.domain.entity.Currency;

import java.util.List;

public interface CurrencyConverterView extends ObservableViewHolder<CurrencyConverterView.Listener> {

    interface Listener {

        void onConvertButtonClicked();

        void onCurrencyFromPicked(Currency currency);

        void onCurrencyToPicked(Currency currency);

        void onTryAgainButtonClicked();
    }

    @NonNull
    @UiThread
    String getAmountToConvert();

    @UiThread
    void bindCurrencies(List<Currency> currencies);

    @UiThread
    void showConvertedValue(String convertedValue);

    @UiThread
    void showRelativeValueTo(String relativeValue);

    @UiThread
    void showRelativeValueFrom(String relativeValue);

    @UiThread
    void showLoading();

    @UiThread
    void hideLoading();

    @UiThread
    void hideError();

    @UiThread
    void showError(String errorMessage);
}
