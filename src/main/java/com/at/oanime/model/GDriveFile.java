package com.at.oanime.model;

import java.io.Serializable;

public class GDriveFile implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	
	private String name;
	
	private String mimeType;
	
	private String folder;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}
	
}
