package fire.pb.npc;


public class SFestivalGift implements mytools.ConvMain.Checkable ,Comparable<SFestivalGift>{

	public int compareTo(SFestivalGift o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SFestivalGift(){
		super();
	}
	public SFestivalGift(SFestivalGift arg){
		this.id=arg.id ;
		this.time=arg.time ;
		this.name=arg.name ;
		this.awardid=arg.awardid ;
		this.sourceid=arg.sourceid ;
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
	public String time  = null  ;
	
	public String getTime(){
		return this.time;
	}
	
	public void setTime(String v){
		this.time=v;
	}
	
	/**
	 * 
	 */
	public String name  = null  ;
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String v){
		this.name=v;
	}
	
	/**
	 * 
	 */
	public int awardid  = 0  ;
	
	public int getAwardid(){
		return this.awardid;
	}
	
	public void setAwardid(int v){
		this.awardid=v;
	}
	
	/**
	 * 
	 */
	public int sourceid  = 0  ;
	
	public int getSourceid(){
		return this.sourceid;
	}
	
	public void setSourceid(int v){
		this.sourceid=v;
	}
	
	
};