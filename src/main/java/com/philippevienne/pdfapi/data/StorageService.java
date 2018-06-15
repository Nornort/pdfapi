package com.philippevienne.pdfapi.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.IOException;

/**
 * Service to manage stored files and encrypt them.
 */
@Service
public class StorageService {

	private final TextEncryptor encryptor;
	private final File storagePath;

	public StorageService(@Value("${storage:/tmp}") File storagePath, @Value("${security:KJAssnxlsOIWDSJL9809e2jhd30d8is}") String secret){
		this.storagePath = storagePath;
		if (!this.storagePath.exists()) {
			if (!this.storagePath.mkdirs()) {
				throw new IllegalStateException("Can not create the storage directory");
			}
		}
		if(!this.storagePath.canWrite()) {
			throw new IllegalStateException("Storage folder must be writable and readable");
		}
		encryptor = Encryptors.text(secret, DigestUtils.md5DigestAsHex(this.storagePath.getAbsolutePath().getBytes()));
	}

	private String encode(String data) {
		return encryptor.encrypt(data);
	}

	private String decode(String data) {
		return encryptor.decrypt(data);
	}

	public PDFFile createNewStoredFile() throws IOException {
		File file = File.createTempFile("tmp-data-", ".pdf", storagePath);
		return new PDFFile(encode(file.getAbsolutePath()));
	}

	public File resolve(PDFFile file){
		File f = new File(decode(file.getReference()));
		if (!f.exists()) {
			return null;
		}
		return f;
	}

}
