/*
 * @author: Jean Lazarou
 * @date: 5 mars 2004
 */
package com.ap.jdbcunit;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

import com.ap.jdbcunit.util.DatabaseService;
import com.ap.jdbcunit.util.JDBCUnitTestCase;

public class TestRecordingPreparedStatements extends JDBCUnitTestCase implements Recorder {

	public TestRecordingPreparedStatements(String name) {
		super(name);
	}

	public void testSelect() throws Exception {

		JDBCUnit.start(this);
		
		JDBCUnit.record();

		PreparedStatement pstmt = con.prepareStatement("SELECT * FROM persons WHERE id = ?");
		
		stmt = pstmt;
		
		pstmt.setInt(1, 1);

		rs = pstmt.executeQuery();
		
		verify();
		
		JDBCUnit.stop();
	}

	public void testInsert() throws Exception {

		JDBCUnit.start(this);
		
		JDBCUnit.record();

		PreparedStatement pstmt = con.prepareStatement("INSERT INTO persons (?, ?, ?, ?)");
		
		stmt = pstmt;
		
		pstmt.setInt(1, 777);
		pstmt.setString(2, "Bond");
		pstmt.setString(3, "James");
		pstmt.setInt(4, 1);

		pstmt.executeUpdate();
		
		verify();
		
		JDBCUnit.stop();
	}
    
	public boolean existsTrack(String dbURL, String sql) {
		return false;
	}

	public void add(String dbURL, String sql, ResultSet rs) {
		
		count++;
		
		assertEquals(count, 1);
		assertEquals(dbURL, "jdbc:ap:TestDatabase");
		
		if(sql.startsWith("INSERT")) {
			assertEquals(sql, "INSERT INTO persons (777, 'Bond', 'James', 1)");
			assertOne(rs);		
		} else {
			assertEquals(sql, "SELECT * FROM persons WHERE id = 1");
			assertTrue(DatabaseService.containsPerson(1, rs));
		}
		
	}

	public ResultSet get(Statement stmt, String dbURL, String sql) {
		fail("Unexpected call");
		return null;
	}
	
	public void verify() {
		assertEquals(count, 1);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		count = 0;
	}

	public void start() {
	}

	public void stop() {
	}
	
	public void clear() {
	}
	
	int count;
}
