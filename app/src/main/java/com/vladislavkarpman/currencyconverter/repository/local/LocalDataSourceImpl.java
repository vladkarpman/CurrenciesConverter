package com.vladislavkarpman.currencyconverter.repository.local;

import android.support.annotation.Nullable;

import com.vladislavkarpman.currencyconverter.BuildConfig;
import com.vladislavkarpman.currencyconverter.repository.currencies.CurrenciesSchema;

import org.simpleframework.xml.Serializer;

import java.io.File;
import java.lang.annotation.Retention;

public class LocalDataSourceImpl implements LocalDataSource {

    private final Serializer serializer;
    private final File file;

    public LocalDataSourceImpl(
            Serializer serializer,
            File file) {
        this.serializer = serializer;
        this.file = file;
    }

    @Override
    public void saveCurrenciesSync(CurrenciesSchema currenciesSchema)  {
        try {
            serializer.write(currenciesSchema, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Nullable
    public CurrenciesSchema getCurrenciesSync() {
        try {
            return serializer.read(CurrenciesSchema.class, file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
