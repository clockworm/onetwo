package org.onetwo.boot.core.config;

import java.util.Properties;

import lombok.Data;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;


/***
 * 某些专属jfish的配置
 * 一般用于增强配置framework
 * @author way
 *
 */
@ConfigurationProperties(prefix="jfish")
@Data
public class BootJFishConfig {
	public static final String ENABLE_JFISH_AUTO_CONFIG = "jfish.autoConfig";
	public static final String VALUE_AUTO_CONFIG_WEB_UI = "web-ui";
	public static final String VALUE_AUTO_CONFIG_WEB_MS = "web-ms";
	
//	public static final String ENABLE_NEGOTIATING_VIEW = "negotiating-view";
	
	@Autowired
	private BootSpringConfig bootSpringConfig;
	
//	private String ftlDir = "/jfish/ftl";
	private MessageSourceConfig messageSource = new MessageSourceConfig();
	
//	private DefaultDataBaseConfig dbm = new DefaultDataBaseConfig();
	
	private MvcConfig mvc = new MvcConfig();
	/*private JsonConfig json = new JsonConfig();
	private Properties mediaTypes;*/
	
	//security=BootSecurityConfig
	
	private boolean profile;
	/***
	 * default is web
	 * option: web, ms
	 */
	private String autoConfig;
	
	@Data
	public class MessageSourceConfig {
		private int cacheSeconds = -1;//cache forever
	}
	
	public class MvcConfig {
		@Setter
		private Properties mediaTypes;
		@Setter
		private JsonConfig json = new JsonConfig();

		public MvcConfig() {
			this.mediaTypes = new Properties();
			this.mediaTypes.put("json", "application/json");
			this.mediaTypes.put("xml", "application/xml");
			this.mediaTypes.put("xls", "application/vnd.ms-excel");
		}

		@Data
		public class JsonConfig {
			private Boolean prettyPrint;
			
			public boolean isPrettyPrint(){
				 if(prettyPrint==null)
					 return !bootSpringConfig.isProduct();
				 else
					 return prettyPrint;
			}
		}

		public Properties getMediaTypes() {
			return mediaTypes;
		}

		public JsonConfig getJson() {
			return json;
		}

	}
	
	
	
}
