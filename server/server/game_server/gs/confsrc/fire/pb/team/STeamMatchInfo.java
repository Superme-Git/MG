package fire.pb.team;


public class STeamMatchInfo implements mytools.ConvMain.Checkable ,Comparable<STeamMatchInfo>{

	public int compareTo(STeamMatchInfo o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public STeamMatchInfo(){
		super();
	}
	public STeamMatchInfo(STeamMatchInfo arg){
		this.id=arg.id ;
		this.DisplayInfo=arg.DisplayInfo ;
		this.Target=arg.Target ;
		this.Type=arg.Type ;
		this.MinLevel=arg.MinLevel ;
		this.MaxLevel=arg.MaxLevel ;
		this.MinPlayer=arg.MinPlayer ;
		this.MaxPlayer=arg.MaxPlayer ;
		this.Append=arg.Append ;
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
	public String DisplayInfo  = null  ;
	
	public String getDisplayInfo(){
		return this.DisplayInfo;
	}
	
	public void setDisplayInfo(String v){
		this.DisplayInfo=v;
	}
	
	/**
	 * 
	 */
	public String Target  = null  ;
	
	public String getTarget(){
		return this.Target;
	}
	
	public void setTarget(String v){
		this.Target=v;
	}
	
	/**
	 * 
	 */
	public int Type  = 0  ;
	
	public int getType(){
		return this.Type;
	}
	
	public void setType(int v){
		this.Type=v;
	}
	
	/**
	 * 
	 */
	public int MinLevel  = 0  ;
	
	public int getMinLevel(){
		return this.MinLevel;
	}
	
	public void setMinLevel(int v){
		this.MinLevel=v;
	}
	
	/**
	 * 
	 */
	public int MaxLevel  = 0  ;
	
	public int getMaxLevel(){
		return this.MaxLevel;
	}
	
	public void setMaxLevel(int v){
		this.MaxLevel=v;
	}
	
	/**
	 * 
	 */
	public int MinPlayer  = 0  ;
	
	public int getMinPlayer(){
		return this.MinPlayer;
	}
	
	public void setMinPlayer(int v){
		this.MinPlayer=v;
	}
	
	/**
	 * 
	 */
	public int MaxPlayer  = 0  ;
	
	public int getMaxPlayer(){
		return this.MaxPlayer;
	}
	
	public void setMaxPlayer(int v){
		this.MaxPlayer=v;
	}
	
	/**
	 * 
	 */
	public int Append  = 0  ;
	
	public int getAppend(){
		return this.Append;
	}
	
	public void setAppend(int v){
		this.Append=v;
	}
	
	
};