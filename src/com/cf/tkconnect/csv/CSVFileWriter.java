package com.cf.tkconnect.csv;

import au.com.bytecode.opencsv.CSVWriter;
import java.io.*;
import java.util.*;

public class CSVFileWriter extends CSVWriter {
	
	public CSVFileWriter(Writer writer) {
		super(writer);
	}
	
	public CSVFileWriter(OutputStream out) throws Exception {
		super(new OutputStreamWriter(out, "UTF-8"));
	}
	protected List<String>  arrString = new ArrayList<String>(); 
	
	public void println(){
		String[] arr = arrString.toArray(new String[0]);
		print(arr);
		arrString = new ArrayList<String>(); 
	}
	
	public void print(String message){
		arrString.add(message);
	}
	
	public void println(String message){
		print(message);
		println();
	}
	public void writeln(){
		 println();
	}
	public void write(String message){
		arrString.add(message);
	}
	
	public void print(String[] arr){
		writeNext(arr);
	}
	
	public void println(String[] arr){
		writeNext(arr);
	}

	public void setAutoFlush(boolean value) {
		
	}
}
