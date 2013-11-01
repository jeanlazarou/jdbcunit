/*
 * @author: Jean Lazarou
 * @date: 15 feb. 04
 */
package com.ap.jdbcunit.util;

import com.ap.straight.MemoryResultSet;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import com.ap.jdbcunit.DriverWrapper;
import com.ap.jdbcunit.JDBCUnit;
import com.ap.jdbcunit.Recorder;
import org.hsqldb.jdbc.JDBCDriver;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class JDBCUnitTestCase extends TestCase {

    protected Connection con;
    protected Statement stmt;
    protected ResultSet rs;

    public JDBCUnitTestCase(String name) {
        super(name);
    }

	public static class PlaybackRecorder implements Recorder {
    
		String expectedSQL;
		
		public void setExpectedSQL(String expectedSQL) {
			this.expectedSQL = expectedSQL;    		
		}
    	
		public boolean existsTrack(String dbURL, String sql) {
			return false;
		}

		public void add(String dbURL, String sql, ResultSet rs) {
		
			count++;
		
			assertEquals(count, 1);
			assertEquals(dbURL, "jdbc:hsqldb:mem:TestDatabase");
			assertEquals(expectedSQL, sql);
		
			recorded = MemoryResultSet.create(rs);
		
		}

		public ResultSet get(Statement stmt, String dbURL, String sql) {
			count--;
			return recorded;
		}
	
		public void verify() {
			assertEquals(count, 0);
		}

		public void start() {
		}

		public void stop() {
		}
	
		public void clear() {
		}
	
		public ResultSet getRecorded() {
			return recorded;
		}
		
		int count;
		ResultSet recorded;
	}
	
    protected void setUp() throws Exception {

        JDBCDriver driver = new JDBCDriver();

		DriverManager.registerDriver(driver);

        DatabaseService.createDatabase(false);

        JDBCUnit.registerDriver(driver);

        con = DriverManager.getConnection("jdbc:hsqldb:mem:TestDatabase");
    }

    protected void tearDown() throws Exception {
    	
        if (rs != null) rs.close();
        if (stmt != null) stmt.close();
        if (con != null) con.close();

        DatabaseService.clear();
		DriverWrapper.deregisterDrivers();
		
		JDBCUnit.reset();
    }
    
    public void assertOne(ResultSet rs) {

    	try {
			assertTrue(rs.next());
			assertEquals(1, rs.getInt(1));
			assertTrue(!rs.next());
		} catch (SQLException e) {
			throw new AssertionFailedError("ResultSet value is not 1");
		}
    	
    }
    
}
