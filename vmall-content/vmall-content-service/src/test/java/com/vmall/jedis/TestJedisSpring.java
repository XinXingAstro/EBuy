package com.vmall.jedis;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestJedisSpring {
    @Test
    public void testJedisClientPool() throws Exception {
        //初始化spring容器，由于只加载了applicationContext-redis.xml这一个配置文件，而这个配置文件中没有开启注解，所以对象无法注入，所以要在这个文件中开启注解
        //如果加载多个配置文件，只要有一个配置文件中开启了注解，所有的spring容器中的对象就可以注入了
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
        //从容器中获得JedisClient对象
        JedisClient jedisClient = context.getBean(JedisClient.class);
        //使用JedisClient对象操作redis
//        jedisClient.set("jedisclient2", "mytest2");
        String result = jedisClient.get("jedisclient2");
        System.out.println(result);
    }
}
