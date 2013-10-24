/*
 * @author: Jean Lazarou
 * @date: 15 fevr. 04
 */
package com.ap.jdbcunit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.ap.straight.MemoryResultSet;

public class StatementWrapper implements Statement {

    public StatementWrapper(Connection con, Statement actualStatement) {
    	this.con = con;
        this.actualStatement = actualStatement;
    }

    public ResultSet executeQuery(String sql) throws SQLException {

        if (JDBCUnit.isRecording()) {
			
			if (JDBCUnit.getRecorder().existsTrack(con.getMetaData().getURL(), sql)) {
				return JDBCUnit.getRecorder().get(this, con.getMetaData().getURL(), sql);            
			}
			
            ResultSet rs = actualStatement.executeQuery(sql);

            JDBCUnit.getRecorder().add(con.getMetaData().getURL(), sql, rs);
                        
        } else if (JDBCUnit.isReplaying()) {
			return JDBCUnit.getRecorder().get(this, con.getMetaData().getURL(), sql);            
        }

        return actualStatement.executeQuery(sql);

    }

    public int executeUpdate(String sql) throws SQLException {
        
    	int count = 0;
    	
        if (JDBCUnit.isRecording()) {
			
			if (JDBCUnit.getRecorder().existsTrack(con.getMetaData().getURL(), sql)) {
				return getRecordedUpdate(sql);
			}
			
	    	count = actualStatement.executeUpdate(sql);

            JDBCUnit.getRecorder().add(con.getMetaData().getURL(), sql, createResultSet(count));
                        
        } else if (JDBCUnit.isReplaying()) {
			count = getRecordedUpdate(sql);
        } else {
        	count = actualStatement.executeUpdate(sql);
        }

        return count;
        
    }

	public void close() throws SQLException {
        actualStatement.close();
    }

    public int getMaxFieldSize() throws SQLException {
        return actualStatement.getMaxFieldSize();
    }

    public void setMaxFieldSize(int max) throws SQLException {
        actualStatement.setMaxFieldSize(max);
    }

    public int getMaxRows() throws SQLException {
        return actualStatement.getMaxRows();
    }

    public void setMaxRows(int max) throws SQLException {
        actualStatement.setMaxRows(max);
    }

    public void setEscapeProcessing(boolean enable) throws SQLException {
        actualStatement.setEscapeProcessing(enable);
    }

    public int getQueryTimeout() throws SQLException {
        return actualStatement.getQueryTimeout();
    }

    public void setQueryTimeout(int seconds) throws SQLException {
        actualStatement.setQueryTimeout(seconds);
    }

    public void cancel() throws SQLException {
        actualStatement.cancel();
    }

    public SQLWarning getWarnings() throws SQLException {
        return actualStatement.getWarnings();
    }

    public void clearWarnings() throws SQLException {
        actualStatement.clearWarnings();
    }

    public void setCursorName(String name) throws SQLException {
        actualStatement.setCursorName(name);
    }

    public boolean execute(String sql) throws SQLException {
		if (JDBCUnit.isRecording()) System.out.println("JDBCUnit WARN: execute(String sql) is not recorded");
        return actualStatement.execute(sql);
    }

    public ResultSet getResultSet() throws SQLException {
		if (JDBCUnit.isRecording()) System.out.println("JDBCUnit WARN: getResultSet() is not recorded");
        return actualStatement.getResultSet();
    }

    public int getUpdateCount() throws SQLException {
        return actualStatement.getUpdateCount();
    }

    public boolean getMoreResults() throws SQLException {
        return actualStatement.getMoreResults();
    }

    public void setFetchDirection(int direction) throws SQLException {
        actualStatement.setFetchDirection(direction);
    }

    public int getFetchDirection() throws SQLException {
        return actualStatement.getFetchDirection();
    }

    public void setFetchSize(int rows) throws SQLException {
        actualStatement.setFetchSize(rows);
    }

    public int getFetchSize() throws SQLException {
        return actualStatement.getFetchSize();
    }

    public int getResultSetConcurrency() throws SQLException {
        return actualStatement.getResultSetConcurrency();
    }

    public int getResultSetType() throws SQLException {
        return actualStatement.getResultSetType();
    }

    public void addBatch(String sql) throws SQLException {
		actualStatement.addBatch(sql);
    }

    public void clearBatch() throws SQLException {
		actualStatement.clearBatch();
    }

    public int[] executeBatch() throws SQLException {
		if (JDBCUnit.isRecording()) System.out.println("JDBCUnit WARN: executeBatch() is not recorded");
        return actualStatement.executeBatch();
    }

    public Connection getConnection() throws SQLException {
        return con;
    }

    protected int getRecordedUpdate(String sql) throws SQLException {

    	int count;
    	
		ResultSet rs = JDBCUnit.getRecorder().get(this, con.getMetaData().getURL(), sql);
		
		rs.next();
		
		count = rs.getInt(1);
		
		return count;
	}

	protected MemoryResultSet createResultSet(int count) {
		
		List data = new ArrayList();
		
		List row = new ArrayList();
		row.add("count");
		data.add(row);
		
		row = new ArrayList();
		row.add(new Integer(count));
		data.add(row);
		
		MemoryResultSet rs = new MemoryResultSet(data);
		
		return rs;
		
	}

    Connection con;
    Statement actualStatement;
    
	public int getResultSetHoldability() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public boolean getMoreResults(int current) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public boolean execute(String sql, String[] columnNames) throws SQLException {
		throw new UnsupportedOperationException();
	}

    //--------------------------JDBC 4.0 -----------------------------

	public boolean isClosed() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public void setPoolable(boolean poolable) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public boolean isPoolable() throws SQLException {
		throw new UnsupportedOperationException();
	}

    //--------------------------JDBC 4.1 -----------------------------

    public void closeOnCompletion() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public boolean isCloseOnCompletion() throws SQLException {
		throw new UnsupportedOperationException();
	}

    //----------------------- Wrapper interface ----------------------

    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException();

    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

}
