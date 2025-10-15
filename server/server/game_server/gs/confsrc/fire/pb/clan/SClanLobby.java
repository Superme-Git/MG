package fire.pb.clan;


public class SClanLobby implements mytools.ConvMain.Checkable ,Comparable<SClanLobby>{

	public int compareTo(SClanLobby o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SClanLobby(){
		super();
	}
	public SClanLobby(SClanLobby arg){
		this.id=arg.id ;
		this.levelupcost=arg.levelupcost ;
		this.costeveryday=arg.costeveryday ;
		this.downcompensate=arg.downcompensate ;
		this.othersum=arg.othersum ;
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
	public int levelupcost  = 0  ;
	
	public int getLevelupcost(){
		return this.levelupcost;
	}
	
	public void setLevelupcost(int v){
		this.levelupcost=v;
	}
	
	/**
	 * 
	 */
	public int costeveryday  = 0  ;
	
	public int getCosteveryday(){
		return this.costeveryday;
	}
	
	public void setCosteveryday(int v){
		this.costeveryday=v;
	}
	
	/**
	 * 
	 */
	public int downcompensate  = 0  ;
	
	public int getDowncompensate(){
		return this.downcompensate;
	}
	
	public void setDowncompensate(int v){
		this.downcompensate=v;
	}
	
	/**
	 * 
	 */
	public int othersum  = 0  ;
	
	public int getOthersum(){
		return this.othersum;
	}
	
	public void setOthersum(int v){
		this.othersum=v;
	}
	
	
};