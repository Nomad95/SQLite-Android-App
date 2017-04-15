package com.mojaaplikacja.igor.sqlite;


import org.junit.Test;
import static org.junit.Assert.*;

public class ValidationTest {

    @Test
    public void isWwwAppropriate(){
        String regexCheck = "..pl/sdsdds/asdd?=sdd";
        String regex = "(^www\\.(.*)\\.(.*))|(^http://www\\.(.*)\\.(.*))|(^(.*)\\.(.*))";
        assertTrue(regexCheck.matches(regex));
    }

    @Test
    public void isContainingAtLeastOneDecimal(){
        String regexCheck = "6.0.1";
        String regex = "(.*)[0-9](.*)";
        assertTrue(regexCheck.matches(regex));
    }
}
