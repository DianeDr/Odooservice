/*
 * Created on Sep 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.cf.tkconnect.util;


import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import com.cf.tkconnect.util.FileDrop;

import com.cf.tkconnect.log.Log;
import com.cf.tkconnect.log.LogSource;
import com.cf.tkconnect.log.WebLinkLogLoader;
import com.cf.tkconnect.util.FileUtils;
import com.cf.tkconnect.util.PropertyManager;
import com.cf.tkconnect.util.InitialSetUp;
import com.cf.tkconnect.util.WSConstants;

import static com.cf.tkconnect.util.WSConstants.*;
import static com.cf.tkconnect.util.WSUtil.toInt;

/** 
 * @author cyril
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public final class InitialSetUp   {
	static Log logger = LogSource.getInstance(InitialSetUp.class);
	
	public static Map<String,String> odoo = new HashMap<String,String>();
 	public static  int Interval =60000;// default settings for polling the directory
 	public static String base_directory = "";
// 	public static String authKey = ""; //set in ConfigUI
// 	public static String url = ""; //set in ConfigUI
 	public static boolean useProxy = false; //set in ConfigUI
 	static FileDrop fileSource = null;

 	public static boolean isCallbackService = false; //set in dynamically 
 	public static boolean isSystemCallbackService = false; //set in ConfigUI
 	public static String servicestyle = DOCUMENT_SERVICES; //default service
 	public static  int max_thread_count = MAX_THREADS;// default settings
 	public static  int callback_poll_server = 30;//12000;// default 200 mins
 	
 	public static  Map<Integer,String> callbacklist = new HashMap<Integer,String>();// default 200 mins
 	public static String sendemail = "no"; //if timekarma needs to be sent an email
 	public static String sendemailoncomplete = "no"; //if timekarma needs to be sent an emai
 	public static String senderrormail = "no"; //
 	public static boolean nocallback = false;
 	//public static FTPFileService ftpservice;
 	public static String useftp;
 	public static String cronScheduler="no";
 	//public static Client ftpclient;
 	public static String basefilepath = ""; //base path for storing all processed reqd files
 	public static boolean isusermode = true;
 	public static int SOCKET_PORT = 0;
	public static String directoryService = "no";
	public static String writeerror = "yes"; //thsse 3 are only for writing the response for external files
	public static String writesuccess = "yes"; // usage only
	public static String writeresponse = "yes"; 
	public static int runningmode = TKLINK_EXTERNAL; //default setting for weblink running mode  is external 
	public static String isproxyset= "no";
	public static String attachzip= "yes";
	public static boolean started = false;
	public static boolean inprocess		= false;
	public static  int  cleanupinterval = 21600000; // 6 hours = 21600000 ms
	public static  int  syncinterval = 7; // 6 hours = 21600000 ms
	public static String appHome = "";;
	private static String database_alter ="no";
	public static boolean directoriesSet = false;
	
    public void init(String appHome) {  
    	
    	
    	this.appHome = appHome;
    	if(logger.isDebugEnabled())
    		logger.debug("connect in  starting with prop appHome ::"+appHome);    	
	//	PropertyManager.setPropsName(PROPERTY_FILENAME);
       
        String interval = PropertyManager.getSysProperty(PROPERTY_SCAN_INTERVAL); 
        syncinterval = toInt(PropertyManager.getSysProperty(UNIFIER_SYNC_INTERVAL),7); 
        directoryService = PropertyManager.getSysProperty(PROPERTY_DIRECTORY_SERVICE,"yes");
       
        // start of the cron scheduler
       			 
		if("yes".equals(PropertyManager.getSysProperty(PROPERTY_ISPROXYSET,"no")))
			useProxy  = true;
	

        if("no".equals(PropertyManager.getSysProperty(PROPERTY_NO_CALLBACK,"no"))){ // set for backward compatibilty
        		isCallbackService = false;
        		nocallback = true;;
        }
 		String socketport = PropertyManager.getSysProperty(PROPERTY_SOCKET_PORT,"9000");
        SOCKET_PORT =toInt(socketport,9000);
       
	    if (interval != null) {
	    		Interval = toInt(interval);
	    		if(Interval < MIN_INTERVAL) 
	    			Interval = MIN_INTERVAL;
	    }

		base_directory = PropertyManager.getSysProperty(PROPERTY_BASE_DIRECTORY,"");
		basefilepath = PropertyManager.getSysProperty(PROPERTY_WEB_FILE_ROOT_PATH,"");
		// get all company details only first time from it will come from database
		odoo.put("url", PropertyManager.getSysProperty("tkconnect.odoo.url"));
		odoo.put("username", PropertyManager.getSysProperty("tkconnect.odoo.username"));//username
		odoo.put("password", PropertyManager.getSysProperty("tkconnect.odoo.password"));
		odoo.put("file_location",base_directory);
		odoo.put("db",  PropertyManager.getSysProperty("tkconnect.odoo.database.name"));
		if(logger.isDebugEnabled())
			logger.debug("in standalonemode setting username:::::"+odoo.get("username")+" url::"+odoo.get("url"));
		// process this from the UI  on startup
		//setUpDirectories();
		
/*	    if(useProxy) {
        	setProxy();
        }else
        	unsetProxy();
 */       
		if(base_directory != null && base_directory.trim().length() > 0  )
					setUpDirectories();
		
		System.out.println(" Started polling");
    }
    
    
    public static void setUpDirectories(){
    	try{
	    	if(directoriesSet){
	    		startPolling();
	    		return;
	    	}	
	    	
    		
			FileUtils.checkAndCreateFileDirectorySystem(base_directory);
			//if("yes".equals(directoryService))
			startPolling();
			directoriesSet = true;
		}catch(IOException ioe){
			logger.error(ioe,ioe);
		
	    }catch(Exception e){
			logger.error(e,e);
		}
    }
    
   
    
  public static void startPolling() throws Exception{
	  if(directoryService.equalsIgnoreCase("yes")){
		  	if(logger.isDebugEnabled())
		  		logger.debug("startPolling  started polling directory ");
		    fileSource = new FileDrop();
			fileSource.init(Interval);
			fileSource.activate();
	  }
  }
    
    public static void stopPolling() {
    	if(logger.isDebugEnabled())
	  		logger.debug("stopPolling  stop polling directory ");
//		if(runningmode !=TKLINK_EXTERNAL) 
			fileSource.deActivate();
//		if(runningmode!=TKLINK_STANDARD)		
//			callbackservicetask.stop();
//		if("yes".equals(useftp) && ftpservice != null){
//			ftpservice.stop();
//		}
    }
    
    public static synchronized Map<Integer,String> useCallbackList(boolean set, int setvalue, String directory){
		Map<Integer,String> dlist = new HashMap<Integer,String>();
    	if(set){
    		callbacklist.put(setvalue,directory);
    	}else{// its consume
    		for( int key : callbacklist.keySet() )
    			dlist.put(key,callbacklist.get(key));
    		
    		callbacklist.clear();
    	}
    	return dlist;
    }
    
    private static void setProxy() {
    	String proxyHost = PropertyManager.getProperty(PROPERTY_PROXYHOST);
    	if(proxyHost == null || proxyHost.trim().length() == 0 ) 
    		return;
    	String proxyPort = PropertyManager.getProperty(PROPERTY_PROXYPORT);
    	if(proxyPort == null || proxyPort.trim().length() == 0) {
    		proxyPort = "80";
    	}
    	try {
	    	System.setProperty("http.proxyHost",proxyHost.trim());
	    	System.setProperty("http.proxyPort",proxyPort.trim());
    	}
    	catch(SecurityException e) {
    		WebLinkLogLoader.getLogger(InitialSetUp.class).error(e.getMessage());
    	}
    
    }
    private static void unsetProxy() {
    	try {
	    	System.clearProperty("http.proxyHost");
	    	System.clearProperty("http.proxyPort");
    	}
    	catch(SecurityException e) {
    		WebLinkLogLoader.getLogger(InitialSetUp.class).error(e.getMessage());
    	}
    
    }
    
    /**
     * Thread that periodically cleans up the request/attachments directory.
     */
    class SyncTask extends TimerTask
    {
    	@Override
    	public void run() 
    	{
    		if(logger.isDebugEnabled())
    			logger.debug("SyncTask thread started at: " + new java.util.Date());
    		// first check if service is running
    		while(inprocess){
    			try{
    				Thread.sleep(1000*60*60*24*syncinterval);
    				//call syncup
    				
    			}catch(InterruptedException ie){}
    		}
    	}
    }

    class CleanupTask extends TimerTask
    {
    	@Override
    	public void run() 
    	{
    		if(logger.isDebugEnabled())
    			logger.debug("Cleanup thread started at: " + new java.util.Date());
    		// first check if service is running
    		int count = 0;
    		while(inprocess){
    			try{
    				Thread.sleep(1000*60*60);
    				count++;
    				if(count > 10){
    					if(logger.isDebugEnabled())
    						logger.debug("Cleanup thread exited at: " + new java.util.Date());
    					return;// too many tries do it nexttinme
    				}
    			}catch(InterruptedException ie){}
    		}
    		// Clean up the files based on time calculation
    		File reqatt = new File(FileUtils.InputFileServiceAttDirectory);
    		File[] files = reqatt.listFiles();
    		long currentTime = System.currentTimeMillis();
    		for ( File f : files )
    		{
    			long modifiedTime = f.lastModified();
    			// file older than 5 hours (18000000 ms)
    			if(logger.isDebugEnabled())
    				logger.debug("currentTime - modifiedTime: " + (currentTime - modifiedTime));
    			if ( Math.abs(modifiedTime - currentTime) > 18000000 )
    			{
        			logger.info("Deleting file: " + f.getAbsolutePath());
        			f.delete();    				
    			}
    		}
    		if(logger.isDebugEnabled())
    			logger.debug("Cleanup thread stopped at: " + new java.util.Date());
    	}
    }

}
