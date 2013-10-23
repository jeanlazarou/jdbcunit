/*
 * @author: Jean Lazarou
 * @date: June 1 2004
 */
package com.ap.jdbcunit;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Logger;

import com.ap.jdbcunit.util.JDBCUnitTestCase;

public class TestPlaybackMode extends JDBCUnitTestCase {

	public TestPlaybackMode(String name) {
		super(name);
	}
	
	public void testMockDriver() throws Exception {

		JDBCUnit.record();

		try {
			con = DriverManager.getConnection("jdbc:mock:MockDatabase");
			fail("Should complain about actual database access");
		} catch (MockDriver.MockDriverException e) {
			// expected
		}

	}
	
	public void testDatabaseIsNeverAccessed() throws Exception {

		JDBCUnit.replay();

		con = DriverManager.getConnection("jdbc:mock:MockDatabase");
		
		stmt = con.createStatement();
		rs = stmt.executeQuery("SELECT * FROM persons");

	}
	
	public void testPreparedStatement() throws Exception {

		JDBCUnit.replay();

		con = DriverManager.getConnection("jdbc:mock:MockDatabase");
		
		stmt = con.prepareStatement("SELECT * FROM persons");
		rs = ((PreparedStatement) stmt).executeQuery();

	}
	
	public void testPreparedStatementWithParameters() throws Exception {

		JDBCUnit.replay();

		con = DriverManager.getConnection("jdbc:mock:MockDatabase");
		
		stmt = con.prepareStatement("SELECT * FROM persons id = ? or name = ?");
		
		PreparedStatement ps = (PreparedStatement) stmt;
		
		ps.setInt(1, 1);
		ps.setString(2, "me");
		
		rs = ps.executeQuery();

	}

	protected void setUp() throws Exception {

		Driver driver = new MockDriver();

		DriverManager.registerDriver(driver);

		JDBCUnit.registerDriver(driver);
		
		PlaybackRecorder mock = new PlaybackRecorder();
		
		mock.setExpectedSQL("SELECT * FROM persons");
		
		JDBCUnit.start(mock);
	}

	protected void tearDown() throws Exception {
    	
		if (rs != null) rs.close();
		if (stmt != null) stmt.close();
		if (con != null) con.close();
        		
		JDBCUnit.reset();
		JDBCUnit.resetDrivers();
	}

	protected Connection con;
	protected Statement stmt;
	protected ResultSet rs;
}

class MockDriver implements Driver {

	public static class MockDriverException extends RuntimeException {
		public MockDriverException(String message) {
			super(message);		
		}
	}
	
	public Connection connect(String url, Properties info) throws SQLException {
		throw new MockDriverException("No connection is expected to be created");
	}

	public boolean acceptsURL(String url) throws SQLException {
		return true;
	}

	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
		return new DriverPropertyInfo[0];
	}

	public int getMajorVersion() {
		return 1;
	}

	public int getMinorVersion() {
		return 0;
	}

	public boolean jdbcCompliant() {
		return false;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException();
	}
	
}