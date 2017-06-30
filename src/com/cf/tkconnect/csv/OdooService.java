package com.cf.tkconnect.csv;


import java.io.File;

import com.cf.tkconnect.models.Products;
import com.cf.tkconnect.models.Sales;
import com.cf.tkconnect.util.FileUtils;
import com.cf.tkconnect.util.WSUtil;


public class OdooService {

	static com.cf.tkconnect.log.Log logger = com.cf.tkconnect.log.LogSource
			.getInstance(OdooService.class);
	
	String csvfilename;
	LoadCSVData csvdata;
	
	public OdooService(String csvfilename) {
		this.csvfilename = csvfilename;
	}
	
	public void process(){
		try{
			// read the CSV & check the name of the model
			this.csvdata = new LoadCSVData(this.csvfilename);
			String modelName = this.csvdata.getModelFromCSV();
			String action = this.csvdata.getAction();
			if(modelName == null){
				logger.error("Error reading csv template file :"+this.csvfilename);
				return;
			}
			if(modelName.equalsIgnoreCase("Sales Order") ){
				// set it for sales processing
				logger.info("Starting with sales "+action);
				Sales s = new Sales(this.csvfilename,this.csvdata);
				if("create".equalsIgnoreCase(action))
					s.createRecords();
				else if("fetch".equalsIgnoreCase(action))
					s.fetchRecords();
				
			}else if(modelName.equalsIgnoreCase("Products")){
				//&& action.equals("create")
				
				Products prod = new Products(this.csvfilename,this.csvdata);
				if("create".equalsIgnoreCase(action))
					prod.createRecords();
				else if("fetch".equalsIgnoreCase(action))
					prod.fetchRecords();
				
			}else{
				
				logger.error("Error --- Unsupported model :"+modelName);
			}
			
		}catch(Exception e){
			logger.error(e,e);
		}
		
	}
	
	
		
	
}
