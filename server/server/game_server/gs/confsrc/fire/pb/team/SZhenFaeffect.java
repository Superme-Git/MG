package fire.pb.team;


public class SZhenFaeffect implements mytools.ConvMain.Checkable ,Comparable<SZhenFaeffect>{

	public int compareTo(SZhenFaeffect o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SZhenFaeffect(){
		super();
	}
	public SZhenFaeffect(SZhenFaeffect arg){
		this.id=arg.id ;
		this.zhenfaid=arg.zhenfaid ;
		this.zhenfaLv=arg.zhenfaLv ;
		this.zhenfaExp=arg.zhenfaExp ;
		this.effect=arg.effect ;
		this.effectloss=arg.effectloss ;
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
	public int zhenfaid  = 0  ;
	
	public int getZhenfaid(){
		return this.zhenfaid;
	}
	
	public void setZhenfaid(int v){
		this.zhenfaid=v;
	}
	
	/**
	 * 
	 */
	public int zhenfaLv  = 0  ;
	
	public int getZhenfaLv(){
		return this.zhenfaLv;
	}
	
	public void setZhenfaLv(int v){
		this.zhenfaLv=v;
	}
	
	/**
	 * 
	 */
	public int zhenfaExp  = 0  ;
	
	public int getZhenfaExp(){
		return this.zhenfaExp;
	}
	
	public void setZhenfaExp(int v){
		this.zhenfaExp=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<String> effect  ;
	
	public java.util.ArrayList<String> getEffect(){
		return this.effect;
	}
	
	public void setEffect(java.util.ArrayList<String> v){
		this.effect=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<String> effectloss  ;
	
	public java.util.ArrayList<String> getEffectloss(){
		return this.effectloss;
	}
	
	public void setEffectloss(java.util.ArrayList<String> v){
		this.effectloss=v;
	}
	
	
};