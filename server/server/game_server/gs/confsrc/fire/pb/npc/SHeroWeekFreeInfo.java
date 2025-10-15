package fire.pb.npc;


public class SHeroWeekFreeInfo implements mytools.ConvMain.Checkable ,Comparable<SHeroWeekFreeInfo>{

	public int compareTo(SHeroWeekFreeInfo o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SHeroWeekFreeInfo(){
		super();
	}
	public SHeroWeekFreeInfo(SHeroWeekFreeInfo arg){
		this.id=arg.id ;
		this.week=arg.week ;
		this.heros=arg.heros ;
		this.vipheros=arg.vipheros ;
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
	public int week  = 0  ;
	
	public int getWeek(){
		return this.week;
	}
	
	public void setWeek(int v){
		this.week=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> heros  ;
	
	public java.util.ArrayList<Integer> getHeros(){
		return this.heros;
	}
	
	public void setHeros(java.util.ArrayList<Integer> v){
		this.heros=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> vipheros  ;
	
	public java.util.ArrayList<Integer> getVipheros(){
		return this.vipheros;
	}
	
	public void setVipheros(java.util.ArrayList<Integer> v){
		this.vipheros=v;
	}
	
	
};