package fire.pb.role;


public class NextExpAndResMoney implements mytools.ConvMain.Checkable ,Comparable<NextExpAndResMoney>{

	public int compareTo(NextExpAndResMoney o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public NextExpAndResMoney(){
		super();
	}
	public NextExpAndResMoney(NextExpAndResMoney arg){
		this.id=arg.id ;
		this.exp=arg.exp ;
		this.petfightnum=arg.petfightnum ;
		this.addpointschemenum=arg.addpointschemenum ;
		this.offlexppermin=arg.offlexppermin ;
		this.dpointexp=arg.dpointexp ;
		this.standardexp=arg.standardexp ;
		this.standardmoney=arg.standardmoney ;
		this.helpfightnum=arg.helpfightnum ;
		this.helpgiveitemnum=arg.helpgiveitemnum ;
		this.helpitemnum=arg.helpitemnum ;
		this.shengwangmax=arg.shengwangmax ;
		this.clanvaluemax=arg.clanvaluemax ;
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
	public int exp  = 0  ;
	
	public int getExp(){
		return this.exp;
	}
	
	public void setExp(int v){
		this.exp=v;
	}
	
	/**
	 * 
	 */
	public int petfightnum  = 0  ;
	
	public int getPetfightnum(){
		return this.petfightnum;
	}
	
	public void setPetfightnum(int v){
		this.petfightnum=v;
	}
	
	/**
	 * 
	 */
	public int addpointschemenum  = 0  ;
	
	public int getAddpointschemenum(){
		return this.addpointschemenum;
	}
	
	public void setAddpointschemenum(int v){
		this.addpointschemenum=v;
	}
	
	/**
	 * 
	 */
	public int offlexppermin  = 0  ;
	
	public int getOfflexppermin(){
		return this.offlexppermin;
	}
	
	public void setOfflexppermin(int v){
		this.offlexppermin=v;
	}
	
	/**
	 * 
	 */
	public int dpointexp  = 0  ;
	
	public int getDpointexp(){
		return this.dpointexp;
	}
	
	public void setDpointexp(int v){
		this.dpointexp=v;
	}
	
	/**
	 * 
	 */
	public int standardexp  = 0  ;
	
	public int getStandardexp(){
		return this.standardexp;
	}
	
	public void setStandardexp(int v){
		this.standardexp=v;
	}
	
	/**
	 * 
	 */
	public int standardmoney  = 0  ;
	
	public int getStandardmoney(){
		return this.standardmoney;
	}
	
	public void setStandardmoney(int v){
		this.standardmoney=v;
	}
	
	/**
	 * 
	 */
	public int helpfightnum  = 0  ;
	
	public int getHelpfightnum(){
		return this.helpfightnum;
	}
	
	public void setHelpfightnum(int v){
		this.helpfightnum=v;
	}
	
	/**
	 * 
	 */
	public int helpgiveitemnum  = 0  ;
	
	public int getHelpgiveitemnum(){
		return this.helpgiveitemnum;
	}
	
	public void setHelpgiveitemnum(int v){
		this.helpgiveitemnum=v;
	}
	
	/**
	 * 
	 */
	public int helpitemnum  = 0  ;
	
	public int getHelpitemnum(){
		return this.helpitemnum;
	}
	
	public void setHelpitemnum(int v){
		this.helpitemnum=v;
	}
	
	/**
	 * 
	 */
	public int shengwangmax  = 0  ;
	
	public int getShengwangmax(){
		return this.shengwangmax;
	}
	
	public void setShengwangmax(int v){
		this.shengwangmax=v;
	}
	
	/**
	 * 
	 */
	public int clanvaluemax  = 0  ;
	
	public int getClanvaluemax(){
		return this.clanvaluemax;
	}
	
	public void setClanvaluemax(int v){
		this.clanvaluemax=v;
	}
	
	
};