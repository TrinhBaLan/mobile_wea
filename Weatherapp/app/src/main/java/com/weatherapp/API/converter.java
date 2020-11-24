package com.weatherapp.API;

public class converter {
    public static String UppingCaseFirstCharacter(String str){
        String output = str.substring(0, 1).toUpperCase() + str.substring(1);
        return output;
    }
    public static String UppingCase(String str){
        String output = str.toUpperCase();
        return output;
    }
}
