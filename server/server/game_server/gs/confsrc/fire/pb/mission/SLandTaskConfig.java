package fire.pb.mission;


public class SLandTaskConfig implements mytools.ConvMain.Checkable ,Comparable<SLandTaskConfig>{

	public int compareTo(SLandTaskConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SLandTaskConfig(){
		super();
	}
	public SLandTaskConfig(SLandTaskConfig arg){
		this.id=arg.id ;
		this.taskid=arg.taskid ;
		this.tasktype=arg.tasktype ;
		this.mapid=arg.mapid ;
		this.solonpcid=arg.solonpcid ;
		this.step=arg.step ;
		this.steprate=arg.steprate ;
		this.lefttopx=arg.lefttopx ;
		this.lefttopy=arg.lefttopy ;
		this.end=arg.end ;
		this.decoratenpc1=arg.decoratenpc1 ;
		this.x1=arg.x1 ;
		this.y1=arg.y1 ;
		this.decoratenpc2=arg.decoratenpc2 ;
		this.x2=arg.x2 ;
		this.y2=arg.y2 ;
		this.decoratenpc3=arg.decoratenpc3 ;
		this.x3=arg.x3 ;
		this.y3=arg.y3 ;
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
	public int taskid  = 0  ;
	
	public int getTaskid(){
		return this.taskid;
	}
	
	public void setTaskid(int v){
		this.taskid=v;
	}
	
	/**
	 * 又做副本ID
	 */
	public int tasktype  = 0  ;
	
	public int getTasktype(){
		return this.tasktype;
	}
	
	public void setTasktype(int v){
		this.tasktype=v;
	}
	
	/**
	 * 
	 */
	public int mapid  = 0  ;
	
	public int getMapid(){
		return this.mapid;
	}
	
	public void setMapid(int v){
		this.mapid=v;
	}
	
	/**
	 * 
	 */
	public int solonpcid  = 0  ;
	
	public int getSolonpcid(){
		return this.solonpcid;
	}
	
	public void setSolonpcid(int v){
		this.solonpcid=v;
	}
	
	/**
	 * 
	 */
	public int step  = 0  ;
	
	public int getStep(){
		return this.step;
	}
	
	public void setStep(int v){
		this.step=v;
	}
	
	/**
	 * 
	 */
	public int steprate  = 0  ;
	
	public int getSteprate(){
		return this.steprate;
	}
	
	public void setSteprate(int v){
		this.steprate=v;
	}
	
	/**
	 * 
	 */
	public int lefttopx  = 0  ;
	
	public int getLefttopx(){
		return this.lefttopx;
	}
	
	public void setLefttopx(int v){
		this.lefttopx=v;
	}
	
	/**
	 * 
	 */
	public int lefttopy  = 0  ;
	
	public int getLefttopy(){
		return this.lefttopy;
	}
	
	public void setLefttopy(int v){
		this.lefttopy=v;
	}
	
	/**
	 * 
	 */
	public int end  = 0  ;
	
	public int getEnd(){
		return this.end;
	}
	
	public void setEnd(int v){
		this.end=v;
	}
	
	/**
	 * 
	 */
	public int decoratenpc1  = 0  ;
	
	public int getDecoratenpc1(){
		return this.decoratenpc1;
	}
	
	public void setDecoratenpc1(int v){
		this.decoratenpc1=v;
	}
	
	/**
	 * 
	 */
	public int x1  = 0  ;
	
	public int getX1(){
		return this.x1;
	}
	
	public void setX1(int v){
		this.x1=v;
	}
	
	/**
	 * 
	 */
	public int y1  = 0  ;
	
	public int getY1(){
		return this.y1;
	}
	
	public void setY1(int v){
		this.y1=v;
	}
	
	/**
	 * 
	 */
	public int decoratenpc2  = 0  ;
	
	public int getDecoratenpc2(){
		return this.decoratenpc2;
	}
	
	public void setDecoratenpc2(int v){
		this.decoratenpc2=v;
	}
	
	/**
	 * 
	 */
	public int x2  = 0  ;
	
	public int getX2(){
		return this.x2;
	}
	
	public void setX2(int v){
		this.x2=v;
	}
	
	/**
	 * 
	 */
	public int y2  = 0  ;
	
	public int getY2(){
		return this.y2;
	}
	
	public void setY2(int v){
		this.y2=v;
	}
	
	/**
	 * 
	 */
	public int decoratenpc3  = 0  ;
	
	public int getDecoratenpc3(){
		return this.decoratenpc3;
	}
	
	public void setDecoratenpc3(int v){
		this.decoratenpc3=v;
	}
	
	/**
	 * 
	 */
	public int x3  = 0  ;
	
	public int getX3(){
		return this.x3;
	}
	
	public void setX3(int v){
		this.x3=v;
	}
	
	/**
	 * 
	 */
	public int y3  = 0  ;
	
	public int getY3(){
		return this.y3;
	}
	
	public void setY3(int v){
		this.y3=v;
	}
	
	
};