/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.freemarker.directive;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import com.ewcms.publication.dao.ArticleDaoable;
import com.ewcms.publication.freemarker.GlobalVariable;
import com.ewcms.publication.freemarker.directive.out.DateDirectiveOut;
import com.ewcms.publication.freemarker.directive.out.DefaultDirectiveOut;
import com.ewcms.publication.freemarker.directive.out.DirectiveOutable;
import com.ewcms.publication.freemarker.directive.out.LengthDirectiveOut;
import com.ewcms.publication.freemarker.directive.out.article.RelationsDirectiveOut;
import com.ewcms.publication.module.Article;

/**
 * 文章属性标签
 * 
 * @author wangwei
 */
public class ArticleDirective extends ChannelDirective{
	
	private final ArticleDaoable articleDao;
	
	public ArticleDirective(ArticleDaoable articleDao){
		this.articleDao = articleDao;
	}
	
    @Override
    protected String defaultValueParamValue(){
        return GlobalVariable.ARTICLE.getVariable();
    }
    
    @Override
    protected Object getValue(Object objectValue,String property)throws NoSuchMethodException{
        try{
            return PropertyUtils.getProperty(objectValue, property);    
        }catch(NoSuchMethodException e){
        	return getAgainValue(objectValue, property);
        }catch(Exception e){
            throw new NoSuchMethodException(e.getMessage());
        }
    }
    
    /**
     * ArticleInfo对象中没有属性，再次从Article中得到属性
     * 
     * @param objectValue 值对象
     * @param property 属性名
     * @return
     * @throws NoSuchMethodException
     */
    private Object getAgainValue(Object objectValue,String property)throws NoSuchMethodException{
    	try {
			Long id  = (Long)PropertyUtils.getProperty(objectValue, "id");
			List<Article> list = articleDao.findPrePublish(Arrays.asList(id));
			Object value = null;
			if(!list.isEmpty()){
				value = PropertyUtils.getProperty(list.get(0), property);
			}
			return value;
		} catch (Exception e) {
			 throw new NoSuchMethodException(e.getMessage());
		}
    }
    
    /**
     * 初始化缺省别名和属性名对应表
     * 
     * @return
     */
    protected Map<String,String> initDefaultAliasProperties(){
        Map<String,String> map = new HashMap<String,String>();
        
        map.put("编号", "id");
        map.put("id", "id");
        
        map.put("标题", "title");
        map.put("title", "title");
        
        map.put("标题样式", "titleStyle");
        map.put("titleStyle", "titleStyle");
        
        map.put("短标题", "shortTitle");
        map.put("shortTitle", "shortTitle");
        
        map.put("短标题样式", "shortTitleStyle");
        map.put("shortTitleStyle", "shortTitleStyle");
        
        map.put("副标题", "subTitle");
        map.put("subTitle", "subTitle");
        
        map.put("副标题样式", "subTitleStyle");
        map.put("subTitleStyle", "subTitleStyle");
        
        map.put("作者", "author");
        map.put("author", "author");
        
        map.put("引导图", "image");
        map.put("image", "image");
        
        map.put("摘要", "summary");
        map.put("summary", "summary");
        
        map.put("来源", "origin");
        map.put("origin", "origin");
        
        map.put("关键字", "keyword");
        map.put("keyword", "keyword");
        
        map.put("标签", "tag");
        map.put("tag", "tag");
        
        map.put("链接地址", "url");
        map.put("url", "url");
        
        map.put("关联文章","relations");
        map.put("relations","relations");
        
        map.put("正文", "content");
        map.put("content", "content");
        map.put("contents", "content");
        
        map.put("创建时间", "createTime");
        map.put("createTime", "createTime");
        
        map.put("修改时间", "modified");
        map.put("modified", "modified");
        map.put("modifiedTime", "modified");
        
        map.put("发布时间", "published");
        map.put("published", "published");
        map.put("publishedTime", "published");
                
        return map;
    }
    
    /**
     * 初始化属性标签输出
     * 
     * @return
     */
    protected Map<String,DirectiveOutable> initDefaultPropertyDirectiveOuts(){
        Map<String,DirectiveOutable> map = new HashMap<String,DirectiveOutable>();
        
        map.put("id", new DefaultDirectiveOut());
        map.put("title", new LengthDirectiveOut());
        map.put("titleStyle", new DefaultDirectiveOut());
        map.put("shortTitle", new LengthDirectiveOut());
        map.put("shortTitleStyle", new DefaultDirectiveOut());
        map.put("subTitle", new LengthDirectiveOut());
        map.put("subTitleStyle", new DefaultDirectiveOut());
        map.put("author", new LengthDirectiveOut());
        map.put("auditReal", new LengthDirectiveOut());
        map.put("image", new DefaultDirectiveOut());
        map.put("summary", new LengthDirectiveOut());
        map.put("origin", new LengthDirectiveOut());
        map.put("keyword", new LengthDirectiveOut());
        map.put("tag", new LengthDirectiveOut());
        map.put("url", new DefaultDirectiveOut());
        map.put("relations", new RelationsDirectiveOut(articleDao));
        map.put("content",new DefaultDirectiveOut());
        map.put("createTime", new DateDirectiveOut());
        map.put("modified", new DateDirectiveOut());
        map.put("published", new DateDirectiveOut());
        
        return map;
    }
}
