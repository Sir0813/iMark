package com.dm.user.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketConfig {

    /**
     * 用于扫描和注册所有携带ServerEndPoint注解的实例。
     * <p>
     * PS:若部署到外部容器 则无需提供此类。
     */
    /*@Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }*/
}
