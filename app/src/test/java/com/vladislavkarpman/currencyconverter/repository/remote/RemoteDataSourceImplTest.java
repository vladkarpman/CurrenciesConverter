package com.vladislavkarpman.currencyconverter.repository.remote;

import com.vladislavkarpman.currencyconverter.repository.currencies.CurrenciesSchema;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.simpleframework.xml.Serializer;

import java.io.InputStream;

import static com.vladislavkarpman.currencyconverter.TestConstants.CURRENCIES_SCHEMA;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RemoteDataSourceImplTest {

    // region constants ------------------------------------------------------------------------------------------------
    private static final String URL = "http://www.cbr.ru/scripts/XML_daily.asp";

    // endregion constants ---------------------------------------------------------------------------------------------

    // region helper fields --------------------------------------------------------------------------------------------
    @Mock
    Serializer serializerMock;
    // endregion helper fields -----------------------------------------------------------------------------------------

    RemoteDataSourceImpl SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new RemoteDataSourceImpl(serializerMock, URL);
    }

    @Test
    public void getCurrenciesSync_passedCorrectParametersToSerializer() throws Exception {
        // Arrange
        ArgumentCaptor<Class> schemaArgumentCaptor = ArgumentCaptor.forClass(Class.class);
        ArgumentCaptor<InputStream> inputStreamArgumentCaptor = ArgumentCaptor.forClass(InputStream.class);
        success();
        // Act
        SUT.getCurrenciesSync();
        // Assert
        verify(serializerMock).read(schemaArgumentCaptor.capture(), inputStreamArgumentCaptor.capture());
        assertThat(schemaArgumentCaptor.getValue(), equalTo(CurrenciesSchema.class));
        assertThat(inputStreamArgumentCaptor.getValue(), instanceOf(InputStream.class));
    }

    @Test
    public void getCurrenciesSync_success_returnedCorrectValue() throws Exception {
        // Arrange
        success();
        // Act
        CurrenciesSchema result = SUT.getCurrenciesSync();
        // Assert
        assertThat(result, is(CURRENCIES_SCHEMA));
    }

    @Test
    public void getCurrencies_error_returnedNullValue() throws Exception {
        // Arrange
        error();
        // Act
        CurrenciesSchema result = SUT.getCurrenciesSync();
        // Assert
        assertThat(result, is(nullValue()));
    }

    // region helper methods -------------------------------------------------------------------------------------------
    private void success() throws Exception {
        when(serializerMock.read(any(), any(InputStream.class)))
                .thenReturn(CURRENCIES_SCHEMA);
    }

    private void error() throws Exception {
        doThrow(Exception.class)
                .when(serializerMock).read(any(), any(InputStream.class));
    }
    // endregion helper methods ----------------------------------------------------------------------------------------

    // region helper classes -------------------------------------------------------------------------------------------

    // endregion helper classes ----------------------------------------------------------------------------------------
}
