package com.dm.user.config;

import com.dm.app.tid.sdk.service.TIDService;
import com.dm.app.tid.sdk.service.impl.TIDServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TidConfig {

    @Bean
    public TIDService tidService() {
        return new TIDServiceImpl();
    }

}
