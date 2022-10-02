//package org.novinomad.picasso.configurations;
//
//import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@EnableWebMvc
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
////        WebMvcConfigurer.super.addResourceHandlers(registry);
//
//        registry
//                .addResourceHandler("/webjars/**")
//                .addResourceLocations("/webjars/");
//    }
//
//    @Bean
//    public LayoutDialect layoutDialect() {
//        return new LayoutDialect();
//    }
////    @Bean
////    public LocaleResolver localeResolver() {
////        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
////        sessionLocaleResolver.setDefaultLocale(Locale.ENGLISH);
////
////        return sessionLocaleResolver;
////    }
////
////    @Bean
////    public LocaleChangeInterceptor localeChangeInterceptor() {
////        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
////        localeChangeInterceptor.setParamName("lang");
////        return localeChangeInterceptor;
////    }
////
////    @Override
////    public void addInterceptors(InterceptorRegistry registry) {
////        WebMvcConfigurer.super.addInterceptors(registry);
////        registry.addInterceptor(localeChangeInterceptor());
////    }
//}
