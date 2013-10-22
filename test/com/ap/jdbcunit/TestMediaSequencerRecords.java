/*
 * Created on 22-mai-2005
 */
package com.ap.jdbcunit;

import com.ap.jdbcunit.util.JDBCUnitTestCase;
import com.ap.jdbcunit.util.MediaMock;

/**
 * @author Jean Lazarou
 */
public class TestMediaSequencerRecords extends JDBCUnitTestCase {

	public TestMediaSequencerRecords(String name) {
		super(name);
	}
	
	protected void setUp() throws Exception {
		
		super.setUp();
		
		mock = new MediaMock();
		
		recorder = new MediaSequencer(mock.getMedia());
		
		mock.setSequencer();
		
	}

	public void testSelectsOnly() throws Exception {

		mock.recordOpen();
		mock.recordSelectPerson();
		mock.recordSelectPayroll();
		mock.recordSelectPerson();
		mock.recordSelectPayroll();
		mock.recordClose();
		mock.replay();
		
		JDBCUnit.start(recorder);
		
        JDBCUnit.record();

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
