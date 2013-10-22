/*
 * @author: Jean Lazarou
 * @date: February 22, 2004
 */
package com.ap.jdbcunit;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.easymock.MockControl;

import com.ap.jdbcunit.util.DatabaseService;
import com.ap.jdbcunit.util.JDBCUnitTestCase;
import com.ap.util.Lists;

public class TestUnitSequence extends JDBCUnitTestCase {

	public TestUnitSequence(String name) {
		super(name);
	}

	public void testNotStarted() throws Exception {
		
		try {
			
			JDBCUnit.record();
			
			fail("IllegalStateException wasn't thrown");
			
		} catch(IllegalStateException e) {
			// ok, that's right
		}
		
		try {
			
			JDBCUnit.stop();
			
			fail("IllegalStateException wasn't thrown");
			
		} catch(IllegalStateException e) {
			// right
		}
		
		try {
			
			JDBCUnit.replay();
			
			fail("IllegalStateException wasn't thrown");
			
		} catch(IllegalStateException e) {
			// ...
		}
		
	}

	public void testNotStopped() throws Exception {
		
		Recorder recorder = new Recorder() {
			public void start() {}
			public void stop() {}
			public void clear() {}
			public boolean existsTrack(String dbURL, String sql) {return false;}
			public void add(String dbURL, String sql, ResultSet rs) {}
			public ResultSet get(Statement stmt, String dbURL, String sql) {return null;}
		};
			
		JDBCUnit.start(recorder);

		try {
			JDBCUnit.start((Recorder) null);
			
			fail("IllegalStateException wasn't thrown");
			
		} catch(IllegalStateException e) {
			// ...
		}
		
	}

	public void testRecording() throws Exception {

		MediaRecorder recorder = new MediaRecorder(recordSequence());

		JDBCUnit.start(recorder);

		JDBCUnit.record();

		stmt = con.createStatement();

		rs = stmt.executeQuery("SELECT * FROM persons");
		
		JDBCUnit.stop();

		control.verify();
	}

	public void testReplaying() throws Exception {

		MediaRecorder recorder = new MediaRecorder(replaySequence());

		JDBCUnit.start(recorder);

		JDBCUnit.replay();

		stmt = con.createStatement();

		rs = stmt.executeQuery("SELECT * FROM persons");
		
		DatabaseService.containsAllPersons(rs);
		
		JDBCUnit.stop();

		control.verify();
	}

	public void testRecordingAndReplaying() throws Exception {

		MediaRecorder recorder = new MediaRecorder(recordReplaySequence());

		JDBCUnit.start(recorder);

		JDBCUnit.record();

		stmt = con.createStatement();

		rs = stmt.executeQuery("SELECT * FROM persons");

		JDBCUnit.replay();

		stmt = con.createStatement();

		rs = stmt.executeQuery("SELECT * FROM persons");
		
		DatabaseService.containsAllPersons(rs);
		
		JDBCUnit.stop();

		control.verify();
	}

	private Media recordSequence() {
		
		Media media = (Media) control.getMock();
		
		media.open();
		control.setVoidCallable();

		recordSequence(media);
		
		media.close();
		control.setVoidCallable();
		
		control.replay();

		return media;
	}
	
	private void recordSequence(Media media) {
		
		media.existsTrack("jdbc:ap:TestDatabase", "select * from persons");
		control.setDefaultReturnValue(false);
		
		media.newTrack("jdbc:ap:TestDatabase", "select * from persons", Lists.toList(new String[] {"id", "lastname", "firstname", "livesin"}));
		control.setVoidCallable();
		
		media.write(Lists.toList(new Object[] {new Integer(1), "LName1", "FName1", new Integer(1)}));
		control.setVoidCallable();
		media.write(Lists.toList(new Object[] {new Integer(2), "LName2", "FName2", new Integer(2)}));
		control.setVoidCallable();
		media.write(Lists.toList(new Object[] {new Integer(3), "LName3", "FName3", new Integer(2)}));
		control.setVoidCallable();
		
		media.closeTrack();
		control.setVoidCallable();

	}

	private Media replaySequence() {

		Media media = (Media) control.getMock();
		
		media.open();
		control.setVoidCallable();

		replaySequence(media);
				
		media.close();
		control.setVoidCallable();
		
		control.replay();

		return media;

	}

	private void replaySequence(Media media) {
		
		List data = new ArrayList();
		
		data.add(Lists.toList(new String[] {"id", "lastname", "firstname", "livesin"}));
		data.add(Lists.toList(new Object[] {new Integer(1), "LName1", "FName1", new Integer(1)}));
		data.add(Lists.toList(new Object[] {new Integer(2), "LName2", "FName2", new Integer(2)}));
		data.add(Lists.toList(new Object[] {new Integer(3), "LName3", "FName3", new Integer(2)}));
		
		media.getTrack("jdbc:ap:TestDatabase", "select * from persons");
		control.setReturnValue(data.iterator());

	}

	private Media recordReplaySequence() {

		Media media = (Media) control.getMock();
		
		media.open();
		control.setVoidCallable();

		recordSequence(media);
		replaySequence(media);
				
		media.close();
		control.setVoidCallable();
		
		control.replay();

		return media;

	}

	protected void setUp() throws Exception {
		super.setUp();
		
		control = MockControl.createStrictControl(Media.class);
	}
	
	private MockControl control;
}
