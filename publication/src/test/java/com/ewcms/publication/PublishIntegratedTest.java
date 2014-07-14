/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 发布服务测试，该测试主要测试集成后发布是否成功和性能。
 * 
 * 该测试涉及到多线程测试，而junit对多线程测试不支持，所以使用了应用程序测试。
 * 
 * @author wangwei
 */
public class PublishIntegratedTest {
    private final static Logger logger = LoggerFactory.getLogger(PublishIntegratedTest.class);
    
    private final static ApplicationContext context;
    private  static String SPACE;
    
    static{
        context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        SPACE = "";
        for(int i = 0 ; i < 10 ; i ++){
            SPACE = SPACE + "----------";    
        }
    }
    
    private PublishServiceable getPublishService(){
        return (PublishServiceable)context.getBean("publishService");
    }
    
    public void runPublishSite()throws Exception{
//    	Thread.sleep(30 * 1000);
    	PublishServiceable publishService = getPublishService();
    	DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	long start = System.currentTimeMillis();
    	logger.info("Test publish start time {}", format.format(start));
//    	publishService.pubChannel(1, 809, false, true);
//    	publishService.pubTemplateSource(1, true);
//    	publishService.pubResource(1, true);
    	publishService.pubSite(-2, true);
    	long end = System.currentTimeMillis();
    	logger.info("Test publis end time {},Running time is {}",format.format(end),end - start);
    }

    public static void main(String[] args){
        try{
        	//Thread.sleep(60 * 1000);
            PublishIntegratedTest test = new PublishIntegratedTest();
            test.runPublishSite();
        }catch(Exception e){
            logger.error(e.getMessage());
        }
    }
    
    @Test
    public void testPublish(){
        logger.info("测试程序使用了多线程，请使用main运行方式。");
    }
}
