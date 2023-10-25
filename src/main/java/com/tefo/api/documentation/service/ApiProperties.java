package com.tefo.api.documentation.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "springdoc.swagger-ui")
public class ApiProperties {
    private List<ApiUrl> urls;

    public List<ApiUrl> getUrls() {
        return urls;
    }

    public void setUrls(List<ApiUrl> urls) {
        this.urls = urls;
    }
}
