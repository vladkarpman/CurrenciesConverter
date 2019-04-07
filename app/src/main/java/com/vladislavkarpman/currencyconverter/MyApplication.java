package com.vladislavkarpman.currencyconverter;

import android.app.Application;

import com.vladislavkarpman.currencyconverter.common.dependencyinjection.CompositionRoot;

public class MyApplication extends Application {

    private CompositionRoot compositionRoot;

    @Override
    public void onCreate() {
        super.onCreate();
        compositionRoot = new CompositionRoot(this);
    }

    public CompositionRoot getCompositionRoot() {
        return compositionRoot;
    }
}
