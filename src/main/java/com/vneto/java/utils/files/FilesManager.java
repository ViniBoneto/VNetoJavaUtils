package com.vneto.java.utils.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;


public class FilesManager {

	public static List<String> readAllLines(String strFilePath) throws IOException {
		
		return readAllLines(strFilePath, StandardCharsets.UTF_8);
	}
	
	public static List<String> readAllLines(String strFilePath, Charset charset) throws IOException {
		Path filePath;
		List<String> lines = null;
		
		if( Files.notExists(filePath = Paths.get(strFilePath)) ) 
			throw new FileNotFoundException("File " + strFilePath + " was not found on the filesystem!!");
			
		if(Files.isReadable(filePath) ) {
			lines = Files.readAllLines(filePath, charset);;
		}
		else
			throw new SecurityException("User don't have read access permission to file " + strFilePath + "!!");
		
		return lines;
	}

	public static void saveFile(File inFile, String outFileNm) throws IOException {
		
		if(inFile == null)
			throw new NullPointerException("parameter imgFile is null");
		
		if(!inFile.exists() || !inFile.isFile())
			throw new FileNotFoundException("File" + inFile.getAbsolutePath() + " not found ou not a regular file");
		
		if(!inFile.canRead())
			throw new SecurityException("File " + inFile.getAbsolutePath() + " can not be opened for reading");
		
		File fOut = new File(outFileNm);
		byte[] buff = new byte[4096];
		int nBytes;
		
		if( !fOut.exists() && !fOut.createNewFile() )
			throw new FileNotFoundException("Outuput file " + inFile + " was not found and it was not possible to create it");
		
		try (
				FileInputStream fis = new FileInputStream(inFile); 
				FileOutputStream fos = new FileOutputStream(fOut);
			) {
				while ( fis.available() > 0) {
					nBytes = fis.read(buff);
					fos.write(buff, 0, nBytes);
				}	
		}
	}
	
	public static void saveFile(String inFileNm, String outFileNm) throws IOException {
		saveFile(new File(inFileNm), outFileNm);
	}
}