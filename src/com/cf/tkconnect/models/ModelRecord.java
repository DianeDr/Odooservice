package com.cf.tkconnect.models;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import com.cf.tkconnect.csv.LoadCSVData;

import com.cf.tkconnect.util.FileUtils;
import com.cf.tkconnect.util.InitialSetUp;
import com.cf.tkconnect.util.WSUtil;

public class ModelRecord {

	static com.cf.tkconnect.log.Log logger = com.cf.tkconnect.log.LogSource
			.getInstance(ModelRecord.class);
	
	LoadCSVData csvdata;
	List<String> header_cols;
	List<String> detail_cols;
	
	String url;
	String username;
	String db;
	String password;
	 XmlRpcClient client;
	 XmlRpcClientConfigImpl common_config;
	 XmlRpcClient models;
	 int uid =0;
	 String csvfilename;
	
	public ModelRecord(String csvfilename, LoadCSVData csvdata) {
		this.csvdata = csvdata;
		this.csvfilename = csvfilename;
	}

	protected void setup(){
		try{
			this.url = getURL(InitialSetUp.odoo.get("url"));//"http://localhost:8069/xmlrpc/2/common";
			this. username=InitialSetUp.odoo.get("username");
			this.password =InitialSetUp.odoo.get("password");
			this.db =InitialSetUp.odoo.get("db");
	       
	         this.client = new XmlRpcClient();
	         this.common_config = new XmlRpcClientConfigImpl();
	        common_config.setServerURL(  new URL( url+"xmlrpc/2/common"));
	      
	        logger.debug(" check before :"+url+"   :"+username+"::"+password+":"+db+":");
	        Object obj = client.execute( common_config, "authenticate", Arrays.asList(
	        	        db, username, password, new HashMap()));
	        logger.debug("login result  :"+obj.toString());	
	        //after you get uid you need to use it        
	       this.header_cols = this.csvdata.getHeaderColumns();
	       this.detail_cols = this.csvdata.getDetailColumns();
	        try{
	        	this.uid = Integer.parseInt(obj.toString());
	        }catch(Exception e){
	        	logger.error(" Error login in check username/password ");
	        	return;
	        }
	        this.models = new XmlRpcClient() {{
	            setConfig(new XmlRpcClientConfigImpl() {{
	                setServerURL(new URL(url+"xmlrpc/2/object"));
	            }});
	        }};
	       
	        
		}catch(Exception e){
			logger.error(e,e);
		}
		
	}
	public void moveFiles() {
		try{
			String today = WSUtil.getDateString();
			FileUtils.createDateDirs(today);
//			String outfile = FileUtils.getFileNameWithoutExtension(this.csvfilename)+"_response"+FileUtils.getFileExtension(this.csvfilename);
			FileUtils.moveFiles(FileUtils.InputFileServiceBaseDirectory,  FileUtils.ResponseFileServiceBaseDirectory+File.separator+today, 
					FileUtils.getFileName(this.csvfilename));
		}catch(Exception ee){
			logger.error(ee,ee);
		}
	}
	
	protected Map<String,Object> getNextRecord(String[] row, Map<String,Object> pmap){
		Map<String,Object> map = new HashMap<String,Object>();
		if(row == null)
			return map;
		//TODO missing type info , int, date, String etc 
		for(int i = 0; i < this.header_cols.size(); i++){
			if(i < row.length){
				if(row[i]== null || row[i].trim().length()==0 || "R".equalsIgnoreCase(row[i])  || "I".equalsIgnoreCase(row[i]) ){
					continue;
				}
				String value = row[i].trim();
				Object objectType = pmap.get(this.header_cols.get(i));
				
				logger.debug("name  "+this.header_cols.get(i)+" val:"+value);
				if(objectType instanceof String){
					map.put(this.header_cols.get(i), value);
					logger.debug(" type String");
				}else if(objectType instanceof Boolean){
					int val = 0;
					if("true".equalsIgnoreCase(value) )
						val=1;
					map.put(this.header_cols.get(i), val);
					logger.debug(" type Boolean :: "+val);
				}else if(objectType instanceof Double){
					map.put(this.header_cols.get(i), new Double(value));
					logger.debug(" type Double");
				}else if(objectType instanceof Integer){
					map.put(this.header_cols.get(i), new Integer(value));
					logger.debug(" type Integer");
				}

				else {
					if(objectType == null)
						map.put(this.header_cols.get(i), value);
					else{
						logger.debug("getNextRecord  missed case type :"+this.header_cols.get(i)+" type:"+objectType.getClass().getName()+"  not accounted for." );
					}
				}
			}
		}
		return map;
	}
	
	private String getURL(String url){
		if(url == null || url.trim().length()==0|| url.endsWith("/"))
			return url;
		return url+"/";
	}
	
	protected String getExportFileName(){
		String today =WSUtil.getDateString();
		String file =FileUtils.ExcelFileServiceBaseDirectory+"/data"+File.separator+FileUtils.getFileNameWithoutExtension(this.csvfilename)+"_"+today+"_export.csv";
		logger.debug("export file name :"+file);
		return file;
	}
}
