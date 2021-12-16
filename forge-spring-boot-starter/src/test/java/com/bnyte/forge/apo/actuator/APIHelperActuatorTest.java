package com.bnyte.forge.apo.actuator;

import com.bnyte.forge.aop.actuator.APIHelperActuator;
import com.bnyte.forge.contoller.APIHelperActuatorControllerTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @auther bnyte
 * @date 2021-12-14 16:54
 * @email bnytezz@gmail.com
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class APIHelperActuatorTest {

    @Resource
    APIHelperActuatorControllerTest apiHelperActuatorControllerTest;

    @Test
    public void test() {
        apiHelperActuatorControllerTest.test();
    }

    @Configuration
    @Import(APIHelperActuator.class)
    static class Config{}



}
