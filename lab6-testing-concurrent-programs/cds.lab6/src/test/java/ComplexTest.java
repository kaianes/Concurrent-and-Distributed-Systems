/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author admin
 */
public class ComplexTest {

    Complex c1;
    Complex c2;

    public ComplexTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        System.out.println("setUpClass ");
    }

    @AfterAll
    public static void tearDownClass() {
        System.out.println("tearDownClass ");
    }

    @BeforeEach
    public void setUp() {
        System.out.println(" setUp ");
        c1 = new Complex(2, 3);
        c2 = new Complex(1, 2);
    }

    @AfterEach
    public void tearDown() {
        System.out.println(" tearDown ");
    }

    @Test
    public void testSum() {
        System.out.println("  test sum ");
        Complex expResult = new Complex(3, 5);
        Complex result = Complex.sum(c1, c2);
        assertEquals(expResult, result);
    }

    @Test
    public void testAbs() {
        System.out.println("  test abs ");
        double expResult = Math.sqrt(13);
        double result = c1.abs();
        assertEquals(expResult, result, 0.0001);
    }

    @Test
    public void testDifference() {
        System.out.println("  test difference ");
        Complex expResult = new Complex(1, 1);
        Complex result = Complex.difference(c1, c2);
        assertEquals(expResult, result);
    }

    @Test
    public void testProduct() {
        System.out.println("  test product ");
        Complex expResult = new Complex(-4, 7);
        Complex result = Complex.product(c1, c2);
        assertEquals(expResult, result);
    }

    @Test
    public void testConjugate() {
        System.out.println("  test conjugate");
        Complex expResult = new Complex(2, -3);
        Complex result = c1.conjugate();
        assertEquals(expResult, result);
    }

    @Test
    public void testToString() {
        System.out.println("  test toString");
        String expResult = "2.0+3.0i";
        String result = c1.toString();
        assertEquals(expResult, result);
    }

    @Test
    public void testEquals() {
        System.out.println("  test equals");
        Object other = new Complex(2, 3);
        boolean expResult = true;
        boolean result = c1.equals(other);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetRe() {
        System.out.println("  test getRe");
        double result = c1.getRe();
        assertEquals(2, result, 0.0);
        result = c2.getRe();
        assertEquals(1, result, 0.0);
    }

    @Test
    public void testGetIm() {
        System.out.println("  test getIm");
        assertEquals(3, c1.getIm(), 0.0);
        assertEquals(2, c2.getIm(), 0.0);
    }

    @Test
    public void testComplex() {
        System.out.println("  test Constructor");
        assertTrue(c1.getIm() == 3 && c1.getRe() == 2);

    }

    @Test
    public void testDefaultConstructor() {
        Complex c1 = new Complex();
        assertTrue(c1.getIm() == 0 && c1.getRe() == 0);

    }

    @Test
    public void testToStringNoReal() {
        System.out.println("  test toString with no real part");
        Complex a = new Complex(0, -1);
        String expResult = "-1.0i";
        String result = a.toString();
        assertEquals(expResult, result);
    }

    @Test
    public void testToStringNegImag() {
        System.out.println("  test toString with negative imag");
        Complex a = new Complex(4, -1);
        String expResult = "4.0-1.0i";
        String result = a.toString();
        assertEquals(expResult, result);
    }

    @Test
    public void testToStringImagZero() {
        System.out.println("  test toString with zero imag");
        Complex a = new Complex(4, 0);
        String expResult = "4.0";
        String result = a.toString();
        assertEquals(expResult, result);
    }

    @Test
    public void testEqualsDifferent1() {
        System.out.println("  test equals with different objects ");
        Complex a = new Complex(2, 0);
        assertFalse(c1.equals(a));
    }

    @Test
    public void testEqualsDifferent2() {
        System.out.println("  test equals with different objects ");
        assertFalse(c1.equals(c2));
    }

    @Test
    public void testEqualsOtherObject() {
        System.out.println("  test equals with non-Complex Object");
        boolean expResult = false;
        boolean result = c1.equals(new Integer(3));
        assertEquals(expResult, result);
    }

    @Test
    public void testEqualsNull() {
        System.out.println("  test equals with a null");
        Complex other = null;
        boolean expResult = false;
        boolean result = c1.equals(other);
        assertEquals(expResult, result);
    }

}
