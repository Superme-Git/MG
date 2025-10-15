package fire.pb.npc;


public class SNpcServiceMapping implements mytools.ConvMain.Checkable ,Comparable<SNpcServiceMapping>{

	public int compareTo(SNpcServiceMapping o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SNpcServiceMapping(){
		super();
	}
	public SNpcServiceMapping(SNpcServiceMapping arg){
		this.id=arg.id ;
		this.type=arg.type ;
		this.param1=arg.param1 ;
		this.param2=arg.param2 ;
		this.param3=arg.param3 ;
		this.param4=arg.param4 ;
		this.param5=arg.param5 ;
		this.param6=arg.param6 ;
		this.param7=arg.param7 ;
		this.param8=arg.param8 ;
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
	public int type  = 0  ;
	
	public int getType(){
		return this.type;
	}
	
	public void setType(int v){
		this.type=v;
	}
	
	/**
	 * 
	 */
	public int param1  = 0  ;
	
	public int getParam1(){
		return this.param1;
	}
	
	public void setParam1(int v){
		this.param1=v;
	}
	
	/**
	 * 
	 */
	public int param2  = 0  ;
	
	public int getParam2(){
		return this.param2;
	}
	
	public void setParam2(int v){
		this.param2=v;
	}
	
	/**
	 * 
	 */
	public int param3  = 0  ;
	
	public int getParam3(){
		return this.param3;
	}
	
	public void setParam3(int v){
		this.param3=v;
	}
	
	/**
	 * 
	 */
	public int param4  = 0  ;
	
	public int getParam4(){
		return this.param4;
	}
	
	public void setParam4(int v){
		this.param4=v;
	}
	
	/**
	 * 
	 */
	public int param5  = 0  ;
	
	public int getParam5(){
		return this.param5;
	}
	
	public void setParam5(int v){
		this.param5=v;
	}
	
	/**
	 * 
	 */
	public int param6  = 0  ;
	
	public int getParam6(){
		return this.param6;
	}
	
	public void setParam6(int v){
		this.param6=v;
	}
	
	/**
	 * 
	 */
	public int param7  = 0  ;
	
	public int getParam7(){
		return this.param7;
	}
	
	public void setParam7(int v){
		this.param7=v;
	}
	
	/**
	 * 
	 */
	public int param8  = 0  ;
	
	public int getParam8(){
		return this.param8;
	}
	
	public void setParam8(int v){
		this.param8=v;
	}
	
	
};