


import java.io.*;

public class LogFilter {
	public String REGEX = "系统负担";
	
	private String inputFile;
	private String outputFile;
	
	private String encoding = "UTF-8";
	
	public LogFilter(String input,String output){
		inputFile = input;
		outputFile = output;
	}
	
	public void parse() throws Exception{ 
		parse(REGEX);
	}
	
	public void parse(String regex) throws Exception{ 
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile),encoding));
		
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(inputFile),encoding));
		String line = "";
		while((line = reader.readLine())!= null){
			if(line.contains(regex)){
//				System.out.println(line);
				writer.println(line);
			}
		}
		writer.close();
		reader.close();
	}
	
	public static void main(String[] args) throws Exception{
		String in = "syslog.2016-02-18.log";
		String out = "out.log";
		String regex = "系统负担";
		try{
			in = args[0];
		} catch(Exception e){
			in = "syslog.2016-02-18.log";
		}
		try{
			out = args[1];
		} catch(Exception e){
			out = "out.log";
		}
		try{
			regex = args[2];
		} catch(Exception e){
			regex = "系统负担";
		}
			
		LogFilter log = new LogFilter(in,out);
		log.parse(regex);
	}

}
