package com.ewcms.publication.contoller.converter;

import java.io.IOException;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonpHttpMessageConverter extends MappingJackson2HttpMessageConverter {
	private static final String JSONP_CALLBACK = "jsonpCallback";
	
	@Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
	    JsonGenerator jsonGenerator = this.getObjectMapper().getFactory().createGenerator(outputMessage.getBody(), encoding);
	    try {
	        jsonGenerator.writeRaw(JSONP_CALLBACK);
	        jsonGenerator.writeRaw('(');
	        this.getObjectMapper().writeValue(jsonGenerator, object);
	        jsonGenerator.writeRaw(");");
	        jsonGenerator.flush();
	    } catch (JsonProcessingException ex) {
	        throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
	    }
    }
}
