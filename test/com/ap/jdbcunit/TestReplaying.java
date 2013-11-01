/*
 * @author: Jean Lazarou
 * @date: 15 feb. 04
 */
package com.ap.jdbcunit;

import com.ap.jdbcunit.util.JDBCUnitTestCase;

public class TestReplaying extends JDBCUnitTestCase {

    public TestReplaying(String name) {
        super(name);
    }

    public void testSelect() throws Exception {

        JDBCUnit.start(mockRecorder);

        JDBCUnit.record();

        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT * FROM persons");

        rs.close();
        stmt.close();

        JDBCUnit.replay();

        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT * FROM persons");

        assertSame(mockRecorder.getRecorded(), rs);

        mockRecorder.verify();

        JDBCUnit.stop();

    }

    protected void setUp() throws Exception {
        super.setUp();

        mockRecorder = new PlaybackRecorder();

        mockRecorder.setExpectedSQL("SELECT * FROM persons");
    }

    PlaybackRecorder mockRecorder;
}
