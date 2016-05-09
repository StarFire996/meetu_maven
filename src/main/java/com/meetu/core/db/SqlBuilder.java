package com.meetu.core.db;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class SqlBuilder {
	private static final Logger jdField_super = Logger
			.getLogger(SqlBuilder.class);

	public static SqlHolder buildInsert(Object paramObject)
	  {
	    SqlHolder localSqlHolder = new SqlHolder();
	    Field[] arrayOfField = paramObject.getClass().getDeclaredFields();
	    StringBuilder localStringBuilder1 = new StringBuilder();
	    StringBuilder localStringBuilder2 = new StringBuilder();
	    for (Field localField : arrayOfField)
	      if (!Object(localField))
	      {
	        localSqlHolder.addParam(o00000(o00000(paramObject, localField)));
	        localStringBuilder1.append(\u00D200000(localField)).append(",");
	        localStringBuilder2.append("?").append(",");
	      }
	    o00000(localStringBuilder1);
	    o00000(localStringBuilder2);
	    StringBuilder builder = new StringBuilder();
	    Map localMap = \u00D200000(paramObject);
	    ((StringBuilder)builder).append("INSERT INTO ").append((String)localMap.get("name")).append(" (");
	    ((StringBuilder)builder).append(localStringBuilder1).append(") ");
	    ((StringBuilder)builder).append(" VALUES(").append(localStringBuilder2).append(") ");
	    localSqlHolder.setSql(((StringBuilder)builder).toString());
	    jdField_super.debug(localSqlHolder);
	    return localSqlHolder;
	  }

	public static SqlHolder buildUpdate(Object paramObject) {
		SqlHolder localSqlHolder = new SqlHolder();
		Field[] arrayOfField1 = paramObject.getClass().getDeclaredFields();
		StringBuilder localStringBuilder = new StringBuilder();
		Map localMap = \u00D200000(paramObject);
		localStringBuilder.append("UPDATE ")
				.append((String) localMap.get("name")).append(" SET ");
		Object localObject = null;
		String str = (String) localMap.get("pk");
		for (Field localField : arrayOfField1)
			if ((!Object(localField)) && (o00000(localField)))
				if (str.equalsIgnoreCase(\u00D200000(localField))) {
					localObject = o00000(o00000(paramObject, localField));
				} else {
					localSqlHolder.addParam(o00000(o00000(paramObject,
							localField)));
					localStringBuilder.append(\u00D200000(localField))
							.append("=?").append(",");
				}
		o00000(localStringBuilder);
		localStringBuilder.append(new StringBuilder().append(" WHERE ")
				.append(str).append("=?").toString());
		localSqlHolder.addParam(localObject);
		localSqlHolder.setSql(localStringBuilder.toString());
		jdField_super.debug(localSqlHolder);
		return localSqlHolder;
	}

	public static SqlHolder buildGet(Object paramObject1, Object paramObject2) {
		SqlHolder localSqlHolder = new SqlHolder();
		StringBuilder localStringBuilder = new StringBuilder();
		Map localMap = \u00D200000(paramObject1);
		localStringBuilder.append("select * from ").append(
				(String) localMap.get("name"));
		String str = (String) localMap.get("pk");
		localStringBuilder.append(new StringBuilder().append(" WHERE ")
				.append(str).append("=?").toString());
		localSqlHolder.addParam(paramObject2);
		localSqlHolder.setSql(localStringBuilder.toString());
		jdField_super.debug(localSqlHolder);
		return localSqlHolder;
	}

	private static void o00000(StringBuilder paramStringBuilder) {
		if (paramStringBuilder.lastIndexOf(",") == paramStringBuilder.length() - 1)
			paramStringBuilder.deleteCharAt(paramStringBuilder.length() - 1);
	}

	private static String \u00D200000(Field paramField) {
		return DbBeanProcessor.prop2column(paramField.getName());
	}

	private static Object o00000(Object paramObject, Field paramField) {
		Object localObject = null;
		try {
			if (paramField.isAccessible()) {
				localObject = paramField.get(paramObject);
			} else {
				paramField.setAccessible(true);
				localObject = paramField.get(paramObject);
				paramField.setAccessible(false);
			}
		} catch (Exception localException) {
			jdField_super.error(
					"\u83B7\u53D6\u5C5E\u6027\u503C\u5931\u8D25\uFF01",
					localException);
			throw new RuntimeException(
					"\u83B7\u53D6\u5C5E\u6027\u503C\u5931\u8D25\uFF01",
					localException);
		}
		return localObject;
	}

	private static boolean o00000(Field paramField) {
		Column localColumn = (Column) paramField.getAnnotation(Column.class);
		if (localColumn != null)
			return localColumn.updatable();
		return true;
	}

	private static boolean Object(Field paramField) {
		int i = paramField.getModifiers();
		if ((Modifier.isStatic(i)) || (Modifier.isFinal(i)))
			return true;
		Transient localTransient = (Transient) paramField
				.getAnnotation(Transient.class);
		return (localTransient != null) && (localTransient.value() == true);
	}

	private static Map<String, String> \u00D200000(Object paramObject) {
		HashMap localHashMap = new HashMap();
		String str = paramObject.getClass().getSimpleName();
		Table localTable = (Table) paramObject.getClass().getAnnotation(
				Table.class);
		if ((localTable != null) && (StringUtils.isNotEmpty(localTable.name()))) {
			str = localTable.name();
			localHashMap.put("name", str);
			localHashMap.put("pk", localTable.pk());
		}
		return localHashMap;
	}

	private static Object o00000(Object paramObject) {
		if (paramObject == null)
			return null;
		if ((paramObject instanceof Date)) {
			Date localDate = (Date) paramObject;
			Timestamp localTimestamp = new Timestamp(localDate.getTime());
			return localTimestamp;
		}
		return paramObject;
	}
}
