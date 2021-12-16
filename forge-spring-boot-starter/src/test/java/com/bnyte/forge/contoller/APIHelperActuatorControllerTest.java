package com.bnyte.forge.contoller;

import com.bnyte.forge.annotation.APIHelper;
import com.bnyte.forge.aop.actuator.APIHelperActuator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @auther bnyte
 * @date 2021-12-14 11:43
 * @email bnytezz@gmail.com
 */
@Component
public class APIHelperActuatorControllerTest {

    @Autowired
    APIHelperActuator apiHelperActuator;

    @APIHelper
    public void test() {
        System.out.println(apiHelperActuator.getBody());
        System.out.println("这是测试");
    }
}
