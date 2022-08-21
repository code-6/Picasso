package org.novinomad.picasso.commons.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext springApplicationContext;

    public static <T extends Object> T getBean(Class<T> beanClass) {
        return springApplicationContext.getBean(beanClass);
    }

    public static <T extends Object> T getBean(Class<T> beanClass, Object ... args) {
        return springApplicationContext.getBean(beanClass, args);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        springApplicationContext = applicationContext;
    }
}
