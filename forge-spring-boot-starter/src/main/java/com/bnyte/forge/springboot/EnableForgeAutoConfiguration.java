package com.bnyte.forge.springboot;

import com.bnyte.forge.springboot.register.EnableForgeBeanRegister;
import com.bnyte.forge.springboot.scanner.EnableForgeScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @auther bnyte
 * @date 2021-12-04 21:21
 * @email bnytezz@gmail.com
 */
@Configuration
//@EnableConfigurationProperties({ForestConfigurationProperties.class})
@Import({EnableForgeScanner.class})
@ComponentScan("com.bnyte.forge.aop")
public class EnableForgeAutoConfiguration {

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Bean
    EnableForgeBeanRegister enableForgeRegister() {
        EnableForgeBeanRegister forgeRegister = new EnableForgeBeanRegister(applicationContext);
        forgeRegister.registerScanner();
        return forgeRegister;
    }

}
