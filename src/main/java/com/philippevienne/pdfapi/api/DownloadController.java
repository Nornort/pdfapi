package com.philippevienne.pdfapi.api;

import com.philippevienne.pdfapi.data.PDFFile;
import com.philippevienne.pdfapi.data.StorageService;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
@CrossOrigin
public class DownloadController {

	private final StorageService service;

	public DownloadController(StorageService service) {
		this.service = service;
	}

	@GetMapping("/api/dl/{id:.+}")
	public byte[] dl(@PathVariable String id) throws IOException {
		PDFFile pdfFile = new PDFFile(id);
		File file = service.resolve(pdfFile);
		if(file == null) {
			throw new RuntimeException();
		}
		return FileUtils.readFileToByteArray(file);
	}
}
