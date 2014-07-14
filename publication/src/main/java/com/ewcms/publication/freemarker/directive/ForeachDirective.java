package com.ewcms.publication.freemarker.directive;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import com.ewcms.publication.freemarker.FreemarkerUtil;
import com.ewcms.publication.freemarker.GlobalVariable;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public abstract class ForeachDirective extends BaseDirective {
	
	@SuppressWarnings("rawtypes")
	protected  TemplateModel[] getLoopVars(Environment env, Map params) throws TemplateException{
		return new TemplateModel[]{env.getObjectWrapper().wrap(getValues(env,params))};
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void executeBody(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body, Writer writer)throws TemplateException, IOException{
		
		List<Object> values = getValues(env,params);
        for (int i = 0 ; i < values.size(); i++) {
             Object v = values.get(i);
             FreemarkerUtil.setVariable(env, GlobalVariable.INDEX.getVariable(), i + 1);
             setVariable(env,params,v);
             
             body.render(writer);
             
             FreemarkerUtil.removeVariable(env, GlobalVariable.INDEX.toString());
             removeVariable(env,params);
        }
        writer.flush();
	}
	
	@SuppressWarnings("rawtypes")
	protected abstract  List<Object> getValues(Environment env, Map params)throws TemplateException;
	
	@SuppressWarnings("rawtypes")
	protected abstract void setVariable(Environment env, Map params,Object value) throws TemplateException;
	
	@SuppressWarnings("rawtypes")
	protected abstract void removeVariable(Environment env, Map params) throws TemplateException;
}
