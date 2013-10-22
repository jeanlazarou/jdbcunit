/*
 * @author: Jean Lazarou
 * @date: March 9, 2004
 */
package com.ap.jdbcunit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ap.store.Store;

import com.ap.jdbcunit.csv.CSVMedia;

public class MediaManager {
	
	public static synchronized Media createMedia(Store store) {
		
		Iterator it = getMediaFactories();
		
		while (it.hasNext()) {
			
			MediaFactory factory = (MediaFactory) it.next();
			
			if (factory.accepts(store)) {
				return factory.create(store);
			}
						
		}
		
		return null;
	}
	
	public static synchronized void registerMediaFactory(MediaFactory factory) {
		if (mediaFactories == null) {
			mediaFactories = new HashMap();
		}
		
		mediaFactories.put(factory.getClass().getName(), factory);
	}
	
	public static synchronized void deregisterMediaFactory(MediaFactory factory) {
		if (mediaFactories == null) return;
		
		mediaFactories.remove(factory.getClass().getName());		
	}
	
	public static synchronized Iterator getMediaFactories() {
		if (mediaFactories == null) {
			mediaFactories = new HashMap();
		}
		
		return mediaFactories.values().iterator(); 
	}	
	
	static {
		registerMediaFactory(new MediaFactory() {

			public Media create(Store store) {
				return new CSVMedia(store);
			}

			public boolean accepts(Store store) {
				return "csv".equals(store.getType());
			}
		});
	}
	
	static Map mediaFactories;
	
	private MediaManager() {}
}
