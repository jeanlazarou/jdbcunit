/*
 * @author: Jean Lazarou
 * @date: March 23, 2004
 */
package com.ap.jdbcunit;

public interface MediaVisitor {
	void visit(String dbURL, String sql, Object track);
}
