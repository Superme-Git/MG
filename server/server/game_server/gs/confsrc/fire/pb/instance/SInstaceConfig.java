package fire.pb.instance;


public class SInstaceConfig implements mytools.ConvMain.Checkable ,Comparable<SInstaceConfig>{

	public int compareTo(SInstaceConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SInstaceConfig(){
		super();
	}
	public SInstaceConfig(SInstaceConfig arg){
		this.id=arg.id ;
		this.name=arg.name ;
		this.classname=arg.classname ;
		this.serviceid=arg.serviceid ;
		this.afterid=arg.afterid ;
		this.team=arg.team ;
		this.minlevel=arg.minlevel ;
		this.maxlevel=arg.maxlevel ;
		this.playernum=arg.playernum ;
		this.maxnum=arg.maxnum ;
		this.mapids=arg.mapids ;
		this.mapidfirst=arg.mapidfirst ;
		this.posxfirst=arg.posxfirst ;
		this.posyfirst=arg.posyfirst ;
		this.mapidgoto=arg.mapidgoto ;
		this.posxgoto=arg.posxgoto ;
		this.posygoto=arg.posygoto ;
		this.refreshtype=arg.refreshtype ;
		this.gototime=arg.gototime ;
		this.gototype=arg.gototype ;
		this.refreshtime=arg.refreshtime ;
		this.firstjindu=arg.firstjindu ;
		this.lastjindu=arg.lastjindu ;
		this.destroy=arg.destroy ;
		this.iscreate=arg.iscreate ;
		this.xiezhan=arg.xiezhan ;
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
	public String classname  = null  ;
	
	public String getClassname(){
		return this.classname;
	}
	
	public void setClassname(String v){
		this.classname=v;
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
	public int team  = 0  ;
	
	public int getTeam(){
		return this.team;
	}
	
	public void setTeam(int v){
		this.team=v;
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
	public int playernum  = 0  ;
	
	public int getPlayernum(){
		return this.playernum;
	}
	
	public void setPlayernum(int v){
		this.playernum=v;
	}
	
	/**
	 * 
	 */
	public int maxnum  = 0  ;
	
	public int getMaxnum(){
		return this.maxnum;
	}
	
	public void setMaxnum(int v){
		this.maxnum=v;
	}
	
	/**
	 * 
	 */
	public String mapids  = null  ;
	
	public String getMapids(){
		return this.mapids;
	}
	
	public void setMapids(String v){
		this.mapids=v;
	}
	
	/**
	 * 
	 */
	public int mapidfirst  = 0  ;
	
	public int getMapidfirst(){
		return this.mapidfirst;
	}
	
	public void setMapidfirst(int v){
		this.mapidfirst=v;
	}
	
	/**
	 * 
	 */
	public int posxfirst  = 0  ;
	
	public int getPosxfirst(){
		return this.posxfirst;
	}
	
	public void setPosxfirst(int v){
		this.posxfirst=v;
	}
	
	/**
	 * 
	 */
	public int posyfirst  = 0  ;
	
	public int getPosyfirst(){
		return this.posyfirst;
	}
	
	public void setPosyfirst(int v){
		this.posyfirst=v;
	}
	
	/**
	 * 
	 */
	public int mapidgoto  = 0  ;
	
	public int getMapidgoto(){
		return this.mapidgoto;
	}
	
	public void setMapidgoto(int v){
		this.mapidgoto=v;
	}
	
	/**
	 * 
	 */
	public int posxgoto  = 0  ;
	
	public int getPosxgoto(){
		return this.posxgoto;
	}
	
	public void setPosxgoto(int v){
		this.posxgoto=v;
	}
	
	/**
	 * 
	 */
	public int posygoto  = 0  ;
	
	public int getPosygoto(){
		return this.posygoto;
	}
	
	public void setPosygoto(int v){
		this.posygoto=v;
	}
	
	/**
	 * 
	 */
	public int refreshtype  = 0  ;
	
	public int getRefreshtype(){
		return this.refreshtype;
	}
	
	public void setRefreshtype(int v){
		this.refreshtype=v;
	}
	
	/**
	 * 
	 */
	public String gototime  = null  ;
	
	public String getGototime(){
		return this.gototime;
	}
	
	public void setGototime(String v){
		this.gototime=v;
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
	public String refreshtime  = null  ;
	
	public String getRefreshtime(){
		return this.refreshtime;
	}
	
	public void setRefreshtime(String v){
		this.refreshtime=v;
	}
	
	/**
	 * 
	 */
	public int firstjindu  = 0  ;
	
	public int getFirstjindu(){
		return this.firstjindu;
	}
	
	public void setFirstjindu(int v){
		this.firstjindu=v;
	}
	
	/**
	 * 
	 */
	public int lastjindu  = 0  ;
	
	public int getLastjindu(){
		return this.lastjindu;
	}
	
	public void setLastjindu(int v){
		this.lastjindu=v;
	}
	
	/**
	 * 
	 */
	public int destroy  = 0  ;
	
	public int getDestroy(){
		return this.destroy;
	}
	
	public void setDestroy(int v){
		this.destroy=v;
	}
	
	/**
	 * 
	 */
	public int iscreate  = 0  ;
	
	public int getIscreate(){
		return this.iscreate;
	}
	
	public void setIscreate(int v){
		this.iscreate=v;
	}
	
	/**
	 * 
	 */
	public int xiezhan  = 0  ;
	
	public int getXiezhan(){
		return this.xiezhan;
	}
	
	public void setXiezhan(int v){
		this.xiezhan=v;
	}
	
	
};