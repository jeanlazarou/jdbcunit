/*
 * @author: Jean Lazarou
 * @date: February 24, 2004
 */
package com.ap.jdbcunit.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ap.store.MemoryStore;
import com.ap.store.Store;

import com.ap.jdbcunit.Media;

import com.ap.jdbcunit.conformance.MediaConformanceTestCase;

public class MediaTestCase extends MediaConformanceTestCase {

	public MediaTestCase(String name) {
		super(name);
	}
	
	protected void addSelectLoyd(Media media) {
		media.newTrack("jdbc:driver:Database", "SELECT * FROM people WHERE name = 'Loyd'", columns);
		media.write(row1);
		media.closeTrack();
	}

	protected void addSelectAll(Media media) {
		media.newTrack("jdbc:driver:Database", "SELECT * FROM people", columns);
		media.write(row1);
		media.write(row2);
		media.closeTrack();
	}

	protected void addSelectWithNulls(Media media) {
		media.newTrack("jdbc:driver:Database", "SELECT * FROM people", columns);
		media.write(row3);
		media.closeTrack();
	}

	protected void addSelectWithQuotes(Media media) {
		media.newTrack("jdbc:driver:Database", "SELECT * FROM books", bookColumns);
		media.write(bookRow1);
		media.write(bookRow2);
		media.write(bookRow3);
		media.closeTrack();
	}

	protected void setUp() throws Exception {

		// "fill" people table		
		columns = new ArrayList();
		columns.add("name");
		columns.add("firstname");
		columns.add("birthdate");

		row1 = new ArrayList();
		row1.add("Loyd");
		row1.add("Mike");
		row1.add("12/03/1901");

		row2 = new ArrayList();
		row2.add("White");
		row2.add("Jack");
		row2.add("1/04/1863");

		row3 = new ArrayList();
		row3.add("null");
		row3.add(null);
		row3.add("1/03/2000");

		
		// "fill" books table		
		bookColumns = new ArrayList();
		bookColumns.add("isbn");
		bookColumns.add("title");
		bookColumns.add("author");

		bookRow1 = new ArrayList();
		bookRow1.add("0596000162");
		bookRow1.add("Java and XML");
		bookRow1.add("Brett McLaughlin");

		bookRow2 = new ArrayList();
		bookRow2.add("0201485435");
		bookRow2.add("XML and Java: \"Developing Web Applications\"");
		bookRow2.add("Hiroshi Maruyama");

		bookRow3 = new ArrayList();
		bookRow3.add("1565927095");
		bookRow3.add("XML \"Pocket Reference");
		bookRow3.add("Robert Eckstein");

		repository = new MemoryStore("repository");
		toc = repository.add("toc");
	}

	public static void assertEquals(String[] expected, Reader actual) {
		
		StringWriter buffer = new StringWriter();
		PrintWriter pw = new PrintWriter(buffer);
		
		for (int i = 0; i < expected.length; i++) {
			pw.println(expected[i]);
		}
		
		assertEquals(buffer.toString(), actual);
		
	}
		
	public static void assertEquals(String expected, Reader actual) {

		try {
			
			char[] buffer = new char[expected.length() + 20];
		
			int n = actual.read(buffer);
			
			assertEquals(expected.length(), n);			
			assertEquals(-1, actual.read());
			
			assertEquals(expected, new String(buffer, 0, n));

		} catch (IOException e) {
			fail(e.getMessage());
		} finally {
			try {
				actual.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
	}

	public static void assertEqualsUnordered(String[] expected, Reader reader) {
		
		try {
			
			BufferedReader br = new BufferedReader(reader);
			
			String line = null;
			
			List actual = new ArrayList(expected.length);
			
			while ((line = br.readLine()) != null) {
				actual.add(line);
			}
				
			if (actual.size() != expected.length) {
				fail("Expected " + expected.length + " lines, but was " + actual.size());	
			}
			
			boolean[] matched = new boolean[expected.length];
			
			for (int i = 0; i < expected.length; i++) {
				
				for (Iterator it = actual.iterator(); it.hasNext();) {
				
					line = (String) it.next();
					
					if (line.equals(expected[i])) {						
						matched[i] = true;
						break;
					}
				
				}				
			}			
		
			for (int i = 0; i < matched.length; i++) {
				if (!matched[i]) {
					fail("Expected line <" + expected[i] + "> was missing");
				}
			}
			
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	protected void assertIsSelectWithNulls(Iterator it) {
		
		assertTrue(it.hasNext());		
		List header = (List) it.next();
		assertEquals(columns, header);
		
		assertTrue(it.hasNext());
		List row = (List) it.next();
		assertEquals(row3, row);

		assertTrue(!it.hasNext());
	}
	
	protected void assertIsSelectWithQuotes(Iterator it) {
		
		assertTrue(it.hasNext());
		List header = (List) it.next();
		assertEquals(bookColumns, header);
		
		assertTrue(it.hasNext());
		List row = (List) it.next();
		assertEquals(bookRow1, row);
		
		assertTrue(it.hasNext());
		row = (List) it.next();
		assertEquals(bookRow2, row);
		
		assertTrue(it.hasNext());
		row = (List) it.next();
		assertEquals(bookRow3, row);

		assertTrue(!it.hasNext());
	}
	
	protected List columns;
	protected List row1, row2, row3;

	protected List bookColumns;
	protected List bookRow1, bookRow2, bookRow3;

	protected Store repository, toc;
}
