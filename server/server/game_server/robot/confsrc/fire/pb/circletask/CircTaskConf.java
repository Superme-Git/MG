package fire.pb.circletask;


public class CircTaskConf implements mytools.ConvMain.Checkable ,Comparable<CircTaskConf>{

	public int compareTo(CircTaskConf o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public CircTaskConf(){
		super();
	}
	public CircTaskConf(CircTaskConf arg){
		this.id=arg.id ;
		this.strtaskname=arg.strtaskname ;
		this.cttype=arg.cttype ;
		this.ctgroup=arg.ctgroup ;
		this.gototype=arg.gototype ;
		this.autocomp=arg.autocomp ;
		this.submitnpc=arg.submitnpc ;
		this.awardid=arg.awardid ;
		this.matchtype=arg.matchtype ;
		this.awardids=arg.awardids ;
		this.isrenxing=arg.isrenxing ;
		this.nextquest=arg.nextquest ;
		this.completetip=arg.completetip ;
		this.tasklevel=arg.tasklevel ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 循环任务id(给随机用的唯一标示，不对应任务类型)
	 */
	public int id  = 0  ;
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int v){
		this.id=v;
	}
	
	/**
	 * 任务名称(提示用)
	 */
	public String strtaskname  = null  ;
	
	public String getStrtaskname(){
		return this.strtaskname;
	}
	
	public void setStrtaskname(String v){
		this.strtaskname=v;
	}
	
	/**
	 * 送信、使用物品之类
	 */
	public int cttype  = 0  ;
	
	public int getCttype(){
		return this.cttype;
	}
	
	public void setCttype(int v){
		this.cttype=v;
	}
	
	/**
	 * 对应到各个类型的分表使用
	 */
	public int ctgroup  = 0  ;
	
	public int getCtgroup(){
		return this.ctgroup;
	}
	
	public void setCtgroup(int v){
		this.ctgroup=v;
	}
	
	/**
	 * 
	 */
	public int gototype  = 0  ;
	
	public int getGototype(){
		return this.gototype;
	}
	
	public void setGototype(int v){
		this.gototype=v;
	}
	
	/**
	 * 
	 */
	public int autocomp  = 0  ;
	
	public int getAutocomp(){
		return this.autocomp;
	}
	
	public void setAutocomp(int v){
		this.autocomp=v;
	}
	
	/**
	 * 
	 */
	public String submitnpc  = null  ;
	
	public String getSubmitnpc(){
		return this.submitnpc;
	}
	
	public void setSubmitnpc(String v){
		this.submitnpc=v;
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
	
	/**
	 * 
	 */
	public int isrenxing  = 0  ;
	
	public int getIsrenxing(){
		return this.isrenxing;
	}
	
	public void setIsrenxing(int v){
		this.isrenxing=v;
	}
	
	/**
	 * 
	 */
	public int nextquest  = 0  ;
	
	public int getNextquest(){
		return this.nextquest;
	}
	
	public void setNextquest(int v){
		this.nextquest=v;
	}
	
	/**
	 * 
	 */
	public int completetip  = 0  ;
	
	public int getCompletetip(){
		return this.completetip;
	}
	
	public void setCompletetip(int v){
		this.completetip=v;
	}
	
	/**
	 * 
	 */
	public int tasklevel  = 0  ;
	
	public int getTasklevel(){
		return this.tasklevel;
	}
	
	public void setTasklevel(int v){
		this.tasklevel=v;
	}
	
	
};