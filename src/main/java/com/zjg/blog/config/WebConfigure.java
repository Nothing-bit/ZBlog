package com.zjg.blog.config;

import com.zjg.blog.interceptor.AdminInterceptor;
import com.zjg.blog.interceptor.ForeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
@Configuration
public class WebConfigure implements WebMvcConfigurer {
    @Value("${images.upload.path}")
    private String picturePath;
    @Value("${images.upload.path.relative}")
    private String picturePathRelative;

    @Autowired
    private AdminInterceptor adminInterceptor;
    @Autowired
    private ForeInterceptor foreInterceptor;
    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        return converter;
    }
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦
        registry.addInterceptor(adminInterceptor).addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login/page")
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/admin/assets/**");
        registry.addInterceptor(foreInterceptor).addPathPatterns("/fore/**")
                .excludePathPatterns("/fore/assets/**");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(picturePathRelative)
                .addResourceLocations("file:/"+picturePath);
    }
}
