/*
 * @author Jean Lazarou
 * Date: April 13, 2004
 */
package com.ap.jdbcunit;

import junit.framework.*;

public class AllTests
{
    public static Test suite ()
    {
		TestSuite suite= new TestSuite("JDBCUnit Tests");

        suite.addTestSuite(TestConnectionWrapping.class);
        suite.addTestSuite(TestStatementWrapping.class);
        suite.addTestSuite(TestRecording.class);
		suite.addTestSuite(TestReplaying.class);
		suite.addTestSuite(TestUnitSequence.class);
		suite.addTestSuite(TestMemoryUsage.class);
		suite.addTestSuite(TestRecordingPreparedStatements.class);		
		suite.addTestSuite(TestReplayingPreparedStatements.class);		
		suite.addTestSuite(TestRecordingReusesResults.class);
		suite.addTestSuite(TestMediaStack.class);
		suite.addTestSuite(TestPlaybackMode.class);
		suite.addTestSuite(TestMediaSequencerPlayback.class);		
		suite.addTestSuite(TestMediaSequencerRecords.class);		
		
		suite.addTest(com.ap.jdbcunit.csv.AllTests.suite());
		
		suite.addTest(TestMediaStackConformance.suite());
		suite.addTest(TestCSVMediaConformance.suite());

        return suite;
	}
}
