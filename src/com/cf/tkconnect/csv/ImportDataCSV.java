package com.cf.tkconnect.csv;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.cf.tkconnect.csv.CSVFileReader;
import com.cf.tkconnect.log.Log;
import com.cf.tkconnect.log.LogSource;
import com.cf.tkconnect.util.WSUtil;


public class ImportDataCSV {

	static Log logger = LogSource.getInstance(ImportDataCSV.class);

	String[] line;
	int lCount = 0; 
	String xmldata;
	Map<String,String> map;
	List<Map<String,String>> newlist  = new ArrayList<Map<String,String>>();
	
	public ImportDataCSV( ){
	}
	
	public List<Map<String,String>> processDDImport(String xmldata, Map<String,String> ddmap) throws Exception {
		this.newlist  = new ArrayList<Map<String,String>>();
		this.xmldata = xmldata;
		this.map = ddmap;
		CSVFileReader inputcsv = new CSVFileReader(new BufferedReader(new StringReader(xmldata)) );
		while ((line = inputcsv.getLine()) != null) {
			
			if( line.length == 0 ||   WSUtil.isBlankOrNull(line[0])  )
				continue;// there is nothing  
			
			lCount++;
			logger.debug("reading data lCount::"+lCount+" len:"+line.length+" line[0]:"+line[0]);
			if((line[0].equalsIgnoreCase("Data Definition - General"))) {
				//logger.debug(" line 1 is correct");
				continue;	
			}
			
			if(line.length < 6)
				continue;
			if(line[0].equalsIgnoreCase("Name") && line[1].equalsIgnoreCase("Data Type") )	{
				//logger.debug(" line 2 is correct");
				continue;
			}
			// 
			if(map.containsKey(line[0]))
				continue; // this dd is found
			
			Map<String,String> mapdd = new HashMap<String,String>();
			mapdd.put("Name", line[0]);
			mapdd.put("Data Type", line[1]);
			mapdd.put("Data Size", line[2]);
			mapdd.put("Input Type", line[3]);
			mapdd.put("Category", line[4]);
			mapdd.put("Used", line[5]);
			newlist.add(mapdd);
				
		}// while loop
		
		return newlist;
	}

	
	public List<Map<String,String>> processDEImport(String xmldata, Map<String,String> demap) throws Exception {
		this.newlist  = new ArrayList<Map<String,String>>();
		this.xmldata = xmldata;
		this.map = demap;
		CSVFileReader inputcsv = new CSVFileReader(new BufferedReader(new StringReader(xmldata)) );
		while ((line = inputcsv.getLine()) != null) {
			
			if( line.length == 0 ||   WSUtil.isBlankOrNull(line[0])  )
				continue;// there is nothing  
			
			lCount++;
			logger.debug("reading data lCount::"+lCount+" len:"+line.length+" line[0]:"+line[0]);
			if((line[0].equalsIgnoreCase("Data Elements"))) {
				//logger.debug(" line 1 is correct");
				continue;	
			}
			
			if(line.length < 3)
				continue;
			if(line[0].equalsIgnoreCase("Data Element") && line[1].equalsIgnoreCase("Data Definition") )	{
				//logger.debug(" line 2 is correct");
				continue;
			}
			// 
			if(map.containsKey(line[0]))
				continue; // this de is found
			
			Map<String,String> mapde = new HashMap<String,String>();
			mapde.put("Data Element", line[0]);
			mapde.put("Data Definition", line[1]);
			mapde.put("Form Label", line[2]);
			
			newlist.add(mapde);
				
		}// while loop
		
		return newlist;
	}

}
