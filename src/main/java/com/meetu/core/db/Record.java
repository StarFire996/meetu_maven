package com.meetu.core.db;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import net.sf.json.JSONObject;

public class Record implements Serializable{

	private static final long serialVersionUID = -280193318728018606L;

	private Map<String, Object> o00000 = new HashMap();

	public Map<String, Object> getColumns()
	{
		return o00000;
	}

	public Record setColumns(Map<String, Object> paramMap)
	{
		o00000.putAll(paramMap);
	    return this;
	}

	public Record setColumns(Record paramRecord)
	{
		o00000.putAll(paramRecord.getColumns());
	    return this;
	}

	public Record remove(String paramString)
	{
		o00000.remove(paramString);
	    return this;
	}

	public Record remove(String[] paramArrayOfString)
	{
		if (paramArrayOfString != null)
			for (String str : paramArrayOfString)
	        o00000.remove(str);
	    return this;
	}

	public Record removeNullValueColumns()
	{
		Iterator localIterator = o00000.entrySet().iterator();
		while (localIterator.hasNext())
		{
			Map.Entry localEntry = (Map.Entry)localIterator.next();
			if (localEntry.getValue() == null)
				localIterator.remove();
		}
		return this;
	}

	public Record keep(String[] paramArrayOfString)
	{
		if ((paramArrayOfString != null) && (paramArrayOfString.length > 0))
		{
			HashMap localHashMap = new HashMap(paramArrayOfString.length);
			for (String str : paramArrayOfString)
				if (o00000.containsKey(str))
					localHashMap.put(str, o00000.get(str));
			o00000 = localHashMap;
		}else{
			o00000.clear();
		}
		return this;
	}

	public Record keep(String paramString)
	{
		if (o00000.containsKey(paramString))
		{
			Object localObject = o00000.get(paramString);
			o00000.clear();
			o00000.put(paramString, localObject);
	    }else{
	    	o00000.clear();
	    }
	    return this;
	}

	public Record clear()
	{
		o00000.clear();
		return this;
	}

	public Record set(String paramString, Object paramObject)
	{
		o00000.put(paramString, paramObject);
		return this;
	}

	public <T> T get(String paramString)
	{
		return (T) o00000.get(paramString);
	}

	public <T> T get(String paramString, Object paramObject)
	{
		Object localObject = o00000.get(paramString);
	    return (T) (localObject != null ? localObject : paramObject);
	}

	public String getStr(String paramString)
	{
		return (String)o00000.get(paramString);
	}

	public Integer getInt(String paramString)
	{
		return (Integer)o00000.get(paramString);
	}

	  public Long getLong(String paramString)
	  {
	    return (Long)o00000.get(paramString);
	  }

	  public Date getDate(String paramString)
	  {
	    return (Date)o00000.get(paramString);
	  }

	  public Time getTime(String paramString)
	  {
	    return (Time)o00000.get(paramString);
	  }

	  /**
	  public Timestamp getOracleStamp(String paramString)
	  {
	    Object localObject = o00000.get(paramString);
	    if ((localObject instanceof TIMESTAMP))
	      try
	      {
	        return ((TIMESTAMP)localObject).timestampValue();
	      }
	      catch (SQLException localSQLException)
	      {
	        localSQLException.printStackTrace();
	      }
	    return (Timestamp)o00000.get(paramString);
	  }
	  **/

	  public Timestamp getTimestamp(String paramString)
	  {
	    return (Timestamp)o00000.get(paramString);
	  }

	  public Double getDouble(String paramString)
	  {
	    return (Double)o00000.get(paramString);
	  }

	  public Float getFloat(String paramString)
	  {
	    return (Float)o00000.get(paramString);
	  }

	  public Boolean getBoolean(String paramString)
	  {
	    return (Boolean)o00000.get(paramString);
	  }

	  public BigDecimal getBigDecimal(String paramString)
	  {
	    return (BigDecimal)o00000.get(paramString);
	  }

	  public byte[] getBytes(String paramString)
	  {
	    return (byte[])o00000.get(paramString);
	  }

	  public Number getNumber(String paramString)
	  {
	    return (Number)o00000.get(paramString);
	  }

	  public String toString()
	  {
	    StringBuilder localStringBuilder = new StringBuilder();
	    localStringBuilder.append(super.toString()).append(" {");
	    int i = 1;
	    Iterator localIterator = o00000.entrySet().iterator();
	    while (localIterator.hasNext())
	    {
	      Map.Entry localEntry = (Map.Entry)localIterator.next();
	      if (i != 0)
	        i = 0;
	      else
	        localStringBuilder.append(", ");
	      Object localObject = localEntry.getValue();
	      if (localObject != null)
	        localObject = ((Object)localObject).toString();
	      localStringBuilder.append((String)localEntry.getKey()).append(":").append(localObject);
	    }
	    localStringBuilder.append("}");
	    return localStringBuilder.toString();
	  }

	  public boolean equals(Object paramObject)
	  {
	    if (!(paramObject instanceof Record))
	      return false;
	    if (paramObject == this)
	      return true;
	    return o00000.equals(((Record)paramObject).o00000);
	  }

	  public int hashCode()
	  {
	    return o00000 == null ? 0 : o00000.hashCode();
	  }

	  public String[] getcolumnNames()
	  {
	    Set localSet = o00000.keySet();
	    return (String[])localSet.toArray(new String[localSet.size()]);
	  }

	  public Object[] getcolumnValues()
	  {
	    Collection localCollection = o00000.values();
	    return localCollection.toArray(new Object[localCollection.size()]);
	  }

	  public String toJson()
	  {
	    return JSONObject.fromObject(o00000).toString();
	  }
}
