package fire.pb.triggers;


public class STriggerCondition implements mytools.ConvMain.Checkable ,Comparable<STriggerCondition>{

	public int compareTo(STriggerCondition o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public STriggerCondition(){
		super();
	}
	public STriggerCondition(STriggerCondition arg){
		this.id=arg.id ;
		this.school=arg.school ;
		this.task=arg.task ;
		this.level=arg.level ;
		this.type=arg.type ;
		this.para=arg.para ;
		this.msg=arg.msg ;
		this.spot=arg.spot ;
		this.chengjiu=arg.chengjiu ;
		this.reward=arg.reward ;
		this.missiondel=arg.missiondel ;
		this.missionadd=arg.missionadd ;
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
	public int school  = 0  ;
	
	public int getSchool(){
		return this.school;
	}
	
	public void setSchool(int v){
		this.school=v;
	}
	
	/**
	 * 
	 */
	public int task  = 0  ;
	
	public int getTask(){
		return this.task;
	}
	
	public void setTask(int v){
		this.task=v;
	}
	
	/**
	 * 
	 */
	public int level  = 0  ;
	
	public int getLevel(){
		return this.level;
	}
	
	public void setLevel(int v){
		this.level=v;
	}
	
	/**
	 * 
	 */
	public int type  = 0  ;
	
	public int getType(){
		return this.type;
	}
	
	public void setType(int v){
		this.type=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<String> para  ;
	
	public java.util.ArrayList<String> getPara(){
		return this.para;
	}
	
	public void setPara(java.util.ArrayList<String> v){
		this.para=v;
	}
	
	/**
	 * 
	 */
	public int msg  = 0  ;
	
	public int getMsg(){
		return this.msg;
	}
	
	public void setMsg(int v){
		this.msg=v;
	}
	
	/**
	 * 
	 */
	public int spot  = 0  ;
	
	public int getSpot(){
		return this.spot;
	}
	
	public void setSpot(int v){
		this.spot=v;
	}
	
	/**
	 * 
	 */
	public int chengjiu  = 0  ;
	
	public int getChengjiu(){
		return this.chengjiu;
	}
	
	public void setChengjiu(int v){
		this.chengjiu=v;
	}
	
	/**
	 * 
	 */
	public int reward  = 0  ;
	
	public int getReward(){
		return this.reward;
	}
	
	public void setReward(int v){
		this.reward=v;
	}
	
	/**
	 * 
	 */
	public int missiondel  = 0  ;
	
	public int getMissiondel(){
		return this.missiondel;
	}
	
	public void setMissiondel(int v){
		this.missiondel=v;
	}
	
	/**
	 * 
	 */
	public int missionadd  = 0  ;
	
	public int getMissionadd(){
		return this.missionadd;
	}
	
	public void setMissionadd(int v){
		this.missionadd=v;
	}
	
	
};