package com.vladislavkarpman.currencyconverter.repository.local;

import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.vladislavkarpman.currencyconverter.repository.currencies.CurrenciesSchema;

public interface LocalDataSource {

    @WorkerThread
    void saveCurrenciesSync(CurrenciesSchema currenciesSchema);

    @WorkerThread
    @Nullable
    CurrenciesSchema getCurrenciesSync();
}
