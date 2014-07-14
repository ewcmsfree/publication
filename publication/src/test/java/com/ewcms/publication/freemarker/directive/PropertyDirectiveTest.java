/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.freemarker.directive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.ewcms.publication.ObjectBean;
import com.ewcms.publication.freemarker.FreemarkerTest;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * ObjectPropertyDirective单元测试
 * 
 * @author wangwei
 *
 */
public class PropertyDirectiveTest extends FreemarkerTest{

    @Override
    protected void currentConfiguration(Configuration cfg) {
        cfg.setSharedVariable("property", new PropertyDirective());
    }
    
    /**
     * 测试得到属性值
     * 
     * @throws Exception
     */
    @Test
    public void testGetValue()throws Exception{
        ObjectBean objectValue = new ObjectBean();
        objectValue.setId(1);
        objectValue.setReadOnly(true);
        
        ObjectBean parent = new ObjectBean();
        parent.setTitle("test");
        objectValue.setParent(parent);
        
        PropertyDirective directive = new PropertyDirective();
        Integer id = (Integer)directive.getValue(objectValue, "id");
        assertEquals(id,new Integer(1));
        Boolean readOnly = (Boolean)directive.getValue(objectValue, "readOnly");
        assertTrue(readOnly);
        
        String title = (String)directive.getValue(objectValue, "parent.title");
        assertEquals(title,"test");
    }
    
    /**
     * 得到模板路径
     * 
     * @param name 模板名
     * @return
     */
    private String getTemplatePath(String name){
        return String.format("directive/objectproperty/%s", name);
    }
    
    /**
     * 测试属性值标签输出
     * 
     * <p>通过参数名获得对象</p>
     * 
     * @throws Exception
     */
    @Test
    public void testVauleTemplate()throws Exception{
        Template template = cfg.getTemplate(getTemplatePath("value.html"));
        
        ObjectBean objectValue = new ObjectBean();
        objectValue.setTitle("test");
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("object", objectValue);
        
        List<ObjectBean> list = new ArrayList<ObjectBean>();
        objectValue = new ObjectBean();
        objectValue.setTitle("test");
        list.add(objectValue);
        objectValue = new ObjectBean();
        objectValue.setTitle("test1");
        list.add(objectValue);
        
        map.put("objects", list);
        
        String content = process(template, map);
        content =StringUtils.deleteWhitespace(content); 
        assertEquals(content,"test|testtest1");
    }
    
    /**
     * 测试属性值为空标签输出
     * 
     * @throws Exception
     */
    @Test
    public void testPropertyIsNullOfValueTemplate()throws Exception{
        Template template = cfg.getTemplate(getTemplatePath("value.html"));
        ObjectBean objectValue = new ObjectBean();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("object", objectValue);
        
        List<ObjectBean> list = new ArrayList<ObjectBean>();
        objectValue = new ObjectBean();
        objectValue.setTitle("test");
        list.add(objectValue);
        objectValue = new ObjectBean();
        objectValue.setTitle("test1");
        list.add(objectValue);
        
        map.put("objects", list);
        
        String content = process(template, map);
        content =StringUtils.deleteWhitespace(content); 
        assertEquals(content,"|testtest1");
    }
    
    /**
     * 测试标签异常，异常错误是否写入内容
     * 
     * @throws Exception
     */
    @Test
    public void testExceptionTemplate() throws Exception {
        Template template = cfg.getTemplate(getTemplatePath("exception.html"));

        ObjectBean objectValue = new ObjectBean();
        objectValue.setTitle("test");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("object", objectValue);

        String content = process(template, map);
        content = StringUtils.deleteWhitespace(content);
        Assert.assertEquals("throwsException", content);
    }

    /**
     * 测试标签返回值
     * 
     * @throws Exception
     */
    @Test
    public void testLoopTemplate()throws Exception{
        Template template = cfg.getTemplate(getTemplatePath("loop.html"));
        
        ObjectBean objectValue = new ObjectBean();
        objectValue.setTitle("test");
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("object", objectValue);
        
        String content = process(template, map);
        content = StringUtils.deleteWhitespace(content); 
        assertEquals(content,"testtest");
    }  
}
