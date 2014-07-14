package com.ewcms.publication.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.ewcms.publication.dao.ResourceDaoable;
import com.ewcms.publication.dao.TemplateSourceDaoable;
import com.ewcms.publication.filter.render.Renderable;
import com.ewcms.publication.filter.render.ResourceRender;
import com.ewcms.publication.filter.render.TemplateSourceRender;

public class Resource404Filter implements Filter, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(Resource404Filter.class);
	
	@Autowired
	private ResourceDaoable resourceDao;
	@Autowired
	private TemplateSourceDaoable templateSourceDao;
	
	private List<Renderable> renders = new ArrayList<Renderable>();;

	@Override
	public void afterPropertiesSet() throws Exception {
		renders.add(new ResourceRender(resourceDao));
	    renders.add(new TemplateSourceRender(templateSourceDao));
	}

	@Override
	public void destroy() {
		//none instance
	}
	
	@Override
	public void init(FilterConfig cfg) throws ServletException {
		//none instance
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse rep,	FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
	    HttpServletResponse response = (HttpServletResponse) rep;
	    Resource404ResponseWrapper responseWrapper = new Resource404ResponseWrapper(response);
	        
	    chain.doFilter(request, responseWrapper);
	    
	    if(responseWrapper.isFound()){
	    	return ;
	    }
	    for(Renderable render : renders){
	    	if(render.render(request, response)){
	    		return ;
	        }
	    }
	    String uri = request.getRequestURI();
	    logger.warn("This is not path = {}",uri);
	    responseWrapper.sendError(HttpServletResponse.SC_NOT_FOUND,uri);
	}

	private static class Resource404ResponseWrapper extends HttpServletResponseWrapper {
		private int status = SC_OK;

		public Resource404ResponseWrapper(HttpServletResponse response) {
			super(response);
		}

		@Override
		public void sendError(int sc) throws IOException {
			this.status = sc;
			if (isFound()) {
				super.sendError(sc);
			} else {
				super.setStatus(SC_OK);
			}
		}

		@Override
		public void sendError(int sc, String msg) throws IOException {
			this.status = sc;
			if (isFound()) {
				super.sendError(sc, msg);
			} else {
				super.setStatus(SC_OK);
			}
		}

		public void setStatus(int status) {
			this.status = status;
			super.setStatus(status);
		}

		@Override
		public void reset() {
			this.status = SC_OK;
			super.reset();
		}

		public boolean isFound() {
			return status != SC_NOT_FOUND;
		}
	}
}
