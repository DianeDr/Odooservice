package com.cf.tkconnect.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cf.tkconnect.csv.LoadCSVData;

public class Products extends ModelRecord {

	Map<String,Object> pmap = new HashMap<String,Object>(); 
	
	String model = "product.template";
	
	public Products(String csvfilename, LoadCSVData csvdata) {
		super(csvfilename,csvdata);
	}
	private void init() throws Exception{

		if(this.csvfilename== null || this.csvfilename.trim().length()==0)
			throw new Exception("No file found");
		setup();
		//
		pmap.put("list_price", 12.0); 
        pmap.put("weight", 20.0); 
        pmap.put("description", "test 1"); 
        pmap.put("name", "books"); 
        pmap.put("active", true); 
        pmap.put("description_sale", "test 1"); 
        pmap.put("description_purchase", "test 1"); 
        pmap.put("default_code", "test 1"); 
        pmap.put("rental", false); 
        pmap.put("sale_ok", false); 
        pmap.put("volume", 50.0); 
        pmap.put("warranty", 34.0123);
        pmap.put("barcode", false);
	}
	
	public void createRecords(){// currently creating records
		try{
			init();
						
	        List<String[]> rows = this.csvdata.getAllDataRows();
	        int count = 0;
	        for(String[] row: rows){
	        	Map<String,Object> recordmap = getNextRecord(row,pmap);
	        	logger.debug("product sending data before :"+count+"  values :"+recordmap);
	        	// create
	        	 Integer cid = (Integer)models.execute("execute_kw", Arrays.asList(
	 	        	    db, uid, password,
	 	        	   model, "create",
	 	        	    Arrays.asList(recordmap)
	 	        	));
	        	 count++;
	        	 logger.debug(" result after :"+count+" cr :"+cid );
	        }
	        // after completing
	      
		}catch(Exception e){
			logger.error(e,e);
		}finally{
			  moveFiles();
		}
		
	}

	
	public void fetchRecords(){
		try{
			init();
			// need to create options map
			/*List<String> fields = new ArrayList<String>();
			Iterator<String> it =this.pmap.keySet().iterator();
					fields.add(it.next());
			*/
			List<String> fields  = new ArrayList<String>();
			fields.addAll( Arrays.asList( pmap.keySet().toArray(new String[0])));
			
			 Map optionsMmap = new HashMap();
		     optionsMmap.put("fields", fields);
		     optionsMmap.put("limit", 1000);
			   // process the fields
		//	   System.out.println(" result col types :"+fids);
		      List slist =  Arrays.asList((Object[])models.execute("execute_kw", Arrays.asList(
		    		    db, uid, password,
		    		    model, "search_read",
		    		    Arrays.asList(Arrays.asList()),
		    		    optionsMmap
		    		)));
		      fields.add(0, "R");  
		      logger.debug(" result after  products   :"+slist );  
		    
		      List<String[]> recordlist = new ArrayList<String[]>();
			  for(int i = 0; i < slist.size(); i++){
			    	Map<String,Object> map = (Map<String,Object>)slist.get(i);
			    	String[] row = new String[fields.size()];
			    	row[0]= "R";
			    	int count = 0;
			    	for(String f : fields){
			    		count++;
			    		row[count] = (map.get(f)==null?"":map.get(f).toString());
			    	}
			    	recordlist.add(row);
			  }
			  String  outfilename = getExportFileName();
			  this.csvdata.writeExportCSV(outfilename, recordlist, model, fields,null);
		    
	      
		}catch(Exception e){
			logger.error(e,e);
		}finally{
			  moveFiles();
		}
	}
	
}
