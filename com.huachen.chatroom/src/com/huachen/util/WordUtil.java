package com.huachen.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class WordUtil {
	private Configuration configuration = null;
	
	public WordUtil(){
		configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8");
	}
	
	public void createDoc(Map<String, Object> dataMap,OutputStream outfil) throws UnsupportedEncodingException {
		
		configuration.setClassForTemplateLoading(this.getClass(), "/ftl");
		Template doct = null;
		try {
			doct = configuration.getTemplate("01.ftl");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Writer out =null;

		OutputStreamWriter oWriter = new OutputStreamWriter(outfil,"UTF-8");
		out = new BufferedWriter(oWriter);
			
		try {
			doct.process(dataMap, out);
			out.close();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
