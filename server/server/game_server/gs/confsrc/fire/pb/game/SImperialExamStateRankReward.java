package fire.pb.game;


public class SImperialExamStateRankReward implements mytools.ConvMain.Checkable ,Comparable<SImperialExamStateRankReward>{

	public int compareTo(SImperialExamStateRankReward o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SImperialExamStateRankReward(){
		super();
	}
	public SImperialExamStateRankReward(SImperialExamStateRankReward arg){
		this.id=arg.id ;
		this.rewardid1=arg.rewardid1 ;
		this.rewardid2=arg.rewardid2 ;
		this.rewardid3=arg.rewardid3 ;
		this.titleid=arg.titleid ;
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
	public int rewardid1  = 0  ;
	
	public int getRewardid1(){
		return this.rewardid1;
	}
	
	public void setRewardid1(int v){
		this.rewardid1=v;
	}
	
	/**
	 * 
	 */
	public int rewardid2  = 0  ;
	
	public int getRewardid2(){
		return this.rewardid2;
	}
	
	public void setRewardid2(int v){
		this.rewardid2=v;
	}
	
	/**
	 * 
	 */
	public int rewardid3  = 0  ;
	
	public int getRewardid3(){
		return this.rewardid3;
	}
	
	public void setRewardid3(int v){
		this.rewardid3=v;
	}
	
	/**
	 * 
	 */
	public int titleid  = 0  ;
	
	public int getTitleid(){
		return this.titleid;
	}
	
	public void setTitleid(int v){
		this.titleid=v;
	}
	
	
};