package com.vladislavkarpman.currencyconverter;

import com.vladislavkarpman.currencyconverter.common.Disposable;

public class DisposableTd implements Disposable {
    private boolean isDisposed = false;

    @Override
    public void dispose() {
        isDisposed = true;
    }

    @Override
    public boolean isDisposed() {
        return isDisposed;
    }
}
