package com.example.service;

import com.example.dao.RegisterDAO;
import com.example.dto.Money;
import com.example.utilities.Denomination;
import com.example.utilities.InsufficientFundsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@SpringBootTest
public class CashRegisterServiceTests {
    @Autowired
    private CashRegisterService service;
    @MockBean
    private RegisterDAO register;


    @Test
    public void serviceWires() throws Exception {
        assertThat(service).isNotNull();
    }
    @Test
    public void zeroMoney() throws Exception {
        EnumMap<Denomination, Integer> registerContents = setUpRegisterContents(0);
        when(register.getRegisterContents()).thenReturn(registerContents);
        InsufficientFundsException thrown = Assertions.assertThrows(InsufficientFundsException.class, () -> {
            service.getChange(new BigDecimal("1.00"), new BigDecimal("2.00"));
        });

    }
    @Test
    public void getAmountOfChangeWithZeroChange() throws Exception{
        assertThat(service.getAmountOfChange(new BigDecimal("2.00"), new BigDecimal("2.00"))).isEqualTo(new BigDecimal("0.00"));
    }
    @Test
    public void getAmountOfChangeHappy() throws Exception{
        assertThat(service.getAmountOfChange(new BigDecimal("5.50"),new BigDecimal("6.00"))).isEqualTo(new BigDecimal("0.50"));
    }
    @Test
    public void getAmountOfChangeWithException() throws Exception{
        InsufficientFundsException thrown = Assertions.assertThrows(InsufficientFundsException.class, () -> {
            service.getAmountOfChange(new BigDecimal("2.00"),new BigDecimal("1.00"));
        });
    }
    @Test
    public void notEnoughPenniesForChange() throws Exception {
        EnumMap<Denomination,Integer> registerContents = setUpRegisterContents(5);
        registerContents.replace(Denomination.PENNY, 0);
        when(register.getRegisterContents()).thenReturn(registerContents);
        InsufficientFundsException thrown = Assertions.assertThrows(InsufficientFundsException.class, () -> {
            service.getActualAmountFromRegister(new BigDecimal(".01"));
        });
    }
    @Test
    public void notEnoughNickelsForChange() throws Exception {
        EnumMap<Denomination,Integer> registerContents = setUpRegisterContents(4);
        registerContents.replace(Denomination.NICKEL, 0);
        when(register.getRegisterContents()).thenReturn(registerContents);
        InsufficientFundsException thrown = Assertions.assertThrows(InsufficientFundsException.class, () -> {
            service.getActualAmountFromRegister(new BigDecimal((".05")));
        });
    }
    @Test
    public void notEnoughDimesForChange() throws Exception {
        EnumMap<Denomination,Integer> registerContents = setUpRegisterContents(4);
        registerContents.replace(Denomination.NICKEL, 1);
        registerContents.replace(Denomination.DIME, 0);
        when(register.getRegisterContents()).thenReturn(registerContents);
        InsufficientFundsException thrown = Assertions.assertThrows(InsufficientFundsException.class, () -> {
            service.getActualAmountFromRegister(new BigDecimal(".10"));
        });
    }
    @Test
    public void notEnoughQuartersForChange() throws Exception {
        EnumMap<Denomination,Integer> registerContents = setUpRegisterContents(4);
        registerContents.replace(Denomination.QUARTER, 0);
        registerContents.replace(Denomination.NICKEL, 2);
        registerContents.replace(Denomination.DIME, 1);
        when(register.getRegisterContents()).thenReturn(registerContents);
        InsufficientFundsException thrown = Assertions.assertThrows(InsufficientFundsException.class, () -> {
            service.getActualAmountFromRegister(new BigDecimal("0.25"));
        });
    }
    @Test
    public void notEnoughFiftyCentForChange() throws Exception {
        EnumMap<Denomination,Integer> registerContents = setUpRegisterContents(4);
        registerContents.replace(Denomination.QUARTER, 1);
        registerContents.replace(Denomination.NICKEL, 1);
        registerContents.replace(Denomination.DIME, 1);
        registerContents.replace(Denomination.FIFTY_CENT_PIECE, 0);
        when(register.getRegisterContents()).thenReturn(registerContents);
        InsufficientFundsException thrown = Assertions.assertThrows(InsufficientFundsException.class, () -> {
            service.getActualAmountFromRegister(new BigDecimal("0.50"));
        });
    }
    @Test
    public void notEnoughDollarsForChange() throws Exception {
        EnumMap<Denomination,Integer> registerContents = setUpRegisterContents(4);
        registerContents.replace(Denomination.QUARTER, 1);
        registerContents.replace(Denomination.NICKEL, 1);
        registerContents.replace(Denomination.DIME, 1);
        registerContents.replace(Denomination.FIFTY_CENT_PIECE, 1);
        registerContents.replace(Denomination.PENNY, 0);
        registerContents.replace(Denomination.ONE_DOLLAR_BILL, 0);
        when(register.getRegisterContents()).thenReturn(registerContents);
        InsufficientFundsException thrown = Assertions.assertThrows(InsufficientFundsException.class, () -> {
            service.getActualAmountFromRegister(new BigDecimal("1.00"));
        });
    }
    @Test
    public void notEnoughFiveDollarsForChange() throws Exception {
        EnumMap<Denomination,Integer> registerContents = setUpRegisterContents(4);
        registerContents.replace(Denomination.FIVE_DOLLAR_BILL, 0);
        registerContents.replace(Denomination.TWO_DOLLAR_BILL, 0);
        registerContents.replace(Denomination.ONE_DOLLAR_BILL, 0);
        when(register.getRegisterContents()).thenReturn(registerContents);
        InsufficientFundsException thrown = Assertions.assertThrows(InsufficientFundsException.class, () -> {
            service.getActualAmountFromRegister(new BigDecimal("5.00"));
        });
    }
    @Test
    public void changeWithPenny() throws Exception {
        EnumMap<Denomination,Integer> registerContents = setUpRegisterContents(4);
        when(register.getRegisterContents()).thenReturn(registerContents);
        EnumMap<Denomination,Integer> expectedChange = new EnumMap<>(Denomination.class);
        expectedChange.put(Denomination.PENNY, 1);
        assertThat(service.getActualAmountFromRegister(new BigDecimal(".01"))).isEqualTo(expectedChange);
    }
    @Test
    public void changeWithNickel() throws Exception {
        EnumMap<Denomination,Integer> registerContents = setUpRegisterContents(4);
        when(register.getRegisterContents()).thenReturn(registerContents);
        EnumMap<Denomination,Integer> expectedChange = new EnumMap<>(Denomination.class);
        expectedChange.put(Denomination.NICKEL, 1);
        assertThat(service.getActualAmountFromRegister(new BigDecimal(".05"))).isEqualTo(expectedChange);
    }
    @Test
    public void changeWithDime() throws Exception {
        EnumMap<Denomination,Integer> registerContents = setUpRegisterContents(4);
        when(register.getRegisterContents()).thenReturn(registerContents);
        EnumMap<Denomination,Integer> expectedChange = new EnumMap<>(Denomination.class);
        expectedChange.put(Denomination.DIME, 1);
        assertThat(service.getActualAmountFromRegister(new BigDecimal("0.10"))).isEqualTo(expectedChange);
    }
    @Test
    public void changeWithQuarter() throws Exception {
        EnumMap<Denomination,Integer> registerContents = setUpRegisterContents(4);
        when(register.getRegisterContents()).thenReturn(registerContents);
        EnumMap<Denomination,Integer> expectedChange = new EnumMap<>(Denomination.class);
        expectedChange.put(Denomination.QUARTER, 1);
        assertThat(service.getActualAmountFromRegister(new BigDecimal("0.25"))).isEqualTo(expectedChange);
    }
    @Test
    public void changeWithFiftyCentPiece() throws Exception {
        EnumMap<Denomination,Integer> registerContents = setUpRegisterContents(4);
        when(register.getRegisterContents()).thenReturn(registerContents);
        EnumMap<Denomination,Integer> expectedChange = new EnumMap<>(Denomination.class);
        expectedChange.put(Denomination.FIFTY_CENT_PIECE, 1);
        assertThat(service.getActualAmountFromRegister(new BigDecimal("0.50"))).isEqualTo(expectedChange);
    }
    @Test
    public void changeWithDollar() throws Exception {
        EnumMap<Denomination,Integer> registerContents = setUpRegisterContents(4);
        when(register.getRegisterContents()).thenReturn(registerContents);
        EnumMap<Denomination,Integer> expectedChange = new EnumMap<>(Denomination.class);
        expectedChange.put(Denomination.ONE_DOLLAR_BILL, 1);
        assertThat(service.getActualAmountFromRegister(new BigDecimal("1.00"))).isEqualTo(expectedChange);
    }
    @Test
    public void changeWithFiveDollar() throws Exception {
        EnumMap<Denomination,Integer> registerContents = setUpRegisterContents(4);
        when(register.getRegisterContents()).thenReturn(registerContents);
        EnumMap<Denomination,Integer> expectedChange = new EnumMap<>(Denomination.class);
        expectedChange.put(Denomination.FIVE_DOLLAR_BILL, 1);
        assertThat(service.getActualAmountFromRegister(new BigDecimal("5.00"))).isEqualTo(expectedChange);
    }
    @Test
    public void changeWithTenDollar() throws Exception {
        EnumMap<Denomination,Integer> registerContents = setUpRegisterContents(4);
        when(register.getRegisterContents()).thenReturn(registerContents);
        EnumMap<Denomination,Integer> expectedChange = new EnumMap<>(Denomination.class);
        expectedChange.put(Denomination.TEN_DOLLAR_BILL, 1);
        assertThat(service.getActualAmountFromRegister(new BigDecimal("10.00"))).isEqualTo(expectedChange);
    }
    @Test
    public void changeWithTwentyDollar() throws Exception {
        EnumMap<Denomination,Integer> registerContents = setUpRegisterContents(4);
        when(register.getRegisterContents()).thenReturn(registerContents);
        EnumMap<Denomination,Integer> expectedChange = new EnumMap<>(Denomination.class);
        expectedChange.put(Denomination.TWENTY_DOLLAR_BILL, 1);
        assertThat(service.getActualAmountFromRegister(new BigDecimal("20.00"))).isEqualTo(expectedChange);
    }
    @Test
    public void changeWithAllDenominations() throws Exception {
        EnumMap<Denomination,Integer> registerContents = setUpRegisterContents(1);
        when(register.getRegisterContents()).thenReturn(registerContents);
        assertThat(service.getActualAmountFromRegister(new BigDecimal("38.91"))).isEqualTo(registerContents);
    }
    @Test
    public void getChangeHappy() throws Exception {
        EnumMap<Denomination,Integer> registerContents = setUpRegisterContents(5);
        when(register.getRegisterContents()).thenReturn(registerContents);
        List<Money> money = new ArrayList<>();
        money.add(new Money("ONE DOLLAR BILL", 1));
        assertThat(service.getChange(new BigDecimal("3.99"),new BigDecimal("4.99"))).isEqualTo(money);
    }
    @Test
    public void getChangeHappyAllDenominations() throws Exception {
        EnumMap<Denomination,Integer> registerContents = setUpRegisterContents(5);
        when(register.getRegisterContents()).thenReturn(registerContents);
        List<Money> money = new ArrayList<>();
        for(Denomination d : Denomination.values()){
            money.add(new Money(d.getDescription(), 1));
        }
        assertThat(service.getChange(new BigDecimal("50.00"),new BigDecimal("88.91"))).isEqualTo(money);
    }

    @Test
    public void getChangeUnHappy() throws Exception {
        EnumMap<Denomination,Integer> registerContents = setUpRegisterContents(1);
        when(register.getRegisterContents()).thenReturn(registerContents);
        InsufficientFundsException thrown = Assertions.assertThrows(InsufficientFundsException.class, () -> {
            service.getChange(new BigDecimal("3.25"),new BigDecimal("200.00"));
        });
    }
    @Test
    public void convertChangeOwedToMoneyExchangeHappy() throws Exception {
        EnumMap<Denomination,Integer> contents = setUpRegisterContents(1);
        List<Money> transactionExpected = new ArrayList<>();
        for(Denomination d : Denomination.values()){
            transactionExpected.add(new Money(d.getDescription(), 1));
        }
        List<Money> transactionActual = service.convertChangeOwedToMoneyExchange(contents);
        assertThat(transactionActual).isEqualTo(transactionExpected);
    }
    @Test
    public void calculateDenominationsPaymentHappy() throws Exception{
        EnumMap<Denomination,Integer> paymentExpected =  setUpRegisterContents(0);
        paymentExpected.replace(Denomination.TWENTY_DOLLAR_BILL,1);
        paymentExpected.replace(Denomination.FIVE_DOLLAR_BILL,1);
        paymentExpected.replace(Denomination.TWO_DOLLAR_BILL,2);
        paymentExpected.replace(Denomination.FIFTY_CENT_PIECE, 1);
        paymentExpected.replace(Denomination.QUARTER,1);
        paymentExpected.replace(Denomination.DIME,2);
        assertThat(service.calculateDenominationsPayment(new BigDecimal("29.95"))).isEqualTo(paymentExpected);
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

}

