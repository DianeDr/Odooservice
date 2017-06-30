package com.cf.tkconnect.log;

import com.cf.tkconnect.log.Log;
import com.cf.tkconnect.log.LogSource;

public class WebLinkLogLoader {
	
	public static Log JobLogger = null;
	public static Log JobErrorLogger = null;

	public static void initLoggers() {
		JobLogger = LogSource.getInstance("smartlinkjob");
		JobErrorLogger = LogSource.getInstance("smartlinkjoberror");
	}
	
	public static Log getLogger(Class c) {
		return LogSource.getInstance(c);
	}
	
}
