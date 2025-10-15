package fire.pb.item;


public class SFoodAndDrugEffect  extends FoodItemAttr {

	public int compareTo(SFoodAndDrugEffect o){
		return this.id-o.id;
	}

	
	public SFoodAndDrugEffect(FoodItemAttr arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SFoodAndDrugEffect(){
		super();
	}
	public SFoodAndDrugEffect(SFoodAndDrugEffect arg){
		super(arg);
		this.effectdescribe=arg.effectdescribe ;
		this.effect=arg.effect ;
		this.funtionid=arg.funtionid ;
		this.needPengrenLevel=arg.needPengrenLevel ;
		this.pengrenWeight=arg.pengrenWeight ;
		this.needLianyaoLevel=arg.needLianyaoLevel ;
		this.lianyaoWeight=arg.lianyaoWeight ;
		this.lianyaoMaterialWeight=arg.lianyaoMaterialWeight ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	/**
	 * 
	 */
	public String effectdescribe  = null  ;
	
	public String getEffectdescribe(){
		return this.effectdescribe;
	}
	
	public void setEffectdescribe(String v){
		this.effectdescribe=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> effect  ;
	
	public java.util.ArrayList<Integer> getEffect(){
		return this.effect;
	}
	
	public void setEffect(java.util.ArrayList<Integer> v){
		this.effect=v;
	}
	
	/**
	 * 
	 */
	public int funtionid  = 0  ;
	
	public int getFuntionid(){
		return this.funtionid;
	}
	
	public void setFuntionid(int v){
		this.funtionid=v;
	}
	
	/**
	 * 
	 */
	public int needPengrenLevel  = 0  ;
	
	public int getNeedPengrenLevel(){
		return this.needPengrenLevel;
	}
	
	public void setNeedPengrenLevel(int v){
		this.needPengrenLevel=v;
	}
	
	/**
	 * 
	 */
	public int pengrenWeight  = 0  ;
	
	public int getPengrenWeight(){
		return this.pengrenWeight;
	}
	
	public void setPengrenWeight(int v){
		this.pengrenWeight=v;
	}
	
	/**
	 * 
	 */
	public int needLianyaoLevel  = 0  ;
	
	public int getNeedLianyaoLevel(){
		return this.needLianyaoLevel;
	}
	
	public void setNeedLianyaoLevel(int v){
		this.needLianyaoLevel=v;
	}
	
	/**
	 * 
	 */
	public int lianyaoWeight  = 0  ;
	
	public int getLianyaoWeight(){
		return this.lianyaoWeight;
	}
	
	public void setLianyaoWeight(int v){
		this.lianyaoWeight=v;
	}
	
	/**
	 * 
	 */
	public int lianyaoMaterialWeight  = 0  ;
	
	public int getLianyaoMaterialWeight(){
		return this.lianyaoMaterialWeight;
	}
	
	public void setLianyaoMaterialWeight(int v){
		this.lianyaoMaterialWeight=v;
	}
	
	
};