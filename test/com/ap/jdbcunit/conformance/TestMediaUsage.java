/*
 * @author: Jean Lazarou
 * @date: April 14, 2004
 */
package com.ap.jdbcunit.conformance;

import java.util.Iterator;

public class TestMediaUsage extends MediaConformanceTestCase {

	public TestMediaUsage(MediaFixture fixture, String method) {
		super(fixture, method);
	}

	public void testSaveNoData() {
		media.open();
		media.close();

		media.open();
		assertEquals(0, media.countTracks());
		media.close();
	}
	
	public void testSaveData() {

		media.open();
		addSelectAll(media);		
		addSelectLoyd(media);
		media.close();
		
		verify();

	}

	public void testSaveSameQueryOnlyOnce() {

		media.open();
		addSelectAll(media);		
		addSelectAll(media);		
		addSelectLoyd(media);
		addSelectAll(media);		
		media.close();
		
		verify();
	}

	public void testCanReOpen() {
		media.open();		
		testSaveData();
	}

	private void verify() {

		media.open();

		assertEquals(2, media.countTracks());
		
		Iterator it;

		it = media.getTrack("jdbc:driver:Database", "SELECT * FROM people");

		assertIsAllPeople(it);
		
		it = media.getTrack("jdbc:driver:Database", "SELECT * FROM people WHERE name = 'Loyd'");
		
		assertIsLoyd(it);

		media.close();
	}	
}
