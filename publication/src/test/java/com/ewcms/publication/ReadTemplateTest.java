package com.ewcms.publication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.ewcms.publication.module.TemplateBody;

public class ReadTemplateTest {
	private static final String FIND_BODY_SQL = "SELECT t1.id,t1.uniquepath,t2.tplentity FROM site_template t1,site_templateentity t2 "
			+ "WHERE t1.tplentityid = t2.id AND t1.channel_id = ?";
	
	private static JdbcTemplate jdbcTemplate;
	
	private String templateRoot = "d:/template";
    
    @SuppressWarnings("resource")
	@BeforeClass
    public static void init(){
    	ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
    	DataSource ds = (DataSource)context.getBean("dataSource");
    	jdbcTemplate = new JdbcTemplate(ds);
    }
    
    @Test
    public void testSaveTemplateEntity()throws Exception{
    	Object[] params = new Object[]{27};
    	List<TemplateBody> bodys = jdbcTemplate.query(FIND_BODY_SQL, params,new TemplateBodyMapper());
    	for(TemplateBody body : bodys){
    		BufferedWriter w = null;
    		try{
    			String name = StringUtils.replace(body.getUniquePath(), "\\", "_");
    			name = StringUtils.replace(body.getUniquePath(), "/", "_");
        		File file =new File(String.format("%s/%s.html", templateRoot, name));
        		w = new BufferedWriter(new FileWriter(file));
        		byte[] c = body.getBody();
        		w.write(new String(c,"UTF-8"));
    		}finally{
    			if(w != null){
    				w.close();
    			}
    		}
    	}
    }
    
    static class TemplateBodyMapper implements RowMapper<TemplateBody>{

		@Override
		public TemplateBody mapRow(ResultSet rs, int rowNum)throws SQLException {
			TemplateBody body = new TemplateBody();
			
			body.setId(rs.getInt("id"));
			body.setUniquePath(rs.getString("uniquepath"));
			body.setBody(rs.getBytes("tplentity"));
			
			return body;
		}
		
	}
}
