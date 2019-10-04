package com.at.oanime.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.api.services.drive.model.File;

public class ResponsePayload implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<File> files = new ArrayList<>();

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}
	
}
