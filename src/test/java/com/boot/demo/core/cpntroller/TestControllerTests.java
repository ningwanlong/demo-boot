package com.boot.demo.core.cpntroller;

import com.boot.demo.core.controller.TestController;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestControllerTests {

    private static TestController testController;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
       /* ClassPathXmlApplicationContext ct=new ClassPathXmlApplicationContext("spring-base.xml");//取到系统核心配置文件
        testController=ct.getBean("com.boot.demo.core.controller.TestController",TestController.class);//从bean工程获取接口bean*/


    }

    //调用接口
    @Test
    public void test(){
        try {
            String result = testController.test(1);
            System.out.println("测试结果：" + result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}