package org.onetwo.boot.core.ms;

import org.onetwo.boot.core.BootContextConfig;
import org.onetwo.boot.core.BootWebCommonAutoConfig;
import org.onetwo.boot.core.config.BootBusinessConfig;
import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.config.BootSpringConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.HttpEncodingProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties({HttpEncodingProperties.class, BootJFishConfig.class, BootSpringConfig.class, BootBusinessConfig.class, BootSiteConfig.class})
@Import({BootContextConfig.class})
//@Import({BootContextConfig.class})
@ConditionalOnProperty(name=BootJFishConfig.ENABLE_JFISH_AUTO_CONFIG, havingValue=BootJFishConfig.VALUE_AUTO_CONFIG_WEB_MS)
public class BootMSContextAutoConfig extends BootWebCommonAutoConfig {
	
	public BootMSContextAutoConfig(){
		System.out.println("BootMSContextAutoConfig init");
	}

}
