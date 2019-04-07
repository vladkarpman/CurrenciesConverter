package com.vladislavkarpman.currencyconverter.common;

import android.support.annotation.NonNull;

public interface SingleListener<T> {

    void onSuccess(@NonNull T data);

    void onError(Throwable error);
}
