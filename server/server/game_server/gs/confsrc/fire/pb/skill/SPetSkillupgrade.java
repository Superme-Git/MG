package fire.pb.skill;


public class SPetSkillupgrade implements mytools.ConvMain.Checkable ,Comparable<SPetSkillupgrade>{

	public int compareTo(SPetSkillupgrade o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SPetSkillupgrade(){
		super();
	}
	public SPetSkillupgrade(SPetSkillupgrade arg){
		this.id=arg.id ;
		this.nextid=arg.nextid ;
		this.book=arg.book ;
		this.needexp=arg.needexp ;
		this.offerbaseexp=arg.offerbaseexp ;
		this.skilllevel=arg.skilllevel ;
		this.sign=arg.sign ;
		this.iscanremovable=arg.iscanremovable ;
		this.range=arg.range ;
		this.iscancertification=arg.iscancertification ;
		this.iscertificationappend=arg.iscertificationappend ;
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
	public int nextid  = 0  ;
	
	public int getNextid(){
		return this.nextid;
	}
	
	public void setNextid(int v){
		this.nextid=v;
	}
	
	/**
	 * 
	 */
	public int book  = 0  ;
	
	public int getBook(){
		return this.book;
	}
	
	public void setBook(int v){
		this.book=v;
	}
	
	/**
	 * 
	 */
	public int needexp  = 0  ;
	
	public int getNeedexp(){
		return this.needexp;
	}
	
	public void setNeedexp(int v){
		this.needexp=v;
	}
	
	/**
	 * 
	 */
	public int offerbaseexp  = 0  ;
	
	public int getOfferbaseexp(){
		return this.offerbaseexp;
	}
	
	public void setOfferbaseexp(int v){
		this.offerbaseexp=v;
	}
	
	/**
	 * 
	 */
	public int skilllevel  = 0  ;
	
	public int getSkilllevel(){
		return this.skilllevel;
	}
	
	public void setSkilllevel(int v){
		this.skilllevel=v;
	}
	
	/**
	 * 
	 */
	public int sign  = 0  ;
	
	public int getSign(){
		return this.sign;
	}
	
	public void setSign(int v){
		this.sign=v;
	}
	
	/**
	 * 
	 */
	public int iscanremovable  = 0  ;
	
	public int getIscanremovable(){
		return this.iscanremovable;
	}
	
	public void setIscanremovable(int v){
		this.iscanremovable=v;
	}
	
	/**
	 * 
	 */
	public int range  = 0  ;
	
	public int getRange(){
		return this.range;
	}
	
	public void setRange(int v){
		this.range=v;
	}
	
	/**
	 * 
	 */
	public int iscancertification  = 0  ;
	
	public int getIscancertification(){
		return this.iscancertification;
	}
	
	public void setIscancertification(int v){
		this.iscancertification=v;
	}
	
	/**
	 * 是否可以作为法术认证后的附加技能
	 */
	public int iscertificationappend  = 0  ;
	
	public int getIscertificationappend(){
		return this.iscertificationappend;
	}
	
	public void setIscertificationappend(int v){
		this.iscertificationappend=v;
	}
	
	
};