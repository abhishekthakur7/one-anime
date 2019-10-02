package com.at.oanime.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.at.oanime.model.UploadFile;
import com.at.oanime.service.AuthorizationService;
import com.at.oanime.service.DriveService;
import com.google.api.services.drive.model.File;

@RestController
public class MainController {

	private Logger logger = LoggerFactory.getLogger(MainController.class);

	@Autowired
	AuthorizationService authorizationService;

	@Autowired
	DriveService driveService;

	/**
	 * Handles the root request. Checks if user is already authenticated via SSO.
	 * 
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value= "/", produces = MediaType.TEXT_PLAIN_VALUE)
	public String showHomePage() throws Exception {
		if (authorizationService.isUserAuthenticated()) {
			logger.debug("User is authenticated");
			return "User is authenticated";
		} else {
			logger.debug("User is not authenticated.");
			return "User is not authenticated";
		}
	}

	/**
	 * Calls the Google OAuth service to authorize the app
	 * 
	 * @param response
	 * @throws Exception
	 */
	@GetMapping(value = "/googlesignin")
	public void doGoogleSignIn(HttpServletResponse response) throws Exception {
		logger.debug("SSO Called...");
		response.sendRedirect(authorizationService.authenticateUserViaGoogle());
	}

	/**
	 * Applications Callback URI for redirection from Google auth server after user
	 * approval/consent
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/oauth/callback", produces = MediaType.TEXT_PLAIN_VALUE)
	public String saveAuthorizationCode(HttpServletRequest request) throws Exception {
		logger.debug("SSO Callback invoked...");
		String code = request.getParameter("code");
		logger.debug("SSO Callback Code Value, {}", code);

		if (code != null) {
			authorizationService.exchangeCodeForTokens(code);
		}
		return "SSO Callback invoked";
	}

	/**
	 * Handles logout
	 * 
	 * @return
	 * @throws Exception 
	 */
	@GetMapping(value = "/logout", produces = MediaType.TEXT_PLAIN_VALUE)
	public String logout(HttpServletRequest request) throws Exception {
		logger.debug("Logout invoked...");
		authorizationService.removeUserSession(request);
		return "Logout invoked...";
	}

	/**
	 * Handles the files being uploaded to GDrive
	 * 
	 * @param request
	 * @param uploadedFile
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/upload", produces = MediaType.TEXT_PLAIN_VALUE)
	public String uploadFile(HttpServletRequest request, @RequestBody UploadFile uploadedFile) throws Exception {
		MultipartFile multipartFile = uploadedFile.getMultipartFile();
		driveService.uploadFile(multipartFile);
		return "File uploaded successfully";
	}
	
	/**
	 * Get all files present in gDrive
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/files", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<File> getAllFiles() throws Exception {
		return driveService.getAllFiles();
	}
}
