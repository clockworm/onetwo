package org.onetwo.boot.module.security.url;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.onetwo.boot.module.permission.BootPermissionContextConfig;
import org.onetwo.ext.security.EnableSecurity;
import org.onetwo.ext.security.EnableSecurity.InterceptMode;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Documented
//@Import({ BootUrlBasedSecurityConfig.class })
@Import({BootPermissionContextConfig.class})
@EnableSecurity(mode=InterceptMode.CUSTOM, configClass=BootUrlBasedSecurityConfig.class, enableJavaStylePermissionManage=false)
@Configuration
public @interface EnableUrlSecurity {
}
