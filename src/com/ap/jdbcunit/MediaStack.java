/*
 * @author: Jean Lazarou
 * @date: April 12, 2004
 */
package com.ap.jdbcunit;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;


public class MediaStack implements Media {
	
	boolean isOpen = false;
	Stack stack = new Stack();
	
	public void push(Media media) {
		
		stack.push(media);
		
		if (isOpen) media.open();
		
	}

	public void open() {
		
		for (int i = stack.size() - 1; i >= 0; i--) {
			
			Media media = (Media) stack.get(i);
			
			media.open();
		}
		
		isOpen = true;
		
	}

	public void close() {

		checkState();				

		for (int i = stack.size() - 1; i >= 0; i--) {
			
			Media media = (Media) stack.get(i);
			
			media.close();
		}
		
		isOpen = true;
		
	}

	public void delete() {

		for (int i = stack.size() - 1; i >= 0; i--) {
			
			Media media = (Media) stack.get(i);
			
			media.delete();
		}
		
	}

	public int countTracks() {
		
		checkState();

		int count = 0;
		
		for (int i = stack.size() - 1; i >= 0; i--) {
			
			Media media = (Media) stack.get(i);
			
			count += media.countTracks();
		}

		return count;
	}

	public void foreachTrack(MediaVisitor visitor) {
		
		checkState();

		for (int i = stack.size() - 1; i >= 0; i--) {
			
			Media media = (Media) stack.get(i);
			
			media.foreachTrack(visitor);
			
		}

	}
	
	public boolean existsTrack(String dbURL, String sql) {
				
		checkState();				
		
		for (int i = stack.size() - 1; i >= 0; i--) {
			
			Media media = (Media) stack.get(i);
			
			if (media.existsTrack(dbURL, sql)) {
				return true;
			}
			
		}

		return false;
	}

	public void newTrack(String dbURL, String sql, List columnNames) {
				
		checkState();				
		
		Media top = (Media) stack.peek();
		
		top.newTrack(dbURL, sql, columnNames);
		
	}

	public void write(List row) {
				
		checkState();				
		
		Media top = (Media) stack.peek();
		
		top.write(row);
		
	}

	public void closeTrack() {
				
		checkState();				
		
		Media top = (Media) stack.peek();
		
		top.closeTrack();
		
	}

	public Iterator getTrack(String dbURL, String sql) {
				
		checkState();				
		
		for (int i = stack.size() - 1; i >= 0; i--) {
			
			Media media = (Media) stack.get(i);
			
			if (media.existsTrack(dbURL, sql)) {
				return media.getTrack(dbURL, sql);
			}
			
		}
		
		throw new NoSuchElementException(dbURL + ", " + sql);
		
	}

	public void deleteTrack(String dbURL, String sql) {
		
		checkState();				
		
		for (int i = stack.size() - 1; i >= 0; i--) {
			
			Media media = (Media) stack.get(i);
			
			if (media.existsTrack(dbURL, sql)) {
				media.deleteTrack(dbURL, sql);
			}
			
		}
		
	}

	private void checkState() {
		if (!isOpen) {
			throw new IllegalStateException("Media should be open");
		}
	}
}
