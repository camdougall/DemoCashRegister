package com.example.dao;

import com.example.utilities.Denomination;
import com.example.utilities.InsufficientFundsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.EnumMap;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RegisterDAOTests {

    @Autowired
    private RegisterDAO register;

    @Test
    @Order(1)
    public void serviceWires() throws Exception {
        assertThat(register).isNotNull();
    }
    @Test
    @Order(2)
    public void registerContents() throws Exception {
        assertThat(register.getRegisterContents()).isNotNull();
    }
    @Test
    @Order(3)
    public void registerContentsEmpty() throws Exception {
        assertThat(register.getRegisterContents()).isEqualTo(setUpRegisterContents(0));
    }
    @Test
    public void subtractChangeHappy() throws Exception {
        EnumMap<Denomination,Integer> registerContentsExpected = setUpRegisterContents(5);
        register.updateRegisterWithAmountAdded(registerContentsExpected);
        assertThat(register.getRegisterContents()).isEqualTo(registerContentsExpected);
        registerContentsExpected.replace(Denomination.PENNY,3);
        register.subtractChange(Denomination.PENNY, 2);
        assertThat(register.getRegisterContents()).isEqualTo(registerContentsExpected);
        cleanUpRegisterToEmpty();
        assertThat(register.getRegisterContents()).isEqualTo(setUpRegisterContents(0));
    }
    @Test
    public void subtractChangeNegative() throws Exception {
        EnumMap<Denomination,Integer> registerContentsExpected = setUpRegisterContents(5);
        register.updateRegisterWithAmountAdded(registerContentsExpected);
        assertThat(register.getRegisterContents()).isEqualTo(registerContentsExpected);
        InsufficientFundsException thrown = Assertions.assertThrows(InsufficientFundsException.class, () -> {
            register.subtractChange(Denomination.PENNY, 7);
        });
        cleanUpRegisterToEmpty();
        assertThat(register.getRegisterContents()).isEqualTo(setUpRegisterContents(0));
    }
    @Test
    public void updateRegisterWithAmountRemovedHappy() throws Exception {
        EnumMap<Denomination,Integer> registerContentsOriginal = setUpRegisterContents(5);
        EnumMap<Denomination,Integer> changeOwed = setUpRegisterContents(3);
        register.updateRegisterWithAmountAdded(registerContentsOriginal);
        assertThat(register.getRegisterContents()).isEqualTo(registerContentsOriginal);
        register.updateRegisterWithAmountRemoved(changeOwed);
        EnumMap<Denomination,Integer> contentsExpected = setUpRegisterContents(2);
        assertThat(register.getRegisterContents()).isEqualTo(contentsExpected);
        cleanUpRegisterToEmpty();
        assertThat(register.getRegisterContents()).isEqualTo(setUpRegisterContents(0));
    }
    @Test
    public void updateRegisterWithAmountRemovedNegative() throws Exception {
        EnumMap<Denomination,Integer> registerContentsExpected = setUpRegisterContents(5);
        register.updateRegisterWithAmountAdded(registerContentsExpected);
        assertThat(register.getRegisterContents()).isEqualTo(registerContentsExpected);
        EnumMap<Denomination,Integer> changeOwed = setUpRegisterContents(6);
        InsufficientFundsException thrown = Assertions.assertThrows(InsufficientFundsException.class, () -> {
            register.updateRegisterWithAmountRemoved(changeOwed);
        });
        cleanUpRegisterToEmpty();
        assertThat(register.getRegisterContents()).isEqualTo(setUpRegisterContents(0));
    }
    @Test
    public void updateRegisterWithAmountAddedHappy() throws Exception {
        EnumMap<Denomination,Integer> registerContentsOriginal = setUpRegisterContents(5);
        EnumMap<Denomination,Integer> cashPaid = setUpRegisterContents(3);
        register.updateRegisterWithAmountAdded(registerContentsOriginal);
        assertThat(register.getRegisterContents()).isEqualTo(registerContentsOriginal);
        register.updateRegisterWithAmountAdded(cashPaid);
        EnumMap<Denomination,Integer> contentsExpected = setUpRegisterContents(8);
        assertThat(register.getRegisterContents()).isEqualTo(contentsExpected);
        cleanUpRegisterToEmpty();
        assertThat(register.getRegisterContents()).isEqualTo(setUpRegisterContents(0));
    }

    /**
     * Setup for test object to have money in the register while test runs
     * @param amountForDenomination how much each denomination should have;
     *                             this sets all denominations to the same number
     * @return EnumMap representation of each denomination and the number present
     */
    public EnumMap<Denomination,Integer> setUpRegisterContents(Integer amountForDenomination){
        EnumMap<Denomination, Integer> registerContents = new EnumMap<Denomination, Integer>(Denomination.class);
        for(Denomination d : Denomination.values()){
            registerContents.put(d,amountForDenomination);
        }
        return registerContents;
    }

    /**
     * Resets register back to zero
     */
    private void cleanUpRegisterToEmpty(){
        register.updateRegisterWithAmountRemoved(register.getRegisterContents());
    }


}
