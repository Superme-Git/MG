package fire.pb.circletask;


public class AnYeMaXiTuanConf implements mytools.ConvMain.Checkable ,Comparable<AnYeMaXiTuanConf>{

	public int compareTo(AnYeMaXiTuanConf o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public AnYeMaXiTuanConf(){
		super();
	}
	public AnYeMaXiTuanConf(AnYeMaXiTuanConf arg){
		this.id=arg.id ;
		this.levelgroup=arg.levelgroup ;
		this.levelmin=arg.levelmin ;
		this.levelmax=arg.levelmax ;
		this.roundgroup=arg.roundgroup ;
		this.roundmin=arg.roundmin ;
		this.roundmax=arg.roundmax ;
		this.roundrate=arg.roundrate ;
		this.period=arg.period ;
		this.tasktype=arg.tasktype ;
		this.group=arg.group ;
		this.isrenxing=arg.isrenxing ;
		this.renxingcost=arg.renxingcost ;
		this.normalaward=arg.normalaward ;
		this.extaward=arg.extaward ;
		this.specialaward=arg.specialaward ;
		this.oneloopaward=arg.oneloopaward ;
		this.round40award=arg.round40award ;
		this.round80award=arg.round80award ;
		this.round120award=arg.round120award ;
		this.round160award=arg.round160award ;
		this.comtips=arg.comtips ;
		this.taskname=arg.taskname ;
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
	public int levelgroup  = 0  ;
	
	public int getLevelgroup(){
		return this.levelgroup;
	}
	
	public void setLevelgroup(int v){
		this.levelgroup=v;
	}
	
	/**
	 * 
	 */
	public int levelmin  = 0  ;
	
	public int getLevelmin(){
		return this.levelmin;
	}
	
	public void setLevelmin(int v){
		this.levelmin=v;
	}
	
	/**
	 * 
	 */
	public int levelmax  = 0  ;
	
	public int getLevelmax(){
		return this.levelmax;
	}
	
	public void setLevelmax(int v){
		this.levelmax=v;
	}
	
	/**
	 * 
	 */
	public int roundgroup  = 0  ;
	
	public int getRoundgroup(){
		return this.roundgroup;
	}
	
	public void setRoundgroup(int v){
		this.roundgroup=v;
	}
	
	/**
	 * 
	 */
	public int roundmin  = 0  ;
	
	public int getRoundmin(){
		return this.roundmin;
	}
	
	public void setRoundmin(int v){
		this.roundmin=v;
	}
	
	/**
	 * 
	 */
	public int roundmax  = 0  ;
	
	public int getRoundmax(){
		return this.roundmax;
	}
	
	public void setRoundmax(int v){
		this.roundmax=v;
	}
	
	/**
	 * 
	 */
	public int roundrate  = 0  ;
	
	public int getRoundrate(){
		return this.roundrate;
	}
	
	public void setRoundrate(int v){
		this.roundrate=v;
	}
	
	/**
	 * 
	 */
	public int period  = 0  ;
	
	public int getPeriod(){
		return this.period;
	}
	
	public void setPeriod(int v){
		this.period=v;
	}
	
	/**
	 * 
	 */
	public int tasktype  = 0  ;
	
	public int getTasktype(){
		return this.tasktype;
	}
	
	public void setTasktype(int v){
		this.tasktype=v;
	}
	
	/**
	 * 
	 */
	public int group  = 0  ;
	
	public int getGroup(){
		return this.group;
	}
	
	public void setGroup(int v){
		this.group=v;
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
	public int renxingcost  = 0  ;
	
	public int getRenxingcost(){
		return this.renxingcost;
	}
	
	public void setRenxingcost(int v){
		this.renxingcost=v;
	}
	
	/**
	 * 
	 */
	public int normalaward  = 0  ;
	
	public int getNormalaward(){
		return this.normalaward;
	}
	
	public void setNormalaward(int v){
		this.normalaward=v;
	}
	
	/**
	 * 
	 */
	public int extaward  = 0  ;
	
	public int getExtaward(){
		return this.extaward;
	}
	
	public void setExtaward(int v){
		this.extaward=v;
	}
	
	/**
	 * 
	 */
	public int specialaward  = 0  ;
	
	public int getSpecialaward(){
		return this.specialaward;
	}
	
	public void setSpecialaward(int v){
		this.specialaward=v;
	}
	
	/**
	 * 
	 */
	public int oneloopaward  = 0  ;
	
	public int getOneloopaward(){
		return this.oneloopaward;
	}
	
	public void setOneloopaward(int v){
		this.oneloopaward=v;
	}
	
	/**
	 * 
	 */
	public int round40award  = 0  ;
	
	public int getRound40award(){
		return this.round40award;
	}
	
	public void setRound40award(int v){
		this.round40award=v;
	}
	
	/**
	 * 
	 */
	public int round80award  = 0  ;
	
	public int getRound80award(){
		return this.round80award;
	}
	
	public void setRound80award(int v){
		this.round80award=v;
	}
	
	/**
	 * 
	 */
	public int round120award  = 0  ;
	
	public int getRound120award(){
		return this.round120award;
	}
	
	public void setRound120award(int v){
		this.round120award=v;
	}
	
	/**
	 * 
	 */
	public int round160award  = 0  ;
	
	public int getRound160award(){
		return this.round160award;
	}
	
	public void setRound160award(int v){
		this.round160award=v;
	}
	
	/**
	 * 
	 */
	public int comtips  = 0  ;
	
	public int getComtips(){
		return this.comtips;
	}
	
	public void setComtips(int v){
		this.comtips=v;
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
	
	
};