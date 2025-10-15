package fire.pb.instance;


public class SInstanceSave implements mytools.ConvMain.Checkable ,Comparable<SInstanceSave>{

	public int compareTo(SInstanceSave o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SInstanceSave(){
		super();
	}
	public SInstanceSave(SInstanceSave arg){
		this.id=arg.id ;
		this.name=arg.name ;
		this.jinduid=arg.jinduid ;
		this.belongfuben=arg.belongfuben ;
		this.maxpoint=arg.maxpoint ;
		this.activeif=arg.activeif ;
		this.endif=arg.endif ;
		this.activeCG=arg.activeCG ;
		this.endCG=arg.endCG ;
		this.chestlibid=arg.chestlibid ;
		this.chestnum=arg.chestnum ;
		this.awardid=arg.awardid ;
		this.isstep=arg.isstep ;
		this.gotoposition=arg.gotoposition ;
		this.haveboss=arg.haveboss ;
		this.pastaward=arg.pastaward ;
		this.mendaward=arg.mendaward ;
		this.pastexpaward=arg.pastexpaward ;
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
	public int jinduid  = 0  ;
	
	public int getJinduid(){
		return this.jinduid;
	}
	
	public void setJinduid(int v){
		this.jinduid=v;
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
	public int maxpoint  = 0  ;
	
	public int getMaxpoint(){
		return this.maxpoint;
	}
	
	public void setMaxpoint(int v){
		this.maxpoint=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<String> activeif  ;
	
	public java.util.ArrayList<String> getActiveif(){
		return this.activeif;
	}
	
	public void setActiveif(java.util.ArrayList<String> v){
		this.activeif=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<String> endif  ;
	
	public java.util.ArrayList<String> getEndif(){
		return this.endif;
	}
	
	public void setEndif(java.util.ArrayList<String> v){
		this.endif=v;
	}
	
	/**
	 * 
	 */
	public int activeCG  = 0  ;
	
	public int getActiveCG(){
		return this.activeCG;
	}
	
	public void setActiveCG(int v){
		this.activeCG=v;
	}
	
	/**
	 * 
	 */
	public int endCG  = 0  ;
	
	public int getEndCG(){
		return this.endCG;
	}
	
	public void setEndCG(int v){
		this.endCG=v;
	}
	
	/**
	 * 
	 */
	public int chestlibid  = 0  ;
	
	public int getChestlibid(){
		return this.chestlibid;
	}
	
	public void setChestlibid(int v){
		this.chestlibid=v;
	}
	
	/**
	 * 
	 */
	public int chestnum  = 0  ;
	
	public int getChestnum(){
		return this.chestnum;
	}
	
	public void setChestnum(int v){
		this.chestnum=v;
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
	public int isstep  = 0  ;
	
	public int getIsstep(){
		return this.isstep;
	}
	
	public void setIsstep(int v){
		this.isstep=v;
	}
	
	/**
	 * 
	 */
	public String gotoposition  = null  ;
	
	public String getGotoposition(){
		return this.gotoposition;
	}
	
	public void setGotoposition(String v){
		this.gotoposition=v;
	}
	
	/**
	 * 
	 */
	public int haveboss  = 0  ;
	
	public int getHaveboss(){
		return this.haveboss;
	}
	
	public void setHaveboss(int v){
		this.haveboss=v;
	}
	
	/**
	 * 
	 */
	public int pastaward  = 0  ;
	
	public int getPastaward(){
		return this.pastaward;
	}
	
	public void setPastaward(int v){
		this.pastaward=v;
	}
	
	/**
	 * 
	 */
	public int mendaward  = 0  ;
	
	public int getMendaward(){
		return this.mendaward;
	}
	
	public void setMendaward(int v){
		this.mendaward=v;
	}
	
	/**
	 * 
	 */
	public int pastexpaward  = 0  ;
	
	public int getPastexpaward(){
		return this.pastexpaward;
	}
	
	public void setPastexpaward(int v){
		this.pastexpaward=v;
	}
	
	
};