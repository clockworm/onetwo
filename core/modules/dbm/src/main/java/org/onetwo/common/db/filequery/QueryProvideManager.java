package org.onetwo.common.db.filequery;

import org.onetwo.common.db.DataBase;
import org.onetwo.common.db.DataQuery;
import org.onetwo.dbm.mapping.DbmTypeMapping;

public interface QueryProvideManager {

	public DataQuery createSQLQuery(String sqlString, Class<?> entityClass);
	public DataQuery createQuery(String ejbqlString);
	public FileNamedQueryManager getFileNamedQueryManager();
	
	public SqlParamterPostfixFunctionRegistry getSqlParamterPostfixFunctionRegistry();
	
	public DataBase getDataBase();
	
	public DbmTypeMapping getSqlTypeMapping();
}
