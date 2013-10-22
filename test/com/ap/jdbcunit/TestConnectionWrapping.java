/*
 * @author: Jean Lazarou
 * @date: February 15 2004
 */
package com.ap.jdbcunit;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.Connection;

import junit.framework.TestCase;

import com.ap.straight.HashDriver;
import com.ap.jdbcunit.util.DatabaseService;

public class TestConnectionWrapping extends TestCase {

	public TestConnectionWrapping(String name) {
        super(name);
    }

    public void testReplaceActualDriver() throws Exception {

        JDBCUnit.registerDriver("com.ap.straight.HashDriver");

        Driver driver = DriverManager.getDriver("jdbc:ap:TestDatabase");

        assertTrue(driver instanceof DriverWrapper);
    }

    public void testReplaceODBCDriver() throws Exception {

        JDBCUnit.registerDriver("sun.jdbc.odbc.JdbcOdbcDriver");

        Driver driver = DriverManager.getDriver("jdbc:odbc:TestDatabase");

        assertTrue(driver instanceof DriverWrapper);
    }

    public void testReplaceActualConnection() throws Exception {

        DatabaseService.createDatabase();

        JDBCUnit.registerDriver("com.ap.straight.HashDriver");

        Connection con = DriverManager.getConnection("jdbc:ap:TestDatabase");

        con.close();

        assertTrue(con instanceof ConnectionWrapper);
    }

	public void testReplaceTwoDrivers() throws Exception {

		DatabaseService.createDatabase();

		JDBCUnit.registerDriver("sun.jdbc.odbc.JdbcOdbcDriver");
		JDBCUnit.registerDriver("com.ap.straight.HashDriver");

		ConnectionWrapper con = (ConnectionWrapper) DriverManager.getConnection("jdbc:ap:TestDatabase");

		assertTrue(con != null);
		assertTrue(con.wrappedConnection() != null);

	}
	
	protected void setUp() throws Exception {
		HashDriver driver = new HashDriver();
		DriverManager.registerDriver(driver);
	}
	
	protected void tearDown() throws Exception {
		DatabaseService.clear();
		DriverWrapper.deregisterDrivers();
	}

}
