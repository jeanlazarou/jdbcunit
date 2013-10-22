/*
 * @author: Jean Lazarou
 * @date: February 15, 2004
 */
package com.ap.jdbcunit;

import java.math.BigDecimal;
import java.sql.PreparedStatement;

import com.ap.jdbcunit.util.JDBCUnitTestCase;
import com.ap.straight.util.DateFactory;

public class TestReplayingPreparedStatements extends JDBCUnitTestCase {

    public TestReplayingPreparedStatements(String name) {
        super(name);
    }

    public void testSelect() throws Exception {
		
		mockRecorder.setExpectedSQL("SELECT * FROM persons WHERE id = 1");

		record("SELECT * FROM persons WHERE id = ?").setInt(1, 1);
		execute();

		replay("SELECT * FROM persons WHERE id = ?").setInt(1, 1);
		execute();
		
		verify();
    }

	public void testStringParameter() throws Exception {
		
		mockRecorder.setExpectedSQL("SELECT * FROM persons WHERE LastName = 'Hello'");

		record("SELECT * FROM persons WHERE LastName = ?").setString(1, "Hello");
		execute();

		replay("SELECT * FROM persons WHERE LastName = ?").setString(1, "Hello");
		execute();
		
		verify();
		
	}

	public void testDateParameter() throws Exception {
		
		mockRecorder.setExpectedSQL("SELECT * FROM payroll WHERE HireDate = '2000-10-01'");

		record("SELECT * FROM payroll WHERE HireDate = ?").setDate(1, DateFactory.newDate(2000, 10, 1));
		execute();

		replay("SELECT * FROM payroll WHERE HireDate = ?").setDate(1, DateFactory.newDate(2000, 10, 1));
		execute();
		
		verify();
		
	}

	public void testBigDecimalParameter() throws Exception {
		
		mockRecorder.setExpectedSQL("SELECT * FROM payroll WHERE Salary > 100.57");

		record("SELECT * FROM payroll WHERE Salary > ?").setBigDecimal(1, BigDecimal.valueOf(10057, 2));
		execute();

		replay("SELECT * FROM payroll WHERE Salary > ?").setBigDecimal(1, BigDecimal.valueOf(10057, 2));
		execute();
		
		verify();
		
	}
    
	protected void setUp() throws Exception {
		super.setUp();
		
		mockRecorder = new PlaybackRecorder();
		
		JDBCUnit.start(mockRecorder);
	}

	PreparedStatement record(String sql) throws Exception {

		JDBCUnit.record();

		PreparedStatement pstmt = con.prepareStatement(sql);
		
		stmt = pstmt;
		
		return pstmt;
				
	}
			
	PreparedStatement replay(String sql) throws Exception {
		
		JDBCUnit.replay();
		
		PreparedStatement pstmt = con.prepareStatement(sql);
		
		stmt = pstmt;
		
		return pstmt;
		
	}
	
	void execute() throws Exception {
		
		PreparedStatement pstmt = (PreparedStatement) stmt;
		        
		rs = pstmt.executeQuery();

		if (JDBCUnit.isRecording()) {        
			rs.close();
			pstmt.close();
		} else {
			JDBCUnit.stop();
		}
				
	}
	
	void verify() {

		assertSame(mockRecorder.getRecorded(), rs);
				
		mockRecorder.verify();

	}
	
	PlaybackRecorder mockRecorder;

}
