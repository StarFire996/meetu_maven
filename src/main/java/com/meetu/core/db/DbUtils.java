package com.meetu.core.db;

import com.alibaba.fastjson.JSONObject;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.text.MessageFormat;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class DbUtils {
	private static Logger \u00D200000 = Logger.getLogger(DbUtils.class);
	private static final MessageFormat jdField_super = new MessageFormat(
			"SELECT GLOBAL_TABLE.* FROM ( SELECT ROW_NUMBER() OVER( {0}) AS __MYSEQ__,TEMP_TABLE.* FROM  ( {1} ) TEMP_TABLE) GLOBAL_TABLE WHERE GLOBAL_TABLE.__MYSEQ__>{2}");

	public static JSONObject getDbPaginate(Connection paramConnection,
			int paramInt1, int paramInt2, String paramString1,
			String paramString2) throws SQLException {
		String str1 = "Oracle";
		StringBuilder localStringBuilder = new StringBuilder(255);
		DatabaseMetaData localDatabaseMetaData = paramConnection.getMetaData();
		if (localDatabaseMetaData != null) {
			String localObject = localDatabaseMetaData.getDatabaseProductName();
			if (StringUtils.isNotBlank((CharSequence) localObject)) {
				if (((String) localObject).startsWith("Oracle")) {
					str1 = "Oracle";
					forOraclePaginate(localStringBuilder, paramInt1, paramInt2,
							paramString1, paramString2);
				} else if (((String) localObject).startsWith("MySQL")) {
					str1 = "MySQL";
					forMySQLPaginate(localStringBuilder, paramInt1, paramInt2,
							paramString1, paramString2);
				} else if (((String) localObject).startsWith("OSCAR")) {
					str1 = "Oscar";
					forOscarPaginate(localStringBuilder, paramInt1, paramInt2,
							paramString1, paramString2);
				} else if (((String) localObject).startsWith("DB2/")) {
					str1 = "DB2";
					forDB2Paginate(localStringBuilder, paramInt1, paramInt2,
							paramString1, paramString2);
				} else if (((String) localObject).startsWith("DM")) {
					str1 = "Dameng";
					forDmPaginate(localStringBuilder, paramInt1, paramInt2,
							paramString1, paramString2);
				} else if (((String) localObject).startsWith("Adaptive")) {
					str1 = "Sybase";
					forSybasePaginate(localStringBuilder, paramInt1, paramInt2,
							paramString1, paramString2);
				} else if (((String) localObject).startsWith("Microsoft")) {
					String str2 = localDatabaseMetaData
							.getDatabaseProductVersion();
					str2 = str2.substring(0, str2.indexOf("."));
					if (StringUtils.isNotBlank(str2)) {
						Double localDouble = Double.valueOf(str2.toString());
						if (localDouble.doubleValue() >= 9.0D) {
							str1 = "MS2005-SQL";
							forSQLServer2005Paginate(localStringBuilder,
									paramInt1, paramInt2, paramString1,
									paramString2);
						} else {
							str1 = "MS-SQL";
							forSQLServer2000Paginate(localStringBuilder,
									paramInt1, paramInt2, paramString1,
									paramString2);
						}
					} else {
						str1 = "MS-SQL";
						forSQLServer2005Paginate(localStringBuilder, paramInt1,
								paramInt2, paramString1, paramString2);
					}
				} else if (((String) localObject).startsWith("KingbaseES")) {
					str1 = "KingbaseES";
					forKingbasePaginate(localStringBuilder, paramInt1,
							paramInt2, paramString1, paramString2);
				} else if (((String) localObject).startsWith("EnterpriseDB")) {
					str1 = "EnterpriseDB";
					forEnterpriseDbPaginate(localStringBuilder, paramInt1,
							paramInt2, paramString1, paramString2);
				}
			} else
				\u00D200000
						.warn("\u67E5\u627E\u4E0D\u5230\u6570\u636E\u7C7B\u578B.");
		}
		Object localObject = new JSONObject();
		((JSONObject) localObject).put("type", str1);
		((JSONObject) localObject).put("sql", localStringBuilder.toString());
		\u00D200000
				.info(new StringBuilder()
						.append("\u5F53\u524D\u8FDE\u63A5\u7684\u6570\u636E\u5E93\uFF1A")
						.append(str1).toString());
		return (JSONObject) localObject;
	}

	public static void forSQLServer2005Paginate(
			StringBuilder paramStringBuilder, int paramInt1, int paramInt2,
			String paramString1, String paramString2) {
		String str1 = new StringBuilder().append(paramString1).append(" ")
				.append(paramString2).toString();
		int i = (paramInt1 - 1) * paramInt2;
		String str2 = str1;
		str2 = new StringBuilder()
				.append("select top ___TOP_NUM___ __TEMP_ORDER_BY_COLUMN__=0, ")
				.append(str2.substring(6)).toString();
		String str3 = jdField_super.format(new String[] {
				"ORDER BY __TEMP_ORDER_BY_COLUMN__", str2,
				new StringBuilder().append(i).append("").toString() });
		str3 = StringUtils.replace(str3, "___TOP_NUM___", new StringBuilder()
				.append(paramInt2 + i).append("").toString());
		paramStringBuilder.append(str3);
	}

	public static void forSQLServer2000Paginate(
			StringBuilder paramStringBuilder, int paramInt1, int paramInt2,
			String paramString1, String paramString2) {
	}

	public static void forSybasePaginate(StringBuilder paramStringBuilder,
			int paramInt1, int paramInt2, String paramString1,
			String paramString2) {
	}

	public static void forKingbasePaginate(StringBuilder paramStringBuilder,
			int paramInt1, int paramInt2, String paramString1,
			String paramString2) {
		String str = new StringBuilder().append(paramString1).append(" ")
				.append(paramString2).toString();
		int i = (paramInt1 - 1) * paramInt2 + 1;
		int j = paramInt1 * paramInt2;
		StringBuffer localStringBuffer = new StringBuffer(100);
		localStringBuffer.append(str);
		localStringBuffer.append(" limit ").append(i).append(" offset ")
				.append(j);
		paramStringBuilder.append(localStringBuffer);
	}

	public static void forEnterpriseDbPaginate(
			StringBuilder paramStringBuilder, int paramInt1, int paramInt2,
			String paramString1, String paramString2) {
		String str = new StringBuilder().append(paramString1).append(" ")
				.append(paramString2).toString();
		int i = (paramInt1 - 1) * paramInt2 + 1;
		int j = paramInt1 * paramInt2;
		StringBuffer localStringBuffer = new StringBuffer(100);
		localStringBuffer
				.append("SELECT * FROM ( SELECT ROW_.*, ROWNUM ROWNUM_ FROM ( ");
		localStringBuffer.append(str);
		localStringBuffer.append(" ) ROW_ WHERE ROWNUM <= ").append(i)
				.append(") WHERE ROWNUM_ > ").append(j);
		paramStringBuilder.append(localStringBuffer);
	}

	public static void forDmPaginate(StringBuilder paramStringBuilder,
			int paramInt1, int paramInt2, String paramString1,
			String paramString2) {
		int i = (paramInt1 - 1) * paramInt2 + 1;
		int j = paramInt1 * paramInt2;
		paramStringBuilder.append("select top ").append(i).append(",")
				.append(j).append("from (");
		paramStringBuilder.append(paramString1).append(" ")
				.append(paramString2);
		paramStringBuilder.append(")");
	}

	public static void forDB2Paginate(StringBuilder paramStringBuilder,
			int paramInt1, int paramInt2, String paramString1,
			String paramString2) {
		String str = new StringBuilder().append(paramString1).append(" ")
				.append(paramString2).toString();
		int i = (paramInt1 - 1) * paramInt2 + 1;
		int j = paramInt1 * paramInt2;
		paramStringBuilder
				.append("SELECT * FROM  (SELECT B.*, ROWNUMBER() OVER() AS RN FROM  (SELECT * FROM (")
				.append(str).append(")   ) AS B   )AS A ");
		paramStringBuilder.append("WHERE A.RN BETWEEN ");
		paramStringBuilder.append(i).append(" AND ").append(j);
	}

	public static void forMySQLPaginate(StringBuilder paramStringBuilder,
			int paramInt1, int paramInt2, String paramString1,
			String paramString2) {
		int i = paramInt2 * (paramInt1 - 1);
		paramStringBuilder.append(paramString1).append(" ");
		paramStringBuilder.append(paramString2);
		paramStringBuilder.append(" limit ").append(i).append(", ")
				.append(paramInt2);
	}

	public static void forOscarPaginate(StringBuilder paramStringBuilder,
			int paramInt1, int paramInt2, String paramString1,
			String paramString2) {
		int i = paramInt2 * (paramInt1 - 1);
		paramStringBuilder.append(paramString1.toUpperCase()).append(" ");
		paramStringBuilder.append(paramString2);
		paramStringBuilder.append(" limit ").append(paramInt2)
				.append(" offset ").append(i);
	}

	public static void forOracle11gPaginate(StringBuilder paramStringBuilder,
			int paramInt1, int paramInt2, String paramString1,
			String paramString2) {
		int i = (paramInt1 - 1) * paramInt2 + 1;
		int j = paramInt1 * paramInt2;
		paramStringBuilder
				.append("select * from (select t.*,rownum as rowno from (");
		paramStringBuilder.append(paramString1).append(" ")
				.append(paramString2);
		paramStringBuilder.append("  t) ");
		paramStringBuilder.append(" where rowno between ").append(i)
				.append(" and ").append(j);
	}

	public static void forOraclePaginate(StringBuilder paramStringBuilder,
			int paramInt1, int paramInt2, String paramString1,
			String paramString2) {
		int i = (paramInt1 - 1) * paramInt2 + 1;
		int j = paramInt1 * paramInt2;
		paramStringBuilder
				.append("select * from ( select row_.*, rownum rownum_ from (  ");
		paramStringBuilder.append(paramString1).append(" ")
				.append(paramString2);
		paramStringBuilder.append(" ) row_ where rownum <= ").append(j)
				.append(") table_alias");
		paramStringBuilder.append(" where table_alias.rownum_ >= ").append(i);
	}

	public static void forOracle12gPaginate(StringBuilder paramStringBuilder,
			int paramInt1, int paramInt2, String paramString1,
			String paramString2) {
		int i = (paramInt1 - 1) * paramInt2 + 1;
		int j = paramInt1 * paramInt2;
		paramStringBuilder
				.append("select * from ( select s.*,row_number() over(ORDER BY rownum) ID_TONGJI_INDEX, COUNT(1) over() ID_TONGJI_ALL from  (");
		paramStringBuilder.append(paramString1).append(" ")
				.append(paramString2);
		paramStringBuilder.append(") s ) where ID_TONGJI_INDEX between ");
		paramStringBuilder.append(i).append(" and ").append(j);
	}

	public static String replaceFormatSqlOrderBy(String paramString) {
		paramString = paramString.replaceAll("(\\s)+", " ");
		int i = paramString.toLowerCase().lastIndexOf("order by");
		if (i > paramString.toLowerCase().lastIndexOf(")")) {
			String str1 = paramString.substring(0, i);
			String str2 = paramString.substring(i);
			str2 = str2
					.replaceAll(
							"[oO][rR][dD][eE][rR] [bB][yY] [\u4E00-\u9FA5a-zA-Z0-9_.]+((\\s)+(([dD][eE][sS][cC])|([aA][sS][cC])))?(( )*,( )*[\u4E00-\u9FA5a-zA-Z0-9_.]+(( )+(([dD][eE][sS][cC])|([aA][sS][cC])))?)*",
							"");
			return new StringBuilder().append(str1).append(str2).toString();
		}
		return paramString;
	}

	public String getPageSQL(String paramString, int paramInt1, int paramInt2) {
		StringBuffer localStringBuffer = new StringBuffer(100);
		localStringBuffer
				.append("select * from ( select row_.*, rownum rownum_ from ( ");
		localStringBuffer.append(paramString);
		localStringBuffer.append(new StringBuilder()
				.append(" ) row_ where rownum <= ")
				.append(paramInt1 + paramInt2).append(") where rownum_ > ")
				.append(paramInt1).toString());
		return localStringBuffer.toString();
	}

	public String getPageSQLBySize(String paramString, int paramInt1,
			int paramInt2) {
		StringBuffer localStringBuffer = new StringBuffer(100);
		localStringBuffer
				.append("select * from ( select row_.*, rownum  rownum_ from ( ");
		localStringBuffer.append(paramString);
		localStringBuffer.append(new StringBuilder()
				.append(" ) row_ where rownum <= ")
				.append(paramInt1 * paramInt2).append(") where rownum_ > ")
				.append((paramInt1 - 1) * paramInt2).toString());
		return localStringBuffer.toString();
	}
}
