package com.ewcms.publication.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewcms.publication.PublishException;
import com.ewcms.publication.PublishServiceable;
import com.ewcms.publication.contoller.request.ArticleRequest;
import com.ewcms.publication.contoller.request.ChannelRequest;
import com.ewcms.publication.contoller.request.SiteRequest;
import com.ewcms.publication.contoller.request.ResourceRequest;
import com.ewcms.publication.contoller.request.TemplateRequest;
import com.ewcms.publication.contoller.request.SourceRequest;
import com.ewcms.publication.contoller.response.PublishResponse;

@Controller
@RequestMapping("/publication")
public class PublishContoller {
	
	@Autowired
	private PublishServiceable pubService;

	@RequestMapping(value="pubSite",consumes="application/json",produces="application/json", method=RequestMethod.POST)
	@ResponseBody
	public PublishResponse pubSite(@RequestBody SiteRequest request){
		try{
			pubService.pubSite(request.getSiteId(), request.isAgain());
			return PublishResponse.success();
		}catch(PublishException e){
			return PublishResponse.fail(1, "发布站点失败");
		}
	}
	
	@RequestMapping(value="pubChannel",consumes="application/json",produces="application/json", method=RequestMethod.POST)
	@ResponseBody
	public PublishResponse pubChannel(@RequestBody ChannelRequest request){
		try{
			pubService.pubChannel(request.getSiteId(), request.getChannelId(), request.isChild(), request.isAgain());
			return PublishResponse.success();
		}catch(PublishException e){
			return PublishResponse.fail(2, "发布频道失败");
		}
	}
	
	@RequestMapping(value="pubTemplate",consumes="application/json",produces="application/json", method=RequestMethod.POST)
	@ResponseBody
	public PublishResponse pubTemplate(@RequestBody TemplateRequest request){
		try{
			pubService.pubTemplate(request.getSiteId(), request.getChannelId(),
					request.getTemplateId(), request.isAgain());
			return PublishResponse.success();
		}catch(PublishException e){
			return PublishResponse.fail(3, "发布模板失败");
		}
	}
	
	@RequestMapping(value="pubArticle",consumes="application/json",produces="application/json", method=RequestMethod.POST)
	@ResponseBody
	public PublishResponse pubArticle(@RequestBody ArticleRequest request){
		try{
			pubService.pubArticle(request.getSiteId(), request.getChannelId(), request.getIds());
			return PublishResponse.success();
		}catch(PublishException e){
			return PublishResponse.fail(3, "发布文章失败");
		}
	}
	
	@RequestMapping(value="pubResource",consumes="application/json",produces="application/json", method=RequestMethod.POST)
	@ResponseBody
	public PublishResponse pubResource(@RequestBody ResourceRequest request){
		try{
			if(request.getIds() == null || request.getIds().isEmpty()){
				pubService.pubResource(request.getSiteId(), request.getIds());	
			}else{
				pubService.pubResource(request.getSiteId(),request.getIds());
			}
			return PublishResponse.success();
		}catch(PublishException e){
			return PublishResponse.fail(4, "站点资源发布失败");
		}
	}
	
	@RequestMapping(value="pubSource",consumes="application/json",produces="application/json", method=RequestMethod.POST)
	@ResponseBody
	public PublishResponse pubTemplateSource(@RequestBody SourceRequest request){
		try{
			if(request.getIds() == null || request.getIds().isEmpty()){
				pubService.pubTemplateSource(request.getSiteId(), request.isAgain());
			}else{
				pubService.pubTemplateSource(request.getSiteId(), request.getIds());
			}
			return PublishResponse.success();
		}catch(PublishException e){
			return PublishResponse.fail(5, "站点模板资源发布失败");
		}
	}
}
