package fire.pb.item;


public class SBaoShiBiao implements mytools.ConvMain.Checkable ,Comparable<SBaoShiBiao>{

	public int compareTo(SBaoShiBiao o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SBaoShiBiao(){
		super();
	}
	public SBaoShiBiao(SBaoShiBiao arg){
		this.id=arg.id ;
		this.type=arg.type ;
		this.level=arg.level ;
		this.nextDiamond=arg.nextDiamond ;
		this.prob=arg.prob ;
		this.shape=arg.shape ;
		this.equip1=arg.equip1 ;
		this.equip2=arg.equip2 ;
		this.resolve=arg.resolve ;
		this.resolveItem=arg.resolveItem ;
		this.resolveNum=arg.resolveNum ;
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
	public int type  = 0  ;
	
	public int getType(){
		return this.type;
	}
	
	public void setType(int v){
		this.type=v;
	}
	
	/**
	 * 
	 */
	public int level  = 0  ;
	
	public int getLevel(){
		return this.level;
	}
	
	public void setLevel(int v){
		this.level=v;
	}
	
	/**
	 * 
	 */
	public int nextDiamond  = 0  ;
	
	public int getNextDiamond(){
		return this.nextDiamond;
	}
	
	public void setNextDiamond(int v){
		this.nextDiamond=v;
	}
	
	/**
	 * 
	 */
	public int prob  = 0  ;
	
	public int getProb(){
		return this.prob;
	}
	
	public void setProb(int v){
		this.prob=v;
	}
	
	/**
	 * 
	 */
	public int shape  = 0  ;
	
	public int getShape(){
		return this.shape;
	}
	
	public void setShape(int v){
		this.shape=v;
	}
	
	/**
	 * 
	 */
	public int equip1  = 0  ;
	
	public int getEquip1(){
		return this.equip1;
	}
	
	public void setEquip1(int v){
		this.equip1=v;
	}
	
	/**
	 * 
	 */
	public int equip2  = 0  ;
	
	public int getEquip2(){
		return this.equip2;
	}
	
	public void setEquip2(int v){
		this.equip2=v;
	}
	
	/**
	 * 
	 */
	public int resolve  = 0  ;
	
	public int getResolve(){
		return this.resolve;
	}
	
	public void setResolve(int v){
		this.resolve=v;
	}
	
	/**
	 * 
	 */
	public int resolveItem  = 0  ;
	
	public int getResolveItem(){
		return this.resolveItem;
	}
	
	public void setResolveItem(int v){
		this.resolveItem=v;
	}
	
	/**
	 * 
	 */
	public int resolveNum  = 0  ;
	
	public int getResolveNum(){
		return this.resolveNum;
	}
	
	public void setResolveNum(int v){
		this.resolveNum=v;
	}
	
	
};