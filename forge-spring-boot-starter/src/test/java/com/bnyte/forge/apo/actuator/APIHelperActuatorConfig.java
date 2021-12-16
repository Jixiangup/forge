package com.bnyte.forge.apo.actuator;

import com.bnyte.forge.aop.actuator.APIHelperActuator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @auther bnyte
 * @date 2021-12-14 17:01
 * @email bnytezz@gmail.com
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan("com.bnyte.forge")
public class APIHelperActuatorConfig {
}
