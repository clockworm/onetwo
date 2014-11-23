package org.onetwo.app.taskserver;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.propconf.AppConfig;
import org.slf4j.Logger;

public class TaskServerConfig extends AppConfig { 

	protected static final Logger logger = MyLoggerFactory.getLogger(TaskServerConfig.class);

	private static final TaskServerConfig instance = new TaskServerConfig();
	
	public static TaskServerConfig getInstance() {
		return instance;
	}

	protected TaskServerConfig() {
		super("siteConfig.properties");
	}
	
	/***
	 * -1 is unlimited
	 * @return
	 */
	public boolean isLimitedQueue(){
		return getQueueMaxSize()>0;
	}
	
	public int getQueueMaxSize(){
		return getInt("queue.max.size", -1);
	}
	
	public int getFetchSize(){
		return getInt("fetch.size", 1000);
	}
	
	public String getLocalAttachmentDir(){
		return getProperty("local.attachement.dir", System.getProperty("java.io.tmpdir")+"/task-local-attachment");
	}
}
