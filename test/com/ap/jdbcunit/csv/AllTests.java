/*
 * @author Jean Lazarou
 * Date: April 27, 2004
 */
package com.ap.jdbcunit.csv;

import junit.framework.*;

public class AllTests
{
    public static Test suite ()
    {
		TestSuite suite= new TestSuite("CSV Tests");

		suite.addTest(new TestSuite(TestCSVFileIterator.class));
		suite.addTest(new TestSuite(TestCSVMedia.class));
		suite.addTest(new TestSuite(TestClearingCSVMedia.class));		

        return suite;
	}
}
