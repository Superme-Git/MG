package fire.pb.instance;


public class SBingFengCfg implements mytools.ConvMain.Checkable ,Comparable<SBingFengCfg>{

	public int compareTo(SBingFengCfg o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SBingFengCfg(){
		super();
	}
	public SBingFengCfg(SBingFengCfg arg){
		this.id=arg.id ;
		this.minlevel=arg.minlevel ;
		this.maxlevel=arg.maxlevel ;
		this.instzoneid=arg.instzoneid ;
		this.state=arg.state ;
		this.Map=arg.Map ;
		this.ZuoBiao=arg.ZuoBiao ;
		this.FocusNpc=arg.FocusNpc ;
		this.expawardid=arg.expawardid ;
		this.serviceid=arg.serviceid ;
		this.Fightid=arg.Fightid ;
		this.JiangyouNpc=arg.JiangyouNpc ;
		this.BaoxiangRate=arg.BaoxiangRate ;
		this.awardid=arg.awardid ;
		this.destmap=arg.destmap ;
		this.destx=arg.destx ;
		this.desty=arg.desty ;
		this.boss=arg.boss ;
		this.fanpaitime=arg.fanpaitime ;
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
	public int instzoneid  = 0  ;
	
	public int getInstzoneid(){
		return this.instzoneid;
	}
	
	public void setInstzoneid(int v){
		this.instzoneid=v;
	}
	
	/**
	 * 
	 */
	public int state  = 0  ;
	
	public int getState(){
		return this.state;
	}
	
	public void setState(int v){
		this.state=v;
	}
	
	/**
	 * 
	 */
	public int Map  = 0  ;
	
	public int getMap(){
		return this.Map;
	}
	
	public void setMap(int v){
		this.Map=v;
	}
	
	/**
	 * 
	 */
	public String ZuoBiao  = null  ;
	
	public String getZuoBiao(){
		return this.ZuoBiao;
	}
	
	public void setZuoBiao(String v){
		this.ZuoBiao=v;
	}
	
	/**
	 * 
	 */
	public int FocusNpc  = 0  ;
	
	public int getFocusNpc(){
		return this.FocusNpc;
	}
	
	public void setFocusNpc(int v){
		this.FocusNpc=v;
	}
	
	/**
	 * 
	 */
	public int expawardid  = 0  ;
	
	public int getExpawardid(){
		return this.expawardid;
	}
	
	public void setExpawardid(int v){
		this.expawardid=v;
	}
	
	/**
	 * 
	 */
	public int serviceid  = 0  ;
	
	public int getServiceid(){
		return this.serviceid;
	}
	
	public void setServiceid(int v){
		this.serviceid=v;
	}
	
	/**
	 * 
	 */
	public int Fightid  = 0  ;
	
	public int getFightid(){
		return this.Fightid;
	}
	
	public void setFightid(int v){
		this.Fightid=v;
	}
	
	/**
	 * 
	 */
	public String JiangyouNpc  = null  ;
	
	public String getJiangyouNpc(){
		return this.JiangyouNpc;
	}
	
	public void setJiangyouNpc(String v){
		this.JiangyouNpc=v;
	}
	
	/**
	 * 
	 */
	public String BaoxiangRate  = null  ;
	
	public String getBaoxiangRate(){
		return this.BaoxiangRate;
	}
	
	public void setBaoxiangRate(String v){
		this.BaoxiangRate=v;
	}
	
	/**
	 * 
	 */
	public String awardid  = null  ;
	
	public String getAwardid(){
		return this.awardid;
	}
	
	public void setAwardid(String v){
		this.awardid=v;
	}
	
	/**
	 * 
	 */
	public int destmap  = 0  ;
	
	public int getDestmap(){
		return this.destmap;
	}
	
	public void setDestmap(int v){
		this.destmap=v;
	}
	
	/**
	 * 
	 */
	public int destx  = 0  ;
	
	public int getDestx(){
		return this.destx;
	}
	
	public void setDestx(int v){
		this.destx=v;
	}
	
	/**
	 * 
	 */
	public int desty  = 0  ;
	
	public int getDesty(){
		return this.desty;
	}
	
	public void setDesty(int v){
		this.desty=v;
	}
	
	/**
	 * 
	 */
	public int boss  = 0  ;
	
	public int getBoss(){
		return this.boss;
	}
	
	public void setBoss(int v){
		this.boss=v;
	}
	
	/**
	 * 
	 */
	public int fanpaitime  = 0  ;
	
	public int getFanpaitime(){
		return this.fanpaitime;
	}
	
	public void setFanpaitime(int v){
		this.fanpaitime=v;
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