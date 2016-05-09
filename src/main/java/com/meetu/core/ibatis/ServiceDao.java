package com.meetu.core.ibatis;

import java.util.List;
import java.util.Map;

import com.meetu.core.db.DbUtilsTemplate;
import com.meetu.core.db.Page;
import com.meetu.core.db.Record;

public class ServiceDao {
	private DbUtilsTemplate jdField_super;

	  public DbUtilsTemplate getDbUtilsTemplate()
	  {
	    return jdField_super;
	  }

	  public void setDbUtilsTemplate(DbUtilsTemplate paramDbUtilsTemplate)
	  {
	    jdField_super = paramDbUtilsTemplate;
	  }

	  public void execute(String paramString)
	    throws Exception
	  {
	    jdField_super.execute(paramString);
	  }

	  public int update(String paramString)
	    throws Exception
	  {
	    return jdField_super.update(paramString);
	  }

	  public int update(String paramString, Object paramObject)
	    throws Exception
	  {
	    return jdField_super.update(paramString, new Object[] { paramObject });
	  }

	  public int update(String paramString, Object[] paramArrayOfObject)
	    throws Exception
	  {
	    return jdField_super.update(paramString, paramArrayOfObject);
	  }

	  public int[] batch(String paramString, Object[][] paramArrayOfObject)
	    throws Exception
	  {
	    return jdField_super.batchUpdate(paramString, paramArrayOfObject);
	  }

	  public List<Map<String, Object>> find(String paramString)
	    throws Exception
	  {
	    paramString = paramString.toUpperCase();
	    return jdField_super.find(paramString);
	  }

	  public List<Map<String, Object>> find(String paramString, Object paramObject)
	    throws Exception
	  {
	    paramString = paramString.toUpperCase();
	    return jdField_super.find(paramString, paramObject);
	  }

	  public List<Map<String, Object>> find(String paramString, Object[] paramArrayOfObject)
	    throws Exception
	  {
	    paramString = paramString.toUpperCase();
	    return jdField_super.find(paramString, paramArrayOfObject);
	  }

	  public Map<String, Object> findOne(String paramString)
	    throws Exception
	  {
	    try
	    {
	      paramString = paramString.toUpperCase();
	      return jdField_super.findOne(paramString);
	    }
	    catch (Exception localException)
	    {
	    }
	    return null;
	  }

	  public Map<String, Object> findOne(String paramString, Object[] paramArrayOfObject)
	    throws Exception
	  {
	    try
	    {
	      paramString = paramString.toUpperCase();
	      return jdField_super.findOne(paramString, paramArrayOfObject);
	    }
	    catch (Exception localException)
	    {
	    }
	    return null;
	  }

	  public Page<Record> paginate(int paramInt1, int paramInt2, String paramString1, String paramString2, Object[] paramArrayOfObject)
	    throws Exception
	  {
	    paramString1 = paramString1.toUpperCase();
	    return jdField_super.paginate(paramInt1, paramInt2, paramString1, paramString2, paramArrayOfObject);
	  }
}
