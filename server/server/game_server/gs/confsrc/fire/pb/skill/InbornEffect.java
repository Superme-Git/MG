package fire.pb.skill;


public class InbornEffect implements mytools.ConvMain.Checkable {


	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public InbornEffect(){
		super();
	}
	public InbornEffect(InbornEffect arg){
		this.inbornId=arg.inbornId ;
		this.effect=arg.effect ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 
	 */
	public int inbornId  = 0  ;
	
	public int getInbornId(){
		return this.inbornId;
	}
	
	public void setInbornId(int v){
		this.inbornId=v;
	}
	
	/**
	 * 
	 */
	public double effect  = 0.0  ;
	
	public double getEffect(){
		return this.effect;
	}
	
	public void setEffect(double v){
		this.effect=v;
	}
	
	
};