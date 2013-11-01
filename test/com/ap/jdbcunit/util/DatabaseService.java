/*
 * @author: Jean Lazarou
 * @date: 15 feb. 04
 */
package com.ap.jdbcunit.util;

import org.hsqldb.jdbc.JDBCDriver;

import java.sql.*;
import java.util.Properties;

public class DatabaseService {

    public static void createDatabase() throws SQLException {
        createDatabase(true);
    }

    public static void createDatabase(boolean empty) throws SQLException {

        Connection con = connect();

        if (!empty) {

            Statement statement = con.createStatement();

            statement.execute("SET DATABASE DEFAULT TABLE TYPE MEMORY");

            statement.execute(
                    "CREATE TABLE Persons (" +
                         "Id INTEGER," +
                         "LastName VARCHAR(50)," +
                         "FirstName VARCHAR(50), " +
                         "LivesIn INTEGER " +
                    ")"
            );

            statement.execute(
                    "CREATE TABLE Countries (" +
                         "Id INTEGER," +
                         "Name VARCHAR(50)" +
                    ")"
            );

            statement.execute(
                    "CREATE TABLE Payroll (" +
                            "Id INTEGER," +
                            "Name VARCHAR(50)," +
                            "HireDate TIMESTAMP," +
                            "Salary DECIMAL" +
                            ")"
            );

            fillTables(con);

            statement.close();

        }

        con.close();

    }

    public static void clear() throws SQLException {

        Connection con = connect();

        Statement statement = con.createStatement();

        statement.execute("DROP TABLE Persons IF EXISTS");
        statement.execute("DROP TABLE Countries IF EXISTS");
        statement.execute("DROP TABLE Payroll IF EXISTS");

        statement.close();

        con.close();

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

    static Connection connect() throws SQLException {

        Properties info = new Properties();
        JDBCDriver driver = new JDBCDriver();

        return driver.connect("jdbc:hsqldb:mem:TestDatabase", info);

    }

    static void fillTables(Connection con) throws SQLException {

        insertPersons(con);
        insertCountries(con);
        insertPayrolls(con);

    }

    static void insertPersons (Connection con) throws SQLException {
        Statement stmt = con.createStatement();

        stmt.executeUpdate("INSERT INTO persons VALUES (1, 'LName1', 'FName1', 1)");
        stmt.executeUpdate("INSERT INTO persons VALUES (2, 'LName2', 'FName2', 2)");
        stmt.executeUpdate("INSERT INTO persons VALUES (3, 'LName3', 'FName3', 2)");

        stmt.close();

    }

    static void insertCountries (Connection con) throws SQLException {
        Statement stmt = con.createStatement();

        stmt.executeUpdate("INSERT INTO countries VALUES (1, 'Country1')");
        stmt.executeUpdate("INSERT INTO countries VALUES (2, 'Country2')");
        stmt.executeUpdate("INSERT INTO countries VALUES (3, 'Country3')");

        stmt.close();

    }
    
    static void insertPayrolls(Connection con) throws SQLException {
    	
        Statement stmt = con.createStatement();

        stmt.executeUpdate("INSERT INTO payroll VALUES (1, 'Big', '2005-05-12 00:00:00', 6000)");
        stmt.executeUpdate("INSERT INTO payroll VALUES (2, 'Small', '2003-08-04 00:00:00', 1200)");

        stmt.close();

    }

}
