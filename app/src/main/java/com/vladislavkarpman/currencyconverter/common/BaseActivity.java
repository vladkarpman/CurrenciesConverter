package com.vladislavkarpman.currencyconverter.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.vladislavkarpman.currencyconverter.MyApplication;
import com.vladislavkarpman.currencyconverter.common.dependencyinjection.PresentationCompositionRoot;

public abstract class BaseActivity extends AppCompatActivity {

    protected PresentationCompositionRoot presentationCompositionRoot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presentationCompositionRoot = new PresentationCompositionRoot(
                ((MyApplication) getApplication()).getCompositionRoot(),
                this
        );
    }

    protected PresentationCompositionRoot getPresentationRoot() {
        return presentationCompositionRoot;
    }
}
