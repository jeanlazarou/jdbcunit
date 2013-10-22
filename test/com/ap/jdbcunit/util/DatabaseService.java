/*
 * @author: Jean Lazarou
 * @date: 15 févr. 04
 */
package com.ap.jdbcunit.util;

import com.ap.straight.HashDatabase;
import com.ap.straight.HashContainer;
import com.ap.straight.HashTable;

import java.math.BigDecimal;
import java.sql.*;

public class DatabaseService {
	
    public static void createDatabase() throws SQLException {
        createDatabase(true);
    }

    public static void createDatabase(boolean empty) throws SQLException {

        HashDatabase db = new HashDatabase();

        db.setName("TestDatabase");

        HashContainer.add(db);

        if (!empty) {

            HashTable table;

            table = new HashTable();
            table.setName("Persons");

            table.addColumn("Id", Integer.class);
            table.addColumn("LastName", String.class);
            table.addColumn("FirstName", String.class);
            table.addColumn("LivesIn", Integer.class);

            db.add(table);

            table = new HashTable();
            table.setName("Countries");

            table.addColumn("Id", Integer.class);
            table.addColumn("Name", String.class);

            db.add(table);

			table = new HashTable();
			table.setName("Payroll");

			table.addColumn("Id", Integer.class);
			table.addColumn("Name", String.class);
			table.addColumn("HireDate", Date.class);
			table.addColumn("Salary", BigDecimal.class);

			db.add(table);

            fillTables();
        }
    }

    public static void clear() {
        HashContainer.clear();
    }

    public static boolean containsAllPersons(ResultSet rs) {

        try {
            if (!rs.next()) return false;

            if (rs.getInt(1) != 1) return false;
            if (!rs.getString(2).equals("LName1")) return false;
            if (!rs.getString(3).equals("FName1")) return false;

            if (!rs.next()) return false;

            if (rs.getInt(1) != 2) return false;
            if (!rs.getString(2).equals("LName2")) return false;
            if (!rs.getString(3).equals("FName2")) return false;

            if (!rs.next()) return false;

            if (rs.getInt(1) != 3) return false;
            if (!rs.getString(2).equals("LName3")) return false;
            if (!rs.getString(3).equals("FName3")) return false;

            if (rs.next()) return false;

            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

    public static boolean containsAllCountries(ResultSet rs) {

        try {
            if (!rs.next()) return false;

            if (rs.getInt(1) != 1) return false;
            if (!rs.getString(2).equals("Country1")) return false;

            if (!rs.next()) return false;

            if (rs.getInt(1) != 2) return false;
            if (!rs.getString(2).equals("Country2")) return false;

            if (!rs.next()) return false;

            if (rs.getInt(1) != 3) return false;
            if (!rs.getString(2).equals("Country3")) return false;

            if (rs.next()) return false;

            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

	public static boolean containsPerson(int id, ResultSet rs) {

		try {
			if (!rs.next()) return false;

			if (id == 1) {
				if (rs.getInt(1) != 1) return false;
				if (!rs.getString(2).equals("LName1")) return false;
				if (!rs.getString(3).equals("FName1")) return false;
			} else if (id == 2) {
				if (rs.getInt(1) != 2) return false;
				if (!rs.getString(2).equals("LName2")) return false;
				if (!rs.getString(3).equals("FName2")) return false;
			} else if (id == 3) {
				if (rs.getInt(1) != 3) return false;
				if (!rs.getString(2).equals("LName3")) return false;
				if (!rs.getString(3).equals("FName3")) return false;
			} else {
				return false;
			}

			if (rs.next()) return false;

			return true;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		return false;

	}

    static void fillTables() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:ap:TestDatabase");

        insertPersons(con);
        insertCountries(con);
        insertPayrolls(con);
        
    }

    static void insertPersons (Connection con) throws SQLException {
        Statement stmt = con.createStatement();

        stmt.executeUpdate("INSERT INTO persons (1, 'LName1', 'FName1', 1)");
        stmt.executeUpdate("INSERT INTO persons (2, 'LName2', 'FName2', 2)");
        stmt.executeUpdate("INSERT INTO persons (3, 'LName3', 'FName3', 2)");
    }

    static void insertCountries (Connection con) throws SQLException {
        Statement stmt = con.createStatement();

        stmt.executeUpdate("INSERT INTO countries (1, 'Country1')");
        stmt.executeUpdate("INSERT INTO countries (2, 'Country2')");
        stmt.executeUpdate("INSERT INTO countries (3, 'Country3')");
    }
    
    static void insertPayrolls(Connection con) throws SQLException {
    	
        Statement stmt = con.createStatement();

        stmt.executeUpdate("INSERT INTO payroll (1, 'Big', '2005-05-12', 6000)");
        stmt.executeUpdate("INSERT INTO payroll (2, 'Small', '2003-08-04', 1200)");
    
    }
    
}
