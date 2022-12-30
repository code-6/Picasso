//package org.novinomad.picasso.configurations;
//
//import org.springframework.cache.Cache;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
//import org.springframework.cache.interceptor.CachePutOperation;
//import org.springframework.cache.interceptor.CacheResolver;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@EnableCaching
//public class CacheConfig {
//
//    @Bean(value = "tourCacheManager")
//    public CacheManager tourCacheManager() {
//        ConcurrentMapCacheManager allToursCache = new ConcurrentMapCacheManager("allToursCache");
//
//        return allToursCache;
//    }
//}
