package fire.pb.mission;


public class SLineTaskFight implements mytools.ConvMain.Checkable ,Comparable<SLineTaskFight>{

	public int compareTo(SLineTaskFight o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SLineTaskFight(){
		super();
	}
	public SLineTaskFight(SLineTaskFight arg){
		this.id=arg.id ;
		this.instancezoneid=arg.instancezoneid ;
		this.step=arg.step ;
		this.fightid=arg.fightid ;
		this.award=arg.award ;
		this.itemawardid=arg.itemawardid ;
		this.awardtimes=arg.awardtimes ;
		this.yuanzhuawardid=arg.yuanzhuawardid ;
		this.yuanzhuawardtimes=arg.yuanzhuawardtimes ;
		this.npcmsgid=arg.npcmsgid ;
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
	public int itemawardid  = 0  ;
	
	public int getItemawardid(){
		return this.itemawardid;
	}
	
	public void setItemawardid(int v){
		this.itemawardid=v;
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
	public int yuanzhuawardid  = 0  ;
	
	public int getYuanzhuawardid(){
		return this.yuanzhuawardid;
	}
	
	public void setYuanzhuawardid(int v){
		this.yuanzhuawardid=v;
	}
	
	/**
	 * 
	 */
	public int yuanzhuawardtimes  = 0  ;
	
	public int getYuanzhuawardtimes(){
		return this.yuanzhuawardtimes;
	}
	
	public void setYuanzhuawardtimes(int v){
		this.yuanzhuawardtimes=v;
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
	
	
};