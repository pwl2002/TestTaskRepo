/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package histogram;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author PM
 */
public class HistogramMakerTest {
    
    public HistogramMakerTest() {
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void histogramTest() {
         
        String []args = new String[5];
        args[0] = "imhist";
        args[1] = "-i";
        args[2] = "c:\\1.jpg";
        args[3] = "-o";
        args[4] = "6.jpg";
                
        if (args.length > 0 && args[0].equals("imhist") && args[1].equals("-i")
                && args[3].equals("-o")) {

            if (args[4].endsWith(".jpg") || args[4].endsWith(".png")
                    || args[4].endsWith(".gif") || args[4].endsWith(".bmp")) {
                new HistogramMaker().makeHistogram(args[2], args[4]);
            }else{
                System.out.println("Input correct parameters. For example: imhist -i *.jpg -o *.jpg");
            }
        }else{
            System.out.println("Input correct parameters. For example: imhist -i *.jpg -o *.jpg");
        }
    }
    
}
