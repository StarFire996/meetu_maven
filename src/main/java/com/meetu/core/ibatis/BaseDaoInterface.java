package com.meetu.core.ibatis;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.dao.DataAccessException;

import com.meetu.core.db.Page;
import com.meetu.core.db.Record;

public abstract interface BaseDaoInterface {
	public abstract <T> List<T> selectList(String paramString,
			Object paramObject);

	public abstract <T> List<T> selectList(
			SqlSessionTemplate paramSqlSessionTemplate, String paramString,
			Object paramObject);

	public abstract <T> List<T> selectList(String paramString);

	public abstract <T> List<T> selectList(
			SqlSessionTemplate paramSqlSessionTemplate, String paramString);

	public abstract <T> List<T> selectList(String paramString,
			Object paramObject, RowBounds paramRowBounds);

	public abstract <T> List<T> selectList(
			SqlSessionTemplate paramSqlSessionTemplate, String paramString,
			Object paramObject, RowBounds paramRowBounds);

	public abstract <T> T selectOne(String paramString);

	public abstract <T> T selectOne(SqlSessionTemplate paramSqlSessionTemplate,
			String paramString);

	public abstract Map<?, ?> selectMap(String paramString1, String paramString2);

	public abstract Map<?, ?> selectMap(
			SqlSessionTemplate paramSqlSessionTemplate, String paramString1,
			String paramString2);

	public abstract Map<?, ?> selectMap(String paramString1,
			Object paramObject, String paramString2);

	public abstract Map<?, ?> selectMap(
			SqlSessionTemplate paramSqlSessionTemplate, String paramString1,
			Object paramObject, String paramString2);

	public abstract Map<?, ?> selectMap(String paramString1,
			Object paramObject, String paramString2, RowBounds paramRowBounds);

	public abstract Map<?, ?> selectMap(
			SqlSessionTemplate paramSqlSessionTemplate, String paramString1,
			Object paramObject, String paramString2, RowBounds paramRowBounds);

	public abstract void select(String paramString,
			ResultHandler paramResultHandler);

	public abstract void select(SqlSessionTemplate paramSqlSessionTemplate,
			String paramString, ResultHandler paramResultHandler);

	public abstract void select(String paramString, Object paramObject,
			ResultHandler paramResultHandler);

	public abstract void select(SqlSessionTemplate paramSqlSessionTemplate,
			String paramString, Object paramObject,
			ResultHandler paramResultHandler);

	public abstract void select(String paramString, Object paramObject,
			RowBounds paramRowBounds, ResultHandler paramResultHandler);

	public abstract void select(SqlSessionTemplate paramSqlSessionTemplate,
			String paramString, Object paramObject, RowBounds paramRowBounds,
			ResultHandler paramResultHandler);

	public abstract Page<Record> paginate(DataSource paramDataSource,
			int paramInt1, int paramInt2, String paramString1,
			String paramString2) throws Exception;

	public abstract Page<Record> paginate(DataSource paramDataSource,
			int paramInt1, int paramInt2, String paramString1,
			String paramString2, Object[] paramArrayOfObject) throws Exception;

	public abstract Page<Record> paginate(int paramInt1, int paramInt2,
			String paramString1, String paramString2,
			Object[] paramArrayOfObject) throws Exception;

	public abstract int update(String paramString, Object paramObject);

	public abstract int update(String paramString);

	public abstract int save(String paramString, Object paramObject);

	public abstract int save(String paramString);

	public abstract int save(SqlSessionTemplate paramSqlSessionTemplate,
			String paramString, Object paramObject);

	public abstract int delete(String paramString, Object paramObject);

	public abstract int delete(String paramString);

	public abstract int delete(SqlSessionTemplate paramSqlSessionTemplate,
			String paramString, Object paramObject);

	public abstract void batchInsert(
			SqlSessionTemplate paramSqlSessionTemplate, String paramString,
			List<?> paramList) throws DataAccessException;

	public abstract void batchInsert(String paramString, List<?> paramList)
			throws DataAccessException, SQLException;

	public abstract void batchUpdate(
			SqlSessionTemplate paramSqlSessionTemplate, String paramString,
			List<?> paramList) throws DataAccessException;

	public abstract void batchUpdate(String paramString, List<?> paramList)
			throws DataAccessException;

	public abstract void batchDelete(
			SqlSessionTemplate paramSqlSessionTemplate, String paramString,
			List<?> paramList) throws DataAccessException;

	public abstract void batchDelete(String paramString, List<?> paramList)
			throws DataAccessException;
}
