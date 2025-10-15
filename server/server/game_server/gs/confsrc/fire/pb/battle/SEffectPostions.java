package fire.pb.battle;


public class SEffectPostions implements mytools.ConvMain.Checkable ,Comparable<SEffectPostions>{

	public int compareTo(SEffectPostions o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SEffectPostions(){
		super();
	}
	public SEffectPostions(SEffectPostions arg){
		this.id=arg.id ;
		this.diffuseposs=arg.diffuseposs ;
		this.effectposs=arg.effectposs ;
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
	public java.util.ArrayList<Integer> diffuseposs  ;
	
	public java.util.ArrayList<Integer> getDiffuseposs(){
		return this.diffuseposs;
	}
	
	public void setDiffuseposs(java.util.ArrayList<Integer> v){
		this.diffuseposs=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> effectposs  ;
	
	public java.util.ArrayList<Integer> getEffectposs(){
		return this.effectposs;
	}
	
	public void setEffectposs(java.util.ArrayList<Integer> v){
		this.effectposs=v;
	}
	
	
};