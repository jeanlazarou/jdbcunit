/*
 * Created on 1 juin 2004
 */
package com.ap.jdbcunit.bookstore;

import java.util.Iterator;
import java.util.List;

import com.ap.jdbcunit.Media;
import com.ap.jdbcunit.MediaVisitor;

public class MockMedia implements Media {

	List values;
	
	public MockMedia(List values) {
		this.values = values;
	}
	
	public void open() {
	}

	public void close() {
	}

	public void delete() {
	}

	public int countTracks() {
		return 0;
	}

	public void foreachTrack(MediaVisitor visitor) {
	}

	public boolean existsTrack(String dbURL, String sql) {
		return false;
	}

	public void newTrack(String dbURL, String sql, List columnNames) {
	}

	public void write(List row) {
	}

	public void closeTrack() {
	}

	public Iterator getTrack(String dbURL, String sql) {
		return values.iterator();
	}

	public void deleteTrack(String dbURL, String sql) {
	}

}
