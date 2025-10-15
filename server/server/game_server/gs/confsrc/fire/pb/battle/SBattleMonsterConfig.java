package fire.pb.battle;


public class SBattleMonsterConfig implements mytools.ConvMain.Checkable ,Comparable<SBattleMonsterConfig>{

	public int compareTo(SBattleMonsterConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SBattleMonsterConfig(){
		super();
	}
	public SBattleMonsterConfig(SBattleMonsterConfig arg){
		this.id=arg.id ;
		this.teamsize=arg.teamsize ;
		this.怪物数目=arg.怪物数目 ;
		this.普通怪数目=arg.普通怪数目 ;
		this.头领数目=arg.头领数目 ;
		this.出现概率=arg.出现概率 ;
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
	public int teamsize  = 0  ;
	
	public int getTeamsize(){
		return this.teamsize;
	}
	
	public void setTeamsize(int v){
		this.teamsize=v;
	}
	
	/**
	 * 
	 */
	public int 怪物数目  = 0  ;
	
	public int get怪物数目(){
		return this.怪物数目;
	}
	
	public void set怪物数目(int v){
		this.怪物数目=v;
	}
	
	/**
	 * 
	 */
	public int 普通怪数目  = 0  ;
	
	public int get普通怪数目(){
		return this.普通怪数目;
	}
	
	public void set普通怪数目(int v){
		this.普通怪数目=v;
	}
	
	/**
	 * 
	 */
	public int 头领数目  = 0  ;
	
	public int get头领数目(){
		return this.头领数目;
	}
	
	public void set头领数目(int v){
		this.头领数目=v;
	}
	
	/**
	 * 
	 */
	public int 出现概率  = 0  ;
	
	public int get出现概率(){
		return this.出现概率;
	}
	
	public void set出现概率(int v){
		this.出现概率=v;
	}
	
	
};