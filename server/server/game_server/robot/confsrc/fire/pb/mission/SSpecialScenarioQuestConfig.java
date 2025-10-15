package fire.pb.mission;


public class SSpecialScenarioQuestConfig implements mytools.ConvMain.Checkable ,Comparable<SSpecialScenarioQuestConfig>{

	public int compareTo(SSpecialScenarioQuestConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SSpecialScenarioQuestConfig(){
		super();
	}
	public SSpecialScenarioQuestConfig(SSpecialScenarioQuestConfig arg){
		this.id=arg.id ;
		this.params=arg.params ;
		this.类型ID=arg.类型ID ;
		this.activeparams=arg.activeparams ;
		this.类型ID2=arg.类型ID2 ;
		this.emsg=arg.emsg ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 
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
	public java.util.ArrayList<Integer> params  ;
	
	public java.util.ArrayList<Integer> getParams(){
		return this.params;
	}
	
	public void setParams(java.util.ArrayList<Integer> v){
		this.params=v;
	}
	
	/**
	 * 
	 */
	public int 类型ID  = 0  ;
	
	public int get类型ID(){
		return this.类型ID;
	}
	
	public void set类型ID(int v){
		this.类型ID=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> activeparams  ;
	
	public java.util.ArrayList<Integer> getActiveparams(){
		return this.activeparams;
	}
	
	public void setActiveparams(java.util.ArrayList<Integer> v){
		this.activeparams=v;
	}
	
	/**
	 * 
	 */
	public int 类型ID2  = 0  ;
	
	public int get类型ID2(){
		return this.类型ID2;
	}
	
	public void set类型ID2(int v){
		this.类型ID2=v;
	}
	
	/**
	 * 
	 */
	public int emsg  = 0  ;
	
	public int getEmsg(){
		return this.emsg;
	}
	
	public void setEmsg(int v){
		this.emsg=v;
	}
	
	
};