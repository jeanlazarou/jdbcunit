/*
 * @author: Jean Lazarou
 * @date: February 20, 2004
 */
package com.ap.jdbcunit;

import java.util.List;
import java.util.Iterator;

/**
 * Objects implementing this interface know about storing and retrieving result sets
 * associated to sql statements.  
 *  
 * Conformance tests are available to validate the <tt>Media</tt> interface 
 * implementation
 * 
 * @author Jean Lazarou
 * 
 * @see com.ap.jdbcunit.conformance package
 */
public interface Media {
	
	/**
	 * Open the media so that it can be used
	 */
	void open();
	
	/**
	 * Close the media, the media flushes everything that need to be saved to
	 * the persistent store.
	 */
	void close();
	
	/**
	 * Deletes everything that is related to this media
	 */
	void delete();
	
	/**
	 * @return the number of tracks (result sets) that are contained in this media
	 */
	int countTracks();
	
	/**
	 * Start visiting all the tracks contained in this media.
	 *  
	 * @param visitor the <tt>MediaVisitor</tt> object interrested in handling all the
	 * tracks contained in this media
	 */
	void foreachTrack(MediaVisitor visitor);
	
	/**
	 * Answers the question "Does this media contain some SQL statement?"
	 * 
	 * @param dbURL the database URL as <tt>String</tt>
	 * @param sql the SQL statement as <tt>String</tt>
	 * @return <tt>true</tt> if this media contain the given statement executed on
	 * the given database
	 */
	boolean existsTrack(String dbURL, String sql);
	
	/**
	 * Adds a new track of result, the result was obtained by executing the given
	 * SQL statement on the database (identified by the URL). The result is a table
	 * with the diven header (column names).
	 * 
	 * When the method return the new created track is the current track.
	 * 
	 * @param dbURL the database that returned some result
	 * @param sql the executed SQL statement
	 * @param columnNames the list of all the columns
	 * 
	 * @see Media.write
	 */
	void newTrack(String dbURL, String sql, List columnNames);
	
	/**
	 * Add a row to the current track.
	 * 
	 * @param row a list of values (the size of the list should be thes
	 * as the current track's number of columns.
	 */	
	void write(List row);
	
	/**
	 * Close the current track
	 */
	void closeTrack();
	
	/**
	 * Return the track (the results) identified by the given URL and SQL statement 
	 * 
	 * @param dbURL the database URL
	 * @param sql the SQL statement
	 * @return an iterator or the result ot the query execution
	 */
	Iterator getTrack(String dbURL, String sql);
	
	/**
	 * Deletes the track identified by the given URL and SQL statement
	 * 
	 * @param dbURL the database URL
	 * @param sql the SQL statement
	 */
	void deleteTrack(String dbURL, String sql);
}
