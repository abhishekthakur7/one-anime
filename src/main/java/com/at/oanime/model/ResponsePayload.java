package com.at.oanime.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResponsePayload implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<GDriveFile> files = new ArrayList<>();

	public List<GDriveFile> getFiles() {
		return files;
	}

	public void setFiles(List<GDriveFile> files) {
		this.files = files;
	}
	
}
