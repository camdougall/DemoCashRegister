package com.example.utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum Denomination {
    TWENTY_DOLLAR_BILL(new BigDecimal("20.00").setScale(2, RoundingMode.HALF_UP)),
    TEN_DOLLAR_BILL(new BigDecimal("10.00").setScale(2, RoundingMode.HALF_UP)),
    FIVE_DOLLAR_BILL(new BigDecimal("5.00").setScale(2, RoundingMode.HALF_UP)),
    TWO_DOLLAR_BILL(new BigDecimal("2.00").setScale(2, RoundingMode.HALF_UP)),
    ONE_DOLLAR_BILL(new BigDecimal("1.00").setScale(2, RoundingMode.HALF_UP)),
    FIFTY_CENT_PIECE(new BigDecimal("0.50").setScale(2, RoundingMode.HALF_UP)),
    QUARTER(new BigDecimal("0.25").setScale(2, RoundingMode.HALF_UP)),
    DIME(new BigDecimal("0.10").setScale(2, RoundingMode.HALF_UP)),
    NICKEL(new BigDecimal("0.05").setScale(2, RoundingMode.HALF_UP)),
    PENNY(new BigDecimal("0.01").setScale(2, RoundingMode.HALF_UP));

    private final BigDecimal value;
    private final String description;

    Denomination(BigDecimal value) {
        this.value = value;
        this.description = this.name().replace("_", " ");
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
