/*
 * @author: Jean Lazarou
 * @date: 22 févr. 04
 */
package com.ap.jdbcunit.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.ap.util.Lists;

public class CSVFileIterator implements Iterator {
	
	BufferedReader in;
	String nextLine;
	
	public CSVFileIterator(Reader in) {

		this.in = new BufferedReader(in);

		moveToNext();
	}

	public boolean hasNext() {
		return nextLine != null;
	}

	public Object next() {
		
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		
		Object res = Lists.csvSplit(nextLine, true);
		
		moveToNext();
		
		return res;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	private void moveToNext() {

		try {
			
			nextLine = this.in.readLine();
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
			nextLine = null;
		}
		
	}

}
