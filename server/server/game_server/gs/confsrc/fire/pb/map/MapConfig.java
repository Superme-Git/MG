package fire.pb.map;


public class MapConfig implements mytools.ConvMain.Checkable ,Comparable<MapConfig>{

	public int compareTo(MapConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public MapConfig(){
		super();
	}
	public MapConfig(MapConfig arg){
		this.id=arg.id ;
		this.enterlv=arg.enterlv ;
		this.minlevel=arg.minlevel ;
		this.mapName=arg.mapName ;
		this.desc=arg.desc ;
		this.safemap=arg.safemap ;
		this.dynamic=arg.dynamic ;
		this.relocatemapid=arg.relocatemapid ;
		this.relocatex=arg.relocatex ;
		this.relocatey=arg.relocatey ;
		this.qinggong=arg.qinggong ;
		this.visibletype=arg.visibletype ;
		this.xjPos=arg.xjPos ;
		this.yjPos=arg.yjPos ;
		this.remap=arg.remap ;
		this.rexjPos=arg.rexjPos ;
		this.reyjPos=arg.reyjPos ;
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
	public int enterlv  = 0  ;
	
	public int getEnterlv(){
		return this.enterlv;
	}
	
	public void setEnterlv(int v){
		this.enterlv=v;
	}
	
	/**
	 * 
	 */
	public int minlevel  = 0  ;
	
	public int getMinlevel(){
		return this.minlevel;
	}
	
	public void setMinlevel(int v){
		this.minlevel=v;
	}
	
	/**
	 * 
	 */
	public String mapName  = null  ;
	
	public String getMapName(){
		return this.mapName;
	}
	
	public void setMapName(String v){
		this.mapName=v;
	}
	
	/**
	 * 
	 */
	public String desc  = null  ;
	
	public String getDesc(){
		return this.desc;
	}
	
	public void setDesc(String v){
		this.desc=v;
	}
	
	/**
	 * 1为是否安全区地图，0为非是否安全区地图
	 */
	public int safemap  = 0  ;
	
	public int getSafemap(){
		return this.safemap;
	}
	
	public void setSafemap(int v){
		this.safemap=v;
	}
	
	/**
	 * 
	 */
	public int dynamic  = 0  ;
	
	public int getDynamic(){
		return this.dynamic;
	}
	
	public void setDynamic(int v){
		this.dynamic=v;
	}
	
	/**
	 * 
	 */
	public int relocatemapid  = 0  ;
	
	public int getRelocatemapid(){
		return this.relocatemapid;
	}
	
	public void setRelocatemapid(int v){
		this.relocatemapid=v;
	}
	
	/**
	 * 
	 */
	public int relocatex  = 0  ;
	
	public int getRelocatex(){
		return this.relocatex;
	}
	
	public void setRelocatex(int v){
		this.relocatex=v;
	}
	
	/**
	 * 
	 */
	public int relocatey  = 0  ;
	
	public int getRelocatey(){
		return this.relocatey;
	}
	
	public void setRelocatey(int v){
		this.relocatey=v;
	}
	
	/**
	 * 
	 */
	public int qinggong  = 0  ;
	
	public int getQinggong(){
		return this.qinggong;
	}
	
	public void setQinggong(int v){
		this.qinggong=v;
	}
	
	/**
	 * 
	 */
	public int visibletype  = 0  ;
	
	public int getVisibletype(){
		return this.visibletype;
	}
	
	public void setVisibletype(int v){
		this.visibletype=v;
	}
	
	/**
	 * 
	 */
	public int xjPos  = 0  ;
	
	public int getXjPos(){
		return this.xjPos;
	}
	
	public void setXjPos(int v){
		this.xjPos=v;
	}
	
	/**
	 * 
	 */
	public int yjPos  = 0  ;
	
	public int getYjPos(){
		return this.yjPos;
	}
	
	public void setYjPos(int v){
		this.yjPos=v;
	}
	
	/**
	 * 
	 */
	public int remap  = 0  ;
	
	public int getRemap(){
		return this.remap;
	}
	
	public void setRemap(int v){
		this.remap=v;
	}
	
	/**
	 * 
	 */
	public int rexjPos  = 0  ;
	
	public int getRexjPos(){
		return this.rexjPos;
	}
	
	public void setRexjPos(int v){
		this.rexjPos=v;
	}
	
	/**
	 * 
	 */
	public int reyjPos  = 0  ;
	
	public int getReyjPos(){
		return this.reyjPos;
	}
	
	public void setReyjPos(int v){
		this.reyjPos=v;
	}
	
	
};