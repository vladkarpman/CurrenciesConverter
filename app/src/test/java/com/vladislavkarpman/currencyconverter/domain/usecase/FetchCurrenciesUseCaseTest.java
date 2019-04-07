package com.vladislavkarpman.currencyconverter.domain.usecase;

import com.vladislavkarpman.currencyconverter.DisposableTd;
import com.vladislavkarpman.currencyconverter.TestConstants;
import com.vladislavkarpman.currencyconverter.common.Disposable;
import com.vladislavkarpman.currencyconverter.common.SingleListener;
import com.vladislavkarpman.currencyconverter.domain.entity.Currency;
import com.vladislavkarpman.currencyconverter.repository.Repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static com.vladislavkarpman.currencyconverter.TestConstants.CURRENCY;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FetchCurrenciesUseCaseTest {

    // region constants ------------------------------------------------------------------------------------------------
    private static final Throwable ERROR = new Throwable();
    private static final DisposableTd DISPOSABLE = new DisposableTd();
    // endregion constants ---------------------------------------------------------------------------------------------

    // region helper fields --------------------------------------------------------------------------------------------
    @Mock
    Repository repositoryMock;

    @Mock
    SingleListener<List<Currency>> listenerMock;
    // endregion helper fields -----------------------------------------------------------------------------------------

    FetchCurrenciesUseCase SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new FetchCurrenciesUseCase(repositoryMock);
    }

    @Test
    public void execute_correctListenerPassedToRepository() {
        // Arrange
        ArgumentCaptor<SingleListener<List<Currency>>> argumentCaptor = ArgumentCaptor.forClass(SingleListener.class);
        success();
        // Act
        SUT.execute(listenerMock);
        // Assert
        verify(repositoryMock).getCurrenciesAsync(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue(), is(listenerMock));
    }

    @Test
    public void execute_correctDisposableReturned() {
        // Arrange
        disposableReturned();
        // Act
        Disposable disposable = SUT.execute(any());
        // Assert
        assertThat(DISPOSABLE, is(disposable));
    }

    @Test
    public void execute_success_passedListenerNotifiedWithCorrectData() {
        // Arrange
        ArgumentCaptor<List<Currency>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        success();
        // Act
        SUT.execute(listenerMock);
        // Assert
        verify(listenerMock).onSuccess(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue(), is(getCurrenciesList()));
    }

    @Test
    public void execute_error_passedListenerNotifiedWithCorrectData() {
        // Arrange
        ArgumentCaptor<Throwable> argumentCaptor = ArgumentCaptor.forClass(Throwable.class);
        error();
        // Act
        SUT.execute(listenerMock);
        // Assert
        verify(listenerMock).onError(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue(), is(ERROR));
    }

    private List<Currency> getCurrenciesList() {
        List<Currency> currencies = new ArrayList<>();
        currencies.add(CURRENCY);
        return currencies;
    }

    // region helper methods -------------------------------------------------------------------------------------------
    private void success() {
        doAnswer(invocation -> {
            SingleListener<List<Currency>> listener = invocation.getArgument(0);
            listener.onSuccess(getCurrenciesList());
            return null;
        }).when(repositoryMock).getCurrenciesAsync(any());
    }

    private void disposableReturned() {
        when(repositoryMock.getCurrenciesAsync(any())).thenReturn(DISPOSABLE);
    }

    private void error() {
        doAnswer(invocation -> {
            SingleListener<List<Currency>> listener = invocation.getArgument(0);
            listener.onError(ERROR);
            return null;
        }).when(repositoryMock).getCurrenciesAsync(any());
    }
    // endregion helper methods ----------------------------------------------------------------------------------------

    // region helper classes -------------------------------------------------------------------------------------------

    // endregion helper classes ----------------------------------------------------------------------------------------
}
