package com.vladislavkarpman.currencyconverter.common.viewholder;

public interface ObservableViewHolder<Listener> extends ViewHolder {

    void registerListener(Listener listener);

    void unregisterListener(Listener listener);
}
