/*
 * @author: Jean Lazarou
 * @date: March 22, 2004
 */
package com.ap.jdbcunit.conformance;

import junit.framework.TestSuite;

import com.ap.junit.TestSuites;

public class MediaConformanceTests extends TestSuite {
	
	public MediaConformanceTests(MediaFixture[] fixtures) {
		
		super("Media Conformance Tests");
		
		for (int i = 0; i < fixtures.length; i++) {
			
			MediaFixture fix = fixtures[i];
			
			TestSuite suite = new TestSuite(fix.getName());

			addTest(suite);

			addTestMethods(suite, "TestOpenState", TestMediaOpenState.class, fix);			               
			addTestMethods(suite, "TestMediaUsage", TestMediaUsage.class, fix);			               
			addTestMethods(suite, "TestClearMedia", TestClearMedia.class, fix);
			addTestMethods(suite, "TestMediaVisitor", TestMediaVisitor.class, fix);			

		}
		
	}

	protected static void addTestMethods(TestSuite parent, String suiteName, Class theClass, MediaFixture fix) {
		TestSuites.addTestMethods(parent, suiteName, theClass, MediaFixture.class, fix);
	}
}
