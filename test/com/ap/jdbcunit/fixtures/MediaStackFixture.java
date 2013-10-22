/*
 * @author Jean Lazarou
 * Date: April 13, 2004
 */
package com.ap.jdbcunit.fixtures;

import com.ap.store.MemoryStore;

import com.ap.jdbcunit.Media;
import com.ap.jdbcunit.MediaStack;
import com.ap.jdbcunit.csv.CSVMedia;

import com.ap.jdbcunit.conformance.MediaFixture;

public class MediaStackFixture implements MediaFixture {

	public MediaStackFixture() {
		super();
	}

	public String getName() {
		return "MediaStack";
	}

	public void setUp() throws Exception {
	}

	public void tearDown() throws Exception {
	}

	public Media newMedia() {
		MemoryStore rep = new MemoryStore("repository");
		Media m = new CSVMedia(rep.add("toc.csv"));
		
		MediaStack stack = new MediaStack();
		
		stack.push(m);
		
		return stack;
	}

}
