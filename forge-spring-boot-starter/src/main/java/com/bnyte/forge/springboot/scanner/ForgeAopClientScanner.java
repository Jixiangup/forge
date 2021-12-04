package com.bnyte.forge.springboot.scanner;

import com.bnyte.forge.aop.actuator.APIHelperActuator;
import com.bnyte.forge.springboot.filter.exclude.EnableForgeExclude;
import com.bnyte.forge.springboot.filter.include.EnableForgeInclude;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Arrays;
import java.util.Set;

/**
 * @auther bnyte
 * @date 2021-12-04 21:57
 * @email bnytezz@gmail.com
 */
public class ForgeAopClientScanner extends ClassPathBeanDefinitionScanner {

    private BeanDefinitionRegistry registry;

    ApplicationContext applicationContext;


    public ForgeAopClientScanner(BeanDefinitionRegistry registry, ApplicationContext applicationContext) {
        super(registry, false);
        this.applicationContext = applicationContext;
        if (this.registry == null) setRegistry(registry);
        registerFilters(); // 告诉Spring我们需要注入那些类
    }

    public void setRegistry(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    /**
     * 重写过滤条件
     */
    public void registerFilters() {
        // 排除注册
        addExcludeFilter(new EnableForgeExclude());

        // 添加注册
//        addIncludeFilter(new EnableForgeInclude()); 开启之后没有携带@Aspect注解也会被注入
        addIncludeFilter(new EnableForgeInclude());

    }

    /**
     * 重写扫描逻辑
     * @param basePackages 请求接口类所在的包路径，只能是第一层的包，不包含子包
     * @return BeanDefinitionHolder实例集合
     */
    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        System.out.println(applicationContext.containsBean(APIHelperActuator.class.getSimpleName()));
        // 扫描完成之后我们获得了所有需要注册的类
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        System.out.println(applicationContext.containsBean(APIHelperActuator.class.getSimpleName()));
        for (BeanDefinitionHolder beanDefinition : beanDefinitions) {
            // 该段该bean是否已经被注册，如果已经被注册则自动不注册

//            registerProxy(registry, beanDefinition);
        }

        if (beanDefinitions.isEmpty()) {
            logger.warn("[Forge] No Forge client is found in package '" + Arrays.toString(basePackages) + "'.");
        }
        processBeanDefinitions(beanDefinitions);
        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();

            if (logger.isDebugEnabled()) {
                logger.debug("[Repost] Creating Forest Client Bean with name '" + holder.getBeanName()
                        + "' and Proxy of '" + definition.getBeanClassName() + "' client interface");
            }

            String beanClassName = definition.getBeanClassName();

            logger.info("[Repost] Created Repost Client Bean with name '" + holder.getBeanName()
                    + "' and Proxy of '" + beanClassName + "' client interface");

        }
    }


    public void registerProxy(BeanDefinitionRegistry registry, BeanDefinitionHolder beanDefinitionHolder) {
        String beanName = beanDefinitionHolder.getBeanName();
        String beanClassName = beanDefinitionHolder.getBeanDefinition().getBeanClassName();
        Class<?> clazz = null;
        try {
            clazz = Class.forName(beanClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        beanDefinition.setBeanClass(clazz);
        // 使用RepostFactoryBean类的构造器进行实例化
//        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(clazz); // 代理对象需要这里不需要
        // 将对象注入到IOC容器当中
        if (!registry.isBeanNameInUse(beanName))
            registry.registerBeanDefinition(beanName, beanDefinition);
    }

    /**
     * 过滤没必要的类
     */
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        // 如果为真则会扫描所有，包括类，但是类会抛出异常，也就是动态代理失败
//        if (repostProperties.isAllInterfaces()) {
//            return true;
//        } else {
        return true;
//        }
    }
}
