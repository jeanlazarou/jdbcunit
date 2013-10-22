/*
 * Created on 22-mai-2005
 */
package com.ap.jdbcunit.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.easymock.MockControl;

import com.ap.jdbcunit.Media;
import com.ap.util.Dates;

/**
 * @author Jean Lazarou
 */
public class MediaMock {

	public MediaMock() {
		controller = MockControl.createControl(Media.class);
		media = (Media) controller.getMock();
	}
	
	public void setSequencer() {
		isSequencer = true;
	}
	
	public void setFlatRecorder() {
		isSequencer = false;
	}
	
	public void setRecordMode() {
		isRecording = true;
	}
	
	public void setPlaybackMode() {
		isRecording = false;
	}
	
	public void replay() {
		controller.replay();	
	}

	public void verify() {
		controller.verify();
	}
	
	public Media getMedia() {
		return media;
	}
	
	public void recordOpen() {
		
		media.open();
		
		if (!isSequencer) return;

		if (isRecording) recorded.push(new ArrayList());
		
		media.existsTrack("sequencer", "get sequence");
		controller.setReturnValue(!isRecording);
		
		if (!isRecording) {
			media.getTrack("sequencer", "get sequence");
			controller.setReturnValue(null);
		}
		
	}
	
	public void recordClose() {
		
		if (isSequencer) {
			
			media.existsTrack("sequencer", "get sequence");
			controller.setReturnValue(false);
			
			media.newTrack("sequencer", "get sequence", sequencerColumns);

			int count = recorded.size();
			
			while (!recorded.isEmpty()) {
				
				List frame = (List) recorded.pop();

				for(Iterator i = frame.iterator(); i.hasNext();) {
					
					List row = (List) i.next();
					
					row.set(2, new Integer(count));
					row.set(3, new Integer(count));
					
					media.write(row);
					
				}
				
				count--;
								
			}

			media.closeTrack();

		}

		media.close();
		
	}

	public void recordSelectPerson() {
		
		media.existsTrack("jdbc:ap:TestDatabase", "SELECT * FROM persons WHERE id = 1");
		controller.setReturnValue(false);
		media.newTrack("jdbc:ap:TestDatabase", "select * from persons where id=1", personColumns);
		media.write(Arrays.asList(new Object[] {new Integer(1), "LName1", "FName1", new Integer(1)}));
		media.closeTrack();
		
		addAsRecorded(ROW_FOR_PERSONS);

	}

	public void recordGetSelectPerson() {
		
		List result = new ArrayList();
		result.add(personColumns);
		
		if (isRecording) {
			media.existsTrack("jdbc:ap:TestDatabase", "SELECT * FROM persons WHERE id = 1");
			controller.setReturnValue(true);
		}
		
		media.getTrack("jdbc:ap:TestDatabase", "select * from persons where id=1");
		controller.setReturnValue(result.iterator());
		
	}
	
	public void recordSelectPayroll() {
	
		media.existsTrack("jdbc:ap:TestDatabase", "SELECT * FROM payroll WHERE id = 1");
		controller.setReturnValue(false);
		media.newTrack("jdbc:ap:TestDatabase", "select * from payroll where id=1", payrollColumns);
		media.write(Arrays.asList(new Object[] {new Integer(1), "Big", Dates.createSQLDate(2005, 05, 12), new BigDecimal("6000")}));
		media.closeTrack();
		
		addAsRecorded(ROW_FOR_PAYROLL);
		
	}

	public void recordGetSelectPayroll() {
		
		List result = new ArrayList();
		result.add(payrollColumns);
	
		if (isRecording) {
			media.existsTrack("jdbc:ap:TestDatabase", "SELECT * FROM payroll WHERE id = 1");
			controller.setReturnValue(true);
		}
		
		media.getTrack("jdbc:ap:TestDatabase", "select * from payroll where id=1");
		controller.setReturnValue(result.iterator());
		
	}

	public void recordInsertPerson() {
		
		if (isRecording) {
			
			media.newTrack("jdbc:ap:TestDatabase", "insert into persons(7,'James','Bond',1)", insertColumns);
			media.write(Arrays.asList(new Object[] {new Integer(1)}));
			media.closeTrack();
			
			addAsRecorded(ROW_FOR_INSERT_PERSON);
			
		} else {
			
			List result = new ArrayList();
			result.add(insertColumns);
			result.add(oneInsert);

			media.getTrack("jdbc:ap:TestDatabase", "insert into persons(7,'James','Bond',1)");
			controller.setReturnValue(result.iterator());
		}
		
	}

	public void recordInsertPayroll() {

		if (isRecording) {

			media.newTrack("jdbc:ap:TestDatabase", "insert into payroll(7,'Average','2009-05-12',3700)", insertColumns);
			media.write(Arrays.asList(new Object[] {new Integer(1)}));
			media.closeTrack();
			
			addAsRecorded(ROW_FOR_INSERT_PAYROLL);
			
		} else {

			List result = new ArrayList();
			result.add(insertColumns);
			result.add(oneInsert);

			media.getTrack("jdbc:ap:TestDatabase", "insert into payroll(7,'Average','2009-05-12',3700)");
			controller.setReturnValue(result.iterator());
			
		}
	}

	private void addAsRecorded(List row) {
		
		if (isSequencer && isRecording) {
			
			List frame = (List) recorded.peek();
			
			if (!frame.contains(row)) {
				frame.add(row);
			}

		}
		
	}

	Media media;
	MockControl controller;
	
	boolean isSequencer = false;
	boolean isRecording = true;

	Stack recorded = new Stack();
	
	static final List ROW_FOR_PERSONS = Arrays.asList(new Object[] {"jdbc:ap:TestDatabase", "SELECT * FROM persons WHERE id = 1", new Integer(1), new Integer(1), Boolean.FALSE});
	static final List ROW_FOR_INSERT_PERSON = Arrays.asList(new Object[] {"jdbc:ap:TestDatabase", "INSERT INTO persons (7, 'James', 'Bond', 1)", new Integer(2), new Integer(2), Boolean.TRUE});
	static final List ROW_FOR_PAYROLL = Arrays.asList(new Object[] {"jdbc:ap:TestDatabase", "SELECT * FROM payroll WHERE id = 1", new Integer(1), new Integer(1), Boolean.FALSE});
	static final List ROW_FOR_INSERT_PAYROLL = Arrays.asList(new Object[] {"jdbc:ap:TestDatabase", "INSERT INTO payroll (7, 'Average', '2009-05-12',3700)", new Integer(2), new Integer(2), Boolean.TRUE});


	static final List oneInsert = Arrays.asList(new Object[] {new Integer(1)});
	static final List insertColumns = Arrays.asList(new Object[] {"count"});
	static final List personColumns = Arrays.asList(new Object[] {"id", "lastname", "firstname", "livesin"});
	static final List payrollColumns = Arrays.asList(new Object[] {"id", "name", "hiredate", "salary"});

	static final List sequencerColumns = Arrays.asList(new Object[] {"dbUrl", "sql", "version", "frame", "isUpdate"});

}
