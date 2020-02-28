package com.zjg.blog;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogApplicationTests {
    @Test
    public void contextLoad(){

    }
    @Before
    public void init(){
        System.out.println("--------开始测试---------");
    }
    @After
    public void after(){
        System.out.println("--------结束测试--------");
    }
}
