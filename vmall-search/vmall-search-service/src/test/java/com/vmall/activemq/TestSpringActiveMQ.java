package com.vmall.activemq;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSpringActiveMQ {
    @Test
    public void testSpringActiveMQ() throws Exception {
        //初始化spring容器
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
        //等待
        System.in.read();
    }
}
