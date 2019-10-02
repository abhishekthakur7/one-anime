package com.at.oanime.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.google.api.services.drive.model.File;

public interface DriveService {

	public void uploadFile(MultipartFile multipartFile) throws Exception;
	
	public List<File> getAllFiles() throws Exception;
}
