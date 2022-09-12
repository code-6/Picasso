package org.novinomad.picasso.commons.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(value = false)
public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext springApplicationContext;

    public static <T extends Object> T getBean(Class<T> beanClass) {
        return springApplicationContext.getBean(beanClass);
    }

    public static <T extends Object> T getBean(Class<T> beanClass, Object ... args) {
        return springApplicationContext.getBean(beanClass, args);
    }

    public static <T extends Object> T value(String propertyKey, Class<T> valueType) {
        return springApplicationContext.getEnvironment().getProperty(propertyKey, valueType);
    }

    public static String value(String propertyKey) {
        return springApplicationContext.getEnvironment().getProperty(propertyKey);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        springApplicationContext = applicationContext;
    }
}
