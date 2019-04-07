package com.vladislavkarpman.currencyconverter.common.observable;

import android.support.v4.util.Consumer;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseObservable<Listener> implements Observable<Listener> {

    // thread-safe set of listeners
    private final Set<Listener> listeners = Collections.newSetFromMap(new ConcurrentHashMap<>(1));

    @Override
    public final void registerListener(Listener listener) {
        listeners.add(listener);
    }

    @Override
    public final void unregisterListener(Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public final Set<Listener> getListeners() {
        return Collections.unmodifiableSet(listeners);
    }

    public void notifyListeners(Consumer<Listener> function) {
        for (Listener listener : getListeners()) {
            function.accept(listener);
        }
    }
}
