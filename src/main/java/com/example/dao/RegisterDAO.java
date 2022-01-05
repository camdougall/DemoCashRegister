package com.example.dao;

import com.example.utilities.Denomination;
import com.example.utilities.InsufficientFundsException;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Objects;

/**
 * This class is for keeping track of the contents of the register; In reality this would be a database connection
 * But for the purposes of this example, it is kept here;
 */
@Component
public class RegisterDAO {
    private EnumMap<Denomination, Integer> registerContents = new EnumMap<>(Denomination.class);

    /**
     * Constructor for initializing an empty register;
     */
    public RegisterDAO() {
        for (Denomination d : Denomination.values()) {
            registerContents.put(d, 0);
        }
    }

    /**
     * Get the contents of the register in Denomination, value format
     *
     * @return the contents of the register
     */
    public EnumMap<Denomination, Integer> getRegisterContents() {
        return registerContents;
    }

    /**
     * Removes the amount of money from the register drawer in denomination amounts
     *
     * @param denomination         the US currency type to remove
     * @param numberOfDenomination the number of the denomination to remove
     * @return The current total in the register
     */
    public EnumMap<Denomination, Integer> subtractChange(Denomination denomination, Integer numberOfDenomination) {
        Integer currentValue = registerContents.getOrDefault(denomination, 0);
        if (currentValue < numberOfDenomination) {
            throw new InsufficientFundsException("There are not enough of denomination to remove.");
        }
        registerContents.replace(denomination, currentValue - numberOfDenomination);
        return registerContents;
    }

    /**
     * Updates the register with the denominations and number removed
     *
     * @param amountRemoved the denomination and number remvoved as change
     */
    public void updateRegisterWithAmountRemoved(EnumMap<Denomination, Integer> amountRemoved) {
        for (Denomination d : amountRemoved.keySet()) {
            subtractChange(d, amountRemoved.getOrDefault(d, 0));
        }
    }

    /**
     * Add the total amount in denomination, number pair
     *
     * @param denomination         the US denomination type to add
     * @param numberOfDenomination The number of that denomination to add
     * @return Map of Denomination type and number of that denomination
     */
    protected EnumMap<Denomination, Integer> addChange(Denomination denomination, Integer numberOfDenomination) {
        Integer currentValue = registerContents.getOrDefault(denomination, 0);
        registerContents.replace(denomination, currentValue + numberOfDenomination);
        return registerContents;
    }

    /**
     * Updates the register with the amount received by denomination and number
     *
     * @param amountAdded denomination and number of denomination present
     */
    public void updateRegisterWithAmountAdded(EnumMap<Denomination, Integer> amountAdded) {
        for (Denomination d : amountAdded.keySet()) {
            addChange(d, amountAdded.getOrDefault(d, 0));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegisterDAO)) return false;
        RegisterDAO that = (RegisterDAO) o;
        return Objects.equals(getRegisterContents(), that.getRegisterContents());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRegisterContents());
    }
}
