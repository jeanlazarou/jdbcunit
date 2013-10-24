/*
 * @author: Jean Lazarou
 * @date: 15 fevr. 04
 */
package com.ap.jdbcunit;

import java.sql.*;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PreparedStatementWrapper extends StatementWrapper implements PreparedStatement {

	public PreparedStatementWrapper(Connection con, String sql, PreparedStatement actualStatement) {
		super(con, actualStatement);
		
		actualPrepared = actualStatement;
		
		this.sql = sql;
	}

	public ResultSet executeQuery() throws SQLException {

		if (JDBCUnit.isRecording()) {
			
			String sql = prepareStatement();
			
			if (JDBCUnit.getRecorder().existsTrack(con.getMetaData().getURL(), sql)) {
				return JDBCUnit.getRecorder().get(this, con.getMetaData().getURL(), sql);            
			}

			ResultSet rs = this.actualPrepared.executeQuery();

			JDBCUnit.getRecorder().add(con.getMetaData().getURL(), sql, rs);            

		} else if (JDBCUnit.isReplaying()) {
			return JDBCUnit.getRecorder().get(this, con.getMetaData().getURL(), prepareStatement());            
		}

		return actualPrepared.executeQuery();
	}

	public int executeUpdate() throws SQLException {

		if (JDBCUnit.isRecording()) {
			
			String sql = prepareStatement();
	
			if (JDBCUnit.getRecorder().existsTrack(con.getMetaData().getURL(), sql)) {
				return getRecordedUpdate(sql);            
			}

			int count = this.actualPrepared.executeUpdate();

			JDBCUnit.getRecorder().add(con.getMetaData().getURL(), sql, createResultSet(count));            

			return count;
			
		} else if (JDBCUnit.isReplaying()) {
			return getRecordedUpdate(prepareStatement());
		}

		return actualPrepared.executeUpdate();
		
	}

	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		register(parameterIndex, null);
		actualPrepared.setNull(parameterIndex, sqlType);
	}

	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		register(parameterIndex, x ? Boolean.TRUE : Boolean.FALSE);
		actualPrepared.setBoolean(parameterIndex, x);
	}

	public void setByte(int parameterIndex, byte x) throws SQLException {
		register(parameterIndex, new Integer(x));
		actualPrepared.setByte(parameterIndex, x);
	}

	public void setShort(int parameterIndex, short x) throws SQLException {
		register(parameterIndex, new Integer(x));
		actualPrepared.setShort(parameterIndex, x);
	}

	public void setInt(int parameterIndex, int x) throws SQLException {
		register(parameterIndex, new Integer(x));
		actualPrepared.setInt(parameterIndex, x);
	}

	public void setLong(int parameterIndex, long x) throws SQLException {
		register(parameterIndex, new Long(x));
		actualPrepared.setLong(parameterIndex, x);
	}

	public void setFloat(int parameterIndex, float x) throws SQLException {
		register(parameterIndex, new Float(x));
		actualPrepared.setFloat(parameterIndex, x);
	}

	public void setDouble(int parameterIndex, double x) throws SQLException {
		register(parameterIndex, new Double(x));
		actualPrepared.setDouble(parameterIndex, x);
	}

	public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
		register(parameterIndex, x);
		actualPrepared.setBigDecimal(parameterIndex, x);
	}

	public void setString(int parameterIndex, String x) throws SQLException {
		register(parameterIndex, x);
		actualPrepared.setString(parameterIndex, x);
	}

	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		register(parameterIndex, new String(x));
		actualPrepared.setBytes(parameterIndex, x);
	}

	public void setDate(int parameterIndex, Date x) throws SQLException {
		register(parameterIndex, x);
		actualPrepared.setDate(parameterIndex, x);
	}

	public void setTime(int parameterIndex, Time x) throws SQLException {
		register(parameterIndex, x);
		actualPrepared.setTime(parameterIndex, x);
	}

	public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
		register(parameterIndex, x);
		actualPrepared.setTimestamp(parameterIndex, x);
	}

	public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
		actualPrepared.setAsciiStream(parameterIndex, x, length);
	}

	public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
		actualPrepared.setUnicodeStream(parameterIndex, x, length);
	}

	public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
		actualPrepared.setBinaryStream(parameterIndex, x, length);
	}

	public void clearParameters() throws SQLException {
		values.clear();
		actualPrepared.clearParameters();
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException {
		register(parameterIndex, x);
		actualPrepared.setObject(parameterIndex, x, targetSqlType, scale);
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
		register(parameterIndex, x);
		actualPrepared.setObject(parameterIndex, x, targetSqlType);
	}

	public void setObject(int parameterIndex, Object x) throws SQLException {
		register(parameterIndex, x);
		actualPrepared.setObject(parameterIndex, x);
	}

	public boolean execute() throws SQLException {
		return actualPrepared.execute();
	}

	public void addBatch() throws SQLException {
		actualPrepared.addBatch();
	}

	public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
		actualPrepared.setCharacterStream(parameterIndex, reader, length);
	}

	public void setRef(int parameterIndex, Ref x) throws SQLException {
		actualPrepared.setRef(parameterIndex, x);
	}

	public void setBlob(int parameterIndex, Blob x) throws SQLException {
		actualPrepared.setBlob(parameterIndex, x);
	}

	public void setClob(int parameterIndex, Clob x) throws SQLException {
		actualPrepared.setClob(parameterIndex, x);
	}

	public void setArray(int parameterIndex, Array x) throws SQLException {
		actualPrepared.setArray(parameterIndex, x);
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		return actualPrepared.getMetaData();
	}

	public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
		register(parameterIndex, x);
		actualPrepared.setDate(parameterIndex, x, cal);
	}

	public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
		register(parameterIndex, x);
		actualPrepared.setTime(parameterIndex, x, cal);
	}

	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
		register(parameterIndex, x);
		actualPrepared.setTimestamp(parameterIndex, x, cal);
	}

	public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
		register(parameterIndex, null);
		actualPrepared.setNull(parameterIndex, sqlType, typeName);
	}

	protected void clear() {
		values.clear();
	}
	
	protected void register(int parameterIndex, Object value) {
		
		parameterIndex--;
		
		for (int i = values.size(); i <= parameterIndex; i++) {
			values.add(null);
		}
		
		values.set(parameterIndex, value);
		
	}

	private String prepareStatement() throws SQLException {
		
		StringBuffer buffer = new StringBuffer();

		int index = 0;
		
		int prev = 0;
		int param = sql.indexOf('?');
		
		while (param != -1) {
			
			buffer.append(sql.substring(prev, param));
			
			if (index >= values.size()) {
				break;
			}
			
			Object value = values.get(index++);
			
			if (value instanceof String || value instanceof Date) {
				buffer.append('\'');						
				buffer.append(value);						
				buffer.append('\'');						
			} else {
				buffer.append(value);						
			}
			
			prev = param + 1;
			param = sql.indexOf('?', prev);
		}
		
		if (prev < sql.length()) {
			buffer.append(sql.substring(prev));
		}
		
		return buffer.toString();
	}

	String sql;
	PreparedStatement actualPrepared;

	List values = new ArrayList();

	public void setURL(int parameterIndex, URL x) throws SQLException {
		throw new UnsupportedOperationException();
	}

	public ParameterMetaData getParameterMetaData() throws SQLException {
		throw new UnsupportedOperationException();
	}

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

    //------------------------- JDBC 4.0 -----------------------------------

	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		throw new UnsupportedOperationException();
		
	}

	public void setNString(int parameterIndex, String value) throws SQLException {
		throw new UnsupportedOperationException();
		
	}

	public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
		throw new UnsupportedOperationException();
		
	}

	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		throw new UnsupportedOperationException();
		
	}

	public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
		throw new UnsupportedOperationException();
		
	}

	public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
		throw new UnsupportedOperationException();
		
	}

	public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
		throw new UnsupportedOperationException();
		
	}

	public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
		throw new UnsupportedOperationException();
		
	}

	public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
		throw new UnsupportedOperationException();
		
	}

	public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
		throw new UnsupportedOperationException();
		
	}

	public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
		throw new UnsupportedOperationException();
		
	}

	public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
		throw new UnsupportedOperationException();
		
	}

	public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
		throw new UnsupportedOperationException();
		
	}

	public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
		throw new UnsupportedOperationException();
		
	}

	public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
		throw new UnsupportedOperationException();
		
	}

	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		throw new UnsupportedOperationException();
		
	}

	public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
		throw new UnsupportedOperationException();
		
	}

	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		throw new UnsupportedOperationException();
		
	}
	
}
