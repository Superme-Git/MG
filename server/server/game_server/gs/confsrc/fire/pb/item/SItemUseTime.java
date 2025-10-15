package fire.pb.item;


public class SItemUseTime implements mytools.ConvMain.Checkable ,Comparable<SItemUseTime>{

	public int compareTo(SItemUseTime o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SItemUseTime(){
		super();
	}
	public SItemUseTime(SItemUseTime arg){
		this.id=arg.id ;
		this.times=arg.times ;
		this.types=arg.types ;
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
	public int times  = 0  ;
	
	public int getTimes(){
		return this.times;
	}
	
	public void setTimes(int v){
		this.times=v;
	}
	
	/**
	 * 
	 */
	public int types  = 0  ;
	
	public int getTypes(){
		return this.types;
	}
	
	public void setTypes(int v){
		this.types=v;
	}
	
	
};