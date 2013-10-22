/*
 * @author: Jean Lazarou
 * @date: April 29, 2004
 */
package com.ap.jdbcunit.conformance;

import java.util.ArrayList;
import java.util.List;

import com.ap.jdbcunit.MediaVisitor;

public class TestMediaVisitor extends MediaConformanceTestCase implements MediaVisitor {

	public TestMediaVisitor(MediaFixture fixture, String method) {
		super(fixture, method);
	}
	
	public void testEmptyMedia() {
				
		media.open();
		media.foreachTrack(this);
		media.close();
		
		verify();
		
	}
	
	public void testOneTrack() {

		expected.add(new String[] {"jdbc:driver:Database", "SELECT * FROM people WHERE name = 'Loyd'"});
		
		media.open();
		addSelectLoyd(media);
		media.close();
		
		media.open();
		media.foreachTrack(this);
		media.close();
		
		verify();
		
	}
	
	public void testMultipleTracks() {
		
		expected.add(new String[] {"jdbc:driver:Database", "SELECT * FROM people"});
		expected.add(new String[] {"jdbc:driver:Database", "SELECT * FROM people WHERE name = 'Loyd'"});

		media.open();
		addSelectAll(media);
		addSelectLoyd(media);
		media.close();
		
		media.open();
		media.foreachTrack(this);
		media.close();
		
		verify();
		
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		countSucceeded = 0;
		expected = new ArrayList();
	}

	protected void verify() {
		assertEquals(expected.size(), countSucceeded);
	}
	
	public void visit(String dbURL, String sql, Object track) {
		
		if (countSucceeded >= expected.size()) {
			fail("Unexpected call to visit");
		}
	
		String[] values = (String[]) expected.get(countSucceeded);
		
		assertEquals(values[0], dbURL);	
		assertEquals(values[1], sql);
		assertNotNull(track);
		
		countSucceeded++;
			
	}
	
	List expected;	
	int countSucceeded;	

}
