package fire.pb.game;


public class Sdongtaifuwu implements mytools.ConvMain.Checkable ,Comparable<Sdongtaifuwu>{

	public int compareTo(Sdongtaifuwu o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public Sdongtaifuwu(){
		super();
	}
	public Sdongtaifuwu(Sdongtaifuwu arg){
		this.id=arg.id ;
		this.npcid=arg.npcid ;
		this.fuwuid=arg.fuwuid ;
		this.kaishitime=arg.kaishitime ;
		this.jeishutime=arg.jeishutime ;
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
	public int npcid  = 0  ;
	
	public int getNpcid(){
		return this.npcid;
	}
	
	public void setNpcid(int v){
		this.npcid=v;
	}
	
	/**
	 * 
	 */
	public int fuwuid  = 0  ;
	
	public int getFuwuid(){
		return this.fuwuid;
	}
	
	public void setFuwuid(int v){
		this.fuwuid=v;
	}
	
	/**
	 * 
	 */
	public String kaishitime  = null  ;
	
	public String getKaishitime(){
		return this.kaishitime;
	}
	
	public void setKaishitime(String v){
		this.kaishitime=v;
	}
	
	/**
	 * 
	 */
	public String jeishutime  = null  ;
	
	public String getJeishutime(){
		return this.jeishutime;
	}
	
	public void setJeishutime(String v){
		this.jeishutime=v;
	}
	
	
};