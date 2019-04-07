package com.vladislavkarpman.currencyconverter.presentation.currencyconverter;

import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.vladislavkarpman.currencyconverter.R;
import com.vladislavkarpman.currencyconverter.common.viewholder.BaseObservableViewHolder;
import com.vladislavkarpman.currencyconverter.domain.entity.Currency;

import java.util.List;

public class CurrencyConverterViewImpl
        extends BaseObservableViewHolder<CurrencyConverterView.Listener>
        implements CurrencyConverterView {

    private AppCompatSpinner currencyFromSpinner;
    private AppCompatEditText convertAmountEditText;
    private AppCompatSpinner currencyToSpinner;
    private TextView convertedValue;
    private View convertButton;
    private TextView relativeValueFrom;
    private TextView relativeValueTo;
    private View errorMessageContainer;
    private View tryAgainButton;
    private TextView errorMessageText;
    private Group currenciesGroup;
    private View currenciesLoading;

    public CurrencyConverterViewImpl(
            LayoutInflater layoutInflater,
            ViewGroup parent) {
        setRootView(layoutInflater.inflate(R.layout.currency_converter_view, parent));
        findViews();

        currencyFromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Currency currency = (Currency) parent.getItemAtPosition(position);
                notifyListeners(listener -> listener.onCurrencyFromPicked(currency));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        currencyToSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Currency currency = (Currency) parent.getItemAtPosition(position);
                notifyListeners(listener -> listener.onCurrencyToPicked(currency));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        convertButton.setOnClickListener(v -> notifyListeners(Listener::onConvertButtonClicked));

        tryAgainButton.setOnClickListener(v -> notifyListeners(Listener::onTryAgainButtonClicked));

        convertAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                convertButton.setEnabled(s.length() > 0);
            }
        });

        errorMessageContainer.setOnTouchListener((v, event) -> true);
    }

    private void findViews() {
        currenciesLoading = findViewById(R.id.currenciesLoadingIndicator);
        errorMessageText = findViewById(R.id.errorMessage);
        errorMessageContainer = findViewById(R.id.errorMessageContainer);
        tryAgainButton = findViewById(R.id.tryAgainButton);
        currenciesGroup = findViewById(R.id.currenciesGroup);
        convertAmountEditText = findViewById(R.id.convertAmountEditText);
        currencyFromSpinner = findViewById(R.id.currencyFromSpinner);
        currencyToSpinner = findViewById(R.id.currencyToSpinner);
        convertButton = findViewById(R.id.convertButton);
        convertedValue = findViewById(R.id.convertedValue);
        relativeValueFrom = findViewById(R.id.relativeValueFrom);
        relativeValueTo = findViewById(R.id.relativeValueTo);
    }

    @NonNull
    @Override
    public String getAmountToConvert() {
        return convertAmountEditText.getText().toString();
    }

    @Override
    public void bindCurrencies(List<Currency> currencies) {
        CurrencySpinnerAdapter spinnerAdapter = new CurrencySpinnerAdapter(getContext(), currencies);
        currencyFromSpinner.setAdapter(spinnerAdapter);
        currencyToSpinner.setAdapter(spinnerAdapter);
    }

    @Override
    public void showConvertedValue(String convertedValue) {
        this.convertedValue.setText(convertedValue);
    }

    @Override
    public void showRelativeValueTo(String relativeValue) {
        relativeValueTo.setText(relativeValue);
    }

    @Override
    public void showRelativeValueFrom(String relativeValue) {
        relativeValueFrom.setText(relativeValue);
    }

    @Override
    public void showLoading() {
        currenciesGroup.setVisibility(View.GONE);
        currenciesLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        currenciesLoading.setVisibility(View.GONE);
        currenciesGroup.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideError() {
        errorMessageContainer.animate()
                .alpha(0f)
                .withEndAction(() -> {
                    errorMessageContainer.setVisibility(View.GONE);
                })
                .start();
    }

    @Override
    public void showError(String errorMessage) {
        errorMessageText.setText(errorMessage);
        errorMessageContainer.animate()
                .withStartAction(() -> {
                    errorMessageContainer.setAlpha(0);
                    errorMessageContainer.setVisibility(View.VISIBLE);
                })
                .alpha(1f)
                .start();
    }
}
