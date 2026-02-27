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
public class AccountTest {

    public AccountTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of deposit method, of class Account.
     */
    @Test
    public void testDeposit() {
        System.out.println("test deposit Positive");
        double amount = 20.0;
        Account instance = new Account("John");
        instance.deposit(amount);
        assertEquals(20.0, instance.getBalance(), 0.0);
    }

    @Test
    public void testDepositNegative() {
        System.out.println("test deposit Negative");
        double amount = -30.0;
        Account instance = new Account("Ali");
        instance.deposit(amount);
        assertEquals(0.0, instance.getBalance(), 0.0);
    }

    /**
     * Test of withdraw method, of class Account.
     */
    @Test
    public void testWithdraw() {
        System.out.println("test withdraw OK");
        double amount = 20.0;
        Account instance = new Account("Alice");
        instance.deposit(100.0);
        double expResult = 20.0;
        double result = instance.withdraw(amount);
        assertEquals(expResult, result, 0.0);
        assertEquals(80.0, instance.getBalance(), 0.0);
    }

    /**
     * Test of withdraw method, of class Account.
     */
    @Test
    public void testWithdrawNotAccepted() {
        System.out.println("test withdraw Not Accepted");
        Account instance = new Account("Sarah");
        instance.deposit(100.0);
        double amount = 200.0;
        double result = instance.withdraw(amount);
        double expResult = 0.0;
        assertEquals(expResult, result, 0.0);
        assertEquals(100.0, instance.getBalance(), 0.0);
    }

    /**
     * Test of withdraw method, of class Account.
     */
    @Test
    public void testWithdrawNegative() {
        System.out.println("test withdraw Negative Amount");
        Account instance = new Account("Emma");
        instance.deposit(100.0);
        double amount = -1000.0;
        double result = instance.withdraw(amount);
        double expResult = 0.0;
        assertEquals(expResult, result, 0.0);
        assertEquals(100.0, instance.getBalance(), 0.0);
    }

    /**
     * Test of getBalance method, of class Account.
     */
    @Test
    public void testGetBalance() {
        System.out.println("test getBalance");
        Account instance = new Account("Hassan");
        double expResult = 0.0;
        double result = instance.getBalance();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getHolder method, of class Account.
     */
    @Test
    public void testGetHolder() {
        System.out.println("test getHolder");
        Account instance = new Account("Fatima");
        String expResult = "Fatima";
        String result = instance.getHolder();
        assertEquals(expResult, result);
    }

}
