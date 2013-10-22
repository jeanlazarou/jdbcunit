/*
 * @author: Jean Lazarou
 * @date: 15 févr. 04
 */
package com.ap.jdbcunit;

import java.util.List;
import java.util.ArrayList;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.ap.straight.MemoryResultSet;
import com.ap.straight.SQL;

/**
 * A recorder that records only SELECT sql statements, it fails if some other 
 * statement is executed (like insert, update or delete).
 * <p>
 * This recorder is a light recorder implementation targeted for "read-only"
 * JDBC applications.
 * <p>
 * This recorder is a light recorder because it easily can decide it some
 * statement was already recorded and pick up the previous result.
 * It also doesn't need to have any strategy about track versioning, all 
 * the tracks are version 1.
 * <p>
 * Finally, the recorded application can change, provided the same statements
 * are reused, and still the recorded data remain valid.
 * 
 * @author Jean Lazarou
 */
public class SelectsMediaRecorder implements Recorder {

	public SelectsMediaRecorder(Media media) {
		this.media = media;
	}

	public void start() {
		media.open();
	}

	public void stop() {
		media.close();
	}
	
	public void clear() {
		media.close();
		media.delete();
	}
	
	public boolean existsTrack(String dbURL, String sql) {

		if (!sql.toLowerCase().startsWith("select")) {
    		throw new JDBCUnitException("Recorder only supports SELECT statements, offending statement was: \"" + 
    				sql + "\", on " + dbURL);
    	}

		return media.existsTrack(dbURL, sql);
		
	}
	
    public void add(String dbURL, String sql, ResultSet rs) {

		if (!sql.toLowerCase().startsWith("select")) {
    		throw new JDBCUnitException("Recorder only supports SELECT statements, offending statement was: \"" + 
    				sql + "\", on " + dbURL);
    	}
    	
		doAdd(dbURL, sql, rs);
    }

	public ResultSet get(Statement stmt, String dbURL, String sql) {
		return new MemoryResultSet(stmt, media.getTrack(dbURL, SQL.normalize(sql)));
	}

	protected void doAdd(String dbURL, String sql, ResultSet rs) {
		
		List names;
		
		try {

			ResultSetMetaData meta = rs.getMetaData();
	
			int n = meta.getColumnCount();
	
			names = new ArrayList();
	
			for (int i = 0; i < n; i++) {
				names.add(meta.getColumnName(i + 1));
			}
		
			media.newTrack(dbURL, SQL.normalize(sql), names);
    	    		
			while (rs.next()) {

				List row = new ArrayList(n);

				for (int i = 1; i <= n; i++) {
					row.add(rs.getObject(i));
				}

				media.write(row);
    			
			}
			
			media.closeTrack();

		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
		
	}

	Media media;
	
}
