package com.cf.tkconnect.csv;


import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cf.tkconnect.log.Log;
import com.cf.tkconnect.log.LogSource;
import com.cf.tkconnect.util.InitialSetUp;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;;

/**
 * 
 * This class will process the CSV file specified in the integrator flow definition. 
 *
 */
public class LoadGeneralCSVData {
	
	static Log logger = LogSource.getInstance(LoadCSVData.class);
	
	List<String[]> allRows = null;
	String csvFilePath = null;
	List<String> upperdenames = new ArrayList<String>();
	
	List<String[]> upperdata = new ArrayList<String[]>();
	
	private int pointer=0;
	String type ="wbs";// for wbs & Shell
	// first line is header
	// remaining are rows
	
	
	public LoadGeneralCSVData() {
		
	}
	
	
	public void setFile(String file) throws FileNotFoundException{
		//check if the file exists in the path
		String csvFile = file;
		File f = new File(csvFile);
		if(!f.exists()) {
			String integrationDir = InitialSetUp.basefilepath;
			csvFile = integrationDir + File.separator + file;
			f = new File(csvFile);
			if(!f.exists())
				throw new FileNotFoundException("Input file does not exists in " + file + " or in integration dir " +integrationDir);
		}
		csvFilePath = csvFile;
	}
	
	
	public Map<String, List<String>> process() {
		Map<String, List<String>> csvData = new HashMap<String, List<String>>();
		if(logger.isDebugEnabled())
			logger.debug("start file process ---");
		readFile();
		if(logger.isDebugEnabled())
			logger.debug("after -- file process ---");
		csvData.put("header_cols", upperdenames);
		
		return csvData;
	}
	
	public Map<String, List<String>> getHeaders() {
		Map<String, List<String>> csvData = new HashMap<String, List<String>>();
		if(logger.isDebugEnabled())
			logger.debug("start file getHeaders ---");
		readFile();
		if(logger.isDebugEnabled())
			logger.debug("after -- file process ---");
		csvData.put("header_cols", upperdenames);
		
		return csvData;
	}
	
	
	private void readFile() {
		
		if(csvFilePath==null)
			return;
		
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(csvFilePath));
			
		} catch (FileNotFoundException e) {
			logger.debug("CSV file not found :::: " + csvFilePath);
			logger.error(e, e);
			
		}		
		
		try {
			if(reader == null)
				return;
				
			allRows = reader.readAll();
			if(allRows == null || allRows.get(0).length ==1)
				return;
			
			
			upperdenames = Arrays.asList(allRows.get(0));
			this.pointer++;
			
		} catch (IOException e) {
			logger.debug("Error in reading the CSV file ::: " + csvFilePath);
			logger.error(e,e);			
		}finally{
			try{ if(reader != null) reader.close();}catch(Exception eee){}
		}
	}
	
	public List<String[]> getAllDataRows(){
		return this.allRows.subList(1, this.allRows.size());
	}
	public List<String> getHeaderColumns() throws Exception{
		return 	upperdenames;
	}
	
	
	public String[] getNextRecordRow(){// returns the record & associated lineitems
		if(this.pointer < 1 || this.pointer >= this.allRows.size())
			return null;
		String[] row = null;
		if(this.pointer == 0){
			this.pointer++;
			return allRows.get(0);
		}
		while (this.pointer < this.allRows.size()){
			row = this.allRows.get(this.pointer);
			
			if(row == null || row.length == 0)
				continue;
			this.pointer++;
		}
		return row;
	}
	
	
	public void writeResponseCSV(String outfilename,List<Map<String,Object>> resplist)throws Exception{
		
		FileWriter w = new FileWriter(outfilename);
		CSVWriter cw = new CSVWriter(w);
		this.upperdenames.add("Status");
		this.upperdenames.add("Message");
		String[] u = (String[])this.upperdenames.toArray();
		try{
			cw.writeNext(u);
			int index = 1;
			for(int i = index; i < this.allRows.size(); i++){
				 List<String> arr =Arrays.asList( this.allRows.get(i));
				 Map<String,Object> m = resplist.get(i-index);
				 arr.add(""+(Integer)m.get("status"));
				 arr.add((String)m.get("message"));
				 cw.writeNext((String[])arr.toArray());
			}
			w.flush();
		
		}catch(Exception e){
			logger.error(e,e);
		}finally{
			try{
				if(w != null)
					w.close();
				if(cw != null)
					cw.close();	
			}catch(Exception ee){}
			
		}
	}
	
	
	
}
