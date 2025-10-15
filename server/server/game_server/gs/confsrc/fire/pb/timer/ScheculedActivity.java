package fire.pb.timer;


public class ScheculedActivity implements mytools.ConvMain.Checkable {


	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public ScheculedActivity(){
		super();
	}
	public ScheculedActivity(ScheculedActivity arg){
		this.id=arg.id ;
		this.activityid=arg.activityid ;
		this.zoneid=arg.zoneid ;
		this.startTime=arg.startTime ;
		this.endTime=arg.endTime ;
		this.remind=arg.remind ;
		this.latetime=arg.latetime ;
		this.advanceremind=arg.advanceremind ;
		this.weekrepeat=arg.weekrepeat ;
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
	public String zoneid  = null  ;
	
	public String getZoneid(){
		return this.zoneid;
	}
	
	public void setZoneid(String v){
		this.zoneid=v;
	}
	
	/**
	 * 
	 */
	public String startTime  = null  ;
	
	public String getStartTime(){
		return this.startTime;
	}
	
	public void setStartTime(String v){
		this.startTime=v;
	}
	
	/**
	 * 
	 */
	public String endTime  = null  ;
	
	public String getEndTime(){
		return this.endTime;
	}
	
	public void setEndTime(String v){
		this.endTime=v;
	}
	
	/**
	 * 
	 */
	public int remind  = 0  ;
	
	public int getRemind(){
		return this.remind;
	}
	
	public void setRemind(int v){
		this.remind=v;
	}
	
	/**
	 * 
	 */
	public int latetime  = 0  ;
	
	public int getLatetime(){
		return this.latetime;
	}
	
	public void setLatetime(int v){
		this.latetime=v;
	}
	
	/**
	 * 
	 */
	public int advanceremind  = 0  ;
	
	public int getAdvanceremind(){
		return this.advanceremind;
	}
	
	public void setAdvanceremind(int v){
		this.advanceremind=v;
	}
	
	/**
	 * 
	 */
	public int weekrepeat  = 0  ;
	
	public int getWeekrepeat(){
		return this.weekrepeat;
	}
	
	public void setWeekrepeat(int v){
		this.weekrepeat=v;
	}
	
	
};