/*
 * @author: Jean Lazarou
 * @date: February 15 2004
 */
package com.ap.jdbcunit;

import java.sql.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.DriverPropertyInfo;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;

import com.ap.jdbcunit.playback.PlaybackConnection;;

public class DriverWrapper implements Driver {

    public DriverWrapper(Driver actualDriver) {
        registry.add(actualDriver);
    }

	public static void deregisterDrivers()  throws SQLException {
		registry.clear();
				
		Enumeration it = DriverManager.getDrivers();
		
		while(it.hasMoreElements()) {
			Driver drv = (Driver) it.nextElement();
			DriverManager.deregisterDriver(drv);
		}
	}
	
    public Connection connect(String url, Properties info) throws SQLException {
    	
		Driver drv = getDriver(url);
		
		if (drv == null)
			return null;
		else if (JDBCUnit.isReplaying())
			return new ConnectionWrapper(new PlaybackConnection(drv, url));
		else
			return new ConnectionWrapper(drv.connect(url, info));
			
    }

    public boolean acceptsURL(String url) throws SQLException {
    	return getDriver(url) != null;
    }

    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
    	
		Driver drv = getDriver(url);
		
		if (drv == null)
			return new DriverPropertyInfo[0];
		else
        	return drv.getPropertyInfo(url, info);
        	
    }

    public int getMajorVersion() {
        return 1;
    }

    public int getMinorVersion() {
        return 0;
    }

    public boolean jdbcCompliant() {
        return false;
    }

	private Driver getDriver(String url) throws SQLException {
		
		for (Iterator i = registry.iterator(); i.hasNext();) {
			
			Driver drv = (Driver) i.next();
			
			if (drv.acceptsURL(url)) {
				return drv;
			}
		}
		return null;		
	}
	
    static List registry = new ArrayList();
    
    @Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException();
	}
}
