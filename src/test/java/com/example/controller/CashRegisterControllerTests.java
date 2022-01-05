package com.example.controller;

import com.example.controller.CashRegisterController;
import com.example.dto.Money;
import com.example.service.CashRegisterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(CashRegisterController.class)
public class CashRegisterControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CashRegisterService service;

    @Test
    public void controllerLoads() throws Exception {
        List<Money> contents = new ArrayList<>();
        when(service.getChange(new BigDecimal("2.00"), new BigDecimal("3.00"))).thenReturn(contents);
        this.mockMvc.perform(get("/receiveChange").param("price", "2.00").param("payment", "3.00")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>Create New Project</title>")));
    }

}
