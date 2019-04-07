package com.vladislavkarpman.currencyconverter.repository.remote;

import android.support.annotation.Nullable;

import com.vladislavkarpman.currencyconverter.repository.currencies.CurrenciesSchema;

import org.simpleframework.xml.Serializer;

import java.net.HttpURLConnection;
import java.net.URL;

public class RemoteDataSourceImpl implements RemoteDataSource {

    private final String url;

    private final Serializer serializer;

    public RemoteDataSourceImpl(Serializer serializer, String url) {
        this.serializer = serializer;
        this.url = url;
    }

    @Override
    @Nullable
    public CurrenciesSchema getCurrenciesSync() {
        URL url;
        HttpURLConnection urlConnection;

        try {
            url = new URL(this.url);
            urlConnection = (HttpURLConnection) url.openConnection();

            return serializer.read(CurrenciesSchema.class, urlConnection.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

