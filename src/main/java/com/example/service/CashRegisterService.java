package com.example.service;

import com.example.dao.RegisterDAO;
import com.example.dto.Money;
import com.example.utilities.InsufficientFundsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.utilities.Denomination;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

@Service
public class CashRegisterService {
    @Autowired
    private RegisterDAO register;

    /**
     * This method is for getting change from a transaction
     *
     * @param price   the price of the good or services rendered in US $
     * @param payment the amount of payment received in US $
     * @return A List of every denomination and the number of that denomination
     */
    public List<Money> getChange(BigDecimal price, BigDecimal payment) {
        BigDecimal amountNeeded = getAmountOfChange(price, payment);
        EnumMap<Denomination, Integer> owed = getActualAmountFromRegister(amountNeeded);
        register.updateRegisterWithAmountRemoved(owed);
        register.updateRegisterWithAmountAdded(calculateDenominationsPayment(payment));
        return convertChangeOwedToMoneyExchange(owed);
    }

    /**
     * This returns the amount of change owed in US $
     * Insufficient funds exception if price is greater than payment
     *
     * @param price        the price of the good or service in US $
     * @param cashReceived the amount of money received in US $
     * @return A BigDecimal representation of change owed
     */
    protected BigDecimal getAmountOfChange(BigDecimal price, BigDecimal cashReceived) {
        // throw exception if cashReceived < price
        if (price.compareTo(cashReceived) > 0) {
            throw new InsufficientFundsException("The price is larger than the payment amount.");
        }
        // handle case when they are the same
        if (Objects.equals(cashReceived, price)) {
            return new BigDecimal("0.00");
        }
        return cashReceived.subtract(price);
    }

    /**
     * For recovering the change by denomination from the current contents of the register
     * Insufficient funds thrown when there is not currently enough money to make change in the till
     * OR when there isn't the correct configuration of change (i.e. need $.05 but there is only 4 pennies
     * and no nickels)
     *
     * @param amountNeeded Total amount of money needed in BigDecimal representation of US $
     * @return EnunMap of denomination and the number of that denomination present as change
     */
    protected EnumMap<Denomination, Integer> getActualAmountFromRegister(BigDecimal amountNeeded) {
        EnumMap<Denomination, Integer> changeOwed = new EnumMap<>(Denomination.class);
        EnumMap<Denomination, Integer> registerContents = register.getRegisterContents();
        for (Denomination d : Denomination.values()) {
            float denominationAmount = registerContents.get(d);
            while (amountNeeded.compareTo(d.getValue()) >= 0 && denominationAmount > 0) {
                changeOwed.merge(d, 1, Integer::sum);
                amountNeeded = amountNeeded.subtract(d.getValue());
                denominationAmount = denominationAmount - 1;
            }
        }
        if (amountNeeded.compareTo(new BigDecimal("0.00")) >= 1) {
            throw new InsufficientFundsException("Insufficient Funds in the till");
        }
        return changeOwed;
    }

    /**
     * gets the configuration of denomination from a payment amount
     *
     * @param payment the amount of money to parse into denominations
     * @return denominations and the number of each denomination
     */
    protected EnumMap<Denomination, Integer> calculateDenominationsPayment(BigDecimal payment) {
        EnumMap<Denomination, Integer> denominations = new EnumMap<>(Denomination.class);
        for (Denomination d : Denomination.values()) {
            denominations.put(d, 0);
            while (payment.compareTo(d.getValue()) >= 0) {
                denominations.merge(d, 1, Integer::sum);
                payment = payment.subtract(d.getValue());
            }
        }
        return denominations;
    }

    /**
     * Converts Denominations EnumMap to list of denominations
     *
     * @param transaction EnumpMap representation of change owed
     * @return A list of each denomination and the number of that denomination
     */
    protected List<Money> convertChangeOwedToMoneyExchange(EnumMap<Denomination, Integer> transaction) {
        List<Money> response = new ArrayList<>();
        for (Denomination d : transaction.keySet()) {
            response.add(new Money(d.getDescription(), transaction.get(d)));
        }
        return response;
    }
}
