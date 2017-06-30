

package com.cf.tkconnect.util;

import java.io.File;
import java.util.Arrays;

import com.cf.tkconnect.util.DirSource;
import com.cf.tkconnect.util.FileUtils;


/**
 * DirSource monitors a directory for changes and sends new and deleted files
 * down the pipeline if the appropiate file streams are connected. Stream one is
 * for new files, stream two is for deleted files. When DirSource is first
 * started all files in the directory are sent.
 * 
 * @version 1.0, 08/28/05
 * @author Cyril Furtado
 * @author Copyright (c) 2005 by Skire.com
 */
public class DirSource  {

	static com.cf.tkconnect.log.Log logger = com.cf.tkconnect.log.LogSource
			.getInstance(DirSource.class);

	private DirMonitor monitor = null;

	String baseDirPath = null;

	/**
	 * Initilizes DirSource setting any properties as needed. Valid properties
	 * are:
	 * <p>
	 * <b>interval</b>How long to scan the directory for changes. In
	 * milliseconds. The default is 5 seconds.<br>
	 * <b>scan</b>Whether the directory should be continuously scanned. true or
	 * false. The default is true.<br>
	 * <b>dirPathStr</b>The directory path to scan.<br>
	 */
	public void init(int interval) {

		boolean scan = true;

		baseDirPath = FileUtils.FileServiceBaseDirectory;

		monitor = new DirMonitor(this, FileUtils.InputFileServiceBaseDirectory,
				interval, scan);
	}

	private void sendObject(File f, int num) {
		// send this object to the done file
	}

	/**
	 * Activates the http source
	 */

	public void activate() {
		// super.activate();
		/* Start Directory monitor */
		Thread wt = new Thread(monitor, "DirMonitor");
		wt.start();

	}

	/**
	 * Provide a description of the filter
	 */
	public String about() {
		return "DirSource Filter";
	}

	/**
	 * Called whenever a file is added to the monitored directory. The File
	 * object is sent to the second connect consumter.
	 */
	protected void addFile(File f) {
		sendObject(f, 0); // send to first connected consumer
	}

	/**
	 * Called whenever a file is deleted from the monitored directory. The File
	 * object is sent to the second connected consumer.
	 */
	protected void removeFile(File f) {
		sendObject(f, 1); // send to second connected consumer
		// do nothing for now
	}

	/**
	 * Inner class worker thread for checking a directory
	 */
	class DirMonitor implements Runnable {

		/* How long to wait between scans */
		private int interval;

		/*
		 * Whether dir checks are on or off. If off then the directory will be
		 * read once
		 */
		private boolean scan;

		/* The controller .. can be made an interface to generalize */
		private DirSource myBoss = null;

		/* The directory path to scan */
		private File dirPath = null;

		/* Files from the last directory */
		private File fileList[] = new File[0]; // the filelist

		/**
		 * Init a worker
		 */
		DirMonitor(DirSource myBoss, String dirPathStr, int interval,
				boolean scan) {
			this.myBoss = myBoss;
			this.dirPath = dirPathStr == null ? null : new File(dirPathStr);
			this.interval = interval;
			this.scan = scan;
		}

		/**
		 * Monitor a directory
		 */
		public synchronized void run() {

			// Let's first make sure we have a valid directory path..if not log
			// it and return
			if (dirPath == null || !dirPath.isDirectory()) {
				logger.debug("Not a directory[" + dirPath + "]");
				return;
			}

			while (true) {
				processDirectory();
				if (!scan)
					return; // exit if we are not in scan mode
				try {
					Thread.sleep(interval);
				} catch (InterruptedException ie) {
					// ignore it
				}
			}
		}

		/**
		 * process the directory
		 */
		private void processDirectory() {

			logger.debug("Processing directory [" + dirPath + "]");

			File newList[] = dirPath.listFiles();
			if (newList == null)
				return; // just in case the dir path is no longer valid

			// sort it
			Arrays.sort(newList);

			int oldListIndex = 0;
			int newListIndex = 0;

			// do a merge of the old list and new list
			while (oldListIndex < fileList.length
					|| newListIndex < newList.length) {
				if (oldListIndex >= fileList.length) {
					// we have files in the new list, not in the old one
					myBoss.addFile(newList[newListIndex++]);
				} else if (newListIndex >= newList.length) {
					// we have files in the old list not in the new list
					myBoss.removeFile(fileList[oldListIndex++]);
				} else {
					int ct = fileList[oldListIndex]
							.compareTo(newList[newListIndex]);
					if (ct == 0) {
						oldListIndex++;
						newListIndex++;
					} else if (ct < 0) {
						myBoss.removeFile(fileList[oldListIndex++]);
					} else {
						myBoss.addFile(newList[newListIndex++]);
					}
				}
			}

			// prepare for next time
			fileList = newList;
		}
	}
}
