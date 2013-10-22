/*
 * @author Jean Lazarou
 * Date: April 13, 2004
 */
package com.ap.jdbcunit.fixtures;

import com.ap.store.MemoryStore;

import com.ap.jdbcunit.Media;
import com.ap.jdbcunit.csv.CSVMedia;

import com.ap.jdbcunit.conformance.MediaFixture;

public class CSVMediaFixture implements MediaFixture {

	public CSVMediaFixture() {
		super();
	}

	public String getName() {
		return "CSVMedia";
	}

	public void setUp() throws Exception {
	}

	public void tearDown() throws Exception {
	}

	public Media newMedia() {
		MemoryStore rep = new MemoryStore("repository");
		return new CSVMedia(rep.add("toc.csv"));
	}

}
