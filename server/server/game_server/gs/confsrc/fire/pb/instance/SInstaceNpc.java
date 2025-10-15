package fire.pb.instance;


public class SInstaceNpc implements mytools.ConvMain.Checkable ,Comparable<SInstaceNpc>{

	public int compareTo(SInstaceNpc o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SInstaceNpc(){
		super();
	}
	public SInstaceNpc(SInstaceNpc arg){
		this.id=arg.id ;
		this.name=arg.name ;
		this.belongfuben=arg.belongfuben ;
		this.friststate=arg.friststate ;
		this.srrviceslist=arg.srrviceslist ;
		this.actId=arg.actId ;
		this.jinduidlist=arg.jinduidlist ;
		this.rolenumber=arg.rolenumber ;
		this.npcminnumber=arg.npcminnumber ;
		this.npcmaxnumber=arg.npcmaxnumber ;
		this.baoxiangid=arg.baoxiangid ;
		this.baoxingnumber=arg.baoxingnumber ;
		this.npctype=arg.npctype ;
		this.changestate=arg.changestate ;
		this.awardid=arg.awardid ;
		this.matchtype=arg.matchtype ;
		this.awardids=arg.awardids ;
		this.posx=arg.posx ;
		this.posy=arg.posy ;
		this.multibattle=arg.multibattle ;
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
	public String name  = null  ;
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String v){
		this.name=v;
	}
	
	/**
	 * 
	 */
	public int belongfuben  = 0  ;
	
	public int getBelongfuben(){
		return this.belongfuben;
	}
	
	public void setBelongfuben(int v){
		this.belongfuben=v;
	}
	
	/**
	 * 
	 */
	public int friststate  = 0  ;
	
	public int getFriststate(){
		return this.friststate;
	}
	
	public void setFriststate(int v){
		this.friststate=v;
	}
	
	/**
	 * 
	 */
	public String srrviceslist  = null  ;
	
	public String getSrrviceslist(){
		return this.srrviceslist;
	}
	
	public void setSrrviceslist(String v){
		this.srrviceslist=v;
	}
	
	/**
	 * npc对应的动作id
	 */
	public int actId  = 0  ;
	
	public int getActId(){
		return this.actId;
	}
	
	public void setActId(int v){
		this.actId=v;
	}
	
	/**
	 * 进度列表,一个npc可以属于多个进度
	 */
	public String jinduidlist  = null  ;
	
	public String getJinduidlist(){
		return this.jinduidlist;
	}
	
	public void setJinduidlist(String v){
		this.jinduidlist=v;
	}
	
	/**
	 * 
	 */
	public int rolenumber  = 0  ;
	
	public int getRolenumber(){
		return this.rolenumber;
	}
	
	public void setRolenumber(int v){
		this.rolenumber=v;
	}
	
	/**
	 * 
	 */
	public int npcminnumber  = 0  ;
	
	public int getNpcminnumber(){
		return this.npcminnumber;
	}
	
	public void setNpcminnumber(int v){
		this.npcminnumber=v;
	}
	
	/**
	 * 
	 */
	public int npcmaxnumber  = 0  ;
	
	public int getNpcmaxnumber(){
		return this.npcmaxnumber;
	}
	
	public void setNpcmaxnumber(int v){
		this.npcmaxnumber=v;
	}
	
	/**
	 * 
	 */
	public int baoxiangid  = 0  ;
	
	public int getBaoxiangid(){
		return this.baoxiangid;
	}
	
	public void setBaoxiangid(int v){
		this.baoxiangid=v;
	}
	
	/**
	 * 人数系统
	 */
	public int baoxingnumber  = 0  ;
	
	public int getBaoxingnumber(){
		return this.baoxingnumber;
	}
	
	public void setBaoxingnumber(int v){
		this.baoxingnumber=v;
	}
	
	/**
	 * 0 静态Npc,1 动态Npc
	 */
	public int npctype  = 0  ;
	
	public int getNpctype(){
		return this.npctype;
	}
	
	public void setNpctype(int v){
		this.npctype=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<String> changestate  ;
	
	public java.util.ArrayList<String> getChangestate(){
		return this.changestate;
	}
	
	public void setChangestate(java.util.ArrayList<String> v){
		this.changestate=v;
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
	public int posx  = 0  ;
	
	public int getPosx(){
		return this.posx;
	}
	
	public void setPosx(int v){
		this.posx=v;
	}
	
	/**
	 * 
	 */
	public int posy  = 0  ;
	
	public int getPosy(){
		return this.posy;
	}
	
	public void setPosy(int v){
		this.posy=v;
	}
	
	/**
	 * 
	 */
	public int multibattle  = 0  ;
	
	public int getMultibattle(){
		return this.multibattle;
	}
	
	public void setMultibattle(int v){
		this.multibattle=v;
	}
	
	
};