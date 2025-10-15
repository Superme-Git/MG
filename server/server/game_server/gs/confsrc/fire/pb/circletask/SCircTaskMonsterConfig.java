package fire.pb.circletask;


public class SCircTaskMonsterConfig implements mytools.ConvMain.Checkable ,Comparable<SCircTaskMonsterConfig>{

	public int compareTo(SCircTaskMonsterConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SCircTaskMonsterConfig(){
		super();
	}
	public SCircTaskMonsterConfig(SCircTaskMonsterConfig arg){
		this.id=arg.id ;
		this.group=arg.group ;
		this.mapids=arg.mapids ;
		this.battleinfo=arg.battleinfo ;
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
	public int group  = 0  ;
	
	public int getGroup(){
		return this.group;
	}
	
	public void setGroup(int v){
		this.group=v;
	}
	
	/**
	 * 
	 */
	public String mapids  = null  ;
	
	public String getMapids(){
		return this.mapids;
	}
	
	public void setMapids(String v){
		this.mapids=v;
	}
	
	/**
	 * 
	 */
	public String battleinfo  = null  ;
	
	public String getBattleinfo(){
		return this.battleinfo;
	}
	
	public void setBattleinfo(String v){
		this.battleinfo=v;
	}
	
	
};