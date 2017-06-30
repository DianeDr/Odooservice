package com.cf.tkconnect.util;


import java.net.HttpURLConnection;
import java.net.URL;





public class PingServer {
	static com.cf.tkconnect.log.Log logger = com.cf.tkconnect.log.LogSource
			.getInstance(PingServer.class);

	

	public static String getServerResponse(String urlStr) {

		String response = null;
		HttpURLConnection con = null;
		try {
			// Execute the method.
			//URL url = new URL(getMainServiceUrl(urlStr));
			//logger.debug("pinging the url string urlStr::"+urlStr);
			URL url = new URL(urlStr);
			con = (HttpURLConnection)url.openConnection();
			int statusCode = con.getResponseCode();
			//logger.debug("getServerResponse for url:" + url			+ "  recd statusCode:" + statusCode);
		

		} catch (Exception e) {
			response = "Fatal protocol violation: " + e.getMessage();
			
		
			
		} finally {
			// Release the connection.
			if(con != null) {
				con.disconnect();
			}
		}
		//logger.debug("getServerResponse:"+response);
		return response;
	}
}