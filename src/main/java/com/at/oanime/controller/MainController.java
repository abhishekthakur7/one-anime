package com.at.oanime.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.at.oanime.model.ResponsePayload;
import com.at.oanime.service.AuthorizationService;
import com.at.oanime.service.DriveService;

@RestController
@CrossOrigin
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
	@GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
	public String showHomePage() throws Exception {
		if (authorizationService.isUserAuthenticated()) {
			logger.debug("User is authenticated");
			return "User is authenticated successfully";
		} else {
			logger.debug("User is not authenticated.");
			return "User authentication failed";
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
		logger.debug("Google sign in invoked");
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
		logger.debug("Google sign in Callback invoked");
		String code = request.getParameter("code");
		logger.debug("Google sign in Callback Code Value, {}", code);

		if (code != null) {
			authorizationService.exchangeCodeForTokens(code);
			return "Signed in successfully";
		}
		return "Sign in failure";
	}

	/**
	 * Handles logout
	 * 
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/logout", produces = MediaType.TEXT_PLAIN_VALUE)
	public String logout(HttpServletRequest request) throws Exception {
		logger.debug("Logout invoked");
		authorizationService.removeUserSession(request);
		return "Logged out successfully";
	}

	/**
	 * Handles the files being uploaded to GDrive
	 * 
	 * @param request
	 * @param uploadedFile
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public String uploadFile(@RequestParam("file") MultipartFile multipartFile) throws Exception {
		driveService.uploadFile(multipartFile);
		return "File uploaded successfully";
	}

	/**
	 * Get all files present in gDrive
	 * 
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/files", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponsePayload getAllFiles(@RequestHeader String mimeType) throws Exception {
		return driveService.getAllFiles(mimeType);
	}
}
