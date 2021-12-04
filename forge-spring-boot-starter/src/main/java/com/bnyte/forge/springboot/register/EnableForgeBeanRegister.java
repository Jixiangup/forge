package com.bnyte.forge.springboot.register;

import com.bnyte.forge.springboot.scanner.EnableForgeScanner;
import com.bnyte.forge.springboot.scanner.ForgeAopClientScanner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * EnableForge注解自动装配，扫描包路径中的所有携带@Aspect注解的类
 * @auther bnyte
 * @date 2021-12-04 21:23
 * @email bnytezz@gmail.com
 */
public class EnableForgeBeanRegister implements ResourceLoaderAware, BeanPostProcessor {

    private ResourceLoader resourceLoader;
    private BeanDefinitionRegistry beanFactory;

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    public EnableForgeBeanRegister(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.beanFactory = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public ForgeAopClientScanner registerScanner() {
        List<String> basePackages = EnableForgeScanner.basePackages;

        // 处理需要扫描的类以及需要排除的类，即为过滤器以及加载器
        ForgeAopClientScanner scanner = new ForgeAopClientScanner(beanFactory, applicationContext);

        if (resourceLoader != null) {
            scanner.setResourceLoader(resourceLoader);
        }

        if (CollectionUtils.isEmpty(basePackages)) {
            return scanner;
        }

        // 执行扫描
        scanner.doScan(StringUtils.toStringArray(basePackages));
        return scanner;
    }
}
