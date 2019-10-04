package com.at.oanime.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.at.oanime.constant.ApplicationConstant;
import com.at.oanime.model.GDriveFile;
import com.at.oanime.model.ResponsePayload;
import com.at.oanime.util.ApplicationConfig;
import com.at.oanime.util.StringUtils;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

@Service
public class DriveServiceImpl {

	private Logger logger = LoggerFactory.getLogger(DriveServiceImpl.class);

	private Drive driveService;

	@Autowired
	AuthorizationServiceImpl authorizationService;

	@Autowired
	ApplicationConfig applicationConfig;

	@PostConstruct
	public void init() throws Exception {
		Credential credential = authorizationService.getCredentials();
		driveService = new Drive.Builder(ApplicationConstant.HTTP_TRANSPORT, ApplicationConstant.JSON_FACTORY,
				credential).setApplicationName(ApplicationConstant.APPLICATION_NAME).build();
	}

	public void uploadFile(MultipartFile multipartFile) throws Exception {
		logger.debug("Inside Upload Service...");

		String path = applicationConfig.getTemporaryFolder();
		String fileName = multipartFile.getOriginalFilename();
		String contentType = multipartFile.getContentType();

		java.io.File transferedFile = new java.io.File(path, fileName);
		multipartFile.transferTo(transferedFile);

		File fileMetadata = new File();
		fileMetadata.setName(fileName);

		FileContent mediaContent = new FileContent(contentType, transferedFile);
		File file = driveService.files().create(fileMetadata, mediaContent).setFields("id").execute();

		logger.debug("File name: {} and File Id: {}", file.getName(), file.getId());
	}

	public ResponsePayload getAllFiles(String mimeType) throws Exception {
		FileList result = null;

		if (StringUtils.trimString(mimeType).equals("*") || StringUtils.trimString(mimeType).isEmpty()) {
			result = driveService.files().list().execute();
		} else {
			result = driveService.files().list().setQ("mimeType='" + mimeType + "'")
					.setFields("files(id,name,mimeType,fileExtension,webViewLink,webContentLink,thumbnailLink,videoMediaMetadata,teamDriveId,size)").execute();
		}

		List<File> files = result.getFiles();
		ResponsePayload payload = new ResponsePayload();
		/**
		 * List<GDriveFile> gDriveFiles = new ArrayList<>();
		 * 
		 * for(File file : files) {
		 * 
		 * logger.debug("File: {}", file.toString());
		 * 
		 * GDriveFile gDriveFile = new GDriveFile(); gDriveFile.setId(file.getId());
		 * gDriveFile.setName(file.getName());
		 * gDriveFile.setMimeType(file.getMimeType());
		 * gDriveFile.setWebViewLink(file.getWebViewLink());
		 * gDriveFile.setFileSize(file.getQuotaBytesUsed());
		 * gDriveFile.setIconLink(file.getIconLink()); gDriveFiles.add(gDriveFile); }
		 */

		payload.setFiles(files);

		return payload;
	}

}
