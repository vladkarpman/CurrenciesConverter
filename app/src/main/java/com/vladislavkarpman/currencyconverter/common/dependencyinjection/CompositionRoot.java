package com.vladislavkarpman.currencyconverter.common.dependencyinjection;

import android.app.Application;

import com.vladislavkarpman.currencyconverter.domain.entity.CurrencyConverter;
import com.vladislavkarpman.currencyconverter.domain.entity.CurrencyFormatter;
import com.vladislavkarpman.currencyconverter.domain.entity.CurrencyFormatterImpl;
import com.vladislavkarpman.currencyconverter.domain.usecase.ConvertCurrenciesUseCase;
import com.vladislavkarpman.currencyconverter.domain.usecase.FetchCurrenciesUseCase;
import com.vladislavkarpman.currencyconverter.domain.usecase.FormatCurrencyUseCase;
import com.vladislavkarpman.currencyconverter.presentation.currencyconverter.CurrencyConverterViewModel;
import com.vladislavkarpman.currencyconverter.presentation.currencyconverter.CurrencyConverterViewModelImpl;
import com.vladislavkarpman.currencyconverter.repository.Repository;
import com.vladislavkarpman.currencyconverter.repository.RepositoryImpl;
import com.vladislavkarpman.currencyconverter.repository.local.LocalDataSource;
import com.vladislavkarpman.currencyconverter.repository.local.LocalDataSourceImpl;
import com.vladislavkarpman.currencyconverter.repository.remote.RemoteDataSource;
import com.vladislavkarpman.currencyconverter.repository.remote.RemoteDataSourceImpl;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.vladislavkarpman.currencyconverter.Constants.CACHE_FILE_NAME;
import static com.vladislavkarpman.currencyconverter.Constants.DATA_URL;

public class CompositionRoot {

    private ExecutorService executor;
    private LocalDataSource localDataSource;
    private RemoteDataSource remoteDataSource;
    private File cacheFile;
    private Serializer serializer;
    private CurrencyFormatter currencyFormatter;
    private CurrencyConverter currencyConverter;

    private final Application application;
    private ConvertCurrenciesUseCase convertCurrenciesUseCase;
    private FetchCurrenciesUseCase fetchCurrenciesUseCase;
    private FormatCurrencyUseCase formatCurrencyUseCase;

    public CompositionRoot(Application application) {
        this.application = application;
    }

    private Repository provideRepository() {
        return new RepositoryImpl(provideExecutorService(), provideLocalDataSource(), provideRemoteDataSource());
    }

    private LocalDataSource provideLocalDataSource() {
        if (localDataSource == null) {
            localDataSource = new LocalDataSourceImpl(
                    provideSerializer(),
                    provideCacheFile()
            );
        }
        return localDataSource;
    }

    CurrencyConverterViewModel provideCurrencyConverterViewModel() {
        return new CurrencyConverterViewModelImpl(provideFetchCurrenciesUseCase());
    }

    ConvertCurrenciesUseCase provideConvertCurrenciesUseCase() {
        if (convertCurrenciesUseCase == null) {
            convertCurrenciesUseCase = new ConvertCurrenciesUseCase(provideCurrencyConverter());
        }
        return convertCurrenciesUseCase;
    }

    FetchCurrenciesUseCase provideFetchCurrenciesUseCase() {
        if (fetchCurrenciesUseCase == null) {
            fetchCurrenciesUseCase = new FetchCurrenciesUseCase(provideRepository());
        }
        return fetchCurrenciesUseCase;
    }

    FormatCurrencyUseCase provideFormatCurrencyUseCase() {
        if (formatCurrencyUseCase == null) {
            formatCurrencyUseCase = new FormatCurrencyUseCase(provideCurrencyFormatter());
        }
        return formatCurrencyUseCase;
    }

    private File provideCacheFile() {
        if (cacheFile == null) {
            cacheFile = new File(application.getFilesDir(), CACHE_FILE_NAME);
            if (!cacheFile.exists()) {
                try {
                    cacheFile.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException("can't create cache file " + e.getMessage());
                }
            }
        }
        return cacheFile;
    }

    private CurrencyFormatter provideCurrencyFormatter() {
        if (currencyFormatter == null) {
            currencyFormatter = new CurrencyFormatterImpl();
        }
        return currencyFormatter;
    }

    private CurrencyConverter provideCurrencyConverter() {
        if (currencyConverter == null) {
            currencyConverter = new com.vladislavkarpman.currencyconverter.domain.entity.CurrencyConverterImpl();
        }
        return currencyConverter;
    }

    private RemoteDataSource provideRemoteDataSource() {
        if (remoteDataSource == null) {
            remoteDataSource = new RemoteDataSourceImpl(provideSerializer(), DATA_URL);
        }
        return remoteDataSource;
    }

    private Serializer provideSerializer() {
        if (serializer == null) {
            serializer = new Persister();
        }
        return serializer;
    }

    private ExecutorService provideExecutorService() {
        if (executor == null) {
            executor = Executors.newCachedThreadPool();
        }
        return executor;
    }
}
