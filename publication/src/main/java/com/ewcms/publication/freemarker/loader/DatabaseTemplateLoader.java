/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.freemarker.loader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.ewcms.publication.dao.TemplateDaoable;
import com.ewcms.publication.module.TemplateBody;

import freemarker.cache.TemplateLoader;

/**
 * 加载数据库中的模板
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public class DatabaseTemplateLoader implements TemplateLoader{
    private static final Logger logger = LoggerFactory.getLogger(DatabaseTemplateLoader.class);
    
    private TemplateDaoable templateDao;
    
    public DatabaseTemplateLoader(TemplateDaoable templateDao){
        this.templateDao = templateDao;
    }
    
    @Override
    public Object findTemplateSource(String name) throws IOException {
        Assert.notNull(name,"template uniquepath is null");
        
        String path = StringUtils.removeStart(name, "/");
        TemplateBody body = templateDao.findBody(path);
        
        if(body == null){
            logger.debug("{} is not exist.",path);
            return null;
        }
        
        byte[] content = body.getBody();
        long lastTime = System.currentTimeMillis();
        
        return new TemplateSource(path,content,lastTime);
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) throws IOException {
        TemplateSource source = (TemplateSource)templateSource;
        try{
            return new InputStreamReader(new ByteArrayInputStream(source.source),encoding);
        }catch(IOException e){
            logger.debug("Could not find FreeMarker template:{} " + source.name);
            throw e;
        }
    }

    @Override
    public long getLastModified(Object templateSource) {
        return  ((TemplateSource)templateSource).lastModified;
    }
    
    @Override
    public void closeTemplateSource(Object templateSource) throws IOException {
        //Do nothing
    }
    
    private static class TemplateSource {
        private final String name;
        private final byte[] source;
        private final long lastModified;
        
        TemplateSource(String name, byte[] source, long lastModified) {
            if(name == null) {
                throw new IllegalArgumentException("name == null");
            }
            if(source == null) {
                throw new IllegalArgumentException("source == null");
            }
            if(lastModified < -1L) {
                throw new IllegalArgumentException("lastModified < -1L");
            }
            this.name = name;
            this.source = source;
            this.lastModified = lastModified;
        }
        
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof TemplateSource) {
                return name.equals(((TemplateSource)obj).name);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }
}
