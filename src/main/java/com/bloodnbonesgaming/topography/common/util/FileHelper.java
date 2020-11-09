package com.bloodnbonesgaming.topography.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.bloodnbonesgaming.topography.Topography;

public class FileHelper {
	public static BufferedReader openReader(final File file) throws FileNotFoundException{
		return new BufferedReader(new FileReader(file));
	}
	
	public static void writeToFile(String path, String toWrite) {
		File file = new File(path);
		file.getParentFile().mkdirs();
		try (PrintWriter stream = new PrintWriter(file)){
			stream.write(toWrite);
		} catch (IOException e) {
			Topography.getLog().error("Exception writing to file: " + e.getMessage());
		}
	}
	
	public static String readLineFromFile(String path) {
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(path)))){
			return reader.readLine();
			
		} catch (IOException e) {
			Topography.getLog().error("Exception reading file: " + e.getMessage());
		}
		return null;
	}
	
	public static String[] readLinesFromFile(String path) {
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(path)))){
			return reader.lines().toArray(String[]::new).clone();
			
		} catch (IOException e) {
			Topography.getLog().error("Exception reading file: " + e.getMessage());
		}
		return null;
	}
	
	public static BufferedInputStream openStreamReader(final File file) throws FileNotFoundException {
		return new BufferedInputStream(new FileInputStream(file));
	}
}
