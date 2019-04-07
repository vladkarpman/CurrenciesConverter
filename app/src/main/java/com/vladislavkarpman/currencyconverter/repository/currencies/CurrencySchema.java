package com.vladislavkarpman.currencyconverter.repository.currencies;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Valute")
public class CurrencySchema {

    @Attribute(name = "ID")
    private String id;

    @Element(name = "NumCode")
    private int numCode;

    @Element(name = "CharCode")
    private String charCode;

    @Element(name = "Nominal")
    private int nominal;

    @Element(name = "Name")
    private String name;

    @Element(name = "Value")
    private String value;

    public int getNumCode() {
        return numCode;
    }

    public CurrencySchema setNumCode(int numCode) {
        this.numCode = numCode;
        return this;
    }

    public String getCharCode() {
        return charCode;
    }

    public CurrencySchema setCharCode(String charCode) {
        this.charCode = charCode;
        return this;
    }

    public int getNominal() {
        return nominal;
    }

    public CurrencySchema setNominal(int nominal) {
        this.nominal = nominal;
        return this;
    }

    public String getName() {
        return name;
    }

    public CurrencySchema setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public CurrencySchema setValue(String value) {
        this.value = value;
        return this;
    }
}
