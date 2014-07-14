/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.filter.render;

import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewcms.publication.dao.ResourceDaoable;
import com.ewcms.publication.module.Resource;

/**
 * 返回指定的资源
 * 
 * @author wangwei
 */
public class ResourceRender extends AbstractResourceRender{
    private static final Logger logger = LoggerFactory.getLogger(ResourceRender.class);
    
    private ResourceDaoable resourceDao;
    
    public ResourceRender(ResourceDaoable resourceDao){
        this.resourceDao = resourceDao;
    }
    
 
    
    /**
     * 输出站点资源
     * 
     * @param response
     * @param uri   资源路基
     * @return
     * @throws IOException
     */
    @Override
    protected boolean output(HttpServletResponse response, Integer siteId, String uri)throws IOException{
        Resource resource = resourceDao.findByUri(siteId, uri);
        if(resource == null){
            logger.debug("Resource is not exist,uri is {}",uri);
            return false;
        }
        String realPath = resource.getPath();
        try{
        	IOUtils.copy(new FileInputStream(realPath), response.getOutputStream());
        }catch(Exception e){
        	logger.warn("Resource is not exit,real path is{}", realPath);
        }
        response.flushBuffer();
        
        return true;
    }
}
