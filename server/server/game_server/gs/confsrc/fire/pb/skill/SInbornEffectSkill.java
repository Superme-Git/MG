package fire.pb.skill;


public class SInbornEffectSkill implements mytools.ConvMain.Checkable ,Comparable<SInbornEffectSkill>{

	public int compareTo(SInbornEffectSkill o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SInbornEffectSkill(){
		super();
	}
	public SInbornEffectSkill(SInbornEffectSkill arg){
		this.id=arg.id ;
		this.initEffect=arg.initEffect ;
		this.inborns=arg.inborns ;
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
	public int initEffect  = 0  ;
	
	public int getInitEffect(){
		return this.initEffect;
	}
	
	public void setInitEffect(int v){
		this.initEffect=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<InbornEffect> inborns  ;
	
	public java.util.ArrayList<InbornEffect> getInborns(){
		return this.inborns;
	}
	
	public void setInborns(java.util.ArrayList<InbornEffect> v){
		this.inborns=v;
	}
	
	
};