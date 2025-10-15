package fire.pb.javascript;


public class SJavaScriptConfig implements mytools.ConvMain.Checkable ,Comparable<SJavaScriptConfig>{

	public int compareTo(SJavaScriptConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		
		private static final long serialVersionUID = 1L;
		
	}
	public SJavaScriptConfig(){
		super();
	}
	public SJavaScriptConfig(SJavaScriptConfig arg){
		this.id=arg.id ;
		this.fileName=arg.fileName ;
		this.filePath=arg.filePath ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	
	public int id  = 0  ;
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int v){
		this.id=v;
	}
	
	
	public String fileName  = null  ;
	
	public String getFileName(){
		return this.fileName;
	}
	
	public void setFileName(String v){
		this.fileName=v;
	}
	
	
	public String filePath  = null  ;
	
	public String getFilePath(){
		return this.filePath;
	}
	
	public void setFilePath(String v){
		this.filePath=v;
	}
	
	
};