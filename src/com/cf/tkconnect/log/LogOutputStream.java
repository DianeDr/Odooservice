package com.cf.tkconnect.log;

import java.io.OutputStream;
import java.io.IOException;

import com.cf.tkconnect.log.Log;
import com.cf.tkconnect.log.LogSource;

/**
 * The <code>OutputStream</code> that will use log4j for data sink
 */
public class LogOutputStream extends OutputStream {
      private Log logger; 

      public LogOutputStream(String category) throws IOException {
          logger = LogSource.getInstance(category);
      }

      public void write(byte b[])
      throws IOException {
	  	  logger.debug(new String(b));
      }
	  
      public void write(byte b[], int off, int len)
      throws IOException {
	  	  logger.debug(new String(b, off, len));
      }
      
      public void write(int b)
      throws IOException {
		  logger.debug(new Byte((byte)b));
      }

      public void flush()
      throws IOException {
      }

      public void close()
      throws IOException {
      }

}

