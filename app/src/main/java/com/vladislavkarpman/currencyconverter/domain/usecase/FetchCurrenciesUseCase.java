package com.vladislavkarpman.currencyconverter.domain.usecase;

import android.support.annotation.NonNull;

import com.vladislavkarpman.currencyconverter.common.Disposable;
import com.vladislavkarpman.currencyconverter.common.SingleListener;
import com.vladislavkarpman.currencyconverter.domain.entity.Currency;
import com.vladislavkarpman.currencyconverter.repository.Repository;

import java.util.List;

public class FetchCurrenciesUseCase {

    @NonNull
    private final Repository repository;

    public FetchCurrenciesUseCase(@NonNull Repository repository) {
        this.repository = repository;
    }

    public Disposable execute(SingleListener<List<Currency>> singleListener) {
        return repository.getCurrenciesAsync(singleListener);
    }
}
