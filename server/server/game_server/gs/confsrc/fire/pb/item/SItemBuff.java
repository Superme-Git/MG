package fire.pb.item;


public class SItemBuff implements mytools.ConvMain.Checkable ,Comparable<SItemBuff>{

	public int compareTo(SItemBuff o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SItemBuff(){
		super();
	}
	public SItemBuff(SItemBuff arg){
		this.id=arg.id ;
		this.monsterids=arg.monsterids ;
		this.inskill_id=arg.inskill_id ;
		this.outskill_id=arg.outskill_id ;
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
	public java.util.ArrayList<Integer> monsterids  ;
	
	public java.util.ArrayList<Integer> getMonsterids(){
		return this.monsterids;
	}
	
	public void setMonsterids(java.util.ArrayList<Integer> v){
		this.monsterids=v;
	}
	
	/**
	 * 
	 */
	public int inskill_id  = 0  ;
	
	public int getInskill_id(){
		return this.inskill_id;
	}
	
	public void setInskill_id(int v){
		this.inskill_id=v;
	}
	
	/**
	 * 
	 */
	public int outskill_id  = 0  ;
	
	public int getOutskill_id(){
		return this.outskill_id;
	}
	
	public void setOutskill_id(int v){
		this.outskill_id=v;
	}
	
	
};