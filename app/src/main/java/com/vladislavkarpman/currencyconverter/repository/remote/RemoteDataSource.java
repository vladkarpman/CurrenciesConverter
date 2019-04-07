package com.vladislavkarpman.currencyconverter.repository.remote;

import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.vladislavkarpman.currencyconverter.repository.currencies.CurrenciesSchema;

public interface RemoteDataSource {

    @WorkerThread
    @Nullable
    CurrenciesSchema getCurrenciesSync();
}
