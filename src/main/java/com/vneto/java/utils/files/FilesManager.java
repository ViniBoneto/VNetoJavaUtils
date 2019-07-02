package com.vneto.java.utils.files;

import java.io.FileNotFoundException;
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

}