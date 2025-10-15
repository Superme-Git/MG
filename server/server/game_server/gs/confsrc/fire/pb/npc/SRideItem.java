package fire.pb.npc;


public class SRideItem implements mytools.ConvMain.Checkable ,Comparable<SRideItem>{

	public int compareTo(SRideItem o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SRideItem(){
		super();
	}
	public SRideItem(SRideItem arg){
		this.id=arg.id ;
		this.rideid=arg.rideid ;
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
	public int rideid  = 0  ;
	
	public int getRideid(){
		return this.rideid;
	}
	
	public void setRideid(int v){
		this.rideid=v;
	}
	
	
};