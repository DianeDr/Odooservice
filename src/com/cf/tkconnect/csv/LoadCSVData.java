package com.cf.tkconnect.csv;

import java.io.File;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
public class LoadCSVData {
	
	static Log logger = LogSource.getInstance(LoadCSVData.class);
	// add Data rows will start at Line 4 or later
	List<String[]> allRows = null;
	String csvFilePath = null;
	List<String> upperdenames = new ArrayList<String>();
	List<String> lidenames = new ArrayList<String>();
	List<String[]> upperdata = new ArrayList<String[]>();
	List<String[]> lidata = new ArrayList<String[]>();
	private int pointer=0;
	String type ="bp";
	int data_index = 4;
	String model_name;
	int template_size = 4;// this is the header template
	String action = "create";
	
	boolean fileRead = false;// read file only once
	
	
	public LoadCSVData(String filename) {
		this.csvFilePath = filename;
	}
	
	
	public void setDataStartIndex(int index){
		this.data_index = index;
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
	
	public String getModelFromCSV(){
		readFile();
		return this.model_name;
	}
	
	public String getAction(){
		readFile();
		return this.action;
	}
	
	public Map<String, List<String>> process() {
		Map<String, List<String>> csvData = new HashMap<String, List<String>>();
		if(logger.isDebugEnabled())
			logger.debug("start file process ---");
		readFile();
		if(logger.isDebugEnabled())
			logger.debug("after -- file process ---");
		csvData.put("header_cols", upperdenames);
		csvData.put("detail_cols", lidenames);
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
		csvData.put("detail_cols", lidenames);
		return csvData;
	}
	private List<String> toArrayList(String[] str){
		if(str == null)
			return null;
		List<String> list = new ArrayList<String>();
		for(String s : str)
			list.add(s);
		return list;
		
	}
	
	private String[] toArray(List<String> list){
		if(list == null)
			return null;
		String[] str = new String[list.size()];
		
		for(int i = 0; i < list.size(); i++){
			str[i] = list.get(i);
		}
			
		return str;
		
	}
	private void readFile() {
		
		if(csvFilePath== null)
			return;
		if(fileRead)
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
			logger.debug(" Load CSV    rows read : "+allRows.size());
			if(allRows == null || allRows.size()==0 ){
				logger.info("Error --- CSV rows is null or empty or the CSV  file template is not valid : "+this.csvFilePath);
				return;
			}
			String[] top = allRows.get(0);
			if(top == null || top.length < 6){
				logger.info("Error --- CSV header is not correct, or the CSV  file template is not valid : "+this.csvFilePath);
				return;
			}
			this.model_name = top[2];
			if(top[4].equalsIgnoreCase("fetch"))
				this.action = "fetch";
			logger.info("found model name : "+this.model_name+"  action :"+this.action);
			if(this.action.equalsIgnoreCase("create")){
				if(allRows.size() < 4 ){
					logger.info("Error --- CSV  template is not valid : "+this.csvFilePath+"  ::"+this.allRows.size());
					return;
				}
				this.upperdenames = toArrayList(allRows.get(1));
				this.lidenames = toArrayList(allRows.get(2));
				this.pointer = 4;
			}else{	// this is fetch records 
				this.pointer = 1;
			}
			this.fileRead = true;
		} catch (IOException e) {
			logger.debug("Error in reading the CSV file ::: " + csvFilePath);
			logger.error(e,e);			
		}finally{
			try{ if(reader != null) reader.close();}catch(Exception eee){}
		}
		
	}
	
	public List<String[]> getAllDataRows(){
		if(logger.isDebugEnabled())
			logger.debug("get all rows "+data_index+"  size: "+this.allRows.size());
		if( this.allRows.size() >   data_index )
			return this.allRows.subList(data_index, this.allRows.size());
		return new ArrayList<String[]>();
	}
	
	public String getModelName(){
		return this.model_name;
	}
	
	public List<String> getHeaderColumns() throws Exception{
		return 	upperdenames;
	}
	
	public List<String> getDetailColumns() throws Exception{
		return 	lidenames;
	}
	
	public Map<String,Object> getNextBPRecordRow(){// returns the record & associated lineitems
		if(this.pointer < data_index || this.pointer >= this.allRows.size())
			return null;
		Map<String,Object> m = new HashMap<String,Object>();
		List<String[]> drows = new ArrayList<String[]>();
		
		String[] row = this.allRows.get(this.pointer);// this should be header row or null
		if(logger.isDebugEnabled())
			logger.debug("getNextBPRecordRow   :"+this.pointer+" ::"+row);
		if(row == null)
			return null;
		if(logger.isDebugEnabled())
			logger.debug("getNextBPRecordRow   :"+this.pointer+"  :"+row[0]);
		m.put("record", row);
		int licount = 0;
		this.pointer++;
		while (this.pointer < this.allRows.size()){
			row = this.allRows.get(this.pointer);// check the next row if H or D
			if(row == null || row.length == 0)
				break;
			if(logger.isDebugEnabled())
				logger.debug("getnextbprec loop  :"+this.pointer+"  :: "+licount+" ::"+row[0]);
			if("R".equalsIgnoreCase(row[0]))
					break;// second header found
			else if("I".equalsIgnoreCase(row[0])){
				drows.add(row);
				licount++;
			}
			this.pointer++;
		}
		m.put("lineitem", drows);
		return m;
	}
	public String[] getNextRecordRow(){// returns the record & associated lineitems
		if(this.pointer < 1 || this.pointer >= this.allRows.size())
			return null;
		String[] row = null;
		
		if(logger.isDebugEnabled())
			logger.debug("getNextRecordRow ----------- ptr :"+this.pointer);
		if (this.pointer < this.allRows.size()){
			row = this.allRows.get(this.pointer);
			this.pointer++;
		}
		return row;
	}
	

	
	public void writeResponseCSV(String outfilename,List<Map<String,Object>> resplist, int type)throws Exception{
		
		FileWriter w = new FileWriter(outfilename);
		CSVWriter cw = new CSVWriter(w);
		this.upperdenames.add("Status");
		this.upperdenames.add("Message");
		
		String[] u =toArray(this.upperdenames);
		try{
			cw.writeNext(u);
			if(logger.isDebugEnabled())
				logger.debug("writeResponseCSV ----------- data_index :"+data_index+" ::"+this.allRows.size()+" ::"+resplist.size());
			int ptr = 0;
			for(int i = data_index; i < this.allRows.size(); i++){
				 List<String> arr =toArrayList( this.allRows.get(i));
					if(logger.isDebugEnabled())
						logger.debug("writeResponseCSV  data_index :"+data_index+" i::"+i+" ptr::"+ptr);
					if(resplist.size()> ptr){
						 Map<String,Object> m = resplist.get(ptr);
						 arr.add(""+(Integer)m.get("status"));
						 arr.add((String)m.get("message"));
					}
					cw.writeNext(toArray(arr));
					ptr++;
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
	
	public void writeExportCSV(String outfilename,List<String[]> recordList, String model, List<String> fields , List<String> lifields)throws Exception{
		
		FileWriter w = new FileWriter(outfilename);
		CSVWriter cw = new CSVWriter(w);
		boolean hasItem = ( (lifields == null || lifields.isEmpty())?false:true  );
		String[] u = new String[]{"Name",":",model,"action","create","","Do not modify above the --- line","Data should start after --- line","Start the data row with R for record I for Item"};
		try{
			cw.writeNext(u);
			//fields.add(0,"R");
			if(logger.isDebugEnabled())
				logger.debug("writeResponseCSV ----------- ::"+recordList.size()+" ::"+recordList.size());
			cw.writeNext(toArray(fields));
			if(hasItem)
				cw.writeNext(toArray(lifields));
			else	
				cw.writeNext(new String[]{"",""});
			String[] array = new String[]{"----","-----","-------","-------","---------"}; 
			cw.writeNext(array);
			int i =0;
			cw.writeAll(recordList);
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
