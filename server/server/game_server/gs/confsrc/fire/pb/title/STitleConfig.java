package fire.pb.title;


public class STitleConfig implements mytools.ConvMain.Checkable ,Comparable<STitleConfig>{

	public int compareTo(STitleConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public STitleConfig(){
		super();
	}
	public STitleConfig(STitleConfig arg){
		this.id=arg.id ;
		this.chatsee=arg.chatsee ;
		this.availtime=arg.availtime ;
		this.titlename=arg.titlename ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 称谓id
	 */
	public int id  = 0  ;
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int v){
		this.id=v;
	}
	
	/**
	 * 
	 */
	public boolean chatsee  = false  ;
	
	public boolean getChatsee(){
		return this.chatsee;
	}
	
	public void setChatsee(boolean v){
		this.chatsee=v;
	}
	
	/**
	 * -1为没有固定的有效时间
	 */
	public int availtime  = 0  ;
	
	public int getAvailtime(){
		return this.availtime;
	}
	
	public void setAvailtime(int v){
		this.availtime=v;
	}
	
	/**
	 * 
	 */
	public String titlename  = null  ;
	
	public String getTitlename(){
		return this.titlename;
	}
	
	public void setTitlename(String v){
		this.titlename=v;
	}
	
	
};