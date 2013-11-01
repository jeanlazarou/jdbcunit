/*
 * @author: Jean Lazarou
 * @date: 15 feb. 04
 */
package com.ap.jdbcunit;

import java.sql.ResultSet;
import java.sql.Statement;

import com.ap.jdbcunit.util.JDBCUnitTestCase;
import com.ap.jdbcunit.util.DatabaseService;

public class TestRecording extends JDBCUnitTestCase implements Recorder {

    public TestRecording(String name) {
        super(name);
    }

    public void testSelect() throws Exception {

		JDBCUnit.start(this);
		
        JDBCUnit.record();

        stmt = con.createStatement();

        rs = stmt.executeQuery("SELECT * FROM persons");

		verify();
		
		JDBCUnit.stop();
    }

    public void testInsertStatement() throws Exception {

		JDBCUnit.start(this);
		
        JDBCUnit.record();

        stmt = con.createStatement();

        stmt.executeUpdate("INSERT INTO persons VALUES (777, 'Bond', 'James', 1)");

		verify();
		
		JDBCUnit.stop();

    }

    public void testDeleteStatement() throws Exception {

		JDBCUnit.start(this);
		
        JDBCUnit.record();

        stmt = con.createStatement();

        stmt.executeUpdate("DELETE FROM persons WHERE Id = 1");

		verify();
		
		JDBCUnit.stop();

    }
    
	public boolean existsTrack(String dbURL, String sql) {
		return false;
	}

	public void add(String dbURL, String sql, ResultSet rs) {
		
		count++;
		
		assertEquals(count, 1);
		assertEquals(dbURL, "jdbc:hsqldb:mem:TestDatabase");
		
		if (sql.startsWith("INSERT")) {
			assertEquals(sql, "INSERT INTO persons VALUES (777, 'Bond', 'James', 1)");
			assertOne(rs);
		} else if (sql.startsWith("DELETE")) {
			assertEquals(sql, "DELETE FROM persons WHERE Id = 1");
			assertOne(rs);
		} else {
			assertEquals(sql, "SELECT * FROM persons");
			assertTrue(DatabaseService.containsAllPersons(rs));
		}
		
	}

	public ResultSet get(Statement stmt, String dbURL, String sql) {
		fail("Unexpected call");
		return null;
	}
	
	public void verify() {
		assertEquals(1, count);
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
