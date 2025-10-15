package fire.pb.battle;


public class SAIAction implements mytools.ConvMain.Checkable ,Comparable<SAIAction>{

	public int compareTo(SAIAction o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SAIAction(){
		super();
	}
	public SAIAction(SAIAction arg){
		this.id=arg.id ;
		this.clientAction=arg.clientAction ;
		this.changeshape=arg.changeshape ;
		this.changeground=arg.changeground ;
		this.changebgm=arg.changebgm ;
		this.optiontype=arg.optiontype ;
		this.optionid=arg.optionid ;
		this.skillsoltid=arg.skillsoltid ;
		this.skillfactor=arg.skillfactor ;
		this.skillconstant=arg.skillconstant ;
		this.escapeodds=arg.escapeodds ;
		this.summons=arg.summons ;
		this.target=arg.target ;
		this.bonustask=arg.bonustask ;
		this.changeaim=arg.changeaim ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * id
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
	public boolean clientAction  = false  ;
	
	public boolean getClientAction(){
		return this.clientAction;
	}
	
	public void setClientAction(boolean v){
		this.clientAction=v;
	}
	
	/**
	 * 
	 */
	public String changeshape  = null  ;
	
	public String getChangeshape(){
		return this.changeshape;
	}
	
	public void setChangeshape(String v){
		this.changeshape=v;
	}
	
	/**
	 * 
	 */
	public int changeground  = 0  ;
	
	public int getChangeground(){
		return this.changeground;
	}
	
	public void setChangeground(int v){
		this.changeground=v;
	}
	
	/**
	 * 
	 */
	public int changebgm  = 0  ;
	
	public int getChangebgm(){
		return this.changebgm;
	}
	
	public void setChangebgm(int v){
		this.changebgm=v;
	}
	
	/**
	 * 
	 */
	public String optiontype  = null  ;
	
	public String getOptiontype(){
		return this.optiontype;
	}
	
	public void setOptiontype(String v){
		this.optiontype=v;
	}
	
	/**
	 * 
	 */
	public String optionid  = null  ;
	
	public String getOptionid(){
		return this.optionid;
	}
	
	public void setOptionid(String v){
		this.optionid=v;
	}
	
	/**
	 * 
	 */
	public String skillsoltid  = null  ;
	
	public String getSkillsoltid(){
		return this.skillsoltid;
	}
	
	public void setSkillsoltid(String v){
		this.skillsoltid=v;
	}
	
	/**
	 * 
	 */
	public String skillfactor  = null  ;
	
	public String getSkillfactor(){
		return this.skillfactor;
	}
	
	public void setSkillfactor(String v){
		this.skillfactor=v;
	}
	
	/**
	 * 
	 */
	public String skillconstant  = null  ;
	
	public String getSkillconstant(){
		return this.skillconstant;
	}
	
	public void setSkillconstant(String v){
		this.skillconstant=v;
	}
	
	/**
	 * 
	 */
	public String escapeodds  = null  ;
	
	public String getEscapeodds(){
		return this.escapeodds;
	}
	
	public void setEscapeodds(String v){
		this.escapeodds=v;
	}
	
	/**
	 * 
	 */
	public String summons  = null  ;
	
	public String getSummons(){
		return this.summons;
	}
	
	public void setSummons(String v){
		this.summons=v;
	}
	
	/**
	 * 
	 */
	public String target  = null  ;
	
	public String getTarget(){
		return this.target;
	}
	
	public void setTarget(String v){
		this.target=v;
	}
	
	/**
	 * 
	 */
	public String bonustask  = null  ;
	
	public String getBonustask(){
		return this.bonustask;
	}
	
	public void setBonustask(String v){
		this.bonustask=v;
	}
	
	/**
	 * 
	 */
	public int changeaim  = 0  ;
	
	public int getChangeaim(){
		return this.changeaim;
	}
	
	public void setChangeaim(int v){
		this.changeaim=v;
	}
	
	
};