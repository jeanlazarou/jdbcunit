/*
 * @author: Jean Lazarou
 * @date: February 17, 2004
 */
package com.ap.jdbcunit;

import java.sql.ResultSet;
import java.sql.Statement;

public interface Recorder {
	
	/**
	 * Start the recorder, doesn't mean "entering" in recording mode, rather
	 * it means "swicth on" the recorder
	 */
	void start();
	
	/**
	 * Stop the recorder ("switch off")
	 */
	void stop();
	
	/**
	 * Clears everything...
	 */
	void clear();
	
	/**
	 * Returns <tt>true</tt>
	 * 
	 * @param dbURL the database url
	 * @param sql the SQL statement
	 * @return <tt>true</tt> if recorder contains the track (the result set)
	 */
	boolean existsTrack(String dbURL, String sql);
	
	/**
	 * Records the given result set obtained after executing the given SQL statement
	 * on the given database
	 * 
	 * @param dbURL the database URL
	 * @param sql the executed SQL statement
	 * @param rs the result of the query
	 */
	void add(String dbURL, String sql, ResultSet rs);
	
	/**
	 * Retrieves a previously recorded SQL statement
	 * 
	 * @param stmt the @link{java.sql.Statement}
	 * @param dbURL database URL
	 * @param sql SQL statement string
	 * @return the result set
	 */
	ResultSet get(Statement stmt, String dbURL, String sql);
	
}
