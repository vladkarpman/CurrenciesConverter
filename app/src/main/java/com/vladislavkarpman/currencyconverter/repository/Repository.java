package com.vladislavkarpman.currencyconverter.repository;

import com.vladislavkarpman.currencyconverter.common.Disposable;
import com.vladislavkarpman.currencyconverter.common.SingleListener;
import com.vladislavkarpman.currencyconverter.domain.entity.Currency;

import java.util.List;

public interface Repository {

    Disposable getCurrenciesAsync(SingleListener<List<Currency>> singleListener);
}
