package com.at.oanime.service;

import org.springframework.web.multipart.MultipartFile;

import com.at.oanime.model.ResponsePayload;

public interface DriveService {

	public void uploadFile(MultipartFile multipartFile) throws Exception;
	
	public ResponsePayload getAllFiles(String mimeType) throws Exception;
}
