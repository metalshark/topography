package com.bloodnbonesgaming.topography.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Stream;

import org.apache.commons.lang3.SystemUtils;

import com.bloodnbonesgaming.topography.Topography;

import net.minecraftforge.fml.ModList;

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
	
	public static void copyDirectoryFromJar(final Class<?> classInJar, final String jarDirectory, final String destinationFolder) {
		String path;
		
		try {
			//Easy way
			path = classInJar.getProtectionDomain().getCodeSource().getLocation().getPath();
			
			String decodedPath = URLDecoder.decode(path, "UTF-8");
			String[] split = decodedPath.split("!");
			try (FileSystem fileSystem = FileSystems.newFileSystem(Paths
					.get(split[0].substring(SystemUtils.IS_OS_WINDOWS ? 6 : 5)), classInJar.getClassLoader())) {
				final Path jarPath = fileSystem.getPath(jarDirectory);

				FileHelper.iteratePath(classInJar, jarPath, jarDirectory, destinationFolder);

			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			try {
				//Manual way
				path = "./mods/topography-" + ModList.get().getModContainerById("topography").get().getModInfo().getVersion().toString() + ".jar";
				
				try (FileSystem fileSystem = FileSystems.newFileSystem(Paths
						.get(path), classInJar.getClassLoader())) {
					final Path jarPath = fileSystem.getPath(jarDirectory);

					FileHelper.iteratePath(classInJar, jarPath, jarDirectory, destinationFolder);

				} catch (Exception ioe) {
					ioe.printStackTrace();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	private static void iteratePath(final Class<?> classInJar, final Path jarPath, final String jarDirectory, final String destinationFolder) {
		try (Stream<Path> walk = Files.walk(jarPath, Integer.MAX_VALUE)) {

			for (Iterator<Path> it = walk.iterator(); it.hasNext();) {
				final Path path = it.next();

				if (!Files.isDirectory(path)) {
					FileHelper.readWrite(classInJar, path.toString(), jarDirectory, destinationFolder);
				}
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private static void readWrite(final Class<?> classInJar, final String jarPosition, final String jarDirectory, final String destinationFolder) {
		final File end = new File(destinationFolder.concat(jarPosition.substring(jarDirectory.length())));

		try {
			end.getParentFile().mkdirs();
			end.createNewFile();

			int readBytes;
			final byte[] buffer = new byte[4096];
			// @Cleanup
			try (final InputStream stream = classInJar.getResourceAsStream(jarPosition)) {
				try (final OutputStream outStream = new FileOutputStream(end);) {
					while ((readBytes = stream.read(buffer)) > 0)
						outStream.write(buffer, 0, readBytes);
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
