/*
 * @author: Jean Lazarou
 * @date: 15 févr. 04
 */
package com.ap.jdbcunit;

import com.ap.jdbcunit.util.JDBCUnitTestCase;

public class TestStatementWrapping extends JDBCUnitTestCase {

    public TestStatementWrapping(String name) {
        super(name);
    }

    public void testStatementIsWrapped() throws Exception {
        stmt = con.createStatement();
        assertTrue(stmt instanceof StatementWrapper);
    }

    public void testSelectStatment() throws Exception {

        stmt = con.createStatement();

        rs = stmt.executeQuery("SELECT * FROM persons");

        assertTrue(rs.next());
        assertEquals(1, rs.getInt(1));

        assertTrue(rs.next());
        assertEquals(2, rs.getInt(1));

        assertTrue(rs.next());
        assertEquals(3, rs.getInt(1));

    }

    public void testInsertStatment() throws Exception {

        stmt = con.createStatement();

        int count = stmt.executeUpdate("INSERT INTO persons (777, 'Bond', 'James', 1)");

        assertEquals(1, count);

    }
    
}
