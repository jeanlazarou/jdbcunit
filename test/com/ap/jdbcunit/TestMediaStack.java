/*
 * @author: Jean Lazarou
 * @date: April 12, 2004
 */
package com.ap.jdbcunit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.ap.jdbcunit.util.MediaTestCase;

public class TestMediaStack extends MediaTestCase {

	public TestMediaStack(String name) {
		super(name);
	}
	
	public void testFindTrackLowerInTheStack() {
		
		stack.open();

		assertTrue(stack.existsTrack("jdbc:driver:Database", "SELECT * FROM people"));
				
		Iterator it = stack.getTrack("jdbc:driver:Database", "SELECT * FROM people");
		
		assertIsSelectWithNulls(it);
		
		stack.close();
	}
	
	public void testTopMediaRecords() {
	
		stack.open();
		addSelectWithQuotes(stack);
		stack.close();
		
		top.open();
		
		Iterator it = top.getTrack("jdbc:driver:Database", "SELECT * FROM books");
		
		assertIsSelectWithQuotes(it);
		
		top.close();
	}
	
	public void testPushAndOpen() {
		
		assertTrue(!m1.isOpen);
		assertTrue(!m2.isOpen);
		assertTrue(!top.isOpen);
		
		stack.open();

		assertTrue(m1.isOpen);
		assertTrue(m2.isOpen);
		assertTrue(top.isOpen);
		
		stack.close();

		assertTrue(!m1.isOpen);
		assertTrue(!m2.isOpen);
		assertTrue(!top.isOpen);
				
	}
	
	public void testChangeRecordingMedia() {
		
		MemoryMedia newTop = new MemoryMedia();
		
		stack.open();
		stack.push(newTop);
		addSelectWithQuotes(stack);
		stack.close();
		
		top.open();
		assertTrue(!top.existsTrack("jdbc:driver:Database", "SELECT * FROM books"));
		top.close();
		
		newTop.open();
		assertIsSelectWithQuotes(newTop.getTrack("jdbc:driver:Database", "SELECT * FROM books"));		
		newTop.close();
		
	}
	
	public void testPushAnOpenMedia() {
		
		MemoryMedia newTop = new MemoryMedia();
		
		newTop.open();
		
		stack.open();
		stack.push(newTop);
	
		assertTrue(newTop.isOpen());

		stack.close();
		
		assertTrue(!newTop.isOpen());
		
	}

	protected void setUp() throws Exception {

		super.setUp();

		stack = new MediaStack();		
		
		m1 = new MemoryMedia();
		
		m1.open();
		addSelectWithNulls(m1);
		m1.close();
		
		m2 = new MemoryMedia();
		
		m2.open();
		addSelectAll(m2);
		addSelectLoyd(m2);
		m2.close();
		
		top = new MemoryMedia();
		
		stack.push(m1);
		stack.push(m2);
		stack.push(top);
		
	}

	MemoryMedia m1;
	MemoryMedia m2;
	MemoryMedia top;

	MediaStack stack;
}

class MemoryMedia implements Media {

	public boolean isOpen() {
		return isOpen;
	}
	
	public void open() {
		isOpen = true;
	}

	public void close() {
		isOpen = false;
	}

	public void delete() {
	}
	
	public int countTracks() {
		return 0;
	}
	
	public void foreachTrack(MediaVisitor visitor) {
	}

	public boolean existsTrack(String dbURL, String sql) {
		
		if (!isOpen) {
			throw new IllegalStateException("Media should be open");
		}
		
		return this.dbURL != null && this.dbURL.equals(dbURL) && this.sql.equals(sql);
	}

	public void newTrack(String dbURL, String sql, List columnNames) {
		
		if (!isOpen) {
			throw new IllegalStateException("Media should be open");
		}
		
		this.dbURL = dbURL;
		this.sql = sql;
		
		data = new ArrayList();
		
		data.add(columnNames);
		
	}

	public void write(List row) {
		
		if (!isOpen) {
			throw new IllegalStateException("Media should be open");
		}
		
		data.add(row);
	}

	public void closeTrack() {
		
		if (!isOpen) {
			throw new IllegalStateException("Media should be open");
		}
		
	}

	public Iterator getTrack(String dbURL, String sql) {
				
		if (existsTrack(dbURL, sql)) {
			return data.iterator();
		}
		
		throw new NoSuchElementException(dbURL + ", " + sql);
	}

	public void deleteTrack(String dbURL, String sql) {
		
		if (!existsTrack(dbURL, sql)) {
			throw new NoSuchElementException(dbURL + ", " + sql);
		}
			
		this.dbURL = null;
		this.sql = null;
		data.clear();
				
	}
	
	String dbURL;
	String sql;
	
	List data;

	boolean isOpen = false;

	public String toString() {
		return super.toString() + "[" + dbURL + ", " + sql + "]";
	}
}
