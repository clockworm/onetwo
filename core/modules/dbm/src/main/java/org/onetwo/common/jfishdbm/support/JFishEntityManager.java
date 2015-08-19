package org.onetwo.common.jfishdbm.support;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.jfishdbm.query.JFishQueryBuilder;

public interface JFishEntityManager extends BaseEntityManager {
	
	
//	public <T> Page<T> findPageByQName(String queryName, RowMapper<T> rowMapper, Page<T> page, Object... params);
	
	public int removeAll(Class<?> entityClass);
	
	public JFishDaoImplementor getJfishDao();
	
	public JFishQueryBuilder createQueryBuilder(Class<?> entityClass);
	
}
