package fire.pb.npc;


public class SBaoxiang implements mytools.ConvMain.Checkable ,Comparable<SBaoxiang>{

	public int compareTo(SBaoxiang o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SBaoxiang(){
		super();
	}
	public SBaoxiang(SBaoxiang arg){
		this.id=arg.id ;
		this.baoxianglist=arg.baoxianglist ;
		this.baoxiangtype=arg.baoxiangtype ;
		this.opentimes=arg.opentimes ;
		this.openovermsg=arg.openovermsg ;
		this.openteam=arg.openteam ;
		this.rewardid=arg.rewardid ;
		this.openlevel=arg.openlevel ;
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
	public int baoxianglist  = 0  ;
	
	public int getBaoxianglist(){
		return this.baoxianglist;
	}
	
	public void setBaoxianglist(int v){
		this.baoxianglist=v;
	}
	
	/**
	 * 
	 */
	public int baoxiangtype  = 0  ;
	
	public int getBaoxiangtype(){
		return this.baoxiangtype;
	}
	
	public void setBaoxiangtype(int v){
		this.baoxiangtype=v;
	}
	
	/**
	 * 玩家每天可开启的次数
	 */
	public int opentimes  = 0  ;
	
	public int getOpentimes(){
		return this.opentimes;
	}
	
	public void setOpentimes(int v){
		this.opentimes=v;
	}
	
	/**
	 * 
	 */
	public int openovermsg  = 0  ;
	
	public int getOpenovermsg(){
		return this.openovermsg;
	}
	
	public void setOpenovermsg(int v){
		this.openovermsg=v;
	}
	
	/**
	 * 
	 */
	public int openteam  = 0  ;
	
	public int getOpenteam(){
		return this.openteam;
	}
	
	public void setOpenteam(int v){
		this.openteam=v;
	}
	
	/**
	 * 
	 */
	public int rewardid  = 0  ;
	
	public int getRewardid(){
		return this.rewardid;
	}
	
	public void setRewardid(int v){
		this.rewardid=v;
	}
	
	/**
	 * 
	 */
	public int openlevel  = 0  ;
	
	public int getOpenlevel(){
		return this.openlevel;
	}
	
	public void setOpenlevel(int v){
		this.openlevel=v;
	}
	
	
};