/*
 * @author: Jean Lazarou
 * @date: February 22, 2004
 */
package com.ap.jdbcunit.csv;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import com.ap.store.Store;
import com.ap.jdbcunit.Media;
import com.ap.jdbcunit.MediaVisitor;

	
/**
 * A CSV media is writing the tracks, the result sets, to distinct files
 * and stores, at the same level, the TOC file.
 * 
 * All the files (stores) are formatted using a comma separated values 
 * text format.
 *
 */
public class CSVMedia implements Media {
	
	int id = 1;	
	Store track;
	PrintWriter outWriter;

	Map urls = new HashMap();	
	
	Store toc;
	Store container;

	/**
	 * Creates a CSVMedia that is writing the TOC, the repository, to the 
	 * given store and the result sets are stored at the same level as the 
	 * repository.
	 *  
	 * @param repository the <tt>Store</tt> that contains the TOC
	 */
	public CSVMedia(Store repository) {
		this.container = repository.getParent();
		this.toc = repository;
	}

	public void open() {
		
		if (!toc.exists()) {
			toc.create();			
		} else {
			
			urls.clear();
			
			boolean first = true;
			
			CSVFileIterator it = new CSVFileIterator(toc.reader());
			
			while (it.hasNext()) {
				
				List row = (List) it.next();
				
				if (first) {
					first = false;
					continue;
				} 

				Map statements;
				
				if (!urls.containsKey(row.get(0))) {
					
					statements = new TreeMap();
				
					urls.put(row.get(0), statements);
					
				} else {
					statements = (Map) urls.get(row.get(0));
				}
				
				if (!statements.containsKey(row.get(1))) {
					statements.put(row.get(1), container.child((String) row.get(2)));
				}
								
			}
		}
		
		isOpen = true;
	}

	public void close() {

		checkState();
				
		// save TOC
		 
		final PrintWriter out = toc.printWriter();
		
		out.println("dbURL, SQL, Name");

		foreachTrack(new MediaVisitor() {

			public void visit(String dbURL, String sql, Object track) {

				out.print('"');
				out.print(dbURL);
				out.print("\",\"");
				
				out.print(sql);
				out.print("\",\"");
				
				out.print(((Store) track).getName());
				
				out.print('"');

				out.println();
				
			}
			
		});

		out.flush();
		out.close();
		
		toc.sync();
	}

	public void delete() {

		checkState();
		
		foreachTrack(new MediaVisitor() {

			public void visit(String dbURL, String sql, Object track) {
				((Store) track).delete();
			}
			
		});

		urls.clear();

	}

	public int countTracks() {
		
		checkState();

		Counter counter = new Counter();
		
		foreachTrack(counter);

		return counter.count;
		
	}
	
	public void foreachTrack(MediaVisitor visitor) {

		checkState();

		Iterator it = urls.keySet().iterator();
		
		while(it.hasNext()) {
			
			String url = (String) it.next();

			Map statements = (Map) urls.get(url);
			
			Iterator sqlIt = statements.keySet().iterator();
			
			while(sqlIt.hasNext()) {

				String sql = (String) sqlIt.next();
				Store track = (Store) statements.get(sql);

				visitor.visit(url, sql, track);
								
			}
		}
	}
	
	public boolean existsTrack(String dbURL, String sql) {
		
		checkState();
		
		if (!urls.containsKey(dbURL)) {
			return false;
		}
		
		Map statements = (Map) urls.get(dbURL);
		
		if (!statements.containsKey(sql)) {
			return false;
		}

		return true;
	}
	
	public void newTrack(String dbURL, String sql, List columnNames) {
		
		checkState();
		
		Map statements;
		
		if (!urls.containsKey(dbURL)) {
		
			statements = new TreeMap();
		
			urls.put(dbURL, statements);

		} else {

			statements = (Map) urls.get(dbURL);
			
			if (statements.containsKey(sql)) {
				return;
			}
		}
		
		String trackName = toc.getName();
		
		if (trackName.endsWith(".csv") && trackName.length() > 4) { 
			trackName = trackName.substring(0, trackName.length() - 4);
		}
		
		trackName = trackName + "_" + id++ + ".csv";
		
		track = container.add(trackName);
		
		outWriter = track.printWriter();
		
		writeList(columnNames);
		
		statements.put(sql, track);
	}

	public void closeTrack() {
		
		checkState();
		
		if (outWriter != null) {
			outWriter.flush();
			outWriter.close();
			outWriter = null;
		}

	}

	public void write(List row) {
		checkState();
		
		if (outWriter != null) writeList(row);
	}
	
	public Iterator getTrack(String dbURL, String sql) {
		
		checkState();
		
		if (!urls.containsKey(dbURL)) {
			throw new NoSuchElementException(dbURL);
		}
		
		Map statements = (Map) urls.get(dbURL);
		
		if (!statements.containsKey(sql)) {
			throw new NoSuchElementException(dbURL + ", " + sql);
		}
		
		Store track = (Store) statements.get(sql);
		
		return new CSVFileIterator(track.reader());
		
	}

	public void deleteTrack(String dbURL, String sql) {
		
		checkState();
		
		if (!urls.containsKey(dbURL)) {
			return;
		}

		Map statements = (Map) urls.get(dbURL);
		
		if (!statements.containsKey(sql)) {
			return;
		}

		Store track = (Store) statements.remove(sql);

		track.delete();
		
	}

	private void writeList(List list) {
		
		String sep = "";
		Iterator it = list.iterator();
		
		while (it.hasNext()) {
			
			Object value = it.next();

			outWriter.print(sep);
			
			if (value == null) {
				outWriter.print("null");
			} else {
				outWriter.print('"');
				outWriter.print(escape(value));
				outWriter.print('"');
			}
			
			sep = ",";
			
		}
		
		outWriter.println();
	}
	
	private String escape(Object obj) {
		
		char[] buffer = String.valueOf(obj).toCharArray();
		
		StringBuffer result = new StringBuffer(buffer.length);
		
		for (int i = 0; i < buffer.length; i++) {
			
			if (buffer[i] == '"') {
				result.append('"');
			}
			
			result.append(buffer[i]);
			
		}
		
		return result.toString();
		
	}

	private void checkState() {
		if (!isOpen) {
			throw new IllegalStateException("Media should be open");
		}
	}
	
	boolean isOpen = false;
}

class Counter implements MediaVisitor {

	int count = 0;
			
	public void visit(String dbURL, String sql, Object track) {
		count++;
	}
			
}