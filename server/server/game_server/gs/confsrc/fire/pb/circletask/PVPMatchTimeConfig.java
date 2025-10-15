package fire.pb.circletask;


public class PVPMatchTimeConfig implements mytools.ConvMain.Checkable ,Comparable<PVPMatchTimeConfig>{

	public int compareTo(PVPMatchTimeConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public PVPMatchTimeConfig(){
		super();
	}
	public PVPMatchTimeConfig(PVPMatchTimeConfig arg){
		this.id=arg.id ;
		this.lev=arg.lev ;
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
	public int lev  = 0  ;
	
	public int getLev(){
		return this.lev;
	}
	
	public void setLev(int v){
		this.lev=v;
	}
	
	
};