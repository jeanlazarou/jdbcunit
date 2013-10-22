/*
 * @author: Jean Lazarou
 * @date: February 24, 2004
 */
package com.ap.jdbcunit.csv;

import java.util.Iterator;

import com.ap.store.Store;
import com.ap.jdbcunit.util.MediaTestCase;
import com.ap.util.Lists;

public class TestCSVMedia extends MediaTestCase {

	public TestCSVMedia(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		media = new CSVMedia(toc);
	}

	public void testSaveNoData() {
		media.open();
		media.close();

		assertEquals(new String[] {"dbURL, SQL, Name",}, toc.reader());

		assertEquals(1, repository.children().size());
	}
	
	public void testWritingTOCAndData() {

		media.open();
		addSelectAll(media);		
		addSelectLoyd(media);
		media.close();
		
		verify();

	}

	public void testReloadDataFromStore() {
		
		media.open();
		addSelectAll(media);		
		addSelectLoyd(media);
		media.close();
			
		media = new CSVMedia(toc);
		
		media.open();
		
		Iterator it = media.getTrack("jdbc:driver:Database", "SELECT * FROM people");
		
		assertTrue(it.hasNext());		
		assertEquals(Lists.toList(new String[] {"name","firstname","birthdate"}), 
		             it.next());		
		
		assertTrue(it.hasNext());
		assertEquals(Lists.toList(new String[] {"Loyd","Mike","12/03/1901"}), 
					 it.next());		
		
		assertTrue(it.hasNext());
		assertEquals(Lists.toList(new String[] {"White","Jack","1/04/1863"}), 
					 it.next());		
		
		assertTrue(!it.hasNext());
		
		media.close();
	}
	
	public void testWritingNullValues() {

		media.open();
		addSelectWithNulls(media);		
		media.close();

		Store store = repository.child("toc_1.csv");
		
		assertEquals(new String[] {
			"\"name\",\"firstname\",\"birthdate\"",
			"\"null\",null,\"1/03/2000\"",
			}, store.reader());
	}
	
	public void testEscapedStrings() {

		media.open();
		addSelectWithQuotes(media);		
		media.close();

		Store store = repository.child("toc_1.csv");
		
		assertEquals(new String[] {
			"\"isbn\",\"title\",\"author\"",
			"\"0596000162\",\"Java and XML\",\"Brett McLaughlin\"",
			"\"0201485435\",\"XML and Java: \"\"Developing Web Applications\"\"\",\"Hiroshi Maruyama\"",
			"\"1565927095\",\"XML \"\"Pocket Reference\",\"Robert Eckstein\"",
			}, store.reader());		

	}
	
	private void verify() {
		assertEquals(new String[] {
			"dbURL, SQL, Name",
			"\"jdbc:driver:Database\",\"SELECT * FROM people\",\"toc_1.csv\"",
			"\"jdbc:driver:Database\",\"SELECT * FROM people WHERE name = 'Loyd'\",\"toc_2.csv\""
			}, toc.reader());
		
		assertEquals(3, repository.children().size());
		
		assertTrue(repository.child("toc_1.csv") != null);
		assertTrue(repository.child("toc_2.csv") != null);
		
		Store store = repository.child("toc_1.csv");
		
		assertEquals(new String[] {
			"\"name\",\"firstname\",\"birthdate\"",
			"\"Loyd\",\"Mike\",\"12/03/1901\"",
			"\"White\",\"Jack\",\"1/04/1863\"",
			}, store.reader());
		
		store = repository.child("toc_2.csv");
		
		assertEquals(new String[] {
			"\"name\",\"firstname\",\"birthdate\"",
			"\"Loyd\",\"Mike\",\"12/03/1901\"",
			}, store.reader());
	}

}
