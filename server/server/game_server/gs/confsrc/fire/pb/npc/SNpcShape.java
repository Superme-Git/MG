package fire.pb.npc;


public class SNpcShape implements mytools.ConvMain.Checkable ,Comparable<SNpcShape>{

	public int compareTo(SNpcShape o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SNpcShape(){
		super();
	}
	public SNpcShape(SNpcShape arg){
		this.id=arg.id ;
		this.shape=arg.shape ;
		this.roleimage=arg.roleimage ;
		this.headID=arg.headID ;
		this.littleheadID=arg.littleheadID ;
		this.name=arg.name ;
		this.dir=arg.dir ;
		this.shadow=arg.shadow ;
		this.attack=arg.attack ;
		this.magic=arg.magic ;
		this.nearorfar=arg.nearorfar ;
		this.shadertype=arg.shadertype ;
		this.part0=arg.part0 ;
		this.part1=arg.part1 ;
		this.part2=arg.part2 ;
		this.showWeaponId=arg.showWeaponId ;
		this.showHorseShape=arg.showHorseShape ;
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
	public String shape  = null  ;
	
	public String getShape(){
		return this.shape;
	}
	
	public void setShape(String v){
		this.shape=v;
	}
	
	/**
	 * 
	 */
	public String roleimage  = null  ;
	
	public String getRoleimage(){
		return this.roleimage;
	}
	
	public void setRoleimage(String v){
		this.roleimage=v;
	}
	
	/**
	 * 
	 */
	public int headID  = 0  ;
	
	public int getHeadID(){
		return this.headID;
	}
	
	public void setHeadID(int v){
		this.headID=v;
	}
	
	/**
	 * 
	 */
	public int littleheadID  = 0  ;
	
	public int getLittleheadID(){
		return this.littleheadID;
	}
	
	public void setLittleheadID(int v){
		this.littleheadID=v;
	}
	
	/**
	 * 
	 */
	public String name  = null  ;
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String v){
		this.name=v;
	}
	
	/**
	 * 
	 */
	public int dir  = 0  ;
	
	public int getDir(){
		return this.dir;
	}
	
	public void setDir(int v){
		this.dir=v;
	}
	
	/**
	 * 
	 */
	public int shadow  = 0  ;
	
	public int getShadow(){
		return this.shadow;
	}
	
	public void setShadow(int v){
		this.shadow=v;
	}
	
	/**
	 * 
	 */
	public String attack  = null  ;
	
	public String getAttack(){
		return this.attack;
	}
	
	public void setAttack(String v){
		this.attack=v;
	}
	
	/**
	 * 
	 */
	public String magic  = null  ;
	
	public String getMagic(){
		return this.magic;
	}
	
	public void setMagic(String v){
		this.magic=v;
	}
	
	/**
	 * 
	 */
	public int nearorfar  = 0  ;
	
	public int getNearorfar(){
		return this.nearorfar;
	}
	
	public void setNearorfar(int v){
		this.nearorfar=v;
	}
	
	/**
	 * 
	 */
	public int shadertype  = 0  ;
	
	public int getShadertype(){
		return this.shadertype;
	}
	
	public void setShadertype(int v){
		this.shadertype=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> part0  ;
	
	public java.util.ArrayList<Integer> getPart0(){
		return this.part0;
	}
	
	public void setPart0(java.util.ArrayList<Integer> v){
		this.part0=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> part1  ;
	
	public java.util.ArrayList<Integer> getPart1(){
		return this.part1;
	}
	
	public void setPart1(java.util.ArrayList<Integer> v){
		this.part1=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> part2  ;
	
	public java.util.ArrayList<Integer> getPart2(){
		return this.part2;
	}
	
	public void setPart2(java.util.ArrayList<Integer> v){
		this.part2=v;
	}
	
	/**
	 * 
	 */
	public int showWeaponId  = 0  ;
	
	public int getShowWeaponId(){
		return this.showWeaponId;
	}
	
	public void setShowWeaponId(int v){
		this.showWeaponId=v;
	}
	
	/**
	 * 
	 */
	public int showHorseShape  = 0  ;
	
	public int getShowHorseShape(){
		return this.showHorseShape;
	}
	
	public void setShowHorseShape(int v){
		this.showHorseShape=v;
	}
	
	
};