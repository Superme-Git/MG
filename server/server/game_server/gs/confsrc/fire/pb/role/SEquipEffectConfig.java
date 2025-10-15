package fire.pb.role;


public class SEquipEffectConfig implements mytools.ConvMain.Checkable ,Comparable<SEquipEffectConfig>{

	public int compareTo(SEquipEffectConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SEquipEffectConfig(){
		super();
	}
	public SEquipEffectConfig(SEquipEffectConfig arg){
		this.id=arg.id ;
		this.equipNum=arg.equipNum ;
		this.quality=arg.quality ;
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
	public int equipNum  = 0  ;
	
	public int getEquipNum(){
		return this.equipNum;
	}
	
	public void setEquipNum(int v){
		this.equipNum=v;
	}
	
	/**
	 * 
	 */
	public int quality  = 0  ;
	
	public int getQuality(){
		return this.quality;
	}
	
	public void setQuality(int v){
		this.quality=v;
	}
	
	
};