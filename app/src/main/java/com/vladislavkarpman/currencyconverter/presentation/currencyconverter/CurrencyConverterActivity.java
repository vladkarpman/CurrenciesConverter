package com.vladislavkarpman.currencyconverter.presentation.currencyconverter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vladislavkarpman.currencyconverter.R;
import com.vladislavkarpman.currencyconverter.common.NoDataError;
import com.vladislavkarpman.currencyconverter.common.viewmodel.ViewModelActivity;
import com.vladislavkarpman.currencyconverter.domain.entity.Currency;
import com.vladislavkarpman.currencyconverter.domain.usecase.ConvertCurrenciesUseCase;
import com.vladislavkarpman.currencyconverter.domain.usecase.FormatCurrencyUseCase;

import java.text.NumberFormat;
import java.util.List;

public class CurrencyConverterActivity
        extends ViewModelActivity<CurrencyConverterViewModel>
        implements CurrencyConverterView.Listener, CurrencyConverterViewModel.Listener {

    private CurrencyConverterView view;

    @Nullable
    private Currency currencyFrom;

    @Nullable
    private Currency currencyTo;

    private FormatCurrencyUseCase formatCurrencyUseCase;

    private ConvertCurrenciesUseCase convertCurrenciesUseCase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = getPresentationRoot()
                .getViewMvcFactory()
                .createCurrencyConverterView(null);

        formatCurrencyUseCase = getPresentationRoot().getFormatCurrencyUseCase();

        convertCurrenciesUseCase = getPresentationRoot().getConvertCurrenciesUseCase();

        setContentView(view.getRootView());
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.registerListener(this);
        view.registerListener(this);

        view.showLoading();
        viewModel.fetchCurrenciesAndNotify();
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.unregisterListener(this);
        view.unregisterListener(this);
    }

    @NonNull
    @Override
    public CurrencyConverterViewModel createViewModel() {
        return getPresentationRoot().getCurrencyConverterViewModel();
    }

    @Override
    public void onConvertButtonClicked() {
        if (currencyFrom == null || currencyTo == null) {
            return;
        }
        Double currentAmount = Double.parseDouble(view.getAmountToConvert());
        double convertedValue = convertCurrenciesUseCase.execute(currentAmount, currencyFrom, currencyTo);
        String formattedValue = formatCurrencyUseCase.execute(convertedValue, currencyTo);
        view.showConvertedValue(formattedValue);
    }

    @Override
    public void onCurrencyFromPicked(Currency currency) {
        currencyFrom = currency;
        showRelativeValues();
    }

    private void showRelativeValues() {
        if (currencyTo == null || currencyFrom == null) {
            return;
        }
        NumberFormat numberInstance = NumberFormat.getNumberInstance();
        numberInstance.setMaximumFractionDigits(2);

        view.showRelativeValueFrom(String.format(
                getString(R.string.relativeValueFormat),
                currencyFrom.getNominal(),
                currencyFrom.getCharCode(),
                numberInstance.format(currencyTo.getValue() / currencyFrom.getValue()),
                currencyTo.getCharCode()));

        view.showRelativeValueTo(String.format(
                getString(R.string.relativeValueFormat),
                currencyTo.getNominal(),
                currencyTo.getCharCode(),
                numberInstance.format(currencyFrom.getValue() / currencyTo.getValue()),
                currencyFrom.getCharCode()));
    }

    @Override
    public void onCurrencyToPicked(Currency currency) {
        currencyTo = currency;
        showRelativeValues();
    }

    @Override
    public void onTryAgainButtonClicked() {
        runOnUiThread(() -> {
            view.hideError();
            view.showLoading();
            viewModel.fetchCurrenciesAndNotify();
        });
    }

    @Override
    public void onCurrenciesFetched(List<Currency> currencies) {
        runOnUiThread(() -> {
            view.hideLoading();
            view.bindCurrencies(currencies);
        });
    }

    @Override
    public void onCurrenciesFetchedFailed(Throwable error) {
        runOnUiThread(() -> {
            view.hideLoading();
            view.showError(getErrorMessage(error));
        });
    }

    private String getErrorMessage(Throwable error) {
        if (error instanceof NoDataError) {
            return getString(R.string.error_message_no_data);
        }
        return getString(R.string.error_message_unknown);
    }
}
