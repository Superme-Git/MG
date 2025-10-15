package fire.pb.friends;


public class FriendGiveGift implements mytools.ConvMain.Checkable ,Comparable<FriendGiveGift>{

	public int compareTo(FriendGiveGift o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public FriendGiveGift(){
		super();
	}
	public FriendGiveGift(FriendGiveGift arg){
		this.id=arg.id ;
		this.oppositeSexFriendlyDegrees=arg.oppositeSexFriendlyDegrees ;
		this.sameSexFriendlyDegrees=arg.sameSexFriendlyDegrees ;
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
	public int oppositeSexFriendlyDegrees  = 0  ;
	
	public int getOppositeSexFriendlyDegrees(){
		return this.oppositeSexFriendlyDegrees;
	}
	
	public void setOppositeSexFriendlyDegrees(int v){
		this.oppositeSexFriendlyDegrees=v;
	}
	
	/**
	 * 
	 */
	public int sameSexFriendlyDegrees  = 0  ;
	
	public int getSameSexFriendlyDegrees(){
		return this.sameSexFriendlyDegrees;
	}
	
	public void setSameSexFriendlyDegrees(int v){
		this.sameSexFriendlyDegrees=v;
	}
	
	
};