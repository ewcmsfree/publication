/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.filter.render;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewcms.publication.dao.TemplateSourceDaoable;
import com.ewcms.publication.module.TemplateSource;

/**
 * 返回指定的模版资源
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public class TemplateSourceRender  extends AbstractResourceRender{
    private static final Logger logger = LoggerFactory.getLogger(TemplateSourceRender.class);
    
    private TemplateSourceDaoable sourceDao;
    
    public TemplateSourceRender(TemplateSourceDaoable sourceDao){
        this.sourceDao = sourceDao;
    }
    
   
    
    /**
     * 输出模版资源
     * 
     * @param response
     * @param uri   资源路基
     * @return
     * @throws IOException
     */
    protected boolean output(HttpServletResponse response,Integer siteId, String uri)throws IOException{
        TemplateSource source = sourceDao.findByUri(siteId, uri);
        if(source == null){
            logger.debug("TemplateSource is not exist,uri is {}",uri);
            return false;
        }
       IOUtils.write(source.getContent(), response.getOutputStream());
       response.flushBuffer();
       return true;
    }
}
