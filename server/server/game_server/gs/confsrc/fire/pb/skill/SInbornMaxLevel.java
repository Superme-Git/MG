package fire.pb.skill;


public class SInbornMaxLevel implements mytools.ConvMain.Checkable ,Comparable<SInbornMaxLevel>{

	public int compareTo(SInbornMaxLevel o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SInbornMaxLevel(){
		super();
	}
	public SInbornMaxLevel(SInbornMaxLevel arg){
		this.id=arg.id ;
		this.inbornMaxLevel=arg.inbornMaxLevel ;
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
	public java.util.ArrayList<Integer> inbornMaxLevel  ;
	
	public java.util.ArrayList<Integer> getInbornMaxLevel(){
		return this.inbornMaxLevel;
	}
	
	public void setInbornMaxLevel(java.util.ArrayList<Integer> v){
		this.inbornMaxLevel=v;
	}
	
	
};