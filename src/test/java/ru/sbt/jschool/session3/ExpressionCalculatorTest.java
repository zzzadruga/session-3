package ru.sbt.jschool.session3;

import org.junit.Assert;
import org.junit.Test;
import ru.sbt.jschool.session3.problem2.ExpressionCalculator;

/**
 */
public class ExpressionCalculatorTest {
    ExpressionCalculator calc = new ExpressionCalculator();

    @Test public void test1() throws Exception {
        Assert.assertEquals(2f, calc.calc("1+1"));
        Assert.assertEquals(0f, calc.calc("1-1"));
        Assert.assertEquals(4f, calc.calc("2*2"));
        Assert.assertEquals(4f, calc.calc("8/2"));
        Assert.assertEquals(6f, calc.calc("1+1 + 2*2"));
        Assert.assertEquals(5f, calc.calc("(100-5*10)/10"));
        Assert.assertEquals(1f, calc.calc("(20+20)/(20+20)"));
    }

    @Test public void testWrongFail() throws Exception {
        try {
            calc.calc("(20+20)/(20+20");

            Assert.fail("Wrong expression calculation should throw exception");
        } catch (Exception e) {

        }
    }
}
