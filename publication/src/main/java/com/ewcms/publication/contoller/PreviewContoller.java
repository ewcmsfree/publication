package com.ewcms.publication.contoller;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewcms.publication.PublishException;
import com.ewcms.publication.contoller.request.TemplateRequest;
import com.ewcms.publication.contoller.response.PublishResponse;
import com.ewcms.publication.preview.PreviewServiceable;

@Controller
@RequestMapping("/preview")
public class PreviewContoller {
	private static final Logger logger = LoggerFactory.getLogger(PreviewContoller.class);
	public static final String SESSION_CURRENT_SITE_ID = "preview_current_site_id";
	
	@Autowired
	private PreviewServiceable previewService;

	@RequestMapping(value="/template/{siteId}/{channelId}/{templateId}/{mock}",method = RequestMethod.GET)
	public void previewTemplate(@PathVariable("siteId")Long siteId, @PathVariable("channelId")Long channelId,
			@PathVariable("templateId")Long templateId,@PathVariable("mock")Boolean mock,
			HttpServletResponse response,HttpSession session){

		session.setAttribute(SESSION_CURRENT_SITE_ID, siteId);
		setHttpHeader(response,true);
		Writer writer = null;
		try{
			writer = response.getWriter();
			previewService.viewTemplate(writer, siteId, channelId, templateId, mock);
		}catch(IOException e){
			logger.error("Preview template Response io exception {}", e.getMessage());
		}catch(PublishException e){
			logger.error("Preview article publish exception {}",e.getMessage());
			try{
				writer.write(e.getMessage());	
			}catch(IOException ex){
				//none instance
			}
		}finally{
			closeQuietly(writer);
		}
	}
	
	private void closeQuietly(Writer writer){
		try{
			if(writer != null){
				writer.close();
			}
		}catch(IOException e){
			//none instance
		}
	}
	
	@RequestMapping(value="/article/{siteId}/{channelId}/{articleId}/{pageNumber}", method=RequestMethod.GET)
	public void previewArticle(@PathVariable("siteId")Long siteId,@PathVariable("channelId")Long channelId,
			@PathVariable("articleId")Long articleId,@PathVariable("pageNumber")Integer pageNumber,
			HttpServletResponse response, HttpSession session){
		
		session.setAttribute(SESSION_CURRENT_SITE_ID, siteId);
		setHttpHeader(response,true);
		Writer writer = null;
		try{
			writer = response.getWriter();
			previewService.viewArticle(writer, siteId, channelId, articleId, pageNumber);
		}catch(IOException e){
			logger.error("Preview article io exception {}",e.getMessage());
		}catch(PublishException e){
			logger.error("Preview publish exception {}", e.getMessage());
		}finally{
			closeQuietly(writer);
		}
	}
	
	private void setHttpHeader(HttpServletResponse response,boolean ok){
		int status = ok ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		response.setStatus(status);
		response.setHeader("Content-type", "text/html; charset=utf-8");
	}
	
	@RequestMapping(value="verifyTemplate",consumes="application/json",produces="application/json", method=RequestMethod.POST)
	@ResponseBody
	public PublishResponse verifyTemplate(@RequestBody TemplateRequest request){
		boolean v = previewService.verifyTemplate(
				request.getSiteId(),request.getChannelId(),request.getTemplateId());
		
		return v ? PublishResponse.success() : PublishResponse.fail(500, "模板错误");
	}
}
