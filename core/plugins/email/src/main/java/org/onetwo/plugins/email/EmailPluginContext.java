package org.onetwo.plugins.email;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import javax.annotation.Resource;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.propconf.AppConfig;
import org.slf4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

@Configuration
public class EmailPluginContext implements InitializingBean {

	public static final String JAVA_MAIL_PROPERTIES_KEY = "javaMailProperties.";
	
	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());

	@Resource
	private AppConfig appConfig;
	
	@Resource
	private Properties mailConfig;
	
	@Resource
	private freemarker.template.Configuration mailFreemarkerConfiguration;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(appConfig, "appConfig must not be null!");
	}

	@Bean
	public JavaMailServiceImpl JavaMailServiceImpl() throws Exception{
		JavaMailServiceImpl jm = new JavaMailServiceImpl();
		jm.setJavaMailSender(javaMailSender());
		jm.setConfiguration(mailFreemarkerConfiguration);
		return jm;
	}
	
	@Bean
	public JavaMailSender javaMailSender() throws IOException {
		Properties javaMailProperties = new Properties();
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		Enumeration<String> names = (Enumeration<String>) mailConfig.propertyNames();
		BeanWrapper bw = SpringUtils.newBeanWrapper(sender);
		while (names.hasMoreElements()) {
			String propertyName = names.nextElement();
			String value = mailConfig.getProperty(propertyName);
			if (propertyName.startsWith(JAVA_MAIL_PROPERTIES_KEY)) {
				propertyName = propertyName.substring(JAVA_MAIL_PROPERTIES_KEY.length());
//				LangUtils.println("mail config : ${0}, ${1}", propertyName, value);
				javaMailProperties.setProperty(propertyName, value);
			} else {
				bw.setPropertyValue(propertyName, value);
			}
		}
		sender.setJavaMailProperties(javaMailProperties);
		return sender;
	}

	@Bean
	public PropertiesFactoryBean mailConfig() {
		String envLocation = "/email/mailconfig-" + appConfig.getAppEnvironment() + ".properties";
		PropertiesFactoryBean pfb = new PropertiesFactoryBean();
		pfb.setIgnoreResourceNotFound(true);
		pfb.setLocations(new org.springframework.core.io.Resource[] {
				SpringUtils.classpath("/email/mailconfig.properties"),
				SpringUtils.classpath(envLocation) });
		return pfb;
	}
	
	@Bean
	public FreeMarkerConfigurationFactoryBean mailFreemarkerConfiguration(){
		FreeMarkerConfigurationFactoryBean fcfb = new FreeMarkerConfigurationFactoryBean();
		fcfb.setTemplateLoaderPath("classpath:/email/ftl/");
		return fcfb;
	}

}