package com.vladislavkarpman.currencyconverter.common.viewmodel;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vladislavkarpman.currencyconverter.common.BaseActivity;

public abstract class ViewModelActivity<VM extends ViewModel> extends BaseActivity {

    protected VM viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelConfigurationInstance<VM> nc =
                (ViewModelConfigurationInstance<VM>) getLastCustomNonConfigurationInstance();
        if (nc != null && nc.viewModel != null) {
            viewModel = nc.viewModel;
        } else {
            viewModel = createViewModel();
        }
    }

    @NonNull
    public abstract VM createViewModel();

    @Override
    protected void onDestroy() {
        if (viewModel != null && !isChangingConfigurations()) {
            viewModel.onClear();
            viewModel = null;
        }
        super.onDestroy();
    }

    @Override
    public final Object onRetainCustomNonConfigurationInstance() {
        if (viewModel != null) {
            ViewModelConfigurationInstance<VM> viewModelConfigurationInstance = new ViewModelConfigurationInstance<>();
            viewModelConfigurationInstance.viewModel = viewModel;
            return viewModelConfigurationInstance;
        }
        return super.onRetainCustomNonConfigurationInstance();
    }

    private static class ViewModelConfigurationInstance<VM extends ViewModel> {
        @Nullable
        private VM viewModel;
    }
}
