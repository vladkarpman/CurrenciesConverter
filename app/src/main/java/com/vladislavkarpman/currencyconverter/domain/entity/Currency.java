package com.vladislavkarpman.currencyconverter.domain.entity;

import java.util.Objects;

public class Currency {

    private int numCode;
    private String charCode;
    private int nominal;
    private String name;
    private double value;

    public Currency setNumCode(int numCode) {
        this.numCode = numCode;
        return this;
    }

    public Currency setCharCode(String charCode) {
        this.charCode = charCode;
        return this;
    }

    public Currency setNominal(int nominal) {
        this.nominal = nominal;
        return this;
    }

    public Currency setName(String name) {
        this.name = name;
        return this;
    }

    public Currency setValue(double value) {
        this.value = value;
        return this;
    }

    public int getNumCode() {
        return numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public int getNominal() {
        return nominal;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return numCode == currency.numCode &&
                nominal == currency.nominal &&
                Double.compare(currency.value, value) == 0 &&
                Objects.equals(charCode, currency.charCode) &&
                Objects.equals(name, currency.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numCode, charCode, nominal, name, value);
    }
}
