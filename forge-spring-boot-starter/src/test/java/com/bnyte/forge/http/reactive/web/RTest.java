package com.bnyte.forge.http.reactive.web;

import org.junit.Test;

import static org.junit.Assert.*;

public class RTest {

    @Test
    public void testResponse() {
        R<String> message = R.error("").code(100).message("请求失败");
        System.out.println(message);
    }

}