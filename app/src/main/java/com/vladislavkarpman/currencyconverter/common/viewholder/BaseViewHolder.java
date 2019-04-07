package com.vladislavkarpman.currencyconverter.common.viewholder;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.View;

public abstract class BaseViewHolder implements ViewHolder {
    
    private View rootView;

    @Override
    public View getRootView() {
        return rootView;
    }

    protected void setRootView(View rootView) {
        this.rootView = rootView;
    }

    protected <T extends View> T findViewById(int id) {
        return getRootView().findViewById(id);
    }

    protected Context getContext() {
        return getRootView().getContext();
    }

    protected String getString(@StringRes int id) {
        return getContext().getString(id);
    }
}
