/*

	This program is free software: you can redistribute it and/or modify

    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
 */


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
	
	public Boolean copyFile(String sourcePath, String destinationPath){
		
		Boolean status = false;
		Integer read = 0;
		InputStream readFileInputStream = null;
		OutputStream writeFileOutputStream = null;
		
		
		try{
			
			File inputFile = new File(sourcePath);
			File outputFile = new File(destinationPath);
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

				e.printStackTrace();
			}
			
		}
		return status;
		
	}
	
	public String getCertificateFile(String certificateName){
				
		return getCertificateDirectory()+"/"+certificateName;
	}
	
	public void createCertificateDirectory(){
		File certificateDirectory = new File(getCertificateDirectory());
		certificateDirectory.mkdir();
	}
	
	public String getCertificateDirectory(){
		return new File(context.getFilesDir(), "certificates").getPath();
	}
	

}
