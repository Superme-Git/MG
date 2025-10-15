import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LogSavedTime {
//	保存玩家数据:飙风D潮男猪(1002718)281/281@23[650,451] used:12328
	public static final String REGEX = "保存玩家数据:(.*?)\\(100(.*?)\\)(.*?)used:(.+?)aaa";
	public static final Pattern PATTERN = Pattern.compile(REGEX);

	private String encoding = "UTF-8";
	
	private Matcher matcher;
	
	private String inputFile;
	private String outputFile;
	public LogSavedTime(String file,String outFile){
		inputFile = file;
		outputFile = outFile;
	}
	
	public void parse() throws Exception{
		System.out.println(outputFile);
//		PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile),encoding));
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile),encoding));
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(inputFile),encoding));
		String line = "";
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<String> ids = new ArrayList<String>();
		ArrayList<String> times = new ArrayList<String>();
		
		while((line = reader.readLine())!= null){
			matcher = PATTERN.matcher(line + "aaa");
			if(matcher.find()){
				String name = matcher.group(1);
				String id = matcher.group(2);
				String time = matcher.group(4);
				names.add(name);
				ids.add("0x"+id);
				times.add(time);
			}
		}
		for(int i=0;i<names.size();i++){
			String str = names.get(i) + "\t" + ids.get(i) + "\t" + Integer.decode(ids.get(i)) + "\t" + times.get(i);
//			System.out.println(str);
			writer.println(str);
		}
		writer.close();
		System.out.println("done");
	}
	
	public static void main(String[] args){
		LogSavedTime counter = new LogSavedTime("res/used.log","D:\\ss.log");
		try{
			counter.parse();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
