package com.bnyte.forge.springboot.scanner;

import com.bnyte.forge.springboot.annotation.EnableForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * EnableForge注解自动装配,获取需要自动装配的包名
 * @auther bnyte
 * @date 2021-12-04 21:23
 * @email bnytezz@gmail.com
 */
public class EnableForgeScanner implements BeanFactoryAware, ImportBeanDefinitionRegistrar {

    private static final Logger log = LoggerFactory.getLogger(EnableForgeScanner.class);

    /**
     * 所要扫描的所有包名
     */
    public static List<String> basePackages = new ArrayList<>();

    /**
     * EnableForge全局配置ID
     */
    private static String configurationId;

    private BeanFactory beanFactory;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EnableForge.class.getName());

        if (annotationAttributes != null) {
            AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(annotationAttributes);

            Arrays.stream(annoAttrs.getStringArray("value")).forEach(pkg -> {
                if (StringUtils.hasText(pkg)) {
                    log.debug("[com.bnyte.forge] enable scanner 'value' to " + pkg);
                    basePackages.add(pkg);
                }
            });

            Arrays.stream(annoAttrs.getStringArray("basePackages")).forEach(pkg -> {
                if (StringUtils.hasText(pkg)) {
                    log.debug("[com.bnyte.forge] enable scanner 'basePackages' to " + pkg);
                    basePackages.add(pkg);
                }
            });

            Arrays.stream(annoAttrs.getClassArray("basePackageClasses")).forEach(clazz -> {
                log.debug("[com.bnyte.forge] enable scanner 'basePackageClasses' to " + clazz.getPackageName());
                basePackages.add(clazz.getPackageName());
            });

            Arrays.stream(annoAttrs.getStringArray("defaultPackages")).forEach(pkg -> {
                if (StringUtils.hasText(pkg)) {
                    log.debug("[com.bnyte.forge] enable scanner 'defaultPackages' to " + pkg);
                    basePackages.add(pkg);
                }
            });
//            configurationId = annoAttrs.getString("configuration");
            log.debug("[com.bnyte.forge] enable scanner to " + basePackages);

        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
