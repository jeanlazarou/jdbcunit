/*
 * Created on 2005-05-29
 */
package com.ap.jdbcunit;

import com.ap.jdbcunit.util.JDBCUnitTestCase;
import com.ap.jdbcunit.util.MediaMock;

/**
 * @author Jean Lazarou
 */
public class TestMediaSequencerPlayback extends JDBCUnitTestCase {

	public TestMediaSequencerPlayback(String name) {
		super(name);
	}
	
	protected void setUp() throws Exception {
		
		super.setUp();
		
		mock = new MediaMock();
		
		recorder = new MediaSequencer(mock.getMedia());

		mock.setSequencer();
		mock.setPlaybackMode();
		
	}

	public void testSelectsOnly() throws Exception {

		mock.recordOpen();
		mock.recordGetSelectPerson();
		mock.recordGetSelectPayroll();
		mock.recordGetSelectPerson();
		mock.recordGetSelectPayroll();
		mock.recordClose();
		mock.replay();
		
		JDBCUnit.start(recorder);
		
        JDBCUnit.replay();

        stmt = con.createStatement();

        stmt.executeQuery("SELECT * FROM persons WHERE id = 1").close();
        stmt.executeQuery("SELECT * FROM payroll WHERE id = 1").close();
        stmt.executeQuery("SELECT * FROM persons WHERE id = 1").close();
        stmt.executeQuery("SELECT * FROM payroll WHERE id = 1").close();
		
        stmt.close();
        
		JDBCUnit.stop();

        mock.verify();

	}

	MediaMock mock;
	MediaSequencer recorder;
	
}
