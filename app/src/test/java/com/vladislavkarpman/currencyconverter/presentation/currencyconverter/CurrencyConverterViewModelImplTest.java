package com.vladislavkarpman.currencyconverter.presentation.currencyconverter;

import com.vladislavkarpman.currencyconverter.DisposableTd;
import com.vladislavkarpman.currencyconverter.common.Disposable;
import com.vladislavkarpman.currencyconverter.common.SingleListener;
import com.vladislavkarpman.currencyconverter.domain.entity.Currency;
import com.vladislavkarpman.currencyconverter.domain.usecase.FetchCurrenciesUseCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static com.vladislavkarpman.currencyconverter.TestConstants.CURRENCIES_LIST;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyConverterViewModelImplTest {

    // region constants ------------------------------------------------------------------------------------------------
    private static final Throwable ERROR = new Throwable();
    private static final DisposableTd DISPOSABLE = new DisposableTd();

    // endregion constants ---------------------------------------------------------------------------------------------

    // region helper fields --------------------------------------------------------------------------------------------
    @Mock
    FetchCurrenciesUseCase fetchCurrenciesUseCaseMock;

    @Mock
    CurrencyConverterViewModel.Listener listener1;

    @Mock
    CurrencyConverterViewModel.Listener listener2;

    @Captor
    ArgumentCaptor<List<Currency>> currenciesArgumentCaptor;

    @Captor
    ArgumentCaptor<Throwable> currenciesErrorCaptor;

    @Mock
    private Disposable disposableMock;

    // endregion helper fields -----------------------------------------------------------------------------------------

    private CurrencyConverterViewModelImpl SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new CurrencyConverterViewModelImpl(fetchCurrenciesUseCaseMock);
    }

    @Test
    public void fetchCurrenciesAndNotify_success_observersNotifiedWithCorrectData() {
        // Arrange
        success();
        // Act
        SUT.registerListener(listener1);
        SUT.registerListener(listener2);
        SUT.fetchCurrenciesAndNotify();
        // Assert
        verify(listener1).onCurrenciesFetched(currenciesArgumentCaptor.capture());
        verify(listener2).onCurrenciesFetched(currenciesArgumentCaptor.capture());
        List<List<Currency>> captures = currenciesArgumentCaptor.getAllValues();
        List<Currency> capture1 = captures.get(0);
        List<Currency> capture2 = captures.get(1);
        assertThat(capture1, is(CURRENCIES_LIST));
        assertThat(capture2, is(CURRENCIES_LIST));
    }

    @Test
    public void fetchCurrenciesAndNotify_error_observersNotifiedAboutError() {
        // Arrange
        error();
        // Act
        SUT.registerListener(listener1);
        SUT.registerListener(listener2);
        SUT.fetchCurrenciesAndNotify();
        // Assert
        verify(listener1).onCurrenciesFetchedFailed(currenciesErrorCaptor.capture());
        verify(listener2).onCurrenciesFetchedFailed(currenciesErrorCaptor.capture());
        List<Throwable> captures = currenciesErrorCaptor.getAllValues();
        Throwable capture1 = captures.get(0);
        Throwable capture2 = captures.get(1);
        assertThat(ERROR, is(capture1));
        assertThat(ERROR, is(capture2));
    }

    @Test
    public void fetchCurrenciesAndNotify_success_unsubscribedObserverNotNotified() {
        // Arrange
        success();
        // Act
        SUT.registerListener(listener1);
        SUT.registerListener(listener2);
        SUT.unregisterListener(listener2);
        SUT.fetchCurrenciesAndNotify();
        // Assert
        verifyZeroInteractions(listener2);
        verify(listener1).onCurrenciesFetched(currenciesArgumentCaptor.capture());
        assertThat(CURRENCIES_LIST, is(currenciesArgumentCaptor.getValue()));
    }

    @Test
    public void fetchCurrenciesAndNotify_doubleMethodCall_fetchingPerformedOnce() {
        // Act
        SUT.fetchCurrenciesAndNotify();
        SUT.fetchCurrenciesAndNotify();
        // Assert
        verify(fetchCurrenciesUseCaseMock, only()).execute(any());
    }

    @Test
    public void onClear_fetchCurrenciesCalled_disposableCalledDispose() {
        // Arrange
        disposableMockReturned();
        // Act
        SUT.fetchCurrenciesAndNotify();
        SUT.onClear();
        // Assert
        verify(disposableMock).dispose();
    }

    @Test
    public void onClear_fetchCurrenciesAndNotify_disposableIdDisposed() {
        // Arrange
        disposableTdReturned();
        // Act
        SUT.fetchCurrenciesAndNotify();
        SUT.onClear();
        // Assert
        assertThat(DISPOSABLE.isDisposed(), is(true));
    }

    // region helper methods -------------------------------------------------------------------------------------------
    private void success() {
        doAnswer(invocation -> {
            SingleListener<List<Currency>> listener = invocation.getArgument(0);
            listener.onSuccess(CURRENCIES_LIST);
            return null;
        }).when(fetchCurrenciesUseCaseMock).execute(any());
    }

    private void disposableTdReturned() {
        when(fetchCurrenciesUseCaseMock.execute(any())).thenReturn(DISPOSABLE);
    }

    private void disposableMockReturned() {
        when(fetchCurrenciesUseCaseMock.execute(any())).thenReturn(disposableMock);
    }

    private void error() {
        doAnswer(invocation -> {
            SingleListener<List<Currency>> listener = invocation.getArgument(0);
            listener.onError(ERROR);
            return null;
        }).when(fetchCurrenciesUseCaseMock).execute(any());
    }
    // endregion helper methods ----------------------------------------------------------------------------------------

    // region helper classes -------------------------------------------------------------------------------------------

    // endregion helper classes ----------------------------------------------------------------------------------------
}
