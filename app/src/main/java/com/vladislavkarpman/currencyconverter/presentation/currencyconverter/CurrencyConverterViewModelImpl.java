package com.vladislavkarpman.currencyconverter.presentation.currencyconverter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vladislavkarpman.currencyconverter.common.SingleListener;
import com.vladislavkarpman.currencyconverter.common.observable.BaseObservable;
import com.vladislavkarpman.currencyconverter.common.Disposable;
import com.vladislavkarpman.currencyconverter.domain.entity.Currency;
import com.vladislavkarpman.currencyconverter.domain.usecase.FetchCurrenciesUseCase;

import java.util.ArrayList;
import java.util.List;

public class CurrencyConverterViewModelImpl
        extends BaseObservable<CurrencyConverterViewModel.Listener>
        implements CurrencyConverterViewModel {

    @Nullable
    private Disposable disposable = null;

    @Nullable
    private List<Currency> cachedData = null;

    private boolean isRequestOnGoing = false;

    private final FetchCurrenciesUseCase fetchCurrenciesUseCase;

    public CurrencyConverterViewModelImpl(FetchCurrenciesUseCase fetchCurrenciesUseCase) {
        this.fetchCurrenciesUseCase = fetchCurrenciesUseCase;
    }

    @Override
    public void onClear() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        cachedData = null;
        isRequestOnGoing = false;
    }

    @Override
    public void fetchCurrenciesAndNotify() {
        if (cachedData != null) {
            notifySuccess(new ArrayList<>(cachedData));
            return;
        }
        if (isRequestOnGoing) {
            return;
        }
        fetchCurrencies();
    }

    private void fetchCurrencies() {
        isRequestOnGoing = true;
        disposable = fetchCurrenciesUseCase.execute(new SingleListener<List<Currency>>() {
            @Override
            public void onSuccess(@NonNull List<Currency> currencies) {
                isRequestOnGoing = false;
                notifySuccess(currencies);
            }

            @Override
            public void onError(Throwable error) {
                isRequestOnGoing = false;
                notifyError(error);
            }
        });
    }

    private void notifyError(Throwable error) {
        notifyListeners(listener -> listener.onCurrenciesFetchedFailed(error));
    }

    private void notifySuccess(@NonNull List<Currency> data) {
        cachedData = data;
        notifyListeners(listener -> listener.onCurrenciesFetched(data));
    }
}
