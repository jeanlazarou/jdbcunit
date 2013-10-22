/*
 * @author: Jean Lazarou
 * @date: 15 févr. 04
 */
package com.ap.jdbcunit;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import com.ap.store.Store;

public class JDBCUnit {

	public static void registerDriver(String sqlDriverClass) {

		try {

			Class clazz = Class.forName(sqlDriverClass);

			Driver drv = (Driver) clazz.newInstance();
			
			registerDriver(drv);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}

	public static void registerDriver(Driver sqlDriver) {
        try {
        	
            Enumeration it = DriverManager.getDrivers();

            while (it.hasMoreElements()) {

                Driver driver = (Driver) it.nextElement();

                if (driver.getClass().equals(sqlDriver.getClass())) {
                    DriverManager.deregisterDriver(driver);
                }

            }

            DriverManager.registerDriver(new DriverWrapper(sqlDriver));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	public static void resetDrivers() {

		try {
        	
			DriverWrapper.deregisterDrivers();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void start(Store store) {	
		start(new MediaRecorder(MediaManager.createMedia(store)));
	}
	
	public static void start(Recorder recorder_) {
		if (started) {
			throw new IllegalStateException("Must be stopped");       
		}
		
		if (recorder_ == null) {
			throw new IllegalArgumentException();
		}
		
		recorder = recorder_;		
		recorder.start();

		started = true;
	}
	
	public static void stop() {
		if (!started) {
			throw new IllegalStateException("Must be started");       
		}

		if (recorder != null) {
			recorder.stop();
		}

		recorder = null;
		
		recording = false;
		replaying = false;
		
		started = false;
	}
	
    public static void record() {
    	if (!started) {
    		throw new IllegalStateException("Must be started");       
    	}
    	
        recording = true;
		replaying = false;
    }

    public static boolean isRecording() {
        return recording;
    }

	public static void replay() {
		if (!started) {
			throw new IllegalStateException("Must be started");       
		}

		recording = false;
		replaying = true;
	}

	public static boolean isReplaying() {
		return replaying;
	}
	
    static Recorder getRecorder() {
        return recorder;
    }
    
    public static void clear() {
    	recorder.clear();
    }

	public static void reset() {
		recorder = null;
		
		recording = false;
		replaying = false;
		
		started = false;
	}

	static boolean started;
	static boolean recording;
	static boolean replaying;

	static Recorder recorder;
}
