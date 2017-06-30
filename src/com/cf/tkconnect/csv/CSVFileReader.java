package com.cf.tkconnect.csv;

import au.com.bytecode.opencsv.CSVReader;
import java.io.Reader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CSVFileReader extends CSVReader{
	
	protected boolean skipBlankLines=true;
	public CSVFileReader(Reader reader) {
		super(reader);				
		// TODO Auto-generated constructor stub
	}

	public CSVFileReader(InputStream is) {	
		super(new InputStreamReader(is));
		// TODO Auto-generated constructor stub
	}
	public String[] getLine() throws Exception{
		String[] line = readNext();		
		if(!skipBlankLines)
			return line;		
		while((line!=null)&&(line.length == 1)&&(line[0].trim().length() == 0)){
			//below line to check if reached the last line			
				line = readNext();
			
		}			
		return line;		
	}
	public void setSkipBlankLines(boolean blankLine){
		skipBlankLines = blankLine;
	}
}
