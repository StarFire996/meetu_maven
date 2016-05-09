package com.meetu.core.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SqlHolder {
	private String _sql;
	private List<Object> o00000 = new ArrayList();

	public void addParam(Object paramObject) {
		o00000.add(paramObject);
	}

	public Object[] getParams() {
		return o00000.toArray();
	}

	public String getSql() {
		return _sql;
	}

	public void setSql(String paramString) {
		_sql = paramString;
	}

	public String toString() {
		StringBuilder localStringBuilder = new StringBuilder("sql\u8BED\u53E5:");
		localStringBuilder.append(_sql).append("\r\n")
				.append("             \u53C2\u6570\u503C:");
		Iterator localIterator = o00000.iterator();
		while (localIterator.hasNext()) {
			Object localObject = localIterator.next();
			localStringBuilder.append(localObject).append(",");
		}
		if (o00000.size() > 0)
			localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
		return localStringBuilder.toString();
	}
}
