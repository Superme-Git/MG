package fire.pb.mission;


public class SLandFightConfig implements mytools.ConvMain.Checkable ,Comparable<SLandFightConfig>{

	public int compareTo(SLandFightConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SLandFightConfig(){
		super();
	}
	public SLandFightConfig(SLandFightConfig arg){
		this.id=arg.id ;
		this.instancezoneid=arg.instancezoneid ;
		this.step=arg.step ;
		this.fightid=arg.fightid ;
		this.award=arg.award ;
		this.instancetype=arg.instancetype ;
		this.params=arg.params ;
		this.times=arg.times ;
		this.sawardid=arg.sawardid ;
		this.sawardtimes=arg.sawardtimes ;
		this.awardid=arg.awardid ;
		this.awardtimes=arg.awardtimes ;
		this.npcmsgid=arg.npcmsgid ;
		this.matchtype=arg.matchtype ;
		this.awardids=arg.awardids ;
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
	public int instancezoneid  = 0  ;
	
	public int getInstancezoneid(){
		return this.instancezoneid;
	}
	
	public void setInstancezoneid(int v){
		this.instancezoneid=v;
	}
	
	/**
	 * 
	 */
	public int step  = 0  ;
	
	public int getStep(){
		return this.step;
	}
	
	public void setStep(int v){
		this.step=v;
	}
	
	/**
	 * 
	 */
	public int fightid  = 0  ;
	
	public int getFightid(){
		return this.fightid;
	}
	
	public void setFightid(int v){
		this.fightid=v;
	}
	
	/**
	 * 
	 */
	public int award  = 0  ;
	
	public int getAward(){
		return this.award;
	}
	
	public void setAward(int v){
		this.award=v;
	}
	
	/**
	 * 
	 */
	public int instancetype  = 0  ;
	
	public int getInstancetype(){
		return this.instancetype;
	}
	
	public void setInstancetype(int v){
		this.instancetype=v;
	}
	
	/**
	 * 
	 */
	public String params  = null  ;
	
	public String getParams(){
		return this.params;
	}
	
	public void setParams(String v){
		this.params=v;
	}
	
	/**
	 * 
	 */
	public int times  = 0  ;
	
	public int getTimes(){
		return this.times;
	}
	
	public void setTimes(int v){
		this.times=v;
	}
	
	/**
	 * 
	 */
	public int sawardid  = 0  ;
	
	public int getSawardid(){
		return this.sawardid;
	}
	
	public void setSawardid(int v){
		this.sawardid=v;
	}
	
	/**
	 * 
	 */
	public int sawardtimes  = 0  ;
	
	public int getSawardtimes(){
		return this.sawardtimes;
	}
	
	public void setSawardtimes(int v){
		this.sawardtimes=v;
	}
	
	/**
	 * 
	 */
	public int awardid  = 0  ;
	
	public int getAwardid(){
		return this.awardid;
	}
	
	public void setAwardid(int v){
		this.awardid=v;
	}
	
	/**
	 * 
	 */
	public int awardtimes  = 0  ;
	
	public int getAwardtimes(){
		return this.awardtimes;
	}
	
	public void setAwardtimes(int v){
		this.awardtimes=v;
	}
	
	/**
	 * 
	 */
	public int npcmsgid  = 0  ;
	
	public int getNpcmsgid(){
		return this.npcmsgid;
	}
	
	public void setNpcmsgid(int v){
		this.npcmsgid=v;
	}
	
	/**
	 * 1 表示匹配个人等级 2 表示队伍平均等级
	 */
	public int matchtype  = 0  ;
	
	public int getMatchtype(){
		return this.matchtype;
	}
	
	public void setMatchtype(int v){
		this.matchtype=v;
	}
	
	/**
	 * 添加根据角色等级匹配不同的奖励id
	 */
	public String awardids  = null  ;
	
	public String getAwardids(){
		return this.awardids;
	}
	
	public void setAwardids(String v){
		this.awardids=v;
	}
	
	
};