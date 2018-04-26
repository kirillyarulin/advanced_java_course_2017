package edu.technopolis.advanced.boatswain.Solver;

import java.math.BigDecimal;
import java.math.MathContext;

import org.junit.Test;

import ch.obermuhlner.math.big.BigDecimalMath;

import static org.junit.Assert.*;

public class SolverTest {
    private static Solver solver = new Solver();
    @Test
    public void evaluate() throws Exception {
        assertEquals(Math.cos(3.14)+1-3, solver.evaluate("cos(3.14)+1-3").doubleValue(), 0.01);
    }


    @Test
    public void evaluate1() throws Exception {
        assertEquals(Math.cos(3.14)+(1-3) / Math.sin(1.0-2.2+Math.tan(0.7333)), solver.evaluate("cos(3.14)+(1-3)/sin(1.0-2.2+tg(0.7333))").doubleValue(), 0.01);
    }

    @Test
    public void evaluate2() throws Exception {
        assertEquals(1-0.1-0.1-0.1-0.1, solver.evaluate("1-0.1-0.1-0.1-0.1").doubleValue(), 0.01);
    }


    @Test(expected = ArithmeticException.class)
    public void evaluate3() throws Exception {
        assertEquals(Math.sqrt( Math.log(Math.sin(0.2*2))), solver.evaluate("sqrt(log(sin(0.2*2)))").doubleValue(), 0.01);
    }

    @Test
    public void evaluate4() throws Exception {
        assertEquals( 1.0, solver.evaluate("sin(111)*sin(111)+cos(111)*cos(111)").doubleValue(), 0.00001);
    }

    @Test
    public void evaluate5() throws Exception {
        assertEquals( 1.0, solver.evaluate("sin(111)*sin(111)+cos(111)*cos(111)").doubleValue(), 0);
    }
}