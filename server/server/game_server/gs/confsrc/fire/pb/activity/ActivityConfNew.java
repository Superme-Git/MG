package fire.pb.activity;


public class ActivityConfNew implements mytools.ConvMain.Checkable ,Comparable<ActivityConfNew>{

	public int compareTo(ActivityConfNew o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public ActivityConfNew(){
		super();
	}
	public ActivityConfNew(ActivityConfNew arg){
		this.id=arg.id ;
		this.level=arg.level ;
		this.maxlevel=arg.maxlevel ;
		this.maxnum=arg.maxnum ;
		this.resettype=arg.resettype ;
		this.maxact=arg.maxact ;
		this.peract=arg.peract ;
		this.getfoodid1=arg.getfoodid1 ;
		this.getfoodid2=arg.getfoodid2 ;
		this.getfoodid3=arg.getfoodid3 ;
		this.getfoodid4=arg.getfoodid4 ;
		this.getfoodid5=arg.getfoodid5 ;
		this.recommend=arg.recommend ;
		this.yingfu=arg.yingfu ;
		this.yingfuawardid=arg.yingfuawardid ;
		this.yingfutimes=arg.yingfutimes ;
		this.weekrepeat=arg.weekrepeat ;
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
	public int maxlevel  = 0  ;
	
	public int getMaxlevel(){
		return this.maxlevel;
	}
	
	public void setMaxlevel(int v){
		this.maxlevel=v;
	}
	
	/**
	 * 
	 */
	public int maxnum  = 0  ;
	
	public int getMaxnum(){
		return this.maxnum;
	}
	
	public void setMaxnum(int v){
		this.maxnum=v;
	}
	
	/**
	 * 
	 */
	public int resettype  = 0  ;
	
	public int getResettype(){
		return this.resettype;
	}
	
	public void setResettype(int v){
		this.resettype=v;
	}
	
	/**
	 * 
	 */
	public int maxact  = 0  ;
	
	public int getMaxact(){
		return this.maxact;
	}
	
	public void setMaxact(int v){
		this.maxact=v;
	}
	
	/**
	 * 
	 */
	public double peract  = 0.0  ;
	
	public double getPeract(){
		return this.peract;
	}
	
	public void setPeract(double v){
		this.peract=v;
	}
	
	/**
	 * 
	 */
	public int getfoodid1  = 0  ;
	
	public int getGetfoodid1(){
		return this.getfoodid1;
	}
	
	public void setGetfoodid1(int v){
		this.getfoodid1=v;
	}
	
	/**
	 * 
	 */
	public int getfoodid2  = 0  ;
	
	public int getGetfoodid2(){
		return this.getfoodid2;
	}
	
	public void setGetfoodid2(int v){
		this.getfoodid2=v;
	}
	
	/**
	 * 
	 */
	public int getfoodid3  = 0  ;
	
	public int getGetfoodid3(){
		return this.getfoodid3;
	}
	
	public void setGetfoodid3(int v){
		this.getfoodid3=v;
	}
	
	/**
	 * 
	 */
	public int getfoodid4  = 0  ;
	
	public int getGetfoodid4(){
		return this.getfoodid4;
	}
	
	public void setGetfoodid4(int v){
		this.getfoodid4=v;
	}
	
	/**
	 * 
	 */
	public int getfoodid5  = 0  ;
	
	public int getGetfoodid5(){
		return this.getfoodid5;
	}
	
	public void setGetfoodid5(int v){
		this.getfoodid5=v;
	}
	
	/**
	 * 
	 */
	public int recommend  = 0  ;
	
	public int getRecommend(){
		return this.recommend;
	}
	
	public void setRecommend(int v){
		this.recommend=v;
	}
	
	/**
	 * 
	 */
	public int yingfu  = 0  ;
	
	public int getYingfu(){
		return this.yingfu;
	}
	
	public void setYingfu(int v){
		this.yingfu=v;
	}
	
	/**
	 * 计算该角色会有多少盈福经验
	 */
	public int yingfuawardid  = 0  ;
	
	public int getYingfuawardid(){
		return this.yingfuawardid;
	}
	
	public void setYingfuawardid(int v){
		this.yingfuawardid=v;
	}
	
	/**
	 * 
	 */
	public int yingfutimes  = 0  ;
	
	public int getYingfutimes(){
		return this.yingfutimes;
	}
	
	public void setYingfutimes(int v){
		this.yingfutimes=v;
	}
	
	/**
	 * 
	 */
	public String weekrepeat  = null  ;
	
	public String getWeekrepeat(){
		return this.weekrepeat;
	}
	
	public void setWeekrepeat(String v){
		this.weekrepeat=v;
	}
	
	
};