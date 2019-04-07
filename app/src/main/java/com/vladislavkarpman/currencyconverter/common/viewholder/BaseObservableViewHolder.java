package com.vladislavkarpman.currencyconverter.common.viewholder;

import android.support.v4.util.Consumer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public abstract class BaseObservableViewHolder<Listener>
        extends BaseViewHolder
        implements ObservableViewHolder<Listener> {

    private Set<Listener> listeners = Collections.newSetFromMap(new ConcurrentHashMap<>(1));

    @Override
    public final void registerListener(Listener listener) {
        listeners.add(listener);
    }

    @Override
    public final void unregisterListener(Listener listener) {
        listeners.remove(listener);
    }

    protected final Set<Listener> getListeners() {
        return Collections.unmodifiableSet(listeners);
    }

    protected void notifyListeners(Consumer<Listener> function) {
        for (Listener listener : getListeners()) {
            function.accept(listener);
        }
    }
}
