/*
 * Created on 1 juin 2004
 */
package com.ap.jdbcunit.bookstore;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ap.util.Dates;

/**
 * @author jean
 */
public class BookStore {
	
	public Book[] getNewBooks() throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:odbc:MyBookStore");
		
		PreparedStatement stmt = con.prepareStatement("select * from bookstore where boughtDate >= ?");

		// again, sorry for deprecated method
		stmt.setDate(1, Dates.createSQLDate(Integer.parseInt(today.substring(0, 4)), 1, 1));
		
		ResultSet rs = stmt.executeQuery();
		
		int len = 0;
		
		while (rs.next()) len++;
		 
		return new Book[len];
	}
	
	public void setToday(Date today) {
		this.today = today.toString();
	}
	
	String today = new Date(System.currentTimeMillis()).toString();
}
