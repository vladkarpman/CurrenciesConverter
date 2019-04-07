package com.vladislavkarpman.currencyconverter.repository.local;

import com.vladislavkarpman.currencyconverter.repository.currencies.CurrenciesSchema;
import com.vladislavkarpman.currencyconverter.repository.currencies.CurrencySchema;

import org.junit.*;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;

import static com.vladislavkarpman.currencyconverter.TestConstants.CURRENCIES_SCHEMA;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;

import org.simpleframework.xml.Serializer;

import java.io.File;
import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class LocalDataSourceImplTest {

    // region constants ------------------------------------------------------------------------------------------------
    private static final String FILE_PATH = "file_path";
    private static final File FILE = new File(FILE_PATH);
    // endregion constants ---------------------------------------------------------------------------------------------

    // region helper fields --------------------------------------------------------------------------------------------
    @Mock
    Serializer serializerMock;

    // endregion helper fields -----------------------------------------------------------------------------------------

    LocalDataSourceImpl SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new LocalDataSourceImpl(serializerMock, FILE);
    }

    @Test
    public void saveCurrenciesSync_passedToSerializerCorrectParameters() throws Exception {
        ArgumentCaptor<CurrenciesSchema> schemaArgumentCaptor = ArgumentCaptor.forClass(CurrenciesSchema.class);
        ArgumentCaptor<File> fileArgumentCaptor = ArgumentCaptor.forClass(File.class);
        // Arrange
        saveSuccess();
        // Act
        SUT.saveCurrenciesSync(CURRENCIES_SCHEMA);
        // Assert
        verify(serializerMock).write(schemaArgumentCaptor.capture(), fileArgumentCaptor.capture());
        assertThat(schemaArgumentCaptor.getValue(), is(CURRENCIES_SCHEMA));
        assertThat(fileArgumentCaptor.getValue(), is(FILE));
    }

    @Test
    public void getCurrenciesSync_passedToSerializerCorrectParameters() throws Exception {
        ArgumentCaptor<Class> schemaArgumentCaptor = ArgumentCaptor.forClass(Class.class);
        ArgumentCaptor<File> fileArgumentCaptor = ArgumentCaptor.forClass(File.class);
        // Arrange
        getSuccess();
        // Act
        SUT.getCurrenciesSync();
        // Assert
        verify(serializerMock).read(schemaArgumentCaptor.capture(), fileArgumentCaptor.capture());
        assertThat(schemaArgumentCaptor.getValue(), equalTo(CurrenciesSchema.class));
        assertThat(fileArgumentCaptor.getValue(), is(FILE));
    }

    @Test
    public void getCurrenciesSync_successSaveCurrenciesSync_getSavedValue() throws Exception {
        // Arrange
        saveSuccess();
        getSuccess();
        // Act
        SUT.saveCurrenciesSync(CURRENCIES_SCHEMA);
        CurrenciesSchema result = SUT.getCurrenciesSync();
        // Assert
        assertThat(result, is(CURRENCIES_SCHEMA));
    }

    @Test()
    public void getCurrenciesSync_errorSaveCurrenciesSync_getNullSavedValue() throws Exception {
        // Arrange
        saveError();
        getError();
        // Act
        SUT.saveCurrenciesSync(CURRENCIES_SCHEMA);
        CurrenciesSchema schema = SUT.getCurrenciesSync();
        // Assert
        assertThat(schema, is(nullValue()));
    }

    // region helper methods -------------------------------------------------------------------------------------------

    private void saveSuccess() throws Exception {
        doNothing().when(serializerMock)
                .write(any(), any(File.class));
    }

    private void saveError() throws Exception {
        doThrow(Exception.class).when(serializerMock).write(any(), any(File.class));
    }

    private void getSuccess() throws Exception {
        when(serializerMock.read(any(), any(File.class)))
                .thenReturn(CURRENCIES_SCHEMA);
    }

    private void getError() throws Exception {
        when(serializerMock.read(any(), any(File.class)))
                .thenReturn(null);
    }
    // endregion helper methods ----------------------------------------------------------------------------------------

    // region helper classes -------------------------------------------------------------------------------------------

    // endregion helper classes ----------------------------------------------------------------------------------------
}
