package com.meetu.core.db;

import com.alibaba.fastjson.JSONObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class DbUtilsTemplate {
	private QueryRunner Object;
	private static final Log log = LogFactory
			.getLog(DbUtilsTemplate.class);
	static final Object[] jdField_return = new Object[0];
	private String o00000;
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	public String getJdbcUser() {
		return o00000;
	}

	public void setJdbcUser(String paramString) {
		o00000 = paramString;
	}

	public void setDataSource(DataSource paramDataSource) {
		dataSource = paramDataSource;
	}

	public void setJdbcTemplate(JdbcTemplate paramJdbcTemplate) {
		jdbcTemplate = paramJdbcTemplate;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void execute(String paramString) {
		jdbcTemplate.execute(paramString);
	}

	public int save(Object paramObject) throws Exception {
		SqlHolder localSqlHolder = SqlBuilder.buildInsert(paramObject);
		return update(localSqlHolder.getSql(), localSqlHolder.getParams());
	}

	public int update(Object paramObject) throws Exception {
		SqlHolder localSqlHolder = SqlBuilder.buildUpdate(paramObject);
		return update(localSqlHolder.getSql(), localSqlHolder.getParams());
	}

	public Object get(Object paramObject1, Object paramObject2)
			throws Exception {
		SqlHolder localSqlHolder = SqlBuilder.buildGet(paramObject1,
				paramObject2);
		return findFirst(localSqlHolder.getSql(), localSqlHolder.getParams());
	}

	public int update(String paramString) throws Exception {
		Object = new QueryRunner(dataSource);
		int i = 0;
		i = Object.update(paramString);
		return i;
	}

	public int update(String paramString, Object paramObject) throws Exception {
		return update(paramString, new Object[] { paramObject });
	}

	public int update(String paramString, Object[] paramArrayOfObject)
			throws Exception {
		Object = new QueryRunner(dataSource);
		int i = 0;
		if (paramArrayOfObject == null)
			i = Object.update(paramString);
		else
			i = Object.update(paramString, paramArrayOfObject);
		return i;
	}

	public int[] batchUpdate(String paramString, Object[][] paramArrayOfObject)
			throws Exception {
		Object = new QueryRunner(dataSource);
		int[] arrayOfInt = new int[0];
		arrayOfInt = Object.batch(paramString, paramArrayOfObject);
		return arrayOfInt;
	}

	public List<Map<String, Object>> find(String paramString) throws Exception {
		return find(paramString, null);
	}

	public List<Map<String, Object>> find(String paramString, Object paramObject)
			throws Exception {
		return jdbcTemplate.queryForList(paramString,
				new Object[] { paramObject });
	}

	public List<Map<String, Object>> find(String paramString,
			Object[] paramArrayOfObject) throws Exception {
		return jdbcTemplate.queryForList(paramString, paramArrayOfObject);
	}

	public <T> List<T> find(Class<T> paramClass, String paramString)
			throws Exception {
		return find(paramClass, paramString, null);
	}

	public <T> List<T> find(Class<T> paramClass, String paramString,
			Object paramObject) throws Exception {
		return find(paramClass, paramString, new Object[] { paramObject });
	}

	public <T> List<T> find(Class<T> paramClass, String paramString,
			Object[] paramArrayOfObject) throws Exception {
		Object = new QueryRunner(dataSource);
		Object localObject = new ArrayList();
		if (paramArrayOfObject == null)
			localObject = (List) Object.query(paramString, new BeanListHandler(
					paramClass));
		else
			localObject = (List) Object.query(paramString, new BeanListHandler(
					paramClass), paramArrayOfObject);
		return (List<T>) localObject;
	}

	public <T> T findFirst(Class<T> paramClass, String paramString)
			throws Exception {
		return findFirst(paramClass, paramString, null);
	}

	public <T> T findFirst(Class<T> paramClass, String paramString,
			Object paramObject) throws Exception {
		return findFirst(paramClass, paramString, new Object[] { paramObject });
	}

	public <T> T findFirst(Class<T> paramClass, String paramString,
			Object[] paramArrayOfObject) throws Exception {
		Object = new QueryRunner(dataSource);
		Object localObject = null;
		if (paramArrayOfObject == null)
			localObject = Object
					.query(paramString, new BeanHandler(paramClass));
		else
			localObject = Object.query(paramString,
					new BeanHandler(paramClass), paramArrayOfObject);
		return (T) localObject;
	}

	public Map<String, Object> findFirst(String paramString) throws Exception {
		return findFirst(paramString, null);
	}

	public Map<String, Object> findOne(String paramString) throws Exception {
		return jdbcTemplate.queryForMap(paramString);
	}

	public Map<String, Object> findOne(String paramString, Object paramObject)
			throws Exception {
		return jdbcTemplate.queryForMap(paramString,
				new Object[] { paramObject });
	}

	public Map<String, Object> findOne(String paramString,
			Object[] paramArrayOfObject) throws Exception {
		return jdbcTemplate.queryForMap(paramString, paramArrayOfObject);
	}

	public Map<String, Object> findFirst(String paramString, Object paramObject)
			throws Exception {
		return findFirst(paramString, new Object[] { paramObject });
	}

	public Map<String, Object> findFirst(String paramString,
			Object[] paramArrayOfObject) throws Exception {
		Object = new QueryRunner(dataSource);
		Map localMap = null;
		if (paramArrayOfObject == null)
			localMap = (Map) Object.query(paramString, new MapHandler());
		else
			localMap = (Map) Object.query(paramString, new MapHandler(),
					paramArrayOfObject);
		return localMap;
	}

	public Object findBy(String paramString1, String paramString2)
			throws Exception {
		return findBy(paramString1, paramString2, null);
	}

	public Object findBy(String paramString1, String paramString2,
			Object paramObject) throws Exception {
		return findBy(paramString1, paramString2, new Object[] { paramObject });
	}

	public Object findBy(String paramString1, String paramString2,
			Object[] paramArrayOfObject) throws Exception {
		Object = new QueryRunner(dataSource);
		Object localObject = null;
		if (paramArrayOfObject == null)
			localObject = Object.query(paramString1, new ScalarHandler(
					paramString2));
		else
			localObject = Object.query(paramString1, new ScalarHandler(
					paramString2), paramArrayOfObject);
		return localObject;
	}

	public Object findBy(String paramString, int paramInt) throws Exception {
		return findBy(paramString, paramInt, null);
	}

	public Object findBy(String paramString, int paramInt, Object paramObject)
			throws Exception {
		return findBy(paramString, paramInt, new Object[] { paramObject });
	}

	public Object findBy(String paramString, int paramInt,
			Object[] paramArrayOfObject) throws Exception {
		Object = new QueryRunner(dataSource);
		Object localObject = null;
		if (paramArrayOfObject == null)
			localObject = Object
					.query(paramString, new ScalarHandler(paramInt));
		else
			localObject = Object.query(paramString,
					new ScalarHandler(paramInt), paramArrayOfObject);
		return localObject;
	}

	private List<Record> o00000(Connection paramConnection, String paramString,
			Object[] paramArrayOfObject) throws Exception {
		List localList = null;
		ResultSet localResultSet = null;
		PreparedStatement localPreparedStatement = null;
		try {
			localPreparedStatement = paramConnection
					.prepareStatement(paramString);
			for (int i = 0; i < paramArrayOfObject.length; i++)
				localPreparedStatement.setObject(i + 1, paramArrayOfObject[i]);
			localResultSet = localPreparedStatement.executeQuery();
			localList = RecordBuilder.build(localResultSet);
		} catch (Exception localException) {
			log.debug(localException.getMessage());
			throw localException;
		} finally {
			try {
				if (localResultSet != null)
					localResultSet.close();
				if (localPreparedStatement != null)
					localPreparedStatement.close();
			} catch (SQLException localSQLException2) {
				localSQLException2.printStackTrace();
				log.debug(localSQLException2.getMessage());
				throw localSQLException2;
			}
		}
		return localList;
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
		l = jdbcTemplate.queryForObject(str1, paramArrayOfObject,Long.class);
		if (l == 0L)
			return new Page(new ArrayList(0), paramInt1, paramInt2, 0, 0L);
		i = (int) (l / paramInt2);
		if (l % paramInt2 != 0L)
			i++;
		JSONObject localJSONObject = DbUtils.getDbPaginate(paramConnection,
				paramInt1, paramInt2, paramString1, paramString2);
		String str2 = localJSONObject.getString("sql");
		List localList = o00000(paramConnection, str2.toString(),
				paramArrayOfObject);
		return new Page(localList, paramInt1, paramInt2, i, l);
	}

	public Page<Record> paginate(int paramInt1, int paramInt2,
			String paramString1, String paramString2,
			Object[] paramArrayOfObject) throws Exception {
		Connection localConnection = null;
		try {
			localConnection = dataSource.getConnection();
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
				paramString2, jdField_return);
	}
}
