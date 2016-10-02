/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pavel.testtask.strings;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author PM
 */
public class SubStringFinderTest {
    
    public SubStringFinderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of findSubString method, of class SubStringFinder.
     */
    @Test
    public void testFindSubString() {
        
        SubStringFinder n = new SubStringFinder();
        
        assertEquals("bcda", n.findSubString("abbcdabc").toString());
        assertEquals("bacd", n.findSubString("cbacdcbc").toString());
        
        
    }    
    
}
