/*
 * @author: Jean Lazarou
 * @date: April 14, 2004
 */
package com.ap.jdbcunit.conformance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ap.jdbcunit.Media;

import junit.framework.TestCase;

public class MediaConformanceTestCase extends TestCase {

	public MediaConformanceTestCase(String name) {
		super(name);
	}

	public MediaConformanceTestCase(MediaFixture fixture, String name) {
		super(name);
		
		this.fixture = fixture;
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

	protected void assertIsAllPeople(Iterator it) {
		
		assertTrue(it.hasNext());		
		List header = (List) it.next();
		assertEquals(columns, header);
		
		assertTrue(it.hasNext());
		List row = (List) it.next();
		assertEquals(row1, row);
		
		assertTrue(it.hasNext());
		row = (List) it.next();
		assertEquals(row2, row);

		assertTrue(!it.hasNext());
		
	}
	
	protected void assertIsLoyd(Iterator it) {
		
		assertTrue(it.hasNext());		
		List header = (List) it.next();
		assertEquals(columns, header);
		
		assertTrue(it.hasNext());
		List row = (List) it.next();
		assertEquals(row1, row);
		
		assertTrue(!it.hasNext());
		
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

		fixture.setUp();
		media = fixture.newMedia();
	}

	protected void tearDown() throws Exception {
		if (fixture != null) fixture.tearDown();
	}
	
	protected Media media;
	protected MediaFixture fixture;

	protected List columns;
	protected List row1, row2, row3;

	protected List bookColumns;
	protected List bookRow1, bookRow2, bookRow3;

}
