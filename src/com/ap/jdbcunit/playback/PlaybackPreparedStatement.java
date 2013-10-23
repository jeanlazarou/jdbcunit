/*
 * @author: Jean Lazarou
 * @date: 15 fevr. 04
 */
package com.ap.jdbcunit.playback;

import java.sql.*;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.Calendar;

import com.ap.jdbcunit.PreparedStatementWrapper;

public class PlaybackPreparedStatement extends PreparedStatementWrapper {

	final static String invalid = "Method should never be called";
	
	public PlaybackPreparedStatement(Connection con, String sql) {
		super(con, sql, null);
	}

	public void close() throws SQLException {
	}

	public int executeUpdate() throws SQLException {
		throw new SQLException(invalid);
	}

	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		register(parameterIndex, null);
	}

	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		register(parameterIndex, x ? Boolean.TRUE : Boolean.FALSE);
	}

	public void setByte(int parameterIndex, byte x) throws SQLException {
		register(parameterIndex, new Integer(x));
	}

	public void setShort(int parameterIndex, short x) throws SQLException {
		register(parameterIndex, new Integer(x));
	}

	public void setInt(int parameterIndex, int x) throws SQLException {
		register(parameterIndex, new Integer(x));
	}

	public void setLong(int parameterIndex, long x) throws SQLException {
		register(parameterIndex, new Long(x));
	}

	public void setFloat(int parameterIndex, float x) throws SQLException {
		register(parameterIndex, new Float(x));
	}

	public void setDouble(int parameterIndex, double x) throws SQLException {
		register(parameterIndex, new Double(x));
	}

	public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
		register(parameterIndex, x);
	}

	public void setString(int parameterIndex, String x) throws SQLException {
		register(parameterIndex, x);
	}

	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		register(parameterIndex, new String(x));
	}

	public void setDate(int parameterIndex, Date x) throws SQLException {
		register(parameterIndex, x);
	}

	public void setTime(int parameterIndex, Time x) throws SQLException {
		register(parameterIndex, x);
	}

	public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
		register(parameterIndex, x);
	}

	public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
		throw new SQLException(invalid);
	}

	public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
		throw new SQLException(invalid);
	}

	public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
		throw new SQLException(invalid);
	}

	public void clearParameters() throws SQLException {
		clear();
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException {
		register(parameterIndex, x);
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
		register(parameterIndex, x);
	}

	public void setObject(int parameterIndex, Object x) throws SQLException {
		register(parameterIndex, x);
	}

	public boolean execute() throws SQLException {
		throw new SQLException(invalid);
	}

	public void addBatch() throws SQLException {
		throw new SQLException(invalid);
	}

	public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
		throw new SQLException(invalid);
	}

	public void setRef(int parameterIndex, Ref x) throws SQLException {
		throw new SQLException(invalid);
	}

	public void setBlob(int parameterIndex, Blob x) throws SQLException {
		throw new SQLException(invalid);
	}

	public void setClob(int parameterIndex, Clob x) throws SQLException {
		throw new SQLException(invalid);
	}

	public void setArray(int parameterIndex, Array x) throws SQLException {
		throw new SQLException(invalid);
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		throw new SQLException(invalid);
	}

	public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
		register(parameterIndex, x);
	}

	public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
		register(parameterIndex, x);
	}

	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
		register(parameterIndex, x);
	}

	public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
		register(parameterIndex, null);
	}
}
