package com.cf.tkconnect.util;


import static com.cf.tkconnect.util.FileUtils.*;
import static com.cf.tkconnect.util.InitialSetUp.useCallbackList;
import static com.cf.tkconnect.util.InitialSetUp.useftp;
import static com.cf.tkconnect.util.WSConstants.*;


import com.cf.tkconnect.log.Log;
import com.cf.tkconnect.log.LogSource;
import com.cf.tkconnect.log.WebLinkLogLoader;
import com.cf.tkconnect.util.FileUtils;
import com.cf.tkconnect.util.PropertyManager;
import com.cf.tkconnect.util.WSUtil;

import java.io.File;
import static java.io.File.separator;

import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ArrayList;
import java.util.Set; 
import java.util.Map;



public class WSUtil  {

	static Log logger = LogSource.getInstance(WSUtil.class);

	private static boolean stop = false; 
	private static String CALLBACK_ID = "<callback_id>";
	
	public WSUtil() {
	}

	public static WSUtil getInstance() {
		return new WSUtil();
	}

	public static String getUrl(String inpurl) {
		if (inpurl == null)
			return "";
		StringBuilder buf = new StringBuilder();
		String url = inpurl.toLowerCase();
		if (url != null && !url.startsWith("http"))
			buf.append( "http://");
		buf.append(url);
		if (url != null && url.endsWith("/"))
			buf.append( "ws/services");
		else
			buf.append("/ws/services");
		logger.debug("getUrl :"+buf);
		return buf.toString();
	}

	public static String getDocUrl(String inpurl) {
		if (inpurl == null)
			return "";
		StringBuilder buf = new StringBuilder();
		String url = inpurl.toLowerCase();
		if (url != null && !url.startsWith("http"))
			buf.append( "http://");
		buf.append(url);
		if (url != null && url.endsWith("/"))
			buf.append( "ws/us/services");
		else
			buf.append("/ws/un/services");
		logger.debug("getDocUrl :"+buf);
		return buf.toString();
	}

	public static String getMainServiceUrl(String inpurl) {

		return (getUrl(inpurl) + "/mainservice");
	}

	public static String geDocumentServiceUrl(String inpurl) {

		return (getDocUrl(inpurl) + "/UnifierWebServices");
	}
	public static String getServiceUrl(String inpurl, String service) {
		
		return (getUrl(inpurl) + "/" + service);
	}
	
	public static String toXML(List list, String maintag, String subtag,
			boolean setmaintag, boolean setsubtag, String excludes,
			String supress, int indent) {
		return toXML(list, subtag, maintag, setmaintag, setsubtag, excludes,
				supress, indent, true);
	}

	public static String toXML(List list, String maintag, String subtag,
			boolean setmaintag, boolean setsubtag, String excludes,
			String supress, int indent, boolean setvalue) {
		return "";
	}

	public static String getIndent(int ct) {
		StringBuffer buf = new StringBuffer("");
		for (int i = 0; i < ct; i++)
			buf.append(" ");
		return buf.toString();

	}

	

	public static String getDateString() {
		GregorianCalendar gc = new GregorianCalendar();
		return MONTHS[gc.get(Calendar.MONTH)] + "-" + gc.get(Calendar.DATE)
				+ "-" + gc.get(Calendar.YEAR);
	}
	
	public static synchronized void setStop(boolean s) {
    	stop = s;
    }
    


	private static String getProcessedContents(String contentsval, String inputxmlval){
		StringBuilder buf = new StringBuilder();
		String inputxml = stripISOTags(inputxmlval);
		String contents = stripISOTags(contentsval);
		int ind = inputxml.indexOf("<smartlink>");
		//logger.debug("in getProcessedContents contents::"+contents+" inputxml::"+inputxml+" ind::"+ind);
		if(ind  >= 0){
			buf.append("<smartlink>\n" );
			buf.append(contents).append("\n");
			buf.append(inputxml.substring(ind+7));
			buf.append("\n");
		}else{
			buf.append("<smartlink>\n" );
			buf.append(contents).append("\n");
			buf.append(inputxml).append("\n");
			buf.append("</smartlink>\n" );
		}
		//logger.debug("getProcessedContents buf::"+buf.toString());
		return buf.toString();
	}
	
	private static String stripISOTags(String inputxml){
		if(inputxml == null) return "";
		StringBuilder buf = new StringBuilder();
		int startindex = inputxml.indexOf("<?xml version");
		if(startindex < 0) return inputxml;
		int endindex = inputxml.indexOf("?>", startindex) ;
		if(endindex <= startindex){
			logger.error("stripISOTags found errors:"+inputxml);
			return inputxml;
		}
			// now strip this 
		buf.append(inputxml.substring(0, startindex));
		buf.append(inputxml.substring(endindex+2)); // skip ?>
		return buf.toString();
	}
	


	
	private static void processCallback(String recdXML, String fName_noext, String today) throws IOException{
		
		int ind = recdXML.toLowerCase().indexOf(CALLBACK_ID);
		int id = 0;
		if(ind >= 0){
			String sub = recdXML.substring(ind+CALLBACK_ID.length());
			ind =  sub.indexOf("<");
			if(ind > 0) sub = sub.substring(0,ind);
			try{
				id = Integer.parseInt(sub);
			}catch(Exception e){}
			logger.debug("processCallback sub:"+sub+" id:"+id);
			if(id <= 0) return;
		}
		String outfilename = fName_noext + "__"+id+"__.xml";
		logger.debug("(CallBack)Writing XML contents extracted from response object returned by the server to : "
						+ outfilename);
		WebLinkLogLoader.JobLogger
				.debug("(CallBack)Writing XML contents extracted from response object returned by the server to : "
						+ outfilename);
		logger.debug("processCallback:"+recdXML);
			 copyFileContent(
					 TempFileServiceBaseDirectory+separator +fName_noext+".xml",
					 CallbackFileServiceBaseDirectory
							+ separator + today+separator +outfilename,
							recdXML, true);
	logger.debug("(CallBack)Finished writing received xml file:"+outfilename);
		WebLinkLogLoader.JobLogger
						.debug("(CallBack)Finished writing received xml file.");
			 
		// set this in the Map
		useCallbackList(true,id, CallbackFileServiceBaseDirectory
							+ separator + today+ separator +outfilename);
	}


	
	public static void sendFileToFTPServer(String file) {
		List<String> files = new ArrayList<String>();
		files.add(file);
		sendFilesToFTPServer(files);
	}
	public static void sendFilesToFTPServer(List<String> files) {
		try{
			// get the put directory
			String dir =  PropertyManager.getProperty(PROPERTY_FTP_RESPONSE_DIRECTORY,"output");
			logger.debug("sending  attachments...to:"+dir);
//			if(ftpclient != null){
//				ftpclient.changeDir(dir);
//				ftpclient.putFiles(dir, files);
//				ftpclient.setBaseDirectory();
//			}
		 }catch(Exception me){
			 logger.error(me,me);
		 }
			
	}
	


	public static String getErrorString(String[] errors){
		StringBuilder buf = new StringBuilder();
		if(errors == null) return buf.toString();
		for(String error:errors)
			buf.append(error).append("\n");
		return buf.toString();
	}

	public static String getCallbackFileName(String filename, int id, boolean withext){
		if( filename== null  ) return "";
		int ind = filename.indexOf("__"+id+"__");
		if(ind < 0) return filename;
		String sub = filename.substring(0,ind);
		if(withext) sub = sub+".xml";
		return sub;
	}
	
    public static boolean stopped() {
    	return stop;
    }
    
    public static String filter(String value) {

        if (value == null)
            return (null);

        char content[] = new char[value.length()];
        value.getChars(0, value.length(), content, 0);
        StringBuilder result = new StringBuilder(content.length + 50);
        for (int i = 0; i < content.length; i++) {
        	//TW#22454 Need to support unicode charset for integration, so below check is commented.
      	  //if(content[i] > 127) continue;// remove non ascii chars
            switch (content[i]) {
            case '<':
                result.append("&lt;");
                break;
            case '>':
                result.append("&gt;");
                break;
            case '&':
                result.append("&amp;");
                break;
            case '"':
                result.append("&quot;");
                break;
            default:
                result.append(content[i]);
            }
        }
        return (result.toString());

    }

	public static boolean checkInputValue(String s, Map inputMap) {
		Set keySet = inputMap.keySet();
		if (!keySet.contains(s))
			return false;
		else {
			String v = (String) inputMap.get(s);
			if (v == null || v.trim().length() == 0)
				return false;
		}
		return true;
	}

	 public static int toInt(String val){
		 if(val == null || val.trim().length() == 0) return 0;
		 try{
			 return Integer.parseInt(val);
		 }catch(Exception e){
			 return 0;
		 }
	 }

	 
	 public static int toInt(String val, int defaultvalue){
		 if(val == null || val.trim().length() == 0) return defaultvalue;
		 try{
			 return Integer.parseInt(val);
		 }catch(Exception e){
			 return defaultvalue;
		 }
	 }
 
	 public static long toLong(String val){
		 if(val == null || val.trim().length() == 0) return 0;
		 try{
			 return Long.parseLong(val);
		 }catch(Exception e){
			 return 0;
		 }
	 }

	
	private static boolean checkFiles(String iszipfile, String zipfile,  File principalFile, List<String> filenames, String today) throws Exception {
		boolean error = false;
		if ( "yes".equalsIgnoreCase(iszipfile) )
		{
			// Check for the existence of the zip file
			File zf = new File(FileUtils.InputFileServiceAttDirectory + File.separator + zipfile);
			logger.error("checking zip for:"+zf.getAbsolutePath());
			if ( !zf.exists() )	{
				StringBuilder message = new StringBuilder();
				message.append("The specified zip file: ").append(zipfile).append(" could not be found in the directory: ").append(
						FileUtils.InputFileServiceAttDirectory);
				WebLinkLogLoader.JobErrorLogger.error(message.toString());
				logger.error(message.toString());

				moveErrorFiles(TempFileServiceBaseDirectory, ErrorFileServiceBaseDirectory + separator + today,
						principalFile.getName(), message.toString(), true);
				
				throw new Exception(message.toString());				
			}
		}
		else
		{
			StringBuilder errorfiles = new StringBuilder();
			List<File> existingFiles = new ArrayList<File>();
			for (String filename : filenames) 
			{

				File srcAtt = new File(FileUtils.InputFileServiceAttDirectory + File.separator + filename);
				logger.error("checking att for:"+srcAtt.getAbsolutePath());
				if ( !srcAtt.exists() )
				{
					error = true;
					errorfiles.append(filename).append("; ");
					logger.error("not found checking att for:"+srcAtt.getAbsolutePath());
				}
				else
				{
					existingFiles.add(srcAtt);
				}
			}
			
			if ( error )
			{
				WebLinkLogLoader.JobErrorLogger.error("One or more attachments listed in the web request do not exist in the directory: " +
					FileUtils.InputFileServiceAttDirectory +" error files :"+errorfiles.toString() );
				logger.error("One or more attachments listed in the web request do not exist in the directory: " +
						FileUtils.InputFileServiceAttDirectory+" error files :"+errorfiles.toString() );

				// move attachment files from request attachments to error
				String tempdirname = "_" + principalFile.getName();
				for ( File existing : existingFiles )
				{
					copyFiles(FileUtils.InputFileServiceAttDirectory, FileUtils.ErrorFileServiceBaseDirectory + File.separator + today + 
						File.separator + WSConstants.ATTACHMENTS_FILE_DIRECTORY + File.separator + tempdirname, 
						existing.getName(), existing.getName(), true);
				}
				moveErrorFiles(
						 TempFileServiceBaseDirectory,
						 ErrorFileServiceBaseDirectory
								+ separator + today,
								principalFile.getName(),"One or more attachments listed in the web request do not exist in the directory: " +
								FileUtils.InputFileServiceAttDirectory+" error files :"+errorfiles.toString(), true);
				
				throw new Exception("One or more attachments listed in the web request do not exist in the directory: " +
						FileUtils.InputFileServiceAttDirectory+" error files :"+errorfiles.toString() );
				
			}			
		}
		return error;
	}
	
	public static boolean isBlankOrNull(String str){
		if(str == null || str.trim().length() == 0)
			return true;
		return false;
	}
	
	
	public static Date parseDate(String date, String format){
		if(date == null || date.length() == 0 || format== null)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try{
			Date dt = sdf.parse( date, new ParsePosition(0));
			return dt;
		}catch(Exception e){
			return null;
		}
	
	}
	
	 public static String jsfilter(String value) {

	        if (value == null)
	            return ("");

	        char content[] = new char[value.length()];
	        value.getChars(0, value.length(), content, 0);
	        StringBuffer result = new StringBuffer(content.length + 50);
	        for (int i = 0; i < content.length; i++) {
	            switch (content[i]) {
	            case '\'':
	            case '"':
	                result.append('\\');
	                result.append(content[i]);
	                break;
	            case '\r':
	            case '\n':
	                break;
	    		default:
	    			result.append(content[i]);
	    			break;
	            }
	        }
	        return (result.toString());

	    }
	 
	 public static String jsfilter2(String value) {

	        if (value == null)
	            return ("");

	        char content[] = new char[value.length()];
	        value.getChars(0, value.length(), content, 0);
	        StringBuffer result = new StringBuffer(content.length + 50);
	        for (int i = 0; i < content.length; i++) {
	            switch (content[i]) {
	            case '<':
	                result.append("&lt;");
	                break;
	            case '>':
	                result.append("&gt;");
	                break;
	            case '&':
	                result.append("&amp;");
	                break;
	            case '"':
	                result.append('\\');
	                result.append(content[i]);
	                break;
	            case '\\':
	                result.append('\\');
	                result.append(content[i]);
	                break;
	            case '\r':
	            case '\n':
	                break;
	    		default:
	    			result.append(content[i]);
	    			break;
	            }
	        }
	        return (result.toString());

	    }

	 public static String getExcelFileName(String filename){
		 if(isBlankOrNull(filename))
			 return "";
		 int index = filename.indexOf("unifier_");
		 if(index <=0)
			 return filename;
		 return filename.substring(index);
	 }
	 
} // end class
