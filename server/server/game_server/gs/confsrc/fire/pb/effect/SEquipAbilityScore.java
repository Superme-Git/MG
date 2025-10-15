package fire.pb.effect;


public class SEquipAbilityScore implements mytools.ConvMain.Checkable ,Comparable<SEquipAbilityScore>{

	public int compareTo(SEquipAbilityScore o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SEquipAbilityScore(){
		super();
	}
	public SEquipAbilityScore(SEquipAbilityScore arg){
		this.id=arg.id ;
		this.name=arg.name ;
		this.coefficient=arg.coefficient ;
		this.weapon=arg.weapon ;
		this.armor=arg.armor ;
		this.headdress=arg.headdress ;
		this.accessory=arg.accessory ;
		this.belt=arg.belt ;
		this.boot=arg.boot ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 属性id
	 */
	public int id  = 0  ;
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int v){
		this.id=v;
	}
	
	/**
	 * 属性名
	 */
	public String name  = null  ;
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String v){
		this.name=v;
	}
	
	/**
	 * 对应评分系数
	 */
	public double coefficient  = 0.0  ;
	
	public double getCoefficient(){
		return this.coefficient;
	}
	
	public void setCoefficient(double v){
		this.coefficient=v;
	}
	
	/**
	 * 武器
	 */
	public double weapon  = 0.0  ;
	
	public double getWeapon(){
		return this.weapon;
	}
	
	public void setWeapon(double v){
		this.weapon=v;
	}
	
	/**
	 * 衣服
	 */
	public double armor  = 0.0  ;
	
	public double getArmor(){
		return this.armor;
	}
	
	public void setArmor(double v){
		this.armor=v;
	}
	
	/**
	 * 头盔
	 */
	public double headdress  = 0.0  ;
	
	public double getHeaddress(){
		return this.headdress;
	}
	
	public void setHeaddress(double v){
		this.headdress=v;
	}
	
	/**
	 * 项链
	 */
	public double accessory  = 0.0  ;
	
	public double getAccessory(){
		return this.accessory;
	}
	
	public void setAccessory(double v){
		this.accessory=v;
	}
	
	/**
	 * 腰带
	 */
	public double belt  = 0.0  ;
	
	public double getBelt(){
		return this.belt;
	}
	
	public void setBelt(double v){
		this.belt=v;
	}
	
	/**
	 * 鞋子
	 */
	public double boot  = 0.0  ;
	
	public double getBoot(){
		return this.boot;
	}
	
	public void setBoot(double v){
		this.boot=v;
	}
	
	
};