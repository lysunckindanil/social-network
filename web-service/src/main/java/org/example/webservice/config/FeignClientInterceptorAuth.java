package org.example.webservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptorAuth implements RequestInterceptor {

    @Value("${API_AUTH_KEY}")
    private String API_AUTH_KEY;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Authorization", API_AUTH_KEY);
    }
}
