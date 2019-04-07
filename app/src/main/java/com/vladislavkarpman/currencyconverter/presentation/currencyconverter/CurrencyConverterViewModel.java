package com.vladislavkarpman.currencyconverter.presentation.currencyconverter;

import com.vladislavkarpman.currencyconverter.common.observable.Observable;
import com.vladislavkarpman.currencyconverter.common.viewmodel.ViewModel;
import com.vladislavkarpman.currencyconverter.domain.entity.Currency;

import java.util.List;

public interface CurrencyConverterViewModel extends
        ViewModel,
        Observable<CurrencyConverterViewModel.Listener> {

    interface Listener {

        void onCurrenciesFetched(List<Currency> currencies);

        void onCurrenciesFetchedFailed(Throwable error);
    }

    void fetchCurrenciesAndNotify();
}
