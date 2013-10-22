/*
 * @author: Jean Lazarou
 * @date: March 9, 2004
 */
package com.ap.jdbcunit;

import com.ap.store.Store;

public interface MediaFactory {
	Media create(Store store);
	boolean accepts(Store store);
}
