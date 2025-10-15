package fire.pb.game;


public class SFirstPayReward implements mytools.ConvMain.Checkable ,Comparable<SFirstPayReward>{

	public int compareTo(SFirstPayReward o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SFirstPayReward(){
		super();
	}
	public SFirstPayReward(SFirstPayReward arg){
		this.id=arg.id ;
		this.rewardmap=arg.rewardmap ;
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
	public java.util.ArrayList<FirstPayGiftData> rewardmap  ;
	
	public java.util.ArrayList<FirstPayGiftData> getRewardmap(){
		return this.rewardmap;
	}
	
	public void setRewardmap(java.util.ArrayList<FirstPayGiftData> v){
		this.rewardmap=v;
	}
	
	
};