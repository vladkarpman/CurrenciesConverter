package com.vladislavkarpman.currencyconverter.common.observable;

import java.util.Set;

public interface Observable<Listener> {

    void registerListener(Listener listener);

    void unregisterListener(Listener listener);

    Set<Listener> getListeners();
}
