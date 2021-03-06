package org.onetwo.ext.security;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.onetwo.ext.security.url.SecurityImportSelector;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Documented
@Import({SecurityImportSelector.class })
@Configuration
public @interface EnableSecurity {
	
	InterceptMode mode() default InterceptMode.URL;
	Class<?> configClass() default Object.class;
	boolean enableJavaStylePermissionManage() default true;
	
	public static enum InterceptMode {
		URL,
		METHOD,
		CUSTOM
	}
}
