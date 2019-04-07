package com.vladislavkarpman.currencyconverter.presentation.currencyconverter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vladislavkarpman.currencyconverter.R;
import com.vladislavkarpman.currencyconverter.domain.entity.Currency;

import java.util.List;

public class CurrencySpinnerAdapter extends ArrayAdapter<Currency> {

    CurrencySpinnerAdapter(@NonNull Context context, @NonNull List<Currency> currencies) {
        super(context, R.layout.currency_item, currencies);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View view;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.currency_popup_item, parent, false);
        } else {
            view = convertView;
        }

        final TextView shortName = view.findViewById(R.id.currencyShortName);
        final TextView fullName = view.findViewById(R.id.currencyFullName);

        Currency currency = getItem(position);

        fullName.setText(currency.getName());
        shortName.setText(currency.getCharCode());

        return view;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View view;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.currency_item, parent, false);
        } else {
            view = convertView;
        }

        final TextView name = view.findViewById(R.id.currencyName);

        Currency currency = getItem(position);

        name.setText(currency.getCharCode());

        return view;
    }


}
