/*
 * @author: Jean Lazarou
 * @date: March 22, 2004
 */
package com.ap.jdbcunit.conformance;

import com.ap.jdbcunit.Media;

public interface MediaFixture {
	String getName();

	void setUp() throws Exception;
	void tearDown() throws Exception;
	
	Media newMedia();
}
