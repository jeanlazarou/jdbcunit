/*
 * @author: Jean Lazarou
 * @date: March 22, 2004
 */
package com.ap.jdbcunit.conformance;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class TestMediaOpenState extends MediaConformanceTestCase {

	public TestMediaOpenState(MediaFixture fixture, String method) {
		super(fixture, method);
	}

	public void testLegalCalls() {

		media.open();
		
		media.existsTrack("","");
		
		try {
			media.getTrack("", "");
		} catch(NoSuchElementException e) {
			// of course
		}
		
		media.countTracks();
		media.foreachTrack(null);
		
		media.newTrack("", "", new ArrayList());
		media.write(new ArrayList());
		media.closeTrack();

		media.close();

	}

	public void testIllegalCalls() {
		
		try {
			media.existsTrack("","");
			fail("Media is not open");
		} catch(IllegalStateException e) {
			// indeed
		}
		
		try {
			media.getTrack("", "");
			fail("Media is not open");
		} catch(IllegalStateException e) {
		}

		try {
			media.countTracks();
			fail("Media is not open");
		} catch(IllegalStateException e) {
		}

		try {
			media.foreachTrack(null);
			fail("Media is not open");
		} catch(IllegalStateException e) {
		}
		
		try {
			media.newTrack("", "", null);
			fail("Media is not open");
		} catch(IllegalStateException e) {
		}

		try {
			media.write(null);
			fail("Media is not open");
		} catch(IllegalStateException e) {
		}

		try {
			media.closeTrack();
			fail("Media is not open");
		} catch(IllegalStateException e) {
		}

		try {
			media.close();
			fail("Media is not open");
		} catch(IllegalStateException e) {
		}
	}

}
