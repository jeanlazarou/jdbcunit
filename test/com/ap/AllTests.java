/*
 * @author: Jean Lazarou
 * @date: February 15, 2004
 */
package com.ap;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {
    public static Test suite() {
        TestSuite suite= new TestSuite("AlephProduction Tests");

        //suite.addTest(com.ap.store.AllTests.suite());
        //suite.addTest(com.ap.straight.AllTests.suite());
		suite.addTest(com.ap.jdbcunit.AllTests.suite());
		//suite.addTest(com.ap.util.AllTests.suite());

        return suite;
    }
}
