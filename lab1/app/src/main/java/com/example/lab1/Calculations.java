package com.example.lab1;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;

public class Calculations {
    public static String Calculate(String number, double firstUnit, double secondUnit)
    {
        if(Objects.equals(number, "0")) return "0";
        if(Objects.equals(number, "0.")) return "0";
//        if(!number.contains(".")){
//            BigInteger input = new BigInteger(number);
//            BigInteger course = new BigInteger(String.valueOf(secondUnit / firstUnit));
//            BigInteger output = input.multiply(course);
//            return output.toString();
//        }
        System.out.println(firstUnit);
        System.out.println(secondUnit);

        BigDecimal firstUnitB = new BigDecimal(firstUnit);
        BigDecimal secondUnitB = new BigDecimal(secondUnit);
        BigDecimal course = firstUnitB.divide(secondUnitB, 20, RoundingMode.HALF_UP);
        double bla = firstUnit / secondUnit;
        System.out.println(String.valueOf(bla));
        System.out.println(String.valueOf(course));
        BigDecimal input = new BigDecimal(number);
        BigDecimal course2 = new BigDecimal(String.valueOf(bla));
        BigDecimal output = input.multiply(course2);
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
