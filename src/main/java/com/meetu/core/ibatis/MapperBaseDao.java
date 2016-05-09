package com.meetu.core.ibatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class MapperBaseDao<T> extends BaseWriteDao<T> implements
		BaseDaoInterface {

	@Autowired
	public JdbcTemplate jdbcTemplate;

	public void executeSQL(String paramString) throws Exception {
		jdbcTemplate.execute(paramString);
	}

	public int updateSQL(String paramString, Object paramObject)
			throws Exception {
		return jdbcTemplate.update(paramString, new Object[] { paramObject });
	}

	public int updateSQL(String paramString, Object[] paramArrayOfObject)
			throws Exception {
		return jdbcTemplate.update(paramString, paramArrayOfObject);
	}

	public int[] batchSQL(String paramString, Object[][] paramArrayOfObject)
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

	public int[] batchSQL(String paramString, List<Object[]> paramList)
			throws Exception {
		return jdbcTemplate.batchUpdate(paramString, paramList);
	}

	public List<Map<String, Object>> findSQL(String paramString)
			throws Exception {
		return jdbcTemplate.queryForList(paramString);
	}

	public List<Map<String, Object>> findSQL(String paramString,
			Object paramObject) throws Exception {
		return jdbcTemplate.queryForList(paramString,
				new Object[] { paramObject });
	}

	public List<Map<String, Object>> findSQL(String paramString,
			Object[] paramArrayOfObject) throws Exception {
		return jdbcTemplate.queryForList(paramString, paramArrayOfObject);
	}

	public Object findObjectSQL(String paramString,
			Object[] paramArrayOfObject, Class paramClass) {
		return jdbcTemplate.queryForObject(paramString, paramArrayOfObject,
				paramClass);
	}

	public Map<String, Object> findMapSQL(String paramString,
			Object[] paramArrayOfObject) {
		return jdbcTemplate.queryForMap(paramString, paramArrayOfObject);
	}
}
