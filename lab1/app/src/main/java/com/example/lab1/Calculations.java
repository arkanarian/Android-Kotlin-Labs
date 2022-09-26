package com.example.lab1;

import java.math.BigDecimal;

public class Calculations {
    public static String Calculate(String number, float firstUnit, float secondUnit)
    {
        System.out.println(firstUnit);
        System.out.println(secondUnit);
        BigDecimal input = new BigDecimal(number);
        BigDecimal course = new BigDecimal(String.valueOf(secondUnit / firstUnit));
        BigDecimal output = input.multiply(course);
        return output.toString();
    }

}
