package fire.pb.mission;


public class SLandTask implements mytools.ConvMain.Checkable ,Comparable<SLandTask>{

	public int compareTo(SLandTask o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SLandTask(){
		super();
	}
	public SLandTask(SLandTask arg){
		this.id=arg.id ;
		this.taskname=arg.taskname ;
		this.taskready=arg.taskready ;
		this.minlevel=arg.minlevel ;
		this.maxlevel=arg.maxlevel ;
		this.teamnum=arg.teamnum ;
		this.activityid=arg.activityid ;
		this.activitytypeid=arg.activitytypeid ;
		this.instancezoneid=arg.instancezoneid ;
		this.degree=arg.degree ;
		this.turntype=arg.turntype ;
		this.turngroup=arg.turngroup ;
		this.turnid=arg.turnid ;
		this.awardtype=arg.awardtype ;
		this.awardtime=arg.awardtime ;
		this.xzuobiao=arg.xzuobiao ;
		this.yzuobiao=arg.yzuobiao ;
		this.fanpaiid=arg.fanpaiid ;
		this.fanpailist=arg.fanpailist ;
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
	public String taskname  = null  ;
	
	public String getTaskname(){
		return this.taskname;
	}
	
	public void setTaskname(String v){
		this.taskname=v;
	}
	
	/**
	 * 
	 */
	public int taskready  = 0  ;
	
	public int getTaskready(){
		return this.taskready;
	}
	
	public void setTaskready(int v){
		this.taskready=v;
	}
	
	/**
	 * 
	 */
	public int minlevel  = 0  ;
	
	public int getMinlevel(){
		return this.minlevel;
	}
	
	public void setMinlevel(int v){
		this.minlevel=v;
	}
	
	/**
	 * 
	 */
	public int maxlevel  = 0  ;
	
	public int getMaxlevel(){
		return this.maxlevel;
	}
	
	public void setMaxlevel(int v){
		this.maxlevel=v;
	}
	
	/**
	 * 
	 */
	public int teamnum  = 0  ;
	
	public int getTeamnum(){
		return this.teamnum;
	}
	
	public void setTeamnum(int v){
		this.teamnum=v;
	}
	
	/**
	 * 
	 */
	public int activityid  = 0  ;
	
	public int getActivityid(){
		return this.activityid;
	}
	
	public void setActivityid(int v){
		this.activityid=v;
	}
	
	/**
	 * 
	 */
	public int activitytypeid  = 0  ;
	
	public int getActivitytypeid(){
		return this.activitytypeid;
	}
	
	public void setActivitytypeid(int v){
		this.activitytypeid=v;
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
	public int degree  = 0  ;
	
	public int getDegree(){
		return this.degree;
	}
	
	public void setDegree(int v){
		this.degree=v;
	}
	
	/**
	 * 
	 */
	public int turntype  = 0  ;
	
	public int getTurntype(){
		return this.turntype;
	}
	
	public void setTurntype(int v){
		this.turntype=v;
	}
	
	/**
	 * 
	 */
	public int turngroup  = 0  ;
	
	public int getTurngroup(){
		return this.turngroup;
	}
	
	public void setTurngroup(int v){
		this.turngroup=v;
	}
	
	/**
	 * 
	 */
	public int turnid  = 0  ;
	
	public int getTurnid(){
		return this.turnid;
	}
	
	public void setTurnid(int v){
		this.turnid=v;
	}
	
	/**
	 * 
	 */
	public int awardtype  = 0  ;
	
	public int getAwardtype(){
		return this.awardtype;
	}
	
	public void setAwardtype(int v){
		this.awardtype=v;
	}
	
	/**
	 * 
	 */
	public int awardtime  = 0  ;
	
	public int getAwardtime(){
		return this.awardtime;
	}
	
	public void setAwardtime(int v){
		this.awardtime=v;
	}
	
	/**
	 * 
	 */
	public int xzuobiao  = 0  ;
	
	public int getXzuobiao(){
		return this.xzuobiao;
	}
	
	public void setXzuobiao(int v){
		this.xzuobiao=v;
	}
	
	/**
	 * 
	 */
	public int yzuobiao  = 0  ;
	
	public int getYzuobiao(){
		return this.yzuobiao;
	}
	
	public void setYzuobiao(int v){
		this.yzuobiao=v;
	}
	
	/**
	 * 
	 */
	public int fanpaiid  = 0  ;
	
	public int getFanpaiid(){
		return this.fanpaiid;
	}
	
	public void setFanpaiid(int v){
		this.fanpaiid=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> fanpailist  ;
	
	public java.util.ArrayList<Integer> getFanpailist(){
		return this.fanpailist;
	}
	
	public void setFanpailist(java.util.ArrayList<Integer> v){
		this.fanpailist=v;
	}
	
	
};