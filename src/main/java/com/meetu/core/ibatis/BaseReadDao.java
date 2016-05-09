package com.meetu.core.ibatis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.alibaba.fastjson.JSONObject;
import com.meetu.core.db.DbUtils;
import com.meetu.core.db.Page;
import com.meetu.core.db.Record;
import com.meetu.core.db.RecordBuilder;

public class BaseReadDao<T> {
	static final Object[] o00000 = new Object[0];

	  @Autowired
	  @Qualifier("sqlSessionTemplate")
	  public SqlSessionTemplate sqlSessionTemplate;

	  private SqlSession o00000(SqlSessionTemplate paramSqlSessionTemplate, boolean paramBoolean)
	  {
	    if (paramSqlSessionTemplate == null)
	      return sqlSessionTemplate;
	    return paramSqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, paramBoolean);
	  }

	  public <T> List<T> selectList(String paramString, Object paramObject)
	  {
	    SqlSession localSqlSession = o00000(null, false);
	    return localSqlSession.selectList(paramString, paramObject);
	  }

	  public <T> List<T> selectList(SqlSessionTemplate paramSqlSessionTemplate, String paramString, Object paramObject)
	  {
	    SqlSession localSqlSession = o00000(paramSqlSessionTemplate, false);
	    return localSqlSession.selectList(paramString, paramObject);
	  }

	  public <T> List<T> selectList(String paramString)
	  {
	    SqlSession localSqlSession = o00000(null, false);
	    return localSqlSession.selectList(paramString);
	  }

	  public <T> List<T> selectList(SqlSessionTemplate paramSqlSessionTemplate, String paramString)
	  {
	    SqlSession localSqlSession = o00000(null, false);
	    return localSqlSession.selectList(paramString);
	  }

	  public <T> List<T> selectList(String paramString, Object paramObject, RowBounds paramRowBounds)
	  {
	    SqlSession localSqlSession = o00000(null, false);
	    return localSqlSession.selectList(paramString, paramObject, paramRowBounds);
	  }

	  public <T> List<T> selectList(SqlSessionTemplate paramSqlSessionTemplate, String paramString, Object paramObject, RowBounds paramRowBounds)
	  {
	    SqlSession localSqlSession = o00000(paramSqlSessionTemplate, false);
	    return localSqlSession.selectList(paramString, paramObject, paramRowBounds);
	  }

	  public <T> T selectOne(String paramString)
	  {
	    SqlSession localSqlSession = o00000(null, false);
	    return localSqlSession.selectOne(paramString);
	  }

	  public <T> T selectOne(String paramString, Object paramObject)
	  {
	    SqlSession localSqlSession = o00000(null, false);
	    return localSqlSession.selectOne(paramString, paramObject);
	  }

	  public <T> T selectOne(SqlSessionTemplate paramSqlSessionTemplate, String paramString, Object paramObject)
	  {
	    SqlSession localSqlSession = o00000(paramSqlSessionTemplate, false);
	    return localSqlSession.selectOne(paramString, paramObject);
	  }

	  public <T> T selectOne(SqlSessionTemplate paramSqlSessionTemplate, String paramString)
	  {
	    SqlSession localSqlSession = o00000(paramSqlSessionTemplate, false);
	    return localSqlSession.selectOne(paramString);
	  }

	  public Map<?, ?> selectMap(String paramString1, String paramString2)
	  {
	    SqlSession localSqlSession = o00000(null, false);
	    return localSqlSession.selectMap(paramString1, paramString2);
	  }

	  public Map<?, ?> selectMap(SqlSessionTemplate paramSqlSessionTemplate, String paramString1, String paramString2)
	  {
	    SqlSession localSqlSession = o00000(null, false);
	    return localSqlSession.selectMap(paramString1, paramString2);
	  }

	  public Map<?, ?> selectMap(String paramString1, Object paramObject, String paramString2)
	  {
	    SqlSession localSqlSession = o00000(null, false);
	    return localSqlSession.selectMap(paramString1, paramObject, paramString2);
	  }

	  public Map<?, ?> selectMap(SqlSessionTemplate paramSqlSessionTemplate, String paramString1, Object paramObject, String paramString2)
	  {
	    SqlSession localSqlSession = o00000(paramSqlSessionTemplate, false);
	    return localSqlSession.selectMap(paramString1, paramObject, paramString2);
	  }

	  public Map<?, ?> selectMap(String paramString1, Object paramObject, String paramString2, RowBounds paramRowBounds)
	  {
	    SqlSession localSqlSession = o00000(null, false);
	    return localSqlSession.selectMap(paramString1, paramObject, paramString2, paramRowBounds);
	  }

	  public Map<?, ?> selectMap(SqlSessionTemplate paramSqlSessionTemplate, String paramString1, Object paramObject, String paramString2, RowBounds paramRowBounds)
	  {
	    SqlSession localSqlSession = o00000(paramSqlSessionTemplate, false);
	    return localSqlSession.selectMap(paramString1, paramObject, paramString2, paramRowBounds);
	  }

	  public void select(String paramString, ResultHandler paramResultHandler)
	  {
	    SqlSession localSqlSession = o00000(null, false);
	    localSqlSession.select(paramString, paramResultHandler);
	  }

	  public void select(SqlSessionTemplate paramSqlSessionTemplate, String paramString, ResultHandler paramResultHandler)
	  {
	    SqlSession localSqlSession = o00000(paramSqlSessionTemplate, false);
	    localSqlSession.select(paramString, paramResultHandler);
	  }

	  public void select(String paramString, Object paramObject, ResultHandler paramResultHandler)
	  {
	    SqlSession localSqlSession = o00000(null, false);
	    localSqlSession.select(paramString, paramObject, paramResultHandler);
	  }

	  public void select(SqlSessionTemplate paramSqlSessionTemplate, String paramString, Object paramObject, ResultHandler paramResultHandler)
	  {
	    SqlSession localSqlSession = o00000(paramSqlSessionTemplate, false);
	    localSqlSession.select(paramString, paramObject, paramResultHandler);
	  }

	  public void select(String paramString, Object paramObject, RowBounds paramRowBounds, ResultHandler paramResultHandler)
	  {
	    SqlSession localSqlSession = o00000(null, false);
	    localSqlSession.select(paramString, paramObject, paramRowBounds, paramResultHandler);
	  }

	  public void select(SqlSessionTemplate paramSqlSessionTemplate, String paramString, Object paramObject, RowBounds paramRowBounds, ResultHandler paramResultHandler)
	  {
	    SqlSession localSqlSession = o00000(paramSqlSessionTemplate, false);
	    localSqlSession.select(paramString, paramObject, paramRowBounds, paramResultHandler);
	  }

	  public Page<Record> paginate(DataSource paramDataSource, int paramInt1, int paramInt2, String paramString1, String paramString2)
	    throws Exception
	  {
	    return paginate(paramDataSource, paramInt1, paramInt2, paramString1, paramString2, o00000);
	  }

	  public Page<Record> paginate(DataSource paramDataSource, int paramInt1, int paramInt2, String paramString1, String paramString2, Object[] paramArrayOfObject)
	    throws Exception
	  {
	    Connection localConnection = null;
	    try
	    {
	      localConnection = paramDataSource.getConnection();
	      Page localPage = o00000(localConnection, paramInt1, paramInt2, paramString1, paramString2, paramArrayOfObject);
	      return localPage;
	    }
	    catch (Exception localException1)
	    {
	      localException1.printStackTrace();
	      throw localException1;
	    }
	    finally
	    {
	      try
	      {
	        if (localConnection == null);
	      }
	      catch (Exception localException3)
	      {
	        localException3.printStackTrace();
	        throw localException3;
	      }
	    }
	  }

	  public Page<Record> paginate(int paramInt1, int paramInt2, String paramString1, String paramString2, Object[] paramArrayOfObject)
	    throws Exception
	  {
	    Connection localConnection = null;
	    try
	    {
	      localConnection = sqlSessionTemplate.getConnection();
	      Page localPage = o00000(localConnection, paramInt1, paramInt2, paramString1, paramString2, paramArrayOfObject);
	      return localPage;
	    }
	    catch (Exception localException1)
	    {
	      localException1.printStackTrace();
	      throw localException1;
	    }
	    finally
	    {
	      try
	      {
	        if (null == localConnection);
	      }
	      catch (Exception localException3)
	      {
	        throw localException3;
	      }
	    }
	  }

	  private Page<Record> o00000(Connection paramConnection, int paramInt1, int paramInt2, String paramString1, String paramString2, Object[] paramArrayOfObject)
	    throws Exception
	  {
	    if ((paramInt1 < 1) || (paramInt2 < 1))
	      return new Page(new ArrayList(0), paramInt1, paramInt2, 0, 0L);
	    long l = 0L;
	    int i = 0;
	    String str1 = "select count(1) " + DbUtils.replaceFormatSqlOrderBy(paramString2);
	    l = jdMethod_new(paramConnection, str1, paramArrayOfObject);
	    if (l == 0L)
	      return new Page(new ArrayList(0), paramInt1, paramInt2, 0, 0L);
	    i = (int)(l / paramInt2);
	    if (l % paramInt2 != 0L)
	      i++;
	    JSONObject localJSONObject = DbUtils.getDbPaginate(paramConnection, paramInt1, paramInt2, paramString1, paramString2);
	    String str2 = localJSONObject.getString("sql");
	    List localList = o00000(paramConnection, str2, paramArrayOfObject);
	    return new Page(localList, paramInt1, paramInt2, i, (int)l);
	  }

	  private long jdMethod_new(Connection paramConnection, String paramString, Object[] paramArrayOfObject)
	    throws Exception
	  {
	    ResultSet localResultSet = null;
	    PreparedStatement localPreparedStatement = null;
	    try
	    {
	      localPreparedStatement = paramConnection.prepareStatement(paramString);
	      if ((null != paramArrayOfObject) && (paramArrayOfObject.length > 0))
	        for (int i = 0; i < paramArrayOfObject.length; i++)
	          localPreparedStatement.setObject(i + 1, paramArrayOfObject[i]);
	      localResultSet = localPreparedStatement.executeQuery();
	      if (localResultSet.next())
	        return localResultSet.getLong(1);
	    }
	    catch (Exception localException)
	    {
	      localException.printStackTrace();
	      throw localException;
	    }
	    return 0L;
	  }

	  private List<Record> o00000(Connection paramConnection, String paramString, Object[] paramArrayOfObject)
	    throws Exception
	  {
	    List localList = null;
	    ResultSet localResultSet = null;
	    PreparedStatement localPreparedStatement = null;
	    try
	    {
	      localPreparedStatement = paramConnection.prepareStatement(paramString);
	      for (int i = 0; i < paramArrayOfObject.length; i++)
	        localPreparedStatement.setObject(i + 1, paramArrayOfObject[i]);
	      localResultSet = localPreparedStatement.executeQuery();
	      localList = RecordBuilder.build(localResultSet);
	    }
	    catch (Exception localException)
	    {
	      throw localException;
	    }
	    finally
	    {
	      try
	      {
	        if (localResultSet != null)
	          localResultSet.close();
	        if (localPreparedStatement != null)
	          localPreparedStatement.close();
	      }
	      catch (SQLException localSQLException2)
	      {
	        localSQLException2.printStackTrace();
	        throw localSQLException2;
	      }
	    }
	    return localList;
	  }
}
