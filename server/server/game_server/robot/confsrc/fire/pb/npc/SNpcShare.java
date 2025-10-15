package fire.pb.npc;


public class SNpcShare implements mytools.ConvMain.Checkable ,Comparable<SNpcShare>{

	public int compareTo(SNpcShare o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SNpcShare(){
		super();
	}
	public SNpcShare(SNpcShare arg){
		this.id=arg.id ;
		this.bodytype=arg.bodytype ;
		this.npctype=arg.npctype ;
		this.minimapshow=arg.minimapshow ;
		this.share=arg.share ;
		this.shape=arg.shape ;
		this.name=arg.name ;
		this.mulbattle=arg.mulbattle ;
		this.杂学id=arg.杂学id ;
		this.mapid=arg.mapid ;
		this.xPos=arg.xPos ;
		this.yPos=arg.yPos ;
		this.zPos=arg.zPos ;
		this.battleinfo=arg.battleinfo ;
		this.nametable=arg.nametable ;
		this.namepre1=arg.namepre1 ;
		this.namepre2=arg.namepre2 ;
		this.diankafu=arg.diankafu ;
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
	public double bodytype  = 0.0  ;
	
	public double getBodytype(){
		return this.bodytype;
	}
	
	public void setBodytype(double v){
		this.bodytype=v;
	}
	
	/**
	 * 
	 */
	public int npctype  = 0  ;
	
	public int getNpctype(){
		return this.npctype;
	}
	
	public void setNpctype(int v){
		this.npctype=v;
	}
	
	/**
	 * 
	 */
	public String minimapshow  = null  ;
	
	public String getMinimapshow(){
		return this.minimapshow;
	}
	
	public void setMinimapshow(String v){
		this.minimapshow=v;
	}
	
	/**
	 * 
	 */
	public int share  = 0  ;
	
	public int getShare(){
		return this.share;
	}
	
	public void setShare(int v){
		this.share=v;
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
	public int mulbattle  = 0  ;
	
	public int getMulbattle(){
		return this.mulbattle;
	}
	
	public void setMulbattle(int v){
		this.mulbattle=v;
	}
	
	/**
	 * 
	 */
	public int 杂学id  = 0  ;
	
	public int get杂学id(){
		return this.杂学id;
	}
	
	public void set杂学id(int v){
		this.杂学id=v;
	}
	
	/**
	 * 所在地图的id
	 */
	public int mapid  = 0  ;
	
	public int getMapid(){
		return this.mapid;
	}
	
	public void setMapid(int v){
		this.mapid=v;
	}
	
	/**
	 * 所在地图的x坐标
	 */
	public int xPos  = 0  ;
	
	public int getXPos(){
		return this.xPos;
	}
	
	public void setXPos(int v){
		this.xPos=v;
	}
	
	/**
	 * 所在地图的y坐标
	 */
	public int yPos  = 0  ;
	
	public int getYPos(){
		return this.yPos;
	}
	
	public void setYPos(int v){
		this.yPos=v;
	}
	
	/**
	 * 
	 */
	public int zPos  = 0  ;
	
	public int getZPos(){
		return this.zPos;
	}
	
	public void setZPos(int v){
		this.zPos=v;
	}
	
	/**
	 * 
	 */
	public int battleinfo  = 0  ;
	
	public int getBattleinfo(){
		return this.battleinfo;
	}
	
	public void setBattleinfo(int v){
		this.battleinfo=v;
	}
	
	/**
	 * 
	 */
	public int nametable  = 0  ;
	
	public int getNametable(){
		return this.nametable;
	}
	
	public void setNametable(int v){
		this.nametable=v;
	}
	
	/**
	 * 
	 */
	public int namepre1  = 0  ;
	
	public int getNamepre1(){
		return this.namepre1;
	}
	
	public void setNamepre1(int v){
		this.namepre1=v;
	}
	
	/**
	 * 
	 */
	public int namepre2  = 0  ;
	
	public int getNamepre2(){
		return this.namepre2;
	}
	
	public void setNamepre2(int v){
		this.namepre2=v;
	}
	
	/**
	 * 1,在点卡服上显示;0,任何服都显示
	 */
	public int diankafu  = 0  ;
	
	public int getDiankafu(){
		return this.diankafu;
	}
	
	public void setDiankafu(int v){
		this.diankafu=v;
	}
	
	
};