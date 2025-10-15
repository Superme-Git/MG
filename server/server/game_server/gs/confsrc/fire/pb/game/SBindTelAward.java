package fire.pb.game;


public class SBindTelAward implements mytools.ConvMain.Checkable ,Comparable<SBindTelAward>{

	public int compareTo(SBindTelAward o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SBindTelAward(){
		super();
	}
	public SBindTelAward(SBindTelAward arg){
		this.id=arg.id ;
		this.rewardvec=arg.rewardvec ;
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
	public java.util.ArrayList<BindTelAwardData> rewardvec  ;
	
	public java.util.ArrayList<BindTelAwardData> getRewardvec(){
		return this.rewardvec;
	}
	
	public void setRewardvec(java.util.ArrayList<BindTelAwardData> v){
		this.rewardvec=v;
	}
	
	
};