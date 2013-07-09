package com.TroyEmpire.Hebe.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

public class HebeFileHandleToolUtil {
	/**
	 * @param sourceFile the filename of zip file including the absolute path
	 * @param destFileFolder the folder in which the zipped files will be put in
	 */
	public static boolean unzipFile(String sourceFile, String destFileFolder) {
		try{
			File zipFile = new File(destFileFolder);
			if(!zipFile.exists()) zipFile.mkdirs();
			ZipInputStream zipFileStream = new ZipInputStream(new FileInputStream(sourceFile));
			ZipEntry entry = zipFileStream.getNextEntry();
			while(entry != null){
				String fileName = entry.getName();
				File newFile = new File(destFileFolder + File.separator + fileName);
				if(entry.isDirectory()) 
					new File(newFile.getParent()).mkdirs();
				else{
					new File(newFile.getParent()).mkdirs();
					FileOutputStream out = new FileOutputStream(newFile);
					IOUtils.copy(zipFileStream, out);
				}
				entry = zipFileStream.getNextEntry();
			}
			zipFileStream.closeEntry();	
			
		}catch(Exception e){
			return false;
		}	
		return true;
	}
}
