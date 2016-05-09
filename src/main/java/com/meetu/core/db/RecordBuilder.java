package com.meetu.core.db;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecordBuilder {
	public static final List<Record> build(ResultSet paramResultSet)
			throws SQLException {
		ArrayList localArrayList = new ArrayList();
		ResultSetMetaData localResultSetMetaData = paramResultSet.getMetaData();
		int i = localResultSetMetaData.getColumnCount();
		String[] arrayOfString = new String[i + 1];
		int[] arrayOfInt = new int[i + 1];
		o00000(localResultSetMetaData, arrayOfString, arrayOfInt);
		while (paramResultSet.next()) {
			Record localRecord = new Record();
			Map localMap = localRecord.getColumns();
			for (int j = 1; j <= i; j++) {
				Object localObject;
				if (arrayOfInt[j] < 2004)
					localObject = paramResultSet.getObject(j);
				else if (arrayOfInt[j] == 2005)
					localObject = handleClob(paramResultSet.getClob(j));
				else if (arrayOfInt[j] == 2011)
					localObject = handleClob(paramResultSet.getNClob(j));
				else if (arrayOfInt[j] == 2004)
					localObject = handleBlob(paramResultSet.getBlob(j));
				else
					localObject = paramResultSet.getObject(j);
				localMap.put(arrayOfString[j], localObject);
			}
			localArrayList.add(localRecord);
		}
		return localArrayList;
	}

	private static final void o00000(ResultSetMetaData paramResultSetMetaData,
			String[] paramArrayOfString, int[] paramArrayOfInt)
			throws SQLException {
		for (int i = 1; i < paramArrayOfString.length; i++) {
			paramArrayOfString[i] = paramResultSetMetaData.getColumnLabel(i);
			paramArrayOfInt[i] = paramResultSetMetaData.getColumnType(i);
		}
	}

	public static String handleClob(Clob paramClob) throws SQLException {
		if (paramClob == null)
			return null;
		Reader localReader = null;
		try {
			localReader = paramClob.getCharacterStream();
			char[] arrayOfChar = new char[(int) paramClob.length()];
			localReader.read(arrayOfChar);
			String str = new String(arrayOfChar);
			return str;
		} catch (IOException localIOException1) {
			throw new RuntimeException(localIOException1);
		} finally {
			try {
				localReader.close();
			} catch (IOException localIOException3) {
				throw new RuntimeException(localIOException3);
			}
		}
	}

	public static byte[] handleBlob(Blob paramBlob) throws SQLException {
		if (paramBlob == null)
			return null;
		InputStream localInputStream = null;
		try {
			localInputStream = paramBlob.getBinaryStream();
			byte[] arrayOfByte1 = new byte[(int) paramBlob.length()];
			localInputStream.read(arrayOfByte1);
			localInputStream.close();
			byte[] arrayOfByte2 = arrayOfByte1;
			return arrayOfByte2;
		} catch (IOException localIOException1) {
			throw new RuntimeException(localIOException1);
		} finally {
			try {
				localInputStream.close();
			} catch (IOException localIOException3) {
				throw new RuntimeException(localIOException3);
			}
		}
	}
}
