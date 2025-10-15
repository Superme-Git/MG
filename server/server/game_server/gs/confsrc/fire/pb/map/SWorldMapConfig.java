package fire.pb.map;


public class SWorldMapConfig implements mytools.ConvMain.Checkable ,Comparable<SWorldMapConfig>{

	public int compareTo(SWorldMapConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SWorldMapConfig(){
		super();
	}
	public SWorldMapConfig(SWorldMapConfig arg){
		this.id=arg.id ;
		this.maptype=arg.maptype ;
		this.LevelLimitMin=arg.LevelLimitMin ;
		this.LevelLimitMax=arg.LevelLimitMax ;
		this.SubUnderGroundMap=arg.SubUnderGroundMap ;
		this.topx=arg.topx ;
		this.topy=arg.topy ;
		this.bottomx=arg.bottomx ;
		this.bottomy=arg.bottomy ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * id
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
	public int maptype  = 0  ;
	
	public int getMaptype(){
		return this.maptype;
	}
	
	public void setMaptype(int v){
		this.maptype=v;
	}
	
	/**
	 * 
	 */
	public int LevelLimitMin  = 0  ;
	
	public int getLevelLimitMin(){
		return this.LevelLimitMin;
	}
	
	public void setLevelLimitMin(int v){
		this.LevelLimitMin=v;
	}
	
	/**
	 * 
	 */
	public int LevelLimitMax  = 0  ;
	
	public int getLevelLimitMax(){
		return this.LevelLimitMax;
	}
	
	public void setLevelLimitMax(int v){
		this.LevelLimitMax=v;
	}
	
	/**
	 * 
	 */
	public String SubUnderGroundMap  = null  ;
	
	public String getSubUnderGroundMap(){
		return this.SubUnderGroundMap;
	}
	
	public void setSubUnderGroundMap(String v){
		this.SubUnderGroundMap=v;
	}
	
	/**
	 * 
	 */
	public int topx  = 0  ;
	
	public int getTopx(){
		return this.topx;
	}
	
	public void setTopx(int v){
		this.topx=v;
	}
	
	/**
	 * 
	 */
	public int topy  = 0  ;
	
	public int getTopy(){
		return this.topy;
	}
	
	public void setTopy(int v){
		this.topy=v;
	}
	
	/**
	 * 
	 */
	public int bottomx  = 0  ;
	
	public int getBottomx(){
		return this.bottomx;
	}
	
	public void setBottomx(int v){
		this.bottomx=v;
	}
	
	/**
	 * 
	 */
	public int bottomy  = 0  ;
	
	public int getBottomy(){
		return this.bottomy;
	}
	
	public void setBottomy(int v){
		this.bottomy=v;
	}
	
	
};