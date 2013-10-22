/*
 * @author: Jean Lazarou
 * @date: 15 févr. 04
 */
package com.ap.jdbcunit;

import java.util.List;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


import com.ap.jdbcunit.util.JDBCUnitTestCase;
import com.ap.jdbcunit.util.DatabaseService;
import com.ap.straight.MemoryResultSet;

public class TestRecordingReusesResults extends JDBCUnitTestCase implements Recorder {

    public TestRecordingReusesResults(String name) {
        super(name);
    }

    public void testSelect() throws Exception {

		JDBCUnit.start(this);
		
        JDBCUnit.record();

        stmt = con.createStatement();

        rs = stmt.executeQuery("SELECT * FROM persons");

		rs = stmt.executeQuery("SELECT * FROM persons");

		assertTrue(DatabaseService.containsAllPersons(rs));

		verify();
		
		JDBCUnit.stop();
    }
    
	public void testPreparedSelect() throws Exception {

		JDBCUnit.start(this);
		
		JDBCUnit.record();

		PreparedStatement pstmt = con.prepareStatement("SELECT * FROM persons");
		
		stmt = pstmt;

		rs = pstmt.executeQuery();

		rs = pstmt.executeQuery();

		assertTrue(DatabaseService.containsAllPersons(rs));

		verify();
		
		JDBCUnit.stop();
	}
    
	public boolean existsTrack(String dbURL, String sql) {
		
		if (count++ == 0) return false;
		
		assertEquals(count, 3);
		
		return true;
		
	}

	public void add(String dbURL, String sql, ResultSet rs) {
		
		count++;
		
		assertEquals(count, 2);
		assertEquals(dbURL, "jdbc:ap:TestDatabase");
		assertEquals(sql, "SELECT * FROM persons");
		
		List data = MemoryResultSet.toList(rs);
		
		assertTrue(DatabaseService.containsAllPersons(new MemoryResultSet(data)));

		memres = new MemoryResultSet(data);
		
	}

	public ResultSet get(Statement stmt, String dbURL, String sql) {
		
		count++;
		
		assertEquals(count, 4);
		
		return memres;
		
	}
	
	public void verify() {
		assertEquals(count, 4);
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
	
	MemoryResultSet memres;
	int count;

}
