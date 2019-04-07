package com.vladislavkarpman.currencyconverter.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vladislavkarpman.currencyconverter.common.Disposable;
import com.vladislavkarpman.currencyconverter.common.DisposableFuture;
import com.vladislavkarpman.currencyconverter.common.NoDataError;
import com.vladislavkarpman.currencyconverter.common.SingleListener;
import com.vladislavkarpman.currencyconverter.domain.entity.Currency;
import com.vladislavkarpman.currencyconverter.repository.currencies.CurrenciesSchema;
import com.vladislavkarpman.currencyconverter.repository.currencies.CurrencySchema;
import com.vladislavkarpman.currencyconverter.repository.local.LocalDataSource;
import com.vladislavkarpman.currencyconverter.repository.remote.RemoteDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class RepositoryImpl implements Repository {

    @NonNull
    private final ExecutorService executorService;

    @NonNull
    private final LocalDataSource localDataSource;

    @NonNull
    private final RemoteDataSource remoteDataSource;

    public RepositoryImpl(
            @NonNull ExecutorService executorService,
            @NonNull LocalDataSource localDataSource,
            @NonNull RemoteDataSource remoteDataSource) {
        this.executorService = executorService;
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    @Override
    public Disposable getCurrenciesAsync(SingleListener<List<Currency>> singleListener) {
        return new DisposableFuture(executorService.submit(() -> {
            CurrenciesSchema remoteData = remoteDataSource.getCurrenciesSync();
            if (remoteData != null) {
                try {
                    localDataSource.saveCurrenciesSync(remoteData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                notifySuccessIfNotEmpty(singleListener, remoteData);
            } else {
                CurrenciesSchema localData = localDataSource.getCurrenciesSync();
                notifySuccessIfNotEmpty(singleListener, localData);
            }
        }));
    }

    private void notifySuccessIfNotEmpty(
            SingleListener<List<Currency>> singleListener,
            @Nullable CurrenciesSchema dataSchema) {
        List<Currency> data = convertToEntities(dataSchema);
        if (data != null) {
            singleListener.onSuccess(data);
        } else {
            singleListener.onError(new NoDataError());
        }
    }

    @Nullable
    private List<Currency> convertToEntities(@Nullable CurrenciesSchema data) {
        if (data == null) {
            return null;
        }
        List<Currency> currencies = new ArrayList<>();
        List<CurrencySchema> schemaList = data.getCurrenciesSchemaList();
        if (schemaList == null) {
            return null;
        }
        for (CurrencySchema schema : schemaList) {
            currencies.add(new Currency()
                    .setName(schema.getName())
                    .setValue(parseValueToDouble(schema.getValue()))
                    .setCharCode(schema.getCharCode())
                    .setNumCode(schema.getNumCode())
                    .setNominal(schema.getNominal())
            );
        }
        return currencies;
    }

    private double parseValueToDouble(String value) {
        return Double.parseDouble(value.replaceAll(",", "."));
    }
}
