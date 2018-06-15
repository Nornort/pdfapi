package com.philippevienne.pdfapi.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.File;

public class PDFFile {

	private String reference;
	private File file;

	public PDFFile(File file) {
		this.file = file;
	}
	public PDFFile(String reference) {
		this.reference = reference;
	}

	@JsonIgnore
	public File getFile() {
		return file;
	}

	@JsonIgnore
	public void setFile(File file) {
		this.file = file;
	}

	public String getReference() {
		return reference;
	}
}
