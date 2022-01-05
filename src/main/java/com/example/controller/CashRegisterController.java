package com.example.controller;

import com.example.service.CashRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Controller
public class CashRegisterController {
    @Autowired
    private CashRegisterService cashRegisterService;

    @GetMapping("/")
    public String cashRegister(Model model) {
        return "registerInput";
    }

    @GetMapping("/receiveChange")
    public String receiveChange(@RequestParam(value = "price") String price, @RequestParam(value = "payment") String payment, Model model) {
        try {
            model.addAttribute("change", cashRegisterService.getChange(new BigDecimal(price).setScale(2, RoundingMode.HALF_UP), new BigDecimal(payment).setScale(2, RoundingMode.HALF_UP)));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
        return "registerInput";
    }
}
