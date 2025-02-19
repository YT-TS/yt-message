package com.yt.message.api.server.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BeanLoaderUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    public static <T> T getSpringBean(String beanName, Class<T> clazz) {
        Object obj = applicationContext.getBean(beanName);
        return clazz.cast(obj);
    }

    public static <T> T getSpringBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> Map<String, T> getSpringBeansOfType(Class<T> clazz) {
        return applicationContext.getBeansOfType(clazz);
    }

    public static <T> Map<String, T> getSpringBeansOfType(ApplicationContext applicationContext, Class<T> clazz) {
        return applicationContext.getBeansOfType(clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)  {
        BeanLoaderUtils.applicationContext = applicationContext;
    }

}
