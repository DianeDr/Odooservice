/*
 * Created on Sep 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.cf.tkconnect.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;


import com.cf.tkconnect.log.*;
import com.cf.tkconnect.util.FileUtils;
import com.cf.tkconnect.util.InitialSetUp;
import com.cf.tkconnect.util.WSConstants;
import com.cf.tkconnect.util.WSUtil;

import static com.cf.tkconnect.util.InitialSetUp.isCallbackService;
import static com.cf.tkconnect.util.WSConstants.*;




/**
 * @author cyril furtado
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public final class FileUtils  {

	static Log logger = LogSource.getInstance(FileUtils.class);
	
	public static String StartUPDirectory = "";

	public static String FileSystemBaseDirectory = "";

	public static String FileServiceBaseDirectory = "";

	public static String ExcelFileServiceBaseDirectory = "";
	
	public static String InputFileServiceBaseDirectory = "";

	public static String CallbackFileServiceBaseDirectory = "";

	public static String ResponseFileServiceBaseDirectory = "";
	
	public static String SuccessFileServiceBaseDirectory = "";

	public static String TempFileServiceBaseDirectory = "";

	public static String ErrorFileServiceBaseDirectory = "";

	public static String LogBaseDirectory = "";

	public static String ErrorBaseDirectory = "";

	public static String ResponseBaseDirectory = "";

	/* Attachments directory under the 'request' directory */
	public static String InputFileServiceAttDirectory = ""; 

	/* Attachments directory under the 'temp' directory */
	public static String TempFileServiceAttDirectory = "";

//	initialized to 1, but actually read from smartlink_jc.properties file.
	

	public static void checkAndCreateFileDirectorySystem(String basedir)
			throws IOException {
		FileSystemBaseDirectory = basedir;
		if (FileSystemBaseDirectory == null
				|| FileSystemBaseDirectory.trim().length() == 0)
			throw new IOException();
		File filedirectory = new File(FileSystemBaseDirectory);
		if (!filedirectory.exists())
			// throw new IOException();
			filedirectory.mkdirs();
		/*
		 * Now start checking if the the file-system exisits if not create it
		 */

		FileServiceBaseDirectory = FileSystemBaseDirectory + File.separator
				+ FILE_SERVICE_DIRECTORY;
		filedirectory = new File(FileServiceBaseDirectory);
		if (!filedirectory.exists())
			filedirectory.mkdir();

		InputFileServiceBaseDirectory = FileServiceBaseDirectory
				+ File.separator + REQUEST_FILE_SERVICE_DIRECTORY;
		filedirectory = new File(InputFileServiceBaseDirectory);
		if (!filedirectory.exists())
			filedirectory.mkdir();

			
		

		ResponseFileServiceBaseDirectory = FileServiceBaseDirectory
				+ File.separator + RESPONSE_FILE_SERVICE_DIRECTORY;
		filedirectory = new File(ResponseFileServiceBaseDirectory);
		if (!filedirectory.exists())
			filedirectory.mkdir();
			
			
		SuccessFileServiceBaseDirectory = FileServiceBaseDirectory
				+ File.separator + SUCCESS_FILE_SERVICE_DIRECTORY;
		filedirectory = new File(SuccessFileServiceBaseDirectory);
		if (!filedirectory.exists())
			filedirectory.mkdir();
		TempFileServiceBaseDirectory = FileServiceBaseDirectory
			+ File.separator + TEMP_FILE_SERVICE_DIRECTORY;
		filedirectory = new File(TempFileServiceBaseDirectory);
		if (!filedirectory.exists())
			filedirectory.mkdir();
		/*
		TempFileServiceAttDirectory = TempFileServiceBaseDirectory
			+ File.separator + ATTACHMENTS_FILE_DIRECTORY;
		filedirectory = new File(TempFileServiceAttDirectory);
		if (!filedirectory.exists())
			filedirectory.mkdir();
		*/
		ErrorFileServiceBaseDirectory = FileServiceBaseDirectory
				+ File.separator + ERROR_FILE_DIRECTORY;
		filedirectory = new File(ErrorFileServiceBaseDirectory);
		if (!filedirectory.exists())
			filedirectory.mkdir();

		ExcelFileServiceBaseDirectory = FileServiceBaseDirectory
				+ File.separator + EXCEL_FILE_SERVICE_DIRECTORY;
		filedirectory = new File(ExcelFileServiceBaseDirectory);
		if (!filedirectory.exists())
			filedirectory.mkdir();
		// create excel template & data
		filedirectory = new File(ExcelFileServiceBaseDirectory+"/template");
		if (!filedirectory.exists())
			filedirectory.mkdir();		
		filedirectory = new File(ExcelFileServiceBaseDirectory+"/data");
		if (!filedirectory.exists())
			filedirectory.mkdir();			
		
		/*
		LogBaseDirectory = FileSystemBaseDirectory + File.separator
				+ LOG_FILE_DIRECTORY;
		filedirectory = new File(LogBaseDirectory);
		if (!filedirectory.exists())
			filedirectory.mkdir();
*/
	}

	// private static int input = 2048;

	public static void copyFiles(String fromDir, String toDir, String inputFile)
			throws IOException {
		copyFiles(fromDir, toDir, inputFile, inputFile);
	}

	public static void copyFiles(String fromDir, String toDir,
			String inputFile, String outputFile) throws IOException {
		copyFiles(fromDir, toDir, inputFile, inputFile, false);
	}

	public static String copyFiles(String fromDir, String toDir,
			String inputFile, String outputFile, boolean delteInFile)
			throws IOException {
		String inFile = fromDir + File.separator + inputFile;
		String outFile = toDir + File.separator + outputFile;
		WebLinkLogLoader.getLogger(FileUtils.class).debug(
				"copyFiles.....fromDir  " + fromDir + " toDir " + toDir
						+ "  inputFile  " + inputFile);
		if (inFile != null && inFile.equalsIgnoreCase(outFile))
			return null;
		myCopy(inFile, outFile);
		// file copied now delete from temp
		if (fileCheck(outFile, inFile)) {
			try {
				File in_file = new File(inFile); // Get File objects from
				// Strings
				if (delteInFile && in_file.exists()) {
					in_file.delete();
					WebLinkLogLoader.getLogger(FileUtils.class).debug(
							"Finished deleting file: "
									+ in_file.getAbsolutePath());
				}
			} catch (Exception ie) {
				WebLinkLogLoader.getLogger(FileUtils.class).error(ie, ie);
			}
		}
		return outFile;
	}

	public static void moveFiles(String fromDir, String toDir, String inputFile)
			throws IOException {
		WebLinkLogLoader.getLogger(FileUtils.class).debug(
				"Moving file: " + inputFile + " from " + fromDir + " to "
						+ toDir);
		copyFiles(fromDir, toDir, inputFile, inputFile, true);

	}
	public static void moveAndRenameFile(String fromDir, String toDir, String inputFile, String outFile)
		throws IOException {
	WebLinkLogLoader.getLogger(FileUtils.class).debug(
			"Moving and rename file: " + inputFile + " from " + fromDir + " to "+ outFile+
					" "+ toDir);
	copyFiles(fromDir, toDir, inputFile, outFile, true);
	
	}

	public static void copyMultipleFiles(String fromDir, String toDir,
			Vector inputFiles) throws IOException {

		if (fromDir == null || toDir == null || inputFiles == null)
			return;
		for (int i = 0; i < inputFiles.size(); i++) {

			String inputFile = (String) inputFiles.get(i);
			if (inputFile == null || inputFile.length() == 0)
				continue;
			String inFile = fromDir + inputFile;
			String outFile = toDir + inputFile;
			if (inFile != null && inFile.equalsIgnoreCase(outFile))
				return;
			myCopy(inFile, outFile);
			// file copied now delete from temp
			if (fileCheck(outFile, inFile)) {
				try {
					new File(inFile); // Get File objects from Strings
				} catch (Exception ie) {
					WebLinkLogLoader.getLogger(FileUtils.class).debug(ie, ie);
				}
			}// if
		}// for

	}// copyMultipleFiles

	public static void copyAndRenameFiles(String fromDir, String toDir,
			String inputFile, String outputFile) throws IOException {
		String inFile = fromDir + inputFile;
		String outFile = toDir + outputFile;
		if (fileCheck(inFile, outFile)) // if ok then copy
			myCopy(inFile, outFile);
	}

	public static void myCopy(String from_name, String to_name)
			throws IOException {
		
		File from_file = new File(from_name); // Get File objects from Strings
		File to_file = new File(to_name);

		// First make sure the source file exists, is a file, and is readable.
		if (!from_file.exists())
			WebLinkLogLoader.getLogger(FileUtils.class).debug(
					"FileCopy: no such source file: " + from_name);
		if (!from_file.isFile())
			WebLinkLogLoader.getLogger(FileUtils.class).debug(
					"FileCopy: can't copy directory: " + from_name);
		if (!from_file.canRead())
			WebLinkLogLoader.getLogger(FileUtils.class).debug(
					"FileCopy: source file is unreadable: " + from_name);

		// If the destination is a directory, use the source file name
		// as the destination file name
		if (to_file.isDirectory())
			to_file = new File(to_file, from_file.getName());

		// If the destination exists, make sure it is a writeable file
		// and ask before overwriting it. If the destination doesn't
		// exist, make sure the directory exists and is writeable.
		if (to_file.exists()) {
			if (!to_file.canWrite())
				WebLinkLogLoader.getLogger(FileUtils.class).debug(
								"FileCopy: destination file is unwriteable: "
										+ to_name);
			// else
		} else {
			// if file doesn't exist, check if directory exists and is
			// writeable.
			// If getParent() returns null, then the directory is the current
			// dir.
			// so look up the user.dir system property to find out what that is.
			String parent = to_file.getParent(); // Get the destination
			// directory
			if (parent == null)
				parent = System.getProperty("user.dir"); // or CWD
			File dir = new File(parent); // Convert it to a file.
			if (!dir.isDirectory()) {
				WebLinkLogLoader.getLogger(FileUtils.class).info(
								" directory does not exists " + parent
										+ " ...creating");
				if (!dir.mkdirs())
					WebLinkLogLoader.getLogger(FileUtils.class).debug(
							" directory not created " + parent);
				WebLinkLogLoader.getLogger(FileUtils.class).debug(
						"mycopy directory  " + parent + " ...created");
			} else
				WebLinkLogLoader.getLogger(FileUtils.class).debug(
						"There is a such  directory: " + parent);
			if (dir.isFile())
				WebLinkLogLoader.getLogger(FileUtils.class).debug(
						"FileCopy: destination is not a directory: " + parent);
			if (!dir.canWrite())
				WebLinkLogLoader.getLogger(FileUtils.class).debug(
						"FileCopy: destination directory is unwriteable: "
								+ parent);
		}

		// If we've gotten this far, then everything is okay.
		// So we copy the file, a buffer of bytes at a time.
		FileInputStream from = null; // Stream to read from source
		FileOutputStream to = null; // Stream to write to destination
		try {
			from = new FileInputStream(from_file); // Create input stream
			to = new FileOutputStream(to_file); // Create output stream
			byte[] buffer = new byte[4096]; // A buffer to hold file contents
			int bytes_read; // How many bytes in buffer
			while ((bytes_read = from.read(buffer)) != -1)
				// Read bytes until EOF
				to.write(buffer, 0, bytes_read); // write bytes
		}
		// Always close the streams, even if exceptions were thrown
		finally {
			if (from != null)
				try {
					from.close();
				} catch (IOException e) {
					;
				}
			if (to != null)
				try {
					to.close();
				} catch (IOException e) {
					;
				}
		}
	}
	
	public static void moveErrorFiles(String fromDir, String toDir,
		String inputFile,  String errormsg, boolean delteInFile) throws IOException {
		//logger.debug("moveErrorFiles inputFile:"+inputFile );
		String inFile = fromDir + File.separator +  inputFile;
		String outFile = toDir + File.separator +  inputFile;
		if (fileCheck(inFile, outFile)){ // if ok then copy
			copyFileContent(inFile, outFile, errormsg,false);
		//logger.debug("moveErrorFiles  moved inputFile:"+inputFile +" to:"+toDir );
			try {
				File in_file = new File(inFile); // Get File objects from
				// Strings
				if (delteInFile && in_file.exists()) {
					in_file.delete();
					WebLinkLogLoader.getLogger(FileUtils.class).debug(
							"Finished deleting file: "
									+ in_file.getAbsolutePath());
				}
			} catch (Exception ie) {
				WebLinkLogLoader.getLogger(FileUtils.class).error(ie, ie);
			}
		}
		//else
			//logger.debug("moveErrorFiles could not move inputFile:"+inputFile +" to:"+toDir );
			
	}

	
	public static int copyFileContent(String from_name, String to_name,String content, boolean deleteinfile){
	 
		 BufferedReader in = null;
		 BufferedWriter out = null;
		 WebLinkLogLoader.getLogger(FileUtils.class).debug(from_name+" to:"+to_name);
		 try{
			 out  = new BufferedWriter(new FileWriter(to_name));
			 in  = new BufferedReader(new FileReader(from_name));
			 String inline = in.readLine();
			 while( inline  != null){
				 int ind = inline.indexOf("<smartlink>");
				 if(ind  >= 0){
					out.write("<smartlink>");
					out.write(content);
					out.write(inline.substring(7));
				 }else{
					 ind = inline.indexOf("<uclient>");
					 if(ind  >= 0){
						out.write("<uclient>");
						out.write(content);
						out.write(inline.substring(9));
					 }else	
						 out.write(inline);
				 }
				 inline = in.readLine();
			 }
			 
		 }catch(IOException io){
			 WebLinkLogLoader.getLogger(FileUtils.class).error(io,io);
		 }
		 finally{
			 try{
				 if(in != null) in.close();
				 if(out != null) out.close();
				 if(deleteinfile){
					 File f = new File(from_name);
					 if(f.exists()) f.delete();
				 }
			 }catch(IOException ioe){
				 WebLinkLogLoader.getLogger(FileUtils.class).error(ioe,ioe);
			 }
		 }
		 return 1;
	}

	public static boolean fileCheck(String from_name, String to_name)
			throws IOException {

		File from_file = new File(from_name); // Get File objects from Strings
		File to_file = new File(to_name);

		// First make sure the source file exists, is a file, and is readable.
		if (!from_file.exists()) {
			WebLinkLogLoader.getLogger(FileUtils.class).error(
					"FileCopy: no such source file: " + from_name);
			return false;
		}
		if (!from_file.isFile()) {
			WebLinkLogLoader.getLogger(FileUtils.class).error(
					"FileCopy: can't copy directory: " + from_name);
			return false;
		}
		if (!from_file.canRead()) {
			WebLinkLogLoader.getLogger(FileUtils.class).error(
					"FileCopy: source file is unreadable: " + from_name);
			return false;
		}

		// if (to_file.isDirectory())
		// to_file = new File(to_file, from_file.getName());

		if (to_file.exists()) {
			if (!to_file.canWrite()) {
				WebLinkLogLoader.getLogger(FileUtils.class).debug("FileCopy: destination file is unwriteable: "
										+ to_name);
				return false;
			}

		}
		return true;
	} // filecheck

	public static long isFilePresent(String name) throws IOException {
		// name is fullpath + filename
		// return filesize
		File file = new File(name); // Get File objects from Strings
		long size = 0;
		// First make sure the source file exists, is a file, and is readable.
		if (file.exists()) {
			size = file.length();
		} else
			WebLinkLogLoader.getLogger(FileUtils.class).debug("FileCopy: no such  file: " + name);
		return size;
	}

	public void makeCopies(List multidir) throws IOException {

		for (int i = 0; i < multidir.size(); i++) {
			HashMap hfile = (HashMap) multidir.get(i);
			String copyfile = (String) hfile.get("file_name");
			String fromdir = (String) hfile.get("fromdir");
			String todirs = (String) hfile.get("todir");

			if (todirs != null && todirs.length() > 0) {
				StringTokenizer st = new StringTokenizer(todirs, "*");
				while (st.hasMoreTokens()) {
					String todir = st.nextToken();
					if (todir != null && !todir.equals(fromdir)) {
						WebLinkLogLoader.getLogger(FileUtils.class).debug(" copying files ....." + todir + "  " + fromdir
										+ " " + copyfile);
						copyFiles(fromdir, todir, copyfile);
					}// if
				}// while st
			}// if

		}// for i
	}// end method

	public static int deleteFilesInDir(String fromdir) {
		int ret = 0; // 0-error, 1 - done
		if (fromdir == null || fromdir.length() == 0)
			return 0;
		WebLinkLogLoader.getLogger(FileUtils.class).info(" called deleteFilesInDir:" + fromdir);
		try {
			File f = new File(fromdir);
			if (f.isDirectory()) {
				// get all files in this dir
				// this is non recusrive
				File files[] = f.listFiles();
				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						if (files[i].isFile()) {// delete only if its a file
							files[i].delete();
							WebLinkLogLoader.getLogger(FileUtils.class).info(" called deleteFilesInDir deleted:"
											+ files[i]);
						}
					}
				}
			}
			ret = 1;
		} catch (Exception e) {
			WebLinkLogLoader.getLogger(FileUtils.class).error(e, e);
		}
		return ret;
	}

	public static String getFilePath(String filename) {
		if (filename == null)
			return "";
		int fileNameIndex = filename.lastIndexOf("\\");
		String filePath = "";
		if (fileNameIndex >= 0)
			filePath = filename.substring(0, fileNameIndex + 1);
		else {// look for :
			fileNameIndex = filename.lastIndexOf(":");
			if (fileNameIndex >= 0)
				filePath = filename.substring(0, fileNameIndex + 1);
		}

		return filePath;
	}

	public static String getFileName(String filenamewithpath) {
		if (filenamewithpath == null)
			return "";
		int fileNameIndex = filenamewithpath.lastIndexOf("\\");
		String filename = filenamewithpath;
		if (fileNameIndex >= 0)
			filename = filenamewithpath.substring(fileNameIndex + 1);
		else {// look for :
			fileNameIndex = filenamewithpath.lastIndexOf(":");
			if (fileNameIndex >= 0)
				filename = filenamewithpath.substring(fileNameIndex + 1);
		}

		return filename;
	}

	
	public static boolean filePresent(String filenamewithpath) {
		if (filenamewithpath == null)
			return false;
		File file = new File(filenamewithpath);
		if (file.exists())
			return true;

		return false;
	}

	public static long fileSize(String filenamewithpath) {
		if (filenamewithpath == null)
			return 0;
		File file = new File(filenamewithpath);
		if (file.exists())
			return file.length();

		return 0;
	}

	public static String getFileExtension(String filename) {
		if (filename == null)
			return "";
		int fileNameIndex = filename.lastIndexOf(".");
		String fileExt = "";
		if (fileNameIndex >= 0)
			fileExt = filename.substring(fileNameIndex + 1);

		return fileExt;
	}

	public static String getFileNameWithoutExtension(String filename) {
		if (filename == null)
			return "";
		String nm = FileUtils.getFileName( filename);
		int fileNameIndex = nm.lastIndexOf(".");
		String fileExt = nm;
		if (fileNameIndex >= 0)
			fileExt = nm.substring(0,fileNameIndex );

		return fileExt;
	}

	public static boolean isValidFile(String inputfile){
		String ext = getFileExtension(inputfile);
		if(ext == null) return false; 
		if(ext.endsWith("xml")) return true;
		
		return false;
		
	}
	public static String displayFileSize(int filesize) {

		if (filesize <= 0)
			return "0 KB";
		int mb = filesize / (1024 * 1024);
		int mbrem = (filesize % (1024 * 1024));
		StringBuffer buf = new StringBuffer("");
		if (mb > 0) {
			if (mbrem > 0)
				mb++;
			buf.append(mb + " MB");
		} else {
			int kbrem = (filesize % 1024);
			int kb = (int) (filesize / 1024);
			if (kbrem > 0)
				kb++;
			buf.append(kb + " KB");
		}

		WebLinkLogLoader.getLogger(FileUtils.class).debug(
				"displayFileSize inp " + filesize + " ret: " + buf.toString());
		return buf.toString();

	}

	public static void writeOutputFile(String fileName, String outXml) {
		writeOutputFile(ResponseFileServiceBaseDirectory, fileName,  outXml);
	}
	
	public static void writeOutputFile(String path, String fileName, String outXml) {
		java.io.FileOutputStream fos = null;
		try {
			//BufferedWriter bw = new BufferedWriter(new FileWriter(ResponseFileServiceBaseDirectory+
			//														File.separator + fileName));
			
			byte[] contents =  outXml.getBytes();
			fos = new java.io.FileOutputStream(   path+File.separator + fileName);
			
			fos.write(contents);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			logger.error(e,e);
		}finally{
			try{
			if(fos != null)
				fos.close();
			}catch(Exception ee){}
		}
	}
	
	public static File writeErrorContent(String filename, List<String> errorcontent)throws IOException {
		StringBuilder buf = new StringBuilder();
		if(errorcontent != null){
			buf.append("<error>\n");
			for(String str : errorcontent) buf.append("<message>").append(str).append("</message>\n");
			buf.append("</error>\n");
		}
		return writeContent(filename,buf.toString());
	}
	
	public static File writeContent(String filepathwithname, String content)throws IOException {
		 
		 if(filepathwithname == null || filepathwithname.length()== 0 || content == null) return null;
		 java.io.FileOutputStream fos = null;
		 WebLinkLogLoader.getLogger(FileUtils.class).debug("writeContent writing  to:"+filepathwithname);
		 File f = null; 
		 byte[] bytes =  content.getBytes();
		 try{
			  fos = new java.io.FileOutputStream( filepathwithname);
			  fos.write(bytes);
			  fos.flush();
			  fos.close();
		 }catch(IOException io){
			 WebLinkLogLoader.getLogger(FileUtils.class).error(io,io);
			 throw io;
		 }
		 finally{
			 try{
				 if(fos != null) fos.close();
				 f = new File(filepathwithname);
				 
			 }catch(IOException ioe){
				 WebLinkLogLoader.getLogger(FileUtils.class).error(ioe,ioe);
			 }
		 }
		 return f;
	}
	
	public static void deleteOutputFile(String fileName) {
		WebLinkLogLoader.getLogger(FileUtils.class).debug("Deleting outputfile "+ResponseFileServiceBaseDirectory+File.separator+fileName );
		File f = new File(SuccessFileServiceBaseDirectory+File.separator+fileName);
		f.delete();
	}
	
	public static void updateLog4jProperties(String pathstr) {
		updateLog4j(MAINFILEPROP, pathstr,MAINLOG); //main log
    	updateLog4j(JOBFILEPROP,pathstr,JOBFILEPROPVAL); //clientlog
    }
	
	private static void updateLog4j(String[]FileProp,String pathstr,String[] filePropVal) {
		Properties props = new Properties();
    	FileInputStream fis = null;
    	FileOutputStream fos = null;
    	try {
    		fis = new FileInputStream(appHome+ "lib/log4j.properties");
    		props.load(fis);
    		for(int i = 0; i < FileProp.length; i++) {
    			props.setProperty(FileProp[i],pathstr+filePropVal[i]);
    		}
    		fos = new FileOutputStream(appHome + "lib/log4j.properties");
    		props.store(fos,"updated version");
    	}
    	catch(FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
    	catch(IOException e) {
    		e.printStackTrace();
    	}
    	finally {
    		try {
	    		if(fis != null) {
	    			fis.close();
	    		}
	    		if(fos != null) {
	    			fos.close();
	    		}
    		}
    		catch(IOException ioe) {
    			ioe.printStackTrace();
    		}
    	}
	}
	
	public static void checkAndCreatDir(String dir) {
		File f = new File(dir);
		if(f.exists())
			return;
		f.mkdirs();
					
	}
	
	public static void createDateDirs(String today) {
		File f = new File(FileUtils.ResponseFileServiceBaseDirectory
				+ File.separator + today);
		if (!f.exists())
			f.mkdir();
		f = new File(FileUtils.ErrorFileServiceBaseDirectory + File.separator
				+ today);
		if (!f.exists())
			f.mkdir();
		f = new File(FileUtils.ErrorFileServiceBaseDirectory + File.separator
				+ today + File.separator + ATTACHMENTS_FILE_DIRECTORY);
		if (!f.exists())
			f.mkdir();
		f = new File(FileUtils.SuccessFileServiceBaseDirectory + File.separator
				+ today);
		if (!f.exists())
			f.mkdir();
		f = new File(FileUtils.SuccessFileServiceBaseDirectory + File.separator
				+ today + File.separator + ATTACHMENTS_FILE_DIRECTORY);
		if (!f.exists())
			f.mkdir();
		if(isCallbackService)
			f = new File(FileUtils.CallbackFileServiceBaseDirectory + File.separator
				+ today);
		if (!f.exists())
			f.mkdir();
	}

	/**
	 * Copy the attachments directory to another destination directory. Always delete the source directory. 
	 * 
	 * @param attDir is the attachment directory to be copied
	 * @param toDir is the directory to copy the attachment directory to
	 */
	public static void moveAttachmentDirs(String attDir, String toDir)
		throws IOException
	{
		moveAttachmentDirs(attDir, toDir, true);
	}
	
	/**
	 * Copy the attachments directory to another destination directory 
	 * 
	 * @param attDir is the attachment directory to be copied
	 * @param toDir is the directory to copy the attachment directory to
	 */
	public static void moveAttachmentDirs(String attDir, String toDir, boolean delete)
		throws IOException
	{
		if ( attDir == null || toDir == null || attDir.equalsIgnoreCase(toDir))
		{
			return;
		}
		File aDir = new File(attDir);
		File tDir = new File(toDir);
		if ( !aDir.exists() )
		{
			return;
		}
		if ( !tDir.exists() )
		{
			tDir.mkdir();
		}
		
		File[] atts = aDir.listFiles();
		logger.debug("Moving attachments from " + attDir + " to " + toDir);
		for ( File att : atts )
		{
			logger.debug("aDir path: " + aDir.getAbsolutePath() + ";tDirAtt path: " + tDir.getAbsolutePath() + ";att name: " + att.getName());
			copyFiles(aDir.getAbsolutePath(), tDir.getAbsolutePath(), att.getName(), att.getName(), true);
		}
		if ( delete )
		{
			aDir.delete();
			File tempatt = new File(FileUtils.TempFileServiceAttDirectory);
			File[] files = tempatt.listFiles();
			logger.debug("files size in tempatt: " + files.length);
			// deleting the zip file under temp/attachments
			for ( File f : files )
			{
				if ( !f.isDirectory() )
				{
					logger.debug("Deleting the file: " + f.getAbsolutePath());
					f.delete();
				}
			}
		}
	}
	
	public static File getSaveFileforUDR() throws IOException{
		String homedir = WSConstants.appHome +"/lib";
		File dir = new File(homedir);
		return File.createTempFile("udr", ".xml", dir);
		 
	}
	
	public static String filterFileName(String str){
		if( str == null)
			return "";
		char[] chars = str.toCharArray();
		StringBuilder buf = new StringBuilder();
		for(char ch : chars){
			if(	ch == '\\' || ch ==  '/' || ch== ':' || ch=='*' || ch== '?' || ch == '"' || ch =='<'  || ch== '>'  || ch=='|' )
				continue;
			buf.append(ch);
		}
		return buf.toString();
	}

	public static String[] writeXMLContents(String dirtype, String typename, int company_id, String contents)	throws IOException		{

			String path = InitialSetUp.basefilepath+File.separator+company_id+File.separator+dirtype+File.separator+WSUtil.getDateString();
			String filename= typename+".xml";
			File dir = new File(path);
			if ( !dir.exists() )
				dir.mkdirs();
			writeContent(path+File.separator+filename,contents);
			String[] str = new String[2];
			str[0] = filename;
			str[1] = path;
			if(logger.isDebugEnabled())
				logger.debug("writeXMLContents  "+filename+" path :"+path+" :::"+contents );
			return str;
			
	}
	
	public static String getBPPrefixFromFile(String filename){
		String nm = FileUtils.getFileName( filename);
		int sindex = nm.indexOf("unifier_");
		int eindex = nm.indexOf("_bp");
		if(sindex < 0 ||  eindex < 0 || sindex >= eindex )
			return "***error***";
			
		return nm.substring(sindex+8, eindex);	
			
	}
	
}
