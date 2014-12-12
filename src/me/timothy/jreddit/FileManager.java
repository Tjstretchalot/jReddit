package me.timothy.jreddit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Handles basic save file types, wrapping checked
 * exceptions with unchecked exceptions.
 * 
 * @author Timothy
 */
public class FileManager {
	private File dir;
	
	public FileManager(String directory) {
		dir = new File(directory);
		
		if(!dir.exists())
			dir.mkdirs();
	}
	
	public String loadFile(String fileName) {
		File file = getFile(fileName);
		
		if(!file.exists())
			return "";
		
		StringBuilder result = new StringBuilder();
		String ln;
		
		try(BufferedReader fin = new BufferedReader(new FileReader(file))) {
			ln = fin.readLine();
			if(ln == null)
				return "";
			result.append(ln);
			while((ln = fin.readLine()) != null) {
				result.append("\n").append(ln);
			}
		}catch(IOException ex) {
			throw new RuntimeException(ex);
		}
		
		return result.toString();
	}
	
	public void saveFile(String fName, String str) {
		File file = getFile(fName);
		
		try(BufferedWriter fOut = new BufferedWriter(new FileWriter(file))) {
			fOut.write(str);
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public List<String> loadStrList(String fileName, String... defaults) {
		List<String> result = new ArrayList<>();
		
		File file = getFile(fileName);
		if(!file.exists()) {
			for(String str : defaults) 
				result.add(str);
			
			return result;
		}
		
		try(BufferedReader fin = new BufferedReader(new FileReader(file))) {
			String ln;
			while((ln = fin.readLine()) != null)
				result.add(ln);
		}catch(IOException ex) {
			throw new RuntimeException(ex);
		}
		
		return result;
	}
	
	public void saveStrList(String fileName, List<String> strList) {
		File file = getFile(fileName);
		
		try(BufferedWriter fout = new BufferedWriter(new FileWriter(file))) {
			for(String str : strList) {
				fout.write(str);
				fout.newLine();
			}
		}catch(IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public Properties loadProperties(String fName) {
		Properties res = new Properties();
		
		File file = getFile(fName);
		if(!file.exists())
			return res;
		
		try(FileInputStream fIn = new FileInputStream(file)) {
			res.load(fIn);
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
		
		return res;
	}
	
	public void saveProperties(String fName, Properties prop) {
		File file = getFile(fName);
		
		try(FileOutputStream fOut = new FileOutputStream(file)) {
			prop.store(fOut, "");
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public File getFile(String fileName) {
		File file = new File(fileName);
		if(file.isAbsolute())
			return file;
		return new File(dir, fileName);
	}
	
	public File getFileAndCreate(String fileName) {
		File file = getFile(fileName);
		if(!file.exists()) {
			if(fileName.endsWith("/")) {
				file.mkdir();
			}else {
				try {
					file.createNewFile();
				} catch (IOException e) {
					// it is shockingly unlikely the program can compensate
					// for this, as it's either a developer typo or a security restriction
					throw new RuntimeException(e); 
				}
			}
		}
		return file;
	}
	
	public File getFileAndCreate(String fileName, String fillWith) {
		File file = getFile(fileName);
		if(!file.exists()) {
			try {
				file.createNewFile();
				FileWriter writer = new FileWriter(file);
				writer.write(fillWith);
				writer.close();
			}catch(IOException e) {
				throw new RuntimeException(e);
			}
		}
		return file;
	}
}
