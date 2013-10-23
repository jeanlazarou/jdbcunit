package com.ap.jdbcunit;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.ap.straight.MemoryResultSet;

public class CallableStatementWrapper extends PreparedStatementWrapper implements CallableStatement {

	private String sql;
	private CallableStatement actualStatement;
	private Connection connection;

	private List<String> rows;
	private List<Object> values;

	public CallableStatementWrapper(Connection con, String sql, PreparedStatement actualStatement) {
		super(con, sql, actualStatement);
		this.actualStatement = (CallableStatement) actualStatement;
		this.sql = sql;
		this.connection = con;
		this.rows = new ArrayList<String>(3);
		this.values = new ArrayList<Object>(3);
	}

	@Override
	public boolean execute() throws SQLException {
		if (JDBCUnit.isRecording()) {
			return actualStatement.execute();
		} else if (JDBCUnit.isReplaying()) {
			return Boolean.TRUE;
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getUpdateCount() throws SQLException {
		return -1;
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		return Boolean.FALSE;
	}

	private int getLastOutParameterIndex() {
		return Integer.parseInt(rows.get(rows.size() - 1));
	}

	@Override
	public Object getObject(int parameterIndex) throws SQLException {

		Object value;

		if (JDBCUnit.isRecording()) {

			value = actualStatement.getObject(parameterIndex);
			values.add(value);

			if (parameterIndex == getLastOutParameterIndex()) {

				if (JDBCUnit.getRecorder().existsTrack(connection.getMetaData().getURL(), sql)) {
					return JDBCUnit.getRecorder().get(this, connection.getMetaData().getURL(), sql);
				}

				List<Object> resultSet = new ArrayList<>();
				resultSet.add(rows);
				resultSet.add(values);
				MemoryResultSet rs = new MemoryResultSet(resultSet);
				JDBCUnit.getRecorder().add(connection.getMetaData().getURL(), sql, rs);
			}

		} else if (JDBCUnit.isReplaying()) {

			ResultSet resultSet = JDBCUnit.getRecorder().get(this, connection.getMetaData().getURL(), sql);
			value = resultSet.getObject(String.valueOf(parameterIndex));

		} else {
			throw new UnsupportedOperationException();
		}

		return value;
	}

	@Override
	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		actualStatement.setRowId(parameterIndex, x);
	}

	@Override
	public void setNString(int parameterIndex, String value) throws SQLException {
		actualStatement.setNString(parameterIndex, value);
	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
		actualStatement.setNCharacterStream(parameterIndex, value, length);
	}

	@Override
	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		actualStatement.setNClob(parameterIndex, value);
	}

	@Override
	public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
		actualStatement.setClob(parameterIndex, reader, length);
	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
		actualStatement.setBlob(parameterIndex, inputStream, length);
	}

	@Override
	public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
		actualStatement.setNClob(parameterIndex, reader, length);
	}

	@Override
	public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
		actualStatement.setSQLXML(parameterIndex, xmlObject);
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
		actualStatement.setAsciiStream(parameterIndex, x, length);
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
		actualStatement.setBinaryStream(parameterIndex, x, length);
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
		actualStatement.setCharacterStream(parameterIndex, reader, length);
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
		actualStatement.setAsciiStream(parameterIndex, x);
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
		actualStatement.setBinaryStream(parameterIndex, x);
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
		actualStatement.setCharacterStream(parameterIndex, reader);
	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
		actualStatement.setNCharacterStream(parameterIndex, value);
	}

	@Override
	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		actualStatement.setClob(parameterIndex, reader);
	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
		actualStatement.setBlob(parameterIndex, inputStream);
	}

	@Override
	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		actualStatement.setNClob(parameterIndex, reader);
	}

	@Override
	public boolean isClosed() throws SQLException {
		return actualStatement.isClosed();
	}

	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		actualStatement.setPoolable(poolable);
	}

	@Override
	public boolean isPoolable() throws SQLException {
		return actualStatement.isPoolable();
	}

	@Override
	public void closeOnCompletion() throws SQLException {
		actualStatement.closeOnCompletion();
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		return actualStatement.isCloseOnCompletion();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return actualStatement.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return actualStatement.isWrapperFor(iface);
	}

	@Override
	public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
		rows.add(String.valueOf(parameterIndex));
		actualStatement.registerOutParameter(parameterIndex, sqlType);
	}

	@Override
	public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
		actualStatement.registerOutParameter(parameterIndex, sqlType, scale);
	}

	@Override
	public boolean wasNull() throws SQLException {
		return actualStatement.wasNull();
	}

	@Override
	public String getString(int parameterIndex) throws SQLException {
		return actualStatement.getString(parameterIndex);
	}

	@Override
	public boolean getBoolean(int parameterIndex) throws SQLException {
		return actualStatement.getBoolean(parameterIndex);
	}

	@Override
	public byte getByte(int parameterIndex) throws SQLException {
		return actualStatement.getByte(parameterIndex);
	}

	@Override
	public short getShort(int parameterIndex) throws SQLException {
		return actualStatement.getShort(parameterIndex);
	}

	@Override
	public int getInt(int parameterIndex) throws SQLException {
		return actualStatement.getInt(parameterIndex);
	}

	@Override
	public long getLong(int parameterIndex) throws SQLException {
		return actualStatement.getLong(parameterIndex);
	}

	@Override
	public float getFloat(int parameterIndex) throws SQLException {
		return actualStatement.getFloat(parameterIndex);
	}

	@Override
	public double getDouble(int parameterIndex) throws SQLException {
		return actualStatement.getDouble(parameterIndex);

	}

	@Override
	public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
		return actualStatement.getBigDecimal(parameterIndex);
	}

	@Override
	public byte[] getBytes(int parameterIndex) throws SQLException {
		return actualStatement.getBytes(parameterIndex);
	}

	@Override
	public Date getDate(int parameterIndex) throws SQLException {
		return actualStatement.getDate(parameterIndex);
	}

	@Override
	public Time getTime(int parameterIndex) throws SQLException {
		return actualStatement.getTime(parameterIndex);
	}

	@Override
	public Timestamp getTimestamp(int parameterIndex) throws SQLException {
		return actualStatement.getTimestamp(parameterIndex);
	}

	@Override
	public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
		return actualStatement.getBigDecimal(parameterIndex);
	}

	@Override
	public Object getObject(int parameterIndex, Map<String, Class<?>> map) throws SQLException {
		return actualStatement.getObject(parameterIndex, map);
	}

	@Override
	public Ref getRef(int parameterIndex) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Blob getBlob(int parameterIndex) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Clob getClob(int parameterIndex) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Array getArray(int parameterIndex) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
		actualStatement.registerOutParameter(parameterName, sqlType);
	}

	@Override
	public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
		actualStatement.registerOutParameter(parameterName, sqlType, scale);

	}

	@Override
	public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
		actualStatement.registerOutParameter(parameterName, sqlType, typeName);
	}

	@Override
	public URL getURL(int parameterIndex) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setURL(String parameterName, URL val) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setNull(String parameterName, int sqlType) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setBoolean(String parameterName, boolean x) throws SQLException {
		actualStatement.setBoolean(parameterName, x);

	}

	@Override
	public void setByte(String parameterName, byte x) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setShort(String parameterName, short x) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setInt(String parameterName, int x) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setLong(String parameterName, long x) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setFloat(String parameterName, float x) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setDouble(String parameterName, double x) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setString(String parameterName, String x) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setBytes(String parameterName, byte[] x) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setDate(String parameterName, Date x) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setTime(String parameterName, Time x) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setObject(String parameterName, Object x) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public String getString(String parameterName) throws SQLException {
		return actualStatement.getString(parameterName);
	}

	@Override
	public boolean getBoolean(String parameterName) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public byte getByte(String parameterName) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public short getShort(String parameterName) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public int getInt(String parameterName) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public long getLong(String parameterName) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public float getFloat(String parameterName) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public double getDouble(String parameterName) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public byte[] getBytes(String parameterName) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Date getDate(String parameterName) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Time getTime(String parameterName) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Timestamp getTimestamp(String parameterName) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Object getObject(String parameterName) throws SQLException {
		return actualStatement.getObject(parameterName);
	}

	@Override
	public BigDecimal getBigDecimal(String parameterName) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Ref getRef(String parameterName) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Blob getBlob(String parameterName) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Clob getClob(String parameterName) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Array getArray(String parameterName) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Date getDate(String parameterName, Calendar cal) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Time getTime(String parameterName, Calendar cal) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public URL getURL(String parameterName) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public RowId getRowId(int parameterIndex) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public RowId getRowId(String parameterName) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setRowId(String parameterName, RowId x) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setNString(String parameterName, String value) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setNClob(String parameterName, NClob value) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setClob(String parameterName, Reader reader, long length) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public NClob getNClob(int parameterIndex) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public NClob getNClob(String parameterName) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public SQLXML getSQLXML(int parameterIndex) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public SQLXML getSQLXML(String parameterName) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public String getNString(int parameterIndex) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public String getNString(String parameterName) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Reader getNCharacterStream(int parameterIndex) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Reader getNCharacterStream(String parameterName) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Reader getCharacterStream(int parameterIndex) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Reader getCharacterStream(String parameterName) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setBlob(String parameterName, Blob x) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setClob(String parameterName, Clob x) throws SQLException {
		actualStatement.setClob(parameterName, x);
	}

	@Override
	public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
		actualStatement.setAsciiStream(parameterName, x, length);
	}

	@Override
	public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
		actualStatement.setBinaryStream(parameterName, x, length);
	}

	@Override
	public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
		actualStatement.setCharacterStream(parameterName, reader);
	}

	@Override
	public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
		actualStatement.setAsciiStream(parameterName, x);
	}

	@Override
	public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
		actualStatement.setBinaryStream(parameterName, x);
	}

	@Override
	public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
		actualStatement.setCharacterStream(parameterName, reader);
	}

	@Override
	public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
		actualStatement.setNCharacterStream(parameterName, value);
	}

	@Override
	public void setClob(String parameterName, Reader reader) throws SQLException {
		actualStatement.setClob(parameterName, reader);
	}

	@Override
	public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
		actualStatement.setBlob(parameterName, inputStream);
	}

	@Override
	public void setNClob(String parameterName, Reader reader) throws SQLException {
		actualStatement.setNClob(parameterName, reader);
	}

	@Override
	public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
		return actualStatement.getObject(parameterIndex, type);
	}

	@Override
	public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
		return actualStatement.getObject(parameterName, type);
	}

}
