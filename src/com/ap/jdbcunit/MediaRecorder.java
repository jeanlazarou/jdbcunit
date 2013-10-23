/*
 * @author: Jean Lazarou
 * @date: 15 fevr. 04
 */
package com.ap.jdbcunit;

import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import com.ap.straight.MemoryResultSet;
import com.ap.straight.SQL;

public class MediaRecorder implements Recorder {

	public MediaRecorder(Media media) {
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
		return media.existsTrack(dbURL, sql);
	}
	
    public void add(String dbURL, String sql, ResultSet rs) {

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
					if (Types.DATE == meta.getColumnType(i) || Types.TIMESTAMP == meta.getColumnType(i)) {
						row.add(rs.getTimestamp(i));
					} else if (Types.VARCHAR == meta.getColumnType(i)) {
						row.add(getNormalizedString(rs.getString(i)));
					} else {
						row.add(rs.getObject(i));
					}
				}

				media.write(row);
    			
			}
			
			media.closeTrack();

		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
    }

    private String getNormalizedString(String value){
    	return value != null ? value.replaceAll("\n", "") : value;
    }
    
	public ResultSet get(Statement stmt, String dbURL, String sql) {
		return new MemoryResultSet(stmt, media.getTrack(dbURL, SQL.normalize(sql)));
	}

	Media media;
}
