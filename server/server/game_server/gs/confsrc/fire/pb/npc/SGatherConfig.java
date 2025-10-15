package fire.pb.npc;


public class SGatherConfig implements mytools.ConvMain.Checkable ,Comparable<SGatherConfig>{

	public int compareTo(SGatherConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SGatherConfig(){
		super();
	}
	public SGatherConfig(SGatherConfig arg){
		this.id=arg.id ;
		this.progresstime=arg.progresstime ;
		this.coolingtime=arg.coolingtime ;
		this.tasks=arg.tasks ;
		this.战斗概率=arg.战斗概率 ;
		this.战斗ID=arg.战斗ID ;
		this.获得概率=arg.获得概率 ;
		this.奖励=arg.奖励 ;
		this.可采集次数=arg.可采集次数 ;
		this.是否消失=arg.是否消失 ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 采集npcid
	 */
	public int id  = 0  ;
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int v){
		this.id=v;
	}
	
	/**
	 * 读条时间
	 */
	public int progresstime  = 0  ;
	
	public int getProgresstime(){
		return this.progresstime;
	}
	
	public void setProgresstime(int v){
		this.progresstime=v;
	}
	
	/**
	 * 冷却时间
	 */
	public int coolingtime  = 0  ;
	
	public int getCoolingtime(){
		return this.coolingtime;
	}
	
	public void setCoolingtime(int v){
		this.coolingtime=v;
	}
	
	/**
	 * 任务需求
	 */
	public java.util.ArrayList<Integer> tasks  ;
	
	public java.util.ArrayList<Integer> getTasks(){
		return this.tasks;
	}
	
	public void setTasks(java.util.ArrayList<Integer> v){
		this.tasks=v;
	}
	
	/**
	 * 
	 */
	public int 战斗概率  = 0  ;
	
	public int get战斗概率(){
		return this.战斗概率;
	}
	
	public void set战斗概率(int v){
		this.战斗概率=v;
	}
	
	/**
	 * 
	 */
	public int 战斗ID  = 0  ;
	
	public int get战斗ID(){
		return this.战斗ID;
	}
	
	public void set战斗ID(int v){
		this.战斗ID=v;
	}
	
	/**
	 * 
	 */
	public int 获得概率  = 0  ;
	
	public int get获得概率(){
		return this.获得概率;
	}
	
	public void set获得概率(int v){
		this.获得概率=v;
	}
	
	/**
	 * 
	 */
	public int 奖励  = 0  ;
	
	public int get奖励(){
		return this.奖励;
	}
	
	public void set奖励(int v){
		this.奖励=v;
	}
	
	/**
	 * 
	 */
	public int 可采集次数  = 0  ;
	
	public int get可采集次数(){
		return this.可采集次数;
	}
	
	public void set可采集次数(int v){
		this.可采集次数=v;
	}
	
	/**
	 * 
	 */
	public int 是否消失  = 0  ;
	
	public int get是否消失(){
		return this.是否消失;
	}
	
	public void set是否消失(int v){
		this.是否消失=v;
	}
	
	
};