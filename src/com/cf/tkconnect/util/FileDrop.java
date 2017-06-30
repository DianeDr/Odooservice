

package com.cf.tkconnect.util;


import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

import com.cf.tkconnect.csv.OdooService;
import com.cf.tkconnect.log.WebLinkLogLoader;

import com.cf.tkconnect.util.FileUtils;
import com.cf.tkconnect.util.InitialSetUp;
import com.cf.tkconnect.util.WSUtil;


/**
 * 
 * 
 *
 * @author Cyril Furtado
 * @author timekarma
 */
public class FileDrop {

	static com.cf.tkconnect.log.Log logger = com.cf.tkconnect.log.LogSource
			.getInstance(FileDrop.class);

	Thread wt = null;
	private boolean stop = false;
	Timer timer;
	int interval;


	/**
	 * Initilizes FileDrop setting any properties as needed. Valid properties
	 * are:
	 * <p>
	 * <b>interval</b>How long to scan the directory for changes. In
	 * milliseconds. The default is 5 seconds.<br>
	 * <b>scan</b>Whether the directory should be continuously scanned. true or
	 * false. The default is true.<br>
	 * <b>dirPathStr</b>The directory path to scan.<br>
	 */
	public void init(int interval) {
		this.interval = interval;
		logger.debug("Directory Polling Interval: " + interval);
		
		//monitor = new DirMonitor(FileUtils.InputFileServiceBaseDirectory,
		//		interval, scan);
	}

	public void activate() {
		/* Start Directory monitor */
		boolean scan = true;
		stop = false;
		timer = new Timer();
	    timer.schedule(new DirMonitor(FileUtils.InputFileServiceBaseDirectory,
					 scan),0, interval);
	
	}

	public void deActivate() {
		stop = true;
		 if(timer != null)
			 timer.cancel();
		
	}
	
	

	/**
	 * Provide a description of the filter
	 */
	public String about() {
		return "FileDrop Filter";
	}

	/**
	 * Inner class worker thread for checking a directory
	 */
	class DirMonitor extends TimerTask {

		private com.cf.tkconnect.log.Log logger = com.cf.tkconnect.log.LogSource
				.getInstance(DirMonitor.class);


		/*
		 * Whether dir checks are on or off. If off then the directory will be
		 * read once
		 */
		private boolean scan;

		/* The directory path to scan */
		private File dirPath = null;

		/**
		 * Init a worker
		 */
		DirMonitor(String dirPathStr, boolean scan) {
			
			this.dirPath = dirPathStr == null ? null : new File(dirPathStr);
			this.scan = scan;
		}

				
		/**
		 * Monitor a directory
		 */
		public synchronized void run() {

			// Let's first make sure we have a valid directory path..if not log
			// it and return			
			logger.debug("connect directory poll started ...");
			if (dirPath == null) {// || !dirPath.isDirectory()) {
				logger.error("Not a directory[" + dirPath + "]");
				return;
			}
				try {
					processDirectory();
					if (!scan) {
						return; // exit if we are not in scan mode
					}
				} catch (InterruptedException ie) {
					// ignore it
				} catch (Exception e) {
					// report this error
					logger.error(this.getClass().getName() + "--"
							+ e.getMessage());
				}
		}

		/**
		 * process the directory
		 */
		private void processDirectory() throws Exception {
				
			if(!WSUtil.stopped()) {
				
				logger.info("getting files from directory: " + dirPath);
				File files[] = dirPath.listFiles();
				if (files == null || files.length == 0)
					return; // just in case the dir path is no longer valid
				logger.debug("getting files from directory: " + dirPath+"  "+files.length);
				//if(!checkServer()) return; 
				for (int i = 0; i < files.length; i++) {
					String ext = FileUtils.getFileExtension(files[i].getAbsolutePath());
					logger.debug("Processing Input File  ::::: "+files[i].getAbsolutePath()+"  ext:"+ext);
					if (files[i].isDirectory() || ext == null)
						continue;
					if (!ext.equalsIgnoreCase("csv") && !ext.equalsIgnoreCase("xlsx") )
						continue;
					// sendFile(files[i]);
					logger.info("Processing Input File "	+ files[i].getAbsolutePath());
					
					if(files[i].getAbsolutePath().endsWith(".csv")){
						
						//need to process the csv
						OdooService os = new OdooService(files[i].getAbsolutePath());
						os.process();
						
					}
						
				}
			}
		}
		
		private boolean checkServer() {
			if(logger.isDebugEnabled())
				logger.debug("in checkServer url:"+InitialSetUp.odoo.get("url"));
			String url =InitialSetUp.odoo.get("url");
			if(url != null) {
				if(logger.isDebugEnabled())
					logger.debug("Cannot connect to server.  Skip processing directory."+dirPath);
				WebLinkLogLoader.JobErrorLogger.debug("Cannot connect to server.Skip processing directory."+dirPath);
				return false;
			}
			
			
			return true;
		}
	}// dir class

}
