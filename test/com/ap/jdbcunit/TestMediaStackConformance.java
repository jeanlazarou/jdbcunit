/*
 * Created on 20-janv.-2005
 */
package com.ap.jdbcunit;

import com.ap.jdbcunit.conformance.MediaConformanceTests;
import com.ap.jdbcunit.conformance.MediaFixture;
import com.ap.jdbcunit.fixtures.MediaStackFixture;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Jean Lazarou
 */
public class TestMediaStackConformance {

    public static Test suite () {
    	
		TestSuite suite = new TestSuite("TestMediaStack");
		
		MediaFixture[] fixtures = new MediaFixture[] {new MediaStackFixture()};

		suite.addTest(new MediaConformanceTests(fixtures));
		
		return suite;
		
    }

}
