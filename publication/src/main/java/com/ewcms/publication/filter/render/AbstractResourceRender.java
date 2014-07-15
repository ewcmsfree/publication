/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.filter.render;

import static com.ewcms.publication.contoller.PreviewContoller.SESSION_CURRENT_SITE_ID;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 返回资源
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public abstract class AbstractResourceRender implements Renderable {
    private static final Logger logger = LoggerFactory.getLogger(AbstractResourceRender.class);

    protected String getPath(HttpServletRequest request) {

        String uri = request.getRequestURI();
        logger.debug("404's uri is {}", uri);
        String contextPath = request.getContextPath();
        logger.debug("ContextPath is {}", contextPath);

        return uri.replace(contextPath, "");
    }
    
    protected Long getCurrentSiteId(HttpServletRequest request){
    	Long siteId =(Long)request.getSession().getAttribute(SESSION_CURRENT_SITE_ID);
    	return siteId == null ? Long.MIN_VALUE : siteId;
    }
    
    /**
     * 输出指定的资源
     * 
     * @param response 
     * @param siteId 站点编号
     * @param uri  资源地址
     * @return true 返回资源 
     * @throws IOException
     */
    protected abstract boolean output(HttpServletResponse response,Long siteId, String uri)throws IOException;
        
    @Override
    public boolean render(HttpServletRequest request,HttpServletResponse response)throws IOException {
    	Long siteId = getCurrentSiteId(request);
        if( siteId == null){
            return false;
        }
        String uri = getPath(request);
        logger.debug("Resource path is {}",uri);
        return output(response, siteId, uri);
    }
}
