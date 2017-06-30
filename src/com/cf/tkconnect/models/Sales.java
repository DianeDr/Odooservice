package com.cf.tkconnect.models;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import com.cf.tkconnect.csv.LoadCSVData;
import com.cf.tkconnect.util.FileUtils;
import com.cf.tkconnect.util.InitialSetUp;
import com.cf.tkconnect.util.WSUtil;

public class Sales  extends ModelRecord{


	static com.cf.tkconnect.log.Log logger = com.cf.tkconnect.log.LogSource
			.getInstance(Sales.class);
	
	String salesModel = "sale.order";
	String salesItemModel = "sale.order.item";
	Map<String,Object> pmap = new HashMap<String,Object>(); 
	Map<String,Object> limap = new HashMap<String,Object>();
	
	public Sales(String csvfilename, LoadCSVData csvdata) {
		super(csvfilename,csvdata);
	}


	private void init() throws Exception{

		if(this.csvfilename== null || this.csvfilename.trim().length()==0)
			throw new Exception("No file found");
		setup();
		
		Map<String, List<String>>   headers = this.csvdata.getHeaders();
		this.header_cols = headers.get("header_cols");
		this.detail_cols = headers.get("detail_cols");
		//
		  limap = new HashMap<String,Object>();
	        limap.put("product_uom_qty", 12); 
	        limap.put("price_unit", 15.0); 
	        limap.put("price_tax", 15.5); 
	        limap.put("discount", 15.5); 
	        limap.put("product_id", 16); 
	        limap.put("name", "li05"); 
	      
	       // pmap.put("validity_date", new java.sql.Timestamp(new Date().getTime())); 
	        pmap.put("write_date", new Date()); 
	        pmap.put("state", "sale"); 
	        pmap.put("note", "Service create 6"); 
	        pmap.put("name", "SP107"); 
	        pmap.put("amount_tax", 150.0); 
	        pmap.put("origin","origin");
	        pmap.put("partner_id", 6); 
	}
	
	public void createRecords(){// currently creating records
		try{
			init();
	       // List<String[]> rows = this.csvdata.getAllDataRows();
			logger.debug(" Sales data before ");
	        int count = 0;
	        while(true){
	        	Map<String,Object> nextmap =csvdata.getNextBPRecordRow();
	        	if(nextmap == null || nextmap.isEmpty())
	        		break;// no more records to process
	        	String[] row = (String[])nextmap.get("record");
	        	Map<String,Object> recordmap = getNextRecord(row,pmap);
	        	//get lineitems
	        	List<String[]> itemlist = (List<String[]>)nextmap.get("lineitem");
	        	
	        	if(itemlist != null && itemlist.size() > 0){
	        		List lilist = new ArrayList();
	        		lilist.add(0);
	      	        lilist.add(0);
	      	        lilist.add(getNextRecord(itemlist.get(0) ,limap) );
	        		recordmap.put("order_line",Arrays.asList( lilist));
	        	}
	        	logger.debug(" sending data before :"+count+"  values :"+recordmap);
	        	Integer record_id = (Integer)models.execute("execute_kw", Arrays.asList(
	 	        	    db, uid, password,
	 	        	    salesModel, "create",
	 	        	    Arrays.asList(recordmap)
	 	        ));
	        	count++;
		        logger.debug(" result after :"+count+" record_id :"+record_id );
		        for(int i = 1 ; i <  itemlist.size(); i++){
		        	String[] lirow = itemlist.get(i);
	    			 Map<String,Object> itemmap = getNextRecord(lirow,limap);
	    			 itemmap.put("order_id", record_id);
	    			 final Integer liid = (Integer)models.execute("execute_kw", Arrays.asList(
	    		        	    db, uid, password,
	    		        	    "sale.order.line", "create", Arrays.asList(itemmap)
	    		        	));	
	    			 logger.debug("result after creating the next line  id :"+liid +"   ::  "+itemmap);
	    		}
	       }
	        // after completing
	       logger.info("End of processing sales orders");
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
			
			List<String> fields  = new ArrayList<String>();
			fields.addAll( Arrays.asList( pmap.keySet().toArray(new String[0])));
			List<String> lifields   = new ArrayList<String>();
			lifields.addAll(		Arrays.asList( limap.keySet().toArray(new String[0])));
			Map optionsMap = new HashMap();
		    optionsMap.put("fields", fields);
		    optionsMap.put("limit", 1000);
			Map optionsLIMap = new HashMap();
		    optionsLIMap.put("fields", lifields);
		    optionsLIMap.put("limit", 1000);
			   // process the fields
		//	   System.out.println(" result col types :"+fids);
		    List slist =  Arrays.asList((Object[])models.execute("execute_kw", Arrays.asList(
		    		    db, uid, password,
		    		    salesModel, "search_read",
		    		    Arrays.asList(Arrays.asList()),
		    		    optionsMap
		    		)));
		    System.out.println(" result after  Sales   :"+slist );  
		    if(slist == null )
		    	slist = new ArrayList();
		    List<String[]> recordlist = new ArrayList<String[]>();
		    fields.add(0, "R");
		    fields.add("id");
		    for(int i = 0; i < slist.size(); i++){
		    	Map<String,Object> map = (Map<String,Object>)slist.get(i);
		    	String[] row = new String[fields.size()+1];
		    	row[0]= "R";
		    	int count = 0;
		    	int id = 0;
		    	for(String f : fields){
		    		count++;
		    		row[count] = (map.get(f)==null?"":map.get(f).toString());
		    		if(f.equals("id"))
		    			id = Integer.parseInt(map.get(f).toString());
		    		else if("partner_id".equals(f)){
		    			Object[] oos = (Object[])map.get(f);
		    			if(oos != null && oos.length > 1)
		    				row[count]  = oos[0].toString();
		    			
		    		}
		    	}
		    	recordlist.add(row);
		    	logger.debug(" result after  Sales id  :"+id );  
		    	List lilist =  Arrays.asList((Object[])models.execute("execute_kw", Arrays.asList(
		    		    db, uid, password,
		    		    "sale.order.line", "search_read",
		    		    Arrays.asList(Arrays.asList(Arrays.asList("order_id", "=", id))),
		    		    optionsLIMap
		    	)));
		    	//logger.debug(" result after  Sales id  :"+id +" list :"+lilist );  
		    	lifields.add(0, "I");
		    	for(int j = 0; j < lilist.size(); j++){
		    		int licount = 1;
		    		Map<String,Object> liimap = (Map<String,Object>)lilist.get(j);
		    		String[] lirow = new String[lifields.size()];
		    		lirow[0]="I";
			    	for(String f : lifields){
			    		if("I".equalsIgnoreCase(f))
			    			continue;
			    		//logger.debug(" result after  Sales items  :"+licount +" f :"+f ); 
			    		lirow[licount] = (liimap.get(f)==null?"":liimap.get(f).toString());
			    		if("name".equalsIgnoreCase(f)){
			    			String s = (String)liimap.get(f);
			    			logger.debug("--- result after  Sales items f :"+f+" s  :"+s+"  repl::"+ s.replaceAll("[\\t\\n\\r]+"," ") ); 
			    			lirow[licount] =     s.replaceAll("[\\t\\n\\r]+"," ");
			    		}
			    			
			    		if("product_id".equals(f)){
			    			Object[] oos = (Object[])liimap.get(f);
			    			if(oos != null && oos.length > 1)
			    				lirow[licount]  = oos[0].toString();
			    			
			    		}
			    		licount++;
			    	}
			    	recordlist.add(lirow);
		    	}
		    }
		
		    
		    String  outfilename = getExportFileName();
		    this.csvdata.writeExportCSV(outfilename, recordlist, salesModel, fields,lifields);
		    
	      
		}catch(Exception e){
			logger.error(e,e);
		}finally{
			  moveFiles();
		}
	}
	
	
}
