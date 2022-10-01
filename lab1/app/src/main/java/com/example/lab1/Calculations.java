package com.example.lab1;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Calculations {
    public static String Calculate(String number, float firstUnit, float secondUnit)
    {
        System.out.println(firstUnit);
        System.out.println(secondUnit);
        BigDecimal input = new BigDecimal(number);
        BigDecimal course = new BigDecimal(String.valueOf(secondUnit / firstUnit));
        BigDecimal output = input.multiply(course);
        System.out.println("HELLLOOOO----------");
//        String[] parts = output.toString().split(".", 2);
//        String string1 = parts[0];
//        String string2 = parts[1];
//        System.out.println(string2);
//        if(string2 == ".0"){
//            System.out.println("HELLLOOOO----------");
//            BigInteger outputInt = new BigInteger(output.toString());
//            return outputInt.toString();
//        }
        return output.toString();
    }

}
