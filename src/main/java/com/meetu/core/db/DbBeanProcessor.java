package com.meetu.core.db;

import java.beans.PropertyDescriptor;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang.StringUtils;

public class DbBeanProcessor extends BeanProcessor {
	private static String jdMethod_super(String paramString) {
		String[] arrayOfString = paramString.split("_");
		String str = "";
		for (int i = 0; i < arrayOfString.length; i++)
			str = str + StringUtils.capitalize(arrayOfString[i]);
		StringUtils.uncapitalize(str);
		return str;
	}

	public static String prop2column(String paramString) {
		Pattern localPattern = Pattern.compile("([A-Z])");
		Matcher localMatcher = localPattern.matcher(paramString);
		String str2;
		String str1;
		for (str1 = paramString; localMatcher.find(); str1 = str1.replaceFirst(str2, "_" + str2.toLowerCase()))
			str2 = localMatcher.group(1);
		return str1;
	}

	public static void main(String[] paramArrayOfString) {
		System.out.println(prop2column("publishTimeAB"));
	}

	protected int[] mapColumnsToProperties(
			ResultSetMetaData paramResultSetMetaData,
			PropertyDescriptor[] paramArrayOfPropertyDescriptor)
			throws SQLException {
		int i = paramResultSetMetaData.getColumnCount();
		int[] arrayOfInt = new int[i + 1];
		Arrays.fill(arrayOfInt, -1);
		for (int j = 1; j <= i; j++) {
			String str = paramResultSetMetaData.getColumnLabel(j);
			if ((null == str) || (0 == str.length()))
				str = paramResultSetMetaData.getColumnName(j);
			str = jdMethod_super(str);
			for (int k = 0; k < paramArrayOfPropertyDescriptor.length; k++)
				if (str.equalsIgnoreCase(paramArrayOfPropertyDescriptor[k]
						.getName())) {
					arrayOfInt[j] = k;
					break;
				}
		}
		return arrayOfInt;
	}

	protected Object processColumn(ResultSet paramResultSet, int paramInt,
			Class<?> paramClass) throws SQLException {
		if ((!paramClass.isPrimitive())
				&& (paramResultSet.getObject(paramInt) == null))
			return null;
		if (paramClass.equals(String.class))
			return paramResultSet.getString(paramInt);
		if ((paramClass.equals(Integer.TYPE))
				|| (paramClass.equals(Integer.class)))
			return Integer.valueOf(paramResultSet.getInt(paramInt));
		if ((paramClass.equals(Boolean.TYPE))
				|| (paramClass.equals(Boolean.class)))
			return Boolean.valueOf(paramResultSet.getBoolean(paramInt));
		if ((paramClass.equals(Long.TYPE)) || (paramClass.equals(Long.class)))
			return Long.valueOf(paramResultSet.getLong(paramInt));
		if ((paramClass.equals(Double.TYPE))
				|| (paramClass.equals(Double.class)))
			return Double.valueOf(paramResultSet.getDouble(paramInt));
		if ((paramClass.equals(Float.TYPE)) || (paramClass.equals(Float.class)))
			return Float.valueOf(paramResultSet.getFloat(paramInt));
		if ((paramClass.equals(Short.TYPE)) || (paramClass.equals(Short.class)))
			return Short.valueOf(paramResultSet.getShort(paramInt));
		if ((paramClass.equals(Byte.TYPE)) || (paramClass.equals(Byte.class)))
			return Byte.valueOf(paramResultSet.getByte(paramInt));
		if (paramClass.equals(Timestamp.class))
			return paramResultSet.getTimestamp(paramInt);
		if (paramClass.equals(Date.class))
			return new Date(paramResultSet.getTimestamp(paramInt).getTime());
		return paramResultSet.getObject(paramInt);
	}
}
