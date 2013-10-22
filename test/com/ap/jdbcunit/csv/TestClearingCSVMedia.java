/*
 * @author: Jean Lazarou
 * @date: April 11, 2004
 */
package com.ap.jdbcunit.csv;

import com.ap.jdbcunit.util.MediaTestCase;

public class TestClearingCSVMedia extends MediaTestCase {

	public TestClearingCSVMedia(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		media = new CSVMedia(toc);
	}
	
	public void testEmptyMedia() {
		
		media.open();
		media.close();
		
		media.open();
		media.delete();
		media.close();
		
		assertEquals(1, repository.children().size());
		
		assertEquals(new String[] {"dbURL, SQL, Name"}, toc.reader());
		
	}
	
	public void testNonEmptyMedia() {
		
		media.open();
		addSelectAll(media);
		addSelectLoyd(media);
		addSelectWithQuotes(media);		
		media.close();
		
		media.open();
		media.delete();
		media.close();
		
		assertEquals(1, repository.children().size());
		
		assertEquals(new String[] {"dbURL, SQL, Name"}, toc.reader());

	}
	
	public void testOneTrack() {
		
		media.open();
		addSelectAll(media);
		addSelectLoyd(media);
		addSelectWithQuotes(media);		
		media.close();
		
		media.open();
		media.deleteTrack("jdbc:driver:Database", "SELECT * FROM people");
		media.close();
		
		assertEquals(3, repository.children().size());
		
		assertEqualsUnordered(new String[] {
			"dbURL, SQL, Name",
			"\"jdbc:driver:Database\",\"SELECT * FROM people WHERE name = 'Loyd'\",\"toc_2.csv\"",
			"\"jdbc:driver:Database\",\"SELECT * FROM books\",\"toc_3.csv\"",
			}, toc.reader());
			
		assertNotNull(repository.child("toc"));
		assertNotNull(repository.child("toc_2.csv"));
		assertNotNull(repository.child("toc_3.csv"));

	}

}
