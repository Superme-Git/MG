package fire.pb.friends;


public class XshGiveGift implements mytools.ConvMain.Checkable ,Comparable<XshGiveGift>{

	public int compareTo(XshGiveGift o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public XshGiveGift(){
		super();
	}
	public XshGiveGift(XshGiveGift arg){
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