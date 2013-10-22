/*
 * Created on 22-mai-2005
 */
package com.ap.jdbcunit;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A <code>Recorder<code> that records select, insert, update and delete statements.
 * If the update statements changes the result sets of the select statements the
 * correct result set is recorded.
 * <p>
 * In playback mode the result sets also change after the same updates are
 * replayed.
 *  
 * @author Jean Lazarou
 */
public class MediaSequencer extends SelectsMediaRecorder {

	public MediaSequencer(Media media) {
		super(media);
	}
	
	public void start() {
		
		super.start();
		
		if (media.existsTrack("sequencer", "get sequence")) {
			
			media.getTrack("sequencer", "get sequence");
			
		} else {

			frames.add(new Frame());

		}
		
	}

	public void stop() {
		
		if (media.existsTrack("sequencer", "get sequence")) {	
			media.deleteTrack("sequencer", "get sequence");
		}
			
		List columns = Arrays.asList(new Object[] {"dbUrl", "sql", "version", "frame", "isUpdate"});
		
		media.newTrack("sequencer", "get sequence", columns);
		
		int count = 1;
		
		Integer version = new Integer(1);
		
		Iterator it = frames.iterator();
		
		while(it.hasNext()) {
			
			Frame frame = (Frame) it.next();
			
			Boolean isUpdate = frame.isStateChangeFrame() ? Boolean.TRUE : Boolean.FALSE;
			
			Iterator keys = frame.statements.keySet().iterator();
			
			while(keys.hasNext()) {

				Key k = (Key) keys.next();
				
				media.write(Arrays.asList(new Object[] {k.value[0], k.value[1], version, new Integer(count), isUpdate}));
				
			}

			count++;
			
		}
		
		media.closeTrack();
			
		super.stop();
		
	}

	public boolean existsTrack(String dbURL, String sql) {
		return media.existsTrack(dbURL, sql);
	}

	public ResultSet get(Statement stmt, String dbURL, String sql) {

		if (!isSelect(sql)) {
			throw new JDBCUnitException("Current version is limited to select");
		}
		
		return super.get(stmt, dbURL, sql);
		
	}

	public void add(String dbURL, String sql, ResultSet rs) {

		if (!isSelect(sql)) {
			throw new JDBCUnitException("Current version is limited to select");
		}

		Frame frame = (Frame) frames.get(frames.size() - 1);

		frame.add(dbURL, sql);

		doAdd(dbURL, sql, rs);
		
	}

	private boolean isSelect(String sql) {
		return sql.toLowerCase().startsWith("select");
	}

	List frames = new ArrayList();
	
}

class Frame {
	
	public boolean isStateChangeFrame() {
		return isStateChangeFrame;
	}

	public void add(String dbURL, String sql) {
		
		if (statements.size() == 0) {
			isStateChangeFrame = !sql.toLowerCase().startsWith("select");
		}
		
		statements.put(key(dbURL, sql), null);
		
	}

	public void clearMarks() {
		
		Iterator it = statements.keySet().iterator();
		
		while (it.hasNext()) {
			
			String key = (String) it.next();
			
			statements.put(key, null);
			
		}
		
	}

	public void markAsDone(String dbURL, String sql) {
		statements.put(key(dbURL, sql), Boolean.TRUE);
	}
	
	public String missing() {

		StringBuffer buffer = new StringBuffer();
				
		Iterator it = statements.keySet().iterator();
		
		while (it.hasNext()) {
			
			String key = (String) it.next();
			
			Boolean b = (Boolean) statements.get(key);
			
			if (b == null || !b.booleanValue()) {
				buffer.append(key + "\n");
			}
			
		}
		
		return buffer.toString();

	}

	public boolean allDone() {
		
		Iterator it = statements.keySet().iterator();
		
		while (it.hasNext()) {
			
			String key = (String) it.next();
			
			Boolean b = (Boolean) statements.get(key);
			
			if (b == null || !b.booleanValue()) {
				return false;
			}
			
		}
		
		return true;
		
	}

	private Key key(String dbURL, String sql) {
		return new Key(dbURL, sql);
	}

	Map statements = new HashMap();
	
	boolean isStateChangeFrame;
	
}

class Key {

	public Key(String dbURL, String sql) {
		value[0] = dbURL;
		value[1] = sql;
	}
	
	public boolean equals(Object obj) {
		
		if (this == obj) return true;
		if (!(obj instanceof Key)) return false;
		
		Key o =(Key) obj;
		
		return value[0] != null ? value[0].equals(o.value[0]) : o.value[0] == null &&
			   value[1] != null ? value[1].equals(o.value[1]) : o.value[1] == null;
		
	}

	public int hashCode() {
		
		int hc = 17;
		
		if (value[0] != null) hc = 37 * hc + value[0].hashCode();
		if (value[1] != null) hc = 37 * hc + value[1].hashCode();
		
		return hc;
		
	}

	Object[] value = new Object[2];
	
}