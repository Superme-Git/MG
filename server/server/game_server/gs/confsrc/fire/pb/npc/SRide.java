package fire.pb.npc;


public class SRide implements mytools.ConvMain.Checkable ,Comparable<SRide>{

	public int compareTo(SRide o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SRide(){
		super();
	}
	public SRide(SRide arg){
		this.id=arg.id ;
		this.ridemodel=arg.ridemodel ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * id
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
	public int ridemodel  = 0  ;
	
	public int getRidemodel(){
		return this.ridemodel;
	}
	
	public void setRidemodel(int v){
		this.ridemodel=v;
	}
	
	
};