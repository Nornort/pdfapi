package com.philippevienne.pdfapi.api;

import com.philippevienne.pdfapi.data.PDFFile;
import com.philippevienne.pdfapi.data.StorageService;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class SplitPagesController {

	private final StorageService service;

	public SplitPagesController(StorageService service) {
		this.service = service;
	}

	@PostMapping("/api/split/")
	public ArrayList<PDFFile> handleFileUpload(@RequestParam("pdf") MultipartFile file,	@RequestParam("pages") String pages) throws IOException {
		ArrayList<PDFFile> files = new ArrayList<>();
		List<Range> ranges = getRanges(pages);
		Splitter splitter;
		PDDocument document;
		List<PDDocument> output;
		try {

			document = PDDocument.load(file.getInputStream());
			int numPages = document.getNumberOfPages();

			System.out.println(numPages + " pages");

			for (Range range : ranges) {
				splitter = new Splitter();
				splitter.setStartPage(range.start);
				splitter.setEndPage(range.stop);
				splitter.setSplitAtPage(range.stop - range.start + 1);
				output = splitter.split(document);
				for (PDDocument pdDocument : output) {
					PDFFile newStoredFile = service.createNewStoredFile();
					File tmpF = service.resolve(newStoredFile);
					pdDocument.save(tmpF);
					files.add(newStoredFile);
				}
			}
		} catch (Exception ignored){
			ignored.printStackTrace();
		}
		return files;
	}

	private static class Range{
		private int start;
		private int stop;

		public Range(String text) {
			String[] pos = text.split("-", 2);
			if(pos.length == 1) {
				start = Integer.parseInt(pos[0], 10);
				stop = start;
			} else if(pos.length == 2) {
				start = Integer.parseInt(pos[0], 10);
				stop = Integer.parseInt(pos[1], 10);
			} else {
				throw new IllegalArgumentException(text + " is not a valid range");
			}
		}
	}


	private static List<Range> getRanges(String ranges) {
		return Arrays.stream(ranges.split(",")).map(Range::new).collect(Collectors.toList());
	}

}
