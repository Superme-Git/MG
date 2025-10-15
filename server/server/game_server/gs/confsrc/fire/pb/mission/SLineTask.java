package fire.pb.mission;


public class SLineTask implements mytools.ConvMain.Checkable ,Comparable<SLineTask>{

	public int compareTo(SLineTask o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SLineTask(){
		super();
	}
	public SLineTask(SLineTask arg){
		this.id=arg.id ;
		this.taskname=arg.taskname ;
		this.minlevel=arg.minlevel ;
		this.teamnum=arg.teamnum ;
		this.activitytypeid=arg.activitytypeid ;
		this.afterid=arg.afterid ;
		this.awardtype=arg.awardtype ;
		this.xzuobiao=arg.xzuobiao ;
		this.yzuobiao=arg.yzuobiao ;
		this.awardid=arg.awardid ;
		this.stepnum=arg.stepnum ;
		this.fanpailist=arg.fanpailist ;
		this.grade4=arg.grade4 ;
		this.grade3=arg.grade3 ;
		this.grade2=arg.grade2 ;
		this.grade1=arg.grade1 ;
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
	public int afterid  = 0  ;
	
	public int getAfterid(){
		return this.afterid;
	}
	
	public void setAfterid(int v){
		this.afterid=v;
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
	public int stepnum  = 0  ;
	
	public int getStepnum(){
		return this.stepnum;
	}
	
	public void setStepnum(int v){
		this.stepnum=v;
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
	
	/**
	 * 
	 */
	public String grade4  = null  ;
	
	public String getGrade4(){
		return this.grade4;
	}
	
	public void setGrade4(String v){
		this.grade4=v;
	}
	
	/**
	 * 
	 */
	public String grade3  = null  ;
	
	public String getGrade3(){
		return this.grade3;
	}
	
	public void setGrade3(String v){
		this.grade3=v;
	}
	
	/**
	 * 
	 */
	public String grade2  = null  ;
	
	public String getGrade2(){
		return this.grade2;
	}
	
	public void setGrade2(String v){
		this.grade2=v;
	}
	
	/**
	 * 
	 */
	public String grade1  = null  ;
	
	public String getGrade1(){
		return this.grade1;
	}
	
	public void setGrade1(String v){
		this.grade1=v;
	}
	
	
};