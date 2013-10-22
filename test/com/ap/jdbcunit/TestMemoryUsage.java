/*
 * @author: Jean Lazarou
 * @date: 1 mars 04
 */
package com.ap.jdbcunit;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.Iterator;

import com.ap.store.Store;
import com.ap.store.Content;
import com.ap.store.MemoryStore;
import com.ap.jdbcunit.csv.CSVMedia;
import com.ap.jdbcunit.util.JDBCUnitTestCase;

public class TestMemoryUsage extends JDBCUnitTestCase {

	public TestMemoryUsage(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		
		repository = new MemoryStore("repository");
		toc = repository.add("toc.csv", new TOCContent());
		result = repository.add("toc_1.csv", content = new ResultContent());
		
		media = new CSVMedia(toc);
	}

	public void testLoadOnDemand() throws Exception {
		
		media.open();
		
		Iterator it = media.getTrack("my:db:Database", "SELECT * FROM HelloWorld");
		
		assertEquals(1, content.currentLine());

		it.next();
		assertEquals(2, content.currentLine());
		
		it.next();
		assertEquals(3, content.currentLine());
		
		it.next();
		assertEquals(4, content.currentLine());
		
		//media.close(); don't need to do it
	}
		
	ResultContent content;
		
	Store toc;
	Store result;
	MemoryStore repository;
		
	Media media;
}

class TOCContent implements Content {

	public long size() {
		return content.length();
	}

	public void create() {
	}

	public boolean exists() {
		return true;
	}

	public OutputStream output() {
		return null;
	}

	public InputStream input() {
		return new ByteArrayInputStream(content.getBytes());
	}
	
	String content = "dbURL, SQL, Name\n" + 
                     "my:db:Database, SELECT * FROM HelloWorld, toc_1.csv";
}

class ResultContent implements Content {

	public long size() {
		return content.length();
	}

	public void create() {
	}

	public boolean exists() {
		return true;
	}

	public OutputStream output() {
		return null;
	}

	public InputStream input() {
		return new LocalReader(content);
	}
	
	public int currentLine() {
		return line;
	}
						 
	int line;
	
	String content = "Field, Field2\n" + 
					 "1, Hello\n" + 
					 "2, Hello\n" + 
					 "3, Hello\n" ;
					 
	class LocalReader extends InputStream {

		int pos = 0;
		byte[] content;
										 
		public LocalReader(String s) {
			content = s.getBytes();
		}

		public int read() {
			
			if (pos >= content.length) {
				return -1;
			}
			
			int c = content[pos++];
			
			if (c == '\n') line++;
			
			return c;
		}

		public int read(byte[] b, int off, int len) throws IOException {
		
			// force len - 1 to prevent io objects to fill a buffer (cache)
			  	
			int n = super.read(b, off, 1);
			
			if (n == -1) return -1;
			
			return 1;
		
		}

	}
}