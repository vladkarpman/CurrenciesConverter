package com.vladislavkarpman.currencyconverter.common;

import java.util.concurrent.Future;

public class DisposableFuture implements Disposable {

    private final Future future;

    public DisposableFuture(Future future) {
        this.future = future;
    }

    @Override
    public void dispose() {
        future.cancel(true);
    }

    @Override
    public boolean isDisposed() {
        return future.isCancelled();
    }
}
