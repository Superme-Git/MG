package com.mammoth.logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogCounter {
	public static final String REGEX = "(\\d+?):(\\d+?):(\\d+?).";
	public static final Pattern PATTERN = Pattern.compile(REGEX);

	private String encoding = "UTF-8";
	
	private Matcher matcher;
	
	private String inputFile;
	public LogCounter(String file,String outputFile){
		inputFile = file;
		outputFile = file;
	}
	
	public void parse() throws Exception{
//		PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile),encoding));
		
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(inputFile),encoding));
		String line = "";
		ArrayList<String> hours = new ArrayList<String>();
		int[] count = new int[9999];
		while((line = reader.readLine())!= null){
			String h = getHour(line);
			if(h != null){
				int index = hours.indexOf(h);
				if(index == -1){
					hours.add(h);
					index = hours.indexOf(h);
				} 
				count[index]++;
			}
		}
		for(int i=0;i<hours.size();i++){
			System.out.println(hours.get(i) + "\t" + count[i]);
		}
//		writer.close();
	}
	
	public String getHour(String line){
		if(line == null) return null;
		matcher = PATTERN.matcher(line);
		if(matcher.find()){
			return matcher.group(1) + ":" + matcher.group(2);
		}
		return null;
	}
	
	public static void main(String[] args){
		LogCounter counter = new LogCounter("res/death.log","counta.log");
		try{
			counter.parse();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

}
