package com.mobile.syslogng.monitor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;

public class FileManager {
	
	Context context;
	
	public FileManager(){
	
	}
	
	public FileManager(Context context){
		this.context = context;
	}
	
	public Boolean copyFiles(String sourcePath, String destinationPath){
		
		Boolean status = false;
		Integer read = 0;
		InputStream readFileInputStream = null;
		OutputStream writeFileOutputStream = null;
		
		
		try{
			
			File inputFile = new File(sourcePath);
			File outputFile = new File(context.getFilesDir().getAbsolutePath()+"/"+destinationPath);
			readFileInputStream = new BufferedInputStream(new FileInputStream(inputFile));
			writeFileOutputStream = new FileOutputStream(outputFile); 
			
			byte[] bytes = new byte[1024];
			while ((read = readFileInputStream.read(bytes)) != -1){
				writeFileOutputStream.write(bytes, 0, read);
			}
			status = true;
			
			
		} catch (IOException e) {
			status = false;
			e.printStackTrace();
		}
		finally{
			try {
				if(writeFileOutputStream != null){
					writeFileOutputStream.close();
					}
				if(readFileInputStream != null){
					readFileInputStream.close();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return status;
		
	}
	

}
