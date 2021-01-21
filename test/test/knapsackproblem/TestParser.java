/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.knapsackproblem;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import knapsackproblem.Item;
import knapsackproblem.KnapsackProblem;

/**
 *
 * @author katia
 */
public class TestParser {
    
    public TestParser() {
    }
     @Test
     public void test1() throws Exception {
        String text    = "81 : (1,53.38,€45) (2,288.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)";       
        assertEquals(6, KnapsackProblem.parseItems(text).length);     
     }
     
     @Test(expected = NumberFormatException.class)
     public void testFormatException1() throws Exception {
      String text = "81 : (1e,53.38,€45) (2,288.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)";
      KnapsackProblem.parseItems(text);
     }
     
     @Test(expected = Exception.class)
     public void testFormatException2() throws Exception {
      String text = "81 : 1,53.38,€45) (2,288.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)";
      KnapsackProblem.parseItems(text);
     }
 
}
