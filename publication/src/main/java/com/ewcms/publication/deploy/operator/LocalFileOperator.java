package com.ewcms.publication.deploy.operator;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewcms.publication.PublishException;

public class LocalFileOperator implements FileOperatorable{
	private static final Logger logger = LoggerFactory.getLogger(LocalFileOperator.class);

	@Override
	public void copy(String sourcePath, String targetPath)throws PublishException {
		try{
			FileUtils.copyFile(new File(sourcePath), new File(targetPath));
		}catch(IOException e){
			logger.error("Copy file {} to {} fail,Exception is {}.",new Object[]{sourcePath, targetPath,e.getMessage()});
			throw new PublishException("Copy  file fail",e);
		}
	}

	@Override
	public void copy(byte[] content, String targetPath) throws PublishException {
		try{
			FileUtils.writeByteArrayToFile(new File(targetPath), content);
		}catch(IOException e){
			throw new PublishException("Copy file fail",e);
		}
	}

	@Override
	public void delete(String path) throws PublishException {
		FileUtils.deleteQuietly(new File(path));
	}

	@Override
	public boolean test(String path) {
		boolean success = true;
		try{
			File file = FileUtils.getFile(path);
			if(!file.exists()){
				logger.error("{} dir not exist",path);
				success = false;
			}
			if(success && !file.isDirectory()){
				logger.error("{} is not dir",path);
				success = false;
			}
			if(success && !file.canWrite()){
				logger.error("{} can not write",path);
				success = false;
			}
			if(success){
				File testFile = new File(getFullPath(path,"/ewcms_test.txt"));
				FileUtils.writeStringToFile(testFile, "Hello ewcms deploy dir");
				FileUtils.deleteQuietly(testFile);	
			}
		}catch(IOException e){
			logger.error("Test deploy is fail,Exception is {}", e.getMessage());
			success = false;
		}
		return success;
	}
	
	private String getFullPath(String path,String fileName){
		if(!StringUtils.endsWith(path, "/")){
			path = path + "/";
		}
		return  path  + fileName ;
	}
}
