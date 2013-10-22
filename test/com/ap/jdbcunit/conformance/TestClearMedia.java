/*
 * @author: Jean Lazarou
 * @date: April 24, 2004
 */
package com.ap.jdbcunit.conformance;

import java.util.Iterator;

public class TestClearMedia extends MediaConformanceTestCase {

	public TestClearMedia(MediaFixture fixture, String method) {
		super(fixture, method);
	}
	
	public void testEmptyMedia() {
		
		media.open();
		media.close();
		
		media.open();
		media.delete();
		media.close();
		
	}
	
	public void testNonEmptyMedia() {
		
		media.open();
		addSelectAll(media);
		addSelectLoyd(media);
		media.close();
		
		media.open();
		media.delete();
		media.close();

		media.open();
		assertEquals(0, media.countTracks());
		media.close();
		
	}
	
	public void testOneTrack() {
		
		media.open();
		addSelectAll(media);
		addSelectLoyd(media);
		media.close();
		
		media.open();
		media.deleteTrack("jdbc:driver:Database", "SELECT * FROM people");
		media.close();
		
		media.open();

		assertEquals(1, media.countTracks());
		
		Iterator it = media.getTrack("jdbc:driver:Database", "SELECT * FROM people WHERE name = 'Loyd'");
		
		assertIsLoyd(it);

		media.close();

	}
	
}
