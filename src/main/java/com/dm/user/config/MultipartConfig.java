package com.dm.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class MultipartConfig {

	@Bean
    public MultipartResolver multipartResolver(){
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        //resolveLazily属性启用是为了推迟文件解析，自定义捕获文件大小异常
        resolver.setMaxUploadSize(524288000l);//1MB=1048576字节(b) 500M
        resolver.setMaxUploadSizePerFile(52428800l);
        resolver.setResolveLazily(true);
        return resolver;
    }
	
}
