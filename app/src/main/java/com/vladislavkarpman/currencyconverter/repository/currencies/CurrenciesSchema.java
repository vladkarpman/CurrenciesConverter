package com.vladislavkarpman.currencyconverter.repository.currencies;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "ValCurs")
public class CurrenciesSchema {

    @Attribute
    private String name;

    @Attribute(name = "Date")
    private String date;

    @ElementList(inline = true)
    private List<CurrencySchema> currenciesSchemaList;

    public List<CurrencySchema> getCurrenciesSchemaList() {
        return currenciesSchemaList;
    }

    public String getName() {
        return name;
    }

    public CurrenciesSchema setName(String name) {
        this.name = name;
        return this;
    }

    public CurrenciesSchema setDate(String date) {
        this.date = date;
        return this;
    }

    public CurrenciesSchema setCurrenciesSchemaList(List<CurrencySchema> currenciesSchemaList) {
        this.currenciesSchemaList = currenciesSchemaList;
        return this;
    }
}
