package com.dm.user.config;

import com.dm.app.did.sdk.service.DataService;
import com.dm.app.did.sdk.service.impl.DataServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DidConfig {

    @Bean
    public DataService didService() {
        return new DataServiceImpl();
    }

}
