package com.meetu.core.ibatis;

import java.sql.SQLException;
import java.util.List;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;

public class BaseWriteDao<T> extends BaseReadDao<T>{

	private static final Logger jdField_new = Logger.getLogger(BaseWriteDao.class);

	  @Autowired
	  @Qualifier("sqlSessionTemplate")
	  public SqlSessionTemplate sqlSessionTemplate;

	  private SqlSession jdField_new(SqlSessionTemplate paramSqlSessionTemplate, boolean paramBoolean)
	  {
	    if (paramSqlSessionTemplate == null)
	      return sqlSessionTemplate;
	    return paramSqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, paramBoolean);
	  }

	  public int update(String paramString, Object paramObject)
	  {
	    SqlSession localSqlSession = jdField_new(null, true);
	    return localSqlSession.update(paramString, paramObject);
	  }

	  public int update(String paramString)
	  {
	    SqlSession localSqlSession = jdField_new(null, true);
	    return localSqlSession.update(paramString);
	  }

	  public int save(String paramString, Object paramObject)
	  {
	    SqlSession localSqlSession = jdField_new(null, true);
	    return localSqlSession.insert(paramString, paramObject);
	  }

	  public int save(String paramString)
	  {
	    SqlSession localSqlSession = jdField_new(null, true);
	    return localSqlSession.insert(paramString);
	  }

	  public int save(SqlSessionTemplate paramSqlSessionTemplate, String paramString, Object paramObject)
	  {
	    SqlSession localSqlSession = jdField_new(paramSqlSessionTemplate, true);
	    return localSqlSession.insert(paramString, paramObject);
	  }

	  public int delete(String paramString, Object paramObject)
	  {
	    SqlSession localSqlSession = jdField_new(null, true);
	    return localSqlSession.delete(paramString, paramObject);
	  }

	  public int delete(String paramString)
	  {
	    SqlSession localSqlSession = jdField_new(null, true);
	    return localSqlSession.insert(paramString);
	  }

	  public int delete(SqlSessionTemplate paramSqlSessionTemplate, String paramString, Object paramObject)
	  {
	    SqlSession localSqlSession = jdField_new(paramSqlSessionTemplate, true);
	    return localSqlSession.delete(paramString, paramObject);
	  }

	  public void batchInsert(SqlSessionTemplate paramSqlSessionTemplate, String paramString, List<?> paramList)
	    throws DataAccessException
	  {
	    SqlSession localSqlSession = jdField_new(paramSqlSessionTemplate, false);
	    int i = 10000;
	    try
	    {
	      if ((null != paramList) || (paramList.size() > 0))
	      {
	        int j = 0;
	        int k = paramList.size();
	        while (j < k)
	        {
	          localSqlSession.insert(paramString, paramList.get(j));
	          if ((j % 1000 == 0) || (j == i - 1))
	          {
	            localSqlSession.commit();
	            localSqlSession.clearCache();
	          }
	          j++;
	        }
	      }
	    }
	    catch (Exception localException)
	    {
	      localSqlSession.rollback();
	      if (jdField_new.isDebugEnabled())
	        jdField_new.debug("batchInsert error: id [" + paramString + "], parameterObject [" + paramList + "].  Cause: " + localException.getMessage());
	    }
	    finally
	    {
	      if (localSqlSession != null)
	        localSqlSession.close();
	    }
	  }

	  public void batchInsert(String paramString, List<?> paramList)
	    throws DataAccessException, SQLException
	  {
	    batchInsert(null, paramString, paramList);
	  }

	  public void batchUpdate(SqlSessionTemplate paramSqlSessionTemplate, String paramString, List<?> paramList)
	    throws DataAccessException
	  {
	    SqlSession localSqlSession = jdField_new(paramSqlSessionTemplate, false);
	    try
	    {
	      if ((null != paramList) || (paramList.size() > 0))
	      {
	        int i = 10000;
	        int j = 0;
	        int k = paramList.size();
	        while (j < k)
	        {
	          localSqlSession.update(paramString, paramList.get(j));
	          if ((j % 1000 == 0) || (j == i - 1))
	          {
	            localSqlSession.commit();
	            localSqlSession.clearCache();
	          }
	          j++;
	        }
	      }
	    }
	    catch (Exception localException)
	    {
	      localSqlSession.rollback();
	      if (jdField_new.isDebugEnabled())
	      {
	        localException.printStackTrace();
	        jdField_new.debug("batchUpdate error: id [" + paramString + "], parameterObject [" + paramList + "].  Cause: " + localException.getMessage());
	      }
	    }
	    finally
	    {
	      localSqlSession.close();
	    }
	  }

	  public void batchUpdate(String paramString, List<?> paramList)
	    throws DataAccessException
	  {
	    batchUpdate(null, paramString, paramList);
	  }

	  public void batchDelete(SqlSessionTemplate paramSqlSessionTemplate, String paramString, List<?> paramList)
	    throws DataAccessException
	  {
	    SqlSession localSqlSession = jdField_new(paramSqlSessionTemplate, false);
	    int i = 10000;
	    try
	    {
	      if ((null != paramList) || (paramList.size() > 0))
	      {
	        int j = 0;
	        int k = paramList.size();
	        while (j < k)
	        {
	          localSqlSession.delete(paramString, paramList.get(j));
	          if ((j % 1000 == 0) || (j == i - 1))
	          {
	            localSqlSession.commit();
	            localSqlSession.clearCache();
	          }
	          j++;
	        }
	      }
	    }
	    catch (Exception localException)
	    {
	      localSqlSession.rollback();
	      if (jdField_new.isDebugEnabled())
	        jdField_new.debug("batchDelete error: id [" + paramString + "], parameterObject [" + paramList + "].  Cause: " + localException.getMessage());
	    }
	    finally
	    {
	      localSqlSession.close();
	    }
	  }

	  public void batchDelete(String paramString, List<?> paramList)
	    throws DataAccessException
	  {
	    batchDelete(null, paramString, paramList);
	  }
}
