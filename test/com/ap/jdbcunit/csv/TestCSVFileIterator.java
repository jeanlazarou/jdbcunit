/*
 * @author: Jean Lazarou
 * @date: February 22, 2004
 */
package com.ap.jdbcunit.csv;

import java.io.StringReader;
import java.util.Iterator;

import com.ap.util.Lists;

import junit.framework.TestCase;

public class TestCSVFileIterator extends TestCase {

	public TestCSVFileIterator(String name) {
		super(name);
	}

	public void testEmpty() {
		
		StringReader reader = new StringReader("");
		
		Iterator it = new CSVFileIterator(reader);
		
		assertTrue(!it.hasNext());
		
	}

	public void testOneRow() {
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("col1, col2, col3\n");
		
		StringReader reader = new StringReader(buffer.toString());
		
		Iterator it = new CSVFileIterator(reader);
		
		assertTrue(it.hasNext());
		assertEquals(Lists.toList(new String[]{"col1", "col2", "col3"}), it.next());
		
		assertTrue(!it.hasNext());
		
	}

	public void testOneRowWithoutLF() {
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("col1, col2, col3");
		
		StringReader reader = new StringReader(buffer.toString());
		
		Iterator it = new CSVFileIterator(reader);
		
		assertTrue(it.hasNext());
		assertEquals(Lists.toList(new String[]{"col1", "col2", "col3"}), it.next());
		
		assertTrue(!it.hasNext());
		
	}

	public void testSeveralRows() {
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("col1, col2, col3\n");
		buffer.append("1, 3.4, Hello World!\n");
		buffer.append("No, Yes, \"Hello, World!\"\n");
		
		StringReader reader = new StringReader(buffer.toString());
		
		Iterator it = new CSVFileIterator(reader);
		
		assertTrue(it.hasNext());
		assertEquals(Lists.toList(new String[]{"col1", "col2", "col3"}), it.next());
		
		assertTrue(it.hasNext());
		assertEquals(Lists.toList(new String[]{"1", "3.4", "Hello World!"}), it.next());
		
		assertTrue(it.hasNext());
		assertEquals(Lists.toList(new String[]{"No", "Yes", "Hello, World!"}), it.next());
		
		assertTrue(!it.hasNext());
		
	}

	public void testRowWithNulls() {
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("col1, , null, end");
		
		StringReader reader = new StringReader(buffer.toString());
		
		Iterator it = new CSVFileIterator(reader);
		
		assertTrue(it.hasNext());
		assertEquals(Lists.toList(new String[]{"col1", null, null, "end"}), it.next());
		
		assertTrue(!it.hasNext());
		
	}
	
	public void testEscapedStrings() {
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("col1, col2, col3\n");
		buffer.append("John O\"\"Connor, \"Hey \"\"you\"\"\", Hello World!\n");
		buffer.append("No, Yes, \"Hello, World!\"\n");
		
		StringReader reader = new StringReader(buffer.toString());
		
		Iterator it = new CSVFileIterator(reader);
		
		assertTrue(it.hasNext());
		assertEquals(Lists.toList(new String[]{"col1", "col2", "col3"}), it.next());
		
		assertTrue(it.hasNext());
		assertEquals(Lists.toList(new String[]{"John O\"Connor", "Hey \"you\"", "Hello World!"}), it.next());
		
		assertTrue(it.hasNext());
		assertEquals(Lists.toList(new String[]{"No", "Yes", "Hello, World!"}), it.next());
		
		assertTrue(!it.hasNext());
	}
}
