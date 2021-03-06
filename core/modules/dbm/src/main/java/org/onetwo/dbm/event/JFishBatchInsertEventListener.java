package org.onetwo.dbm.event;


import java.util.List;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.dbm.exception.DbmException;
import org.onetwo.dbm.mapping.JFishMappedEntry;
import org.onetwo.dbm.mapping.JdbcStatementContext;

/*******
 * 和普通insert的区别只在，当实体的id策略是自增时，不会通过Statement#getGeneratedKeys接口获取数据库递增的id值和回写到实体的id
 * 只对mysql有效
 * @author wayshall
 *
 */
public class JFishBatchInsertEventListener extends JFishInsertEventListener{


	@Override
	public void onInsert(JFishInsertEvent event) {
		JFishMappedEntry entry = event.getEventSource().getMappedEntryManager().findEntry(event.getObject());
		if(entry==null){
			event.setUpdateCount(0);
			return ;
		}
		super.onInsert(event);
	}
	
	@Override
	protected void doInsert(JFishInsertEvent event, JFishMappedEntry entry) {
		Object entity = event.getObject();
		if(!LangUtils.isMultiple(entity)){
			throw new DbmException("batch insert's args must be a Collection or Array!");
		}
		JFishEventSource es = event.getEventSource();
		this.beforeDoInsert(event, entry);
		this.batchInsert(event, entry, es);
	}
	
	protected void batchInsert(JFishInsertEvent event, JFishMappedEntry entry, JFishEventSource es) {
		Object entity = event.getObject();
		
		JdbcStatementContext<List<Object[]>> insert = entry.makeInsert(entity);
		int total = this.executeJdbcUpdate(true, insert.getSql(), insert.getValue(), es);
		event.setUpdateCount(total);
	}

}
