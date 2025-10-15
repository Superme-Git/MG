package fire.pb.item;


public class PetItemShuXing  extends ItemShuXing {

	public int compareTo(PetItemShuXing o){
		return this.id-o.id;
	}

	
	public PetItemShuXing(ItemShuXing arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public PetItemShuXing(){
		super();
	}
	public PetItemShuXing(PetItemShuXing arg){
		super(arg);
		this.skillid=arg.skillid ;
		this.addExp=arg.addExp ;
		this.addLife=arg.addLife ;
		this.treasure=arg.treasure ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	/**
	 * 
	 */
	public int skillid  = 0  ;
	
	public int getSkillid(){
		return this.skillid;
	}
	
	public void setSkillid(int v){
		this.skillid=v;
	}
	
	/**
	 * 
	 */
	public int addExp  = 0  ;
	
	public int getAddExp(){
		return this.addExp;
	}
	
	public void setAddExp(int v){
		this.addExp=v;
	}
	
	/**
	 * 
	 */
	public int addLife  = 0  ;
	
	public int getAddLife(){
		return this.addLife;
	}
	
	public void setAddLife(int v){
		this.addLife=v;
	}
	
	/**
	 * 
	 */
	public int treasure  = 0  ;
	
	public int getTreasure(){
		return this.treasure;
	}
	
	public void setTreasure(int v){
		this.treasure=v;
	}
	
	
};