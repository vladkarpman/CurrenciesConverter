package com.vladislavkarpman.currencyconverter.repository;

import com.vladislavkarpman.currencyconverter.common.NoDataError;
import com.vladislavkarpman.currencyconverter.common.SingleListener;
import com.vladislavkarpman.currencyconverter.domain.entity.Currency;
import com.vladislavkarpman.currencyconverter.repository.currencies.CurrenciesSchema;
import com.vladislavkarpman.currencyconverter.repository.local.LocalDataSource;
import com.vladislavkarpman.currencyconverter.repository.remote.RemoteDataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.concurrent.ExecutorService;

import static com.vladislavkarpman.currencyconverter.TestConstants.CURRENCIES_LIST;
import static com.vladislavkarpman.currencyconverter.TestConstants.CURRENCIES_SCHEMA;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryImplTest {

    // region constants ------------------------------------------------------------------------------------------------

    // endregion constants ---------------------------------------------------------------------------------------------

    // region helper fields --------------------------------------------------------------------------------------------
    @Mock
    ExecutorService executorService;

    @Mock
    LocalDataSource localDataSourceMock;

    @Mock
    RemoteDataSource remoteDataSourceMock;

    @Mock
    SingleListener<List<Currency>> listSingleListenerMock;
    // endregion helper fields -----------------------------------------------------------------------------------------

    RepositoryImpl SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new RepositoryImpl(
                executorService,
                localDataSourceMock,
                remoteDataSourceMock
        );
    }

    @Test(expected = Exception.class)
    public void getCurrencies_error_exceptionIsThrown() {
        // Arrange
        executeError();
        // Act
        SUT.getCurrenciesAsync(listSingleListenerMock);
        // Assert
    }

    @Test
    public void getCurrenciesAsync_remoteSuccessLocalSuccess_listenerNotifiedWithCorrectData() {
        // Arrange
        ArgumentCaptor<List<Currency>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        executeSuccess();
        getRemoteSuccess();
        // Act
        SUT.getCurrenciesAsync(listSingleListenerMock);
        // Assert
        verify(listSingleListenerMock).onSuccess(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue(), is(CURRENCIES_LIST));
    }

    @Test
    public void getCurrenciesAsync_remoteSuccessLocalError_listenerNotifiedWithCorrectData() {
        // Arrange
        ArgumentCaptor<List<Currency>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        executeSuccess();
        getRemoteSuccess();
        saveLocalError();
        // Act
        SUT.getCurrenciesAsync(listSingleListenerMock);
        // Assert
        verify(listSingleListenerMock).onSuccess(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue(), is(CURRENCIES_LIST));
    }

    @Test
    public void getCurrenciesAsync_remoteErrorLocalError_listenerNotifiedWithNoDataError() {
        // Arrange
        ArgumentCaptor<Exception> argumentCaptor = ArgumentCaptor.forClass(Exception.class);
        executeSuccess();
        getRemoteError();
        getLocalError();
        // Act
        SUT.getCurrenciesAsync(listSingleListenerMock);
        // Assert
        verify(listSingleListenerMock).onError(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue(), instanceOf(NoDataError.class));
    }

    @Test
    public void getCurrenciesAsync_remoteErrorLocalSuccess_listenerNotifiedWithCorrectData() {
        // Arrange
        ArgumentCaptor<List<Currency>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        executeSuccess();
        getRemoteError();
        getLocalSuccess();
        // Act
        SUT.getCurrenciesAsync(listSingleListenerMock);
        // Assert
        verify(listSingleListenerMock).onSuccess(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue(), is(CURRENCIES_LIST));
    }

    @Test
    public void getCurrenciesAsync_remoteSuccess_saveRemoteData() {
        // Arrange
        ArgumentCaptor<CurrenciesSchema> argumentCaptor = ArgumentCaptor.forClass(CurrenciesSchema.class);
        executeSuccess();
        getRemoteSuccess();
        // Act
        SUT.getCurrenciesAsync(listSingleListenerMock);
        // Assert
        verify(localDataSourceMock).saveCurrenciesSync(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue(), is(CURRENCIES_SCHEMA));
    }

    @Test
    public void getCurrenciesAsync_remoteError_noSaveRemoteData() {
        // Arrange
        executeSuccess();
        getRemoteError();
        // Act
        SUT.getCurrenciesAsync(listSingleListenerMock);
        // Assert
        verify(localDataSourceMock, times(0)).saveCurrenciesSync(any());
    }

    @Test
    public void getCurrenciesAsync_remoteError_getRemoteData() {
        // Arrange
        executeSuccess();
        getLocalSuccess();
        getRemoteError();
        // Act
        SUT.getCurrenciesAsync(listSingleListenerMock);
        // Assert
        verify(localDataSourceMock).getCurrenciesSync();
    }

    // region helper methods -------------------------------------------------------------------------------------------

    private void executeSuccess() {
        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return null;
        }).when(executorService).submit(any(Runnable.class));
    }

    private void executeError() {
        doThrow(Exception.class).when(executorService).submit(any(Runnable.class));
    }

    private void saveLocalError() {
        willAnswer(invocation -> {
            throw new Exception();
        }).given(localDataSourceMock).saveCurrenciesSync(any());
    }

    private void getRemoteSuccess() {
        when(remoteDataSourceMock.getCurrenciesSync()).thenReturn(CURRENCIES_SCHEMA);
    }

    private void getRemoteError() {
        when(remoteDataSourceMock.getCurrenciesSync()).thenReturn(null);
    }

    private void getLocalSuccess() {
        when(localDataSourceMock.getCurrenciesSync()).thenReturn(CURRENCIES_SCHEMA);
    }

    private void getLocalError() {
        when(localDataSourceMock.getCurrenciesSync()).thenReturn(null);
    }
    // endregion helper methods ----------------------------------------------------------------------------------------

    // region helper classes -------------------------------------------------------------------------------------------

    // endregion helper classes ----------------------------------------------------------------------------------------
}
