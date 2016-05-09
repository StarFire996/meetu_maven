package com.meetu.core.dao;

import com.alibaba.fastjson.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.meetu.core.db.DbUtils;
import com.meetu.core.db.Page;
import com.meetu.core.db.Record;
import com.meetu.core.db.RecordBuilder;
import com.meetu.core.db.SqlBuilder;
import com.meetu.core.db.SqlHolder;

public class BaseDao<T> {

	static final Object[] o00000 = new Object[0];

	@Resource
	public JdbcTemplate jdbcTemplate;

	public void execute(String paramString) throws Exception {
		jdbcTemplate.execute(paramString);
	}

	public void call(CallableStatementCreator paramCallableStatementCreator,
			List<SqlParameter> paramList) throws Exception {
		jdbcTemplate.call(paramCallableStatementCreator, paramList);
	}

	public int update(String paramString) throws Exception {
		return jdbcTemplate.update(paramString);
	}

	public int update(String paramString, Object paramObject) throws Exception {
		return jdbcTemplate.update(paramString, new Object[] { paramObject });
	}

	public int update(String paramString, Object[] paramArrayOfObject)
			throws Exception {
		return jdbcTemplate.update(paramString, paramArrayOfObject);
	}

	public int[] batch(String paramString, Object[][] paramArrayOfObject)
			throws Exception {
		ArrayList localArrayList = new ArrayList();
		if (paramArrayOfObject != null) {
			int i = 0;
			int j = paramArrayOfObject.length;
			while (i < j) {
				localArrayList.add(paramArrayOfObject[i]);
				i++;
			}
		}
		return jdbcTemplate.batchUpdate(paramString, localArrayList);
	}

	public int[] batch(String paramString, List<Object[]> paramList)
			throws Exception {
		return jdbcTemplate.batchUpdate(paramString, paramList);
	}

	public int queryForInt(String paramString) {
		return jdbcTemplate.queryForObject(paramString, Integer.class);
	}

	public int queryForInt(String paramString, Object[] paramArrayOfObject) {
		return jdbcTemplate.queryForObject(paramString, paramArrayOfObject, Integer.class);
	}

	public long queryForLong(String paramString) {
		return jdbcTemplate.queryForObject(paramString, Long.class);
	}

	public long queryForLong(String paramString, Object[] paramArrayOfObject) {
		return jdbcTemplate.queryForObject(paramString, paramArrayOfObject, Long.class);
	}

	public List<Map<String, Object>> find(String paramString) throws Exception {
		if (StringUtils.isNotBlank(paramString))
			paramString = paramString.toUpperCase();
		return jdbcTemplate.queryForList(paramString);
	}

	public List<Map<String, Object>> find(String paramString, Object paramObject)
			throws Exception {
		if (StringUtils.isNotBlank(paramString))
			paramString = paramString.toUpperCase();
		return jdbcTemplate.queryForList(paramString,
				new Object[] { paramObject });
	}

	public List<Map<String, Object>> find(String paramString,
			Object[] paramArrayOfObject) throws Exception {
		if (StringUtils.isNotBlank(paramString))
			paramString = paramString.toUpperCase();
		return jdbcTemplate.queryForList(paramString, paramArrayOfObject);
	}

	public Object find(Class<T> paramClass, String paramString,
			Object[] paramArrayOfObject) throws Exception {
		return jdbcTemplate.queryForObject(paramString, paramArrayOfObject,
				new BeanPropertyRowMapper(paramClass));
	}

	public Map<String, Object> findFirst(String paramString) {
		try {
			if (StringUtils.isNotBlank(paramString))
				paramString = paramString.toUpperCase();
			return jdbcTemplate.queryForMap(paramString);
		} catch (Exception localException) {
		}
		return null;
	}

	public Map<String, Object> findFirst(String paramString,
			Object[] paramArrayOfObject) {
		try {
			if (StringUtils.isNotBlank(paramString))
				paramString = paramString.toUpperCase();
			return jdbcTemplate.queryForMap(paramString, paramArrayOfObject);
		} catch (Exception localException) {
		}
		return null;
	}

	public SqlRowSet findRowSet(String paramString, Object[] paramArrayOfObject)
			throws Exception {
		return jdbcTemplate.queryForRowSet(paramString, paramArrayOfObject);
	}

	public Object findObject(String paramString, Class paramClass) {
		return jdbcTemplate.queryForObject(paramString, paramClass);
	}

	public Object findObject(String paramString, Object[] paramArrayOfObject,
			Class paramClass) {
		return jdbcTemplate.queryForObject(paramString, paramArrayOfObject,
				paramClass);
	}

	public Map<String, Object> findClobOne(String paramString,
			Object[] paramArrayOfObject) {
		try {
			if (StringUtils.isNotBlank(paramString))
				paramString = paramString.toUpperCase();
			return jdbcTemplate.queryForMap(paramString, paramArrayOfObject);
		} catch (Exception localException) {
		}
		return null;
	}

	public Map<String, Object> findMap(String paramString) {
		try {
			if (StringUtils.isNotBlank(paramString))
				paramString = paramString.toUpperCase();
			return jdbcTemplate.queryForMap(paramString);
		} catch (Exception localException) {
		}
		return null;
	}

	public Map<String, Object> findMap(String paramString,
			Object[] paramArrayOfObject) {
		try {
			if (StringUtils.isNotBlank(paramString))
				paramString = paramString.toUpperCase();
			return jdbcTemplate.queryForMap(paramString, paramArrayOfObject);
		} catch (Exception localException) {
		}
		return null;
	}

	public int save(T paramT) throws Exception {
		SqlHolder localSqlHolder = SqlBuilder.buildInsert(paramT);
		return update(localSqlHolder.getSql(), localSqlHolder.getParams());
	}

	public int update(T paramT) throws Exception {
		SqlHolder localSqlHolder = SqlBuilder.buildUpdate(paramT);
		return update(localSqlHolder.getSql(), localSqlHolder.getParams());
	}

	public Object get(T paramT, Object paramObject) throws Exception {
		SqlHolder localSqlHolder = SqlBuilder.buildGet(paramT, paramObject);
		return findFirst(localSqlHolder.getSql(), localSqlHolder.getParams());
	}

	private Page<Record> o00000(Connection paramConnection, int paramInt1,
			int paramInt2, String paramString1, String paramString2,
			Object[] paramArrayOfObject) throws Exception {
		if ((paramInt1 < 1) || (paramInt2 < 1))
			return new Page(new ArrayList(0), paramInt1, paramInt2, 0, 0L);
		long l = 0L;
		int i = 0;
		String str1 = "select count(1) "
				+ DbUtils.replaceFormatSqlOrderBy(paramString2);
		l = jdbcTemplate.queryForObject(str1, paramArrayOfObject, Long.class);
		if (l == 0L)
			return new Page(new ArrayList(0), paramInt1, paramInt2, 0, 0L);
		i = (int) (l / paramInt2);
		if (l % paramInt2 != 0L)
			i++;
		JSONObject localJSONObject = DbUtils.getDbPaginate(paramConnection,
				paramInt1, paramInt2, paramString1, paramString2);
		String str2 = localJSONObject.getString("sql");
		List localList = o00000(paramConnection, str2.toUpperCase(),
				paramArrayOfObject);
		return new Page(localList, paramInt1, paramInt2, i, l);
	}

	public Page<Record> paginate(int paramInt1, int paramInt2,
			String paramString1, String paramString2,
			Object[] paramArrayOfObject) throws Exception {
		Connection localConnection = null;
		try {
			localConnection = jdbcTemplate.getDataSource().getConnection();
			Page localPage = o00000(localConnection, paramInt1, paramInt2,
					paramString1, paramString2, paramArrayOfObject);
			return localPage;
		} catch (Exception localException) {
			localException.printStackTrace();
			throw localException;
		} finally {
			try {
				if (localConnection != null)
					localConnection.close();
			} catch (SQLException localSQLException2) {
				throw localSQLException2;
			}
		}
	}

	public Page<Record> paginate(DataSource paramDataSource, int paramInt1,
			int paramInt2, String paramString1, String paramString2,
			Object[] paramArrayOfObject) throws Exception {
		Connection localConnection = null;
		try {
			localConnection = paramDataSource.getConnection();
			Page localPage = o00000(localConnection, paramInt1, paramInt2,
					paramString1, paramString2, paramArrayOfObject);
			return localPage;
		} catch (Exception localException) {
			localException.printStackTrace();
			throw localException;
		} finally {
			try {
				localConnection.close();
			} catch (SQLException localSQLException2) {
				localSQLException2.printStackTrace();
				throw localSQLException2;
			}
		}
	}

	public Page<Record> paginate(DataSource paramDataSource, int paramInt1,
			int paramInt2, String paramString1, String paramString2)
			throws Exception {
		return paginate(paramDataSource, paramInt1, paramInt2, paramString1,
				paramString2, o00000);
	}

	private List<Record> o00000(Connection paramConnection, String paramString,
			Object[] paramArrayOfObject) throws Exception {
		List localList = null;
		ResultSet localResultSet = null;
		PreparedStatement localPreparedStatement = null;
		try {
			paramString = paramString.toUpperCase();
			localPreparedStatement = paramConnection
					.prepareStatement(paramString);
			for (int i = 0; i < paramArrayOfObject.length; i++)
				localPreparedStatement.setObject(i + 1, paramArrayOfObject[i]);
			localResultSet = localPreparedStatement.executeQuery();
			localList = RecordBuilder.build(localResultSet);
		} catch (Exception localException) {
			throw localException;
		} finally {
			try {
				if (localResultSet != null)
					localResultSet.close();
				if (localPreparedStatement != null)
					localPreparedStatement.close();
			} catch (SQLException localSQLException2) {
				localSQLException2.printStackTrace();
				throw localSQLException2;
			}
		}
		return localList;
	}
}
