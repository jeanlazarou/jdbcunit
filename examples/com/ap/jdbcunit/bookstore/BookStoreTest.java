/*
 * Created on 1 juin 2004
 */
package com.ap.jdbcunit.bookstore;

import java.util.ArrayList;
import java.util.List;

import com.ap.jdbcunit.JDBCUnit;
import com.ap.jdbcunit.MediaRecorder;
import com.ap.util.Dates;

import junit.framework.TestCase;

public class BookStoreTest extends TestCase {

	public BookStoreTest(String name) {
		super(name);
	}

	public void testNoNewBooks() throws Exception {

		BookStore bs = new BookStore();
		
		Book[] books = bs.getNewBooks();
		
		assertEquals(0, books.length);

	}

	public void testOneBook() throws Exception {

		initData();
		
		BookStore bs = new BookStore();
		
		bs.setToday(Dates.createSQLDate(2004, 6, 6));
		
		Book[] books = bs.getNewBooks();
		
		assertEquals(1, books.length);
		
	}

	void initData() {

		List data = new ArrayList();

		data.add("Hello");
		data.add("2004/02/27");
		
		result.add(data);

	}

	protected void setUp() throws Exception {

		result = new ArrayList();
		header = new ArrayList();

		header.add("name");
		header.add("boughtDate");
		
		result.add(header);
		
		JDBCUnit.registerDriver("sun.jdbc.odbc.JdbcOdbcDriver");
		JDBCUnit.start(new MediaRecorder(new MockMedia(result)));
		JDBCUnit.replay();

	}

	protected void tearDown() throws Exception {
		JDBCUnit.stop();
		JDBCUnit.resetDrivers();
	}

	List result;
	List header;

}
