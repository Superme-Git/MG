package fire.pb.effect;


public class SEquipSkillShowConfig implements mytools.ConvMain.Checkable ,Comparable<SEquipSkillShowConfig>{

	public int compareTo(SEquipSkillShowConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SEquipSkillShowConfig(){
		super();
	}
	public SEquipSkillShowConfig(SEquipSkillShowConfig arg){
		this.id=arg.id ;
		this.产出级别=arg.产出级别 ;
		this.产出级别2=arg.产出级别2 ;
		this.produceEquip=arg.produceEquip ;
		this.lianGongQu=arg.lianGongQu ;
		this.zhenShou=arg.zhenShou ;
		this.effectid=arg.effectid ;
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
	public int 产出级别  = 0  ;
	
	public int get产出级别(){
		return this.产出级别;
	}
	
	public void set产出级别(int v){
		this.产出级别=v;
	}
	
	/**
	 * 
	 */
	public int 产出级别2  = 0  ;
	
	public int get产出级别2(){
		return this.产出级别2;
	}
	
	public void set产出级别2(int v){
		this.产出级别2=v;
	}
	
	/**
	 * 
	 */
	public int produceEquip  = 0  ;
	
	public int getProduceEquip(){
		return this.produceEquip;
	}
	
	public void setProduceEquip(int v){
		this.produceEquip=v;
	}
	
	/**
	 * 
	 */
	public int lianGongQu  = 0  ;
	
	public int getLianGongQu(){
		return this.lianGongQu;
	}
	
	public void setLianGongQu(int v){
		this.lianGongQu=v;
	}
	
	/**
	 * 
	 */
	public int zhenShou  = 0  ;
	
	public int getZhenShou(){
		return this.zhenShou;
	}
	
	public void setZhenShou(int v){
		this.zhenShou=v;
	}
	
	/**
	 * 
	 */
	public int effectid  = 0  ;
	
	public int getEffectid(){
		return this.effectid;
	}
	
	public void setEffectid(int v){
		this.effectid=v;
	}
	
	
};