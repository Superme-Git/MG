package fire.pb.friends;


public class FriendGiveItem implements mytools.ConvMain.Checkable ,Comparable<FriendGiveItem>{

	public int compareTo(FriendGiveItem o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public FriendGiveItem(){
		super();
	}
	public FriendGiveItem(FriendGiveItem arg){
		this.id=arg.id ;
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
	
	
};