package com.philippevienne.pdfapi.api;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@CrossOrigin
public class FindTextController {

	@PostMapping("/api/findUnique/")
	public MatchResponse findUnique(@RequestParam("pdf") MultipartFile file, @RequestParam("regex") String regex) throws IOException {
		Matcher matcher = new PDFTextParse(file, regex).invoke().runMatch();
		if (matcher.find()) {
			return new MatchResponse(matcher.group());
		}
		return new MatchResponse(null);
	}

	@PostMapping("/api/find/")
	public MatchesResponse find(@RequestParam("pdf") MultipartFile file, @RequestParam("regex") String regex) throws IOException {
		Matcher matcher = new PDFTextParse(file, regex).invoke().runMatch();
		MatchesResponse response = new MatchesResponse();
		List<String> data = response.getData();
		while (matcher.find()) {
			data.add(matcher.group());
		}
		return response;
	}

	private class MatchResponse {
		private final String data;

		public MatchResponse(String data) {
			this.data = data;
		}

		public String getData() {
			return data;
		}
	}

	private class MatchesResponse {
		private final List<String> data = new ArrayList<>();

		public List<String> getData() {
			return data;
		}
	}

	private class PDFTextParse {
		private MultipartFile file;
		private String regex;
		private Pattern pattern;
		private String text;

		public PDFTextParse(MultipartFile file, String regex) {
			this.file = file;
			this.regex = regex;
		}

		public Pattern getPattern() {
			return pattern;
		}

		public String getText() {
			return text;
		}

		private Matcher runMatch() {
			return pattern.matcher(text);
		}

		public PDFTextParse invoke() throws IOException {
			pattern = Pattern.compile(regex);

			PDDocument document = PDDocument.load(file.getInputStream());
			int numPages = document.getNumberOfPages();

			System.out.println(numPages + " pages");

			PDFTextStripper stripper = new PDFTextStripper();

			stripper.setStartPage(0);
			stripper.setEndPage(numPages);
			text = stripper.getText(document);
			return this;
		}
	}
}
