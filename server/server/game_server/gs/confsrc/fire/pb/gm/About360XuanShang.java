package fire.pb.gm;


public class About360XuanShang implements mytools.ConvMain.Checkable ,Comparable<About360XuanShang>{

	public int compareTo(About360XuanShang o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public About360XuanShang(){
		super();
	}
	public About360XuanShang(About360XuanShang arg){
		this.id=arg.id ;
		this.needRoleLv=arg.needRoleLv ;
		this.needFirstCharge=arg.needFirstCharge ;
		this.chargeDaysExceptFirst=arg.chargeDaysExceptFirst ;
		this.loginDays=arg.loginDays ;
		this.totalPlayedTime=arg.totalPlayedTime ;
		this.prizeId=arg.prizeId ;
		this.prizeNum=arg.prizeNum ;
		this.desc=arg.desc ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * ID
	 */
	public int id  = 0  ;
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int v){
		this.id=v;
	}
	
	/**
	 * 领取等级
	 */
	public int needRoleLv  = 0  ;
	
	public int getNeedRoleLv(){
		return this.needRoleLv;
	}
	
	public void setNeedRoleLv(int v){
		this.needRoleLv=v;
	}
	
	/**
	 * 是否需要首充
	 */
	public boolean needFirstCharge  = false  ;
	
	public boolean getNeedFirstCharge(){
		return this.needFirstCharge;
	}
	
	public void setNeedFirstCharge(boolean v){
		this.needFirstCharge=v;
	}
	
	/**
	 * 除首充日外的连续充值天数
	 */
	public int chargeDaysExceptFirst  = 0  ;
	
	public int getChargeDaysExceptFirst(){
		return this.chargeDaysExceptFirst;
	}
	
	public void setChargeDaysExceptFirst(int v){
		this.chargeDaysExceptFirst=v;
	}
	
	/**
	 * 领取连续登陆天数
	 */
	public int loginDays  = 0  ;
	
	public int getLoginDays(){
		return this.loginDays;
	}
	
	public void setLoginDays(int v){
		this.loginDays=v;
	}
	
	/**
	 * 累积游戏时间
	 */
	public long totalPlayedTime  = 0L  ;
	
	public long getTotalPlayedTime(){
		return this.totalPlayedTime;
	}
	
	public void setTotalPlayedTime(long v){
		this.totalPlayedTime=v;
	}
	
	/**
	 * 道具ID
	 */
	public java.util.ArrayList<Integer> prizeId  ;
	
	public java.util.ArrayList<Integer> getPrizeId(){
		return this.prizeId;
	}
	
	public void setPrizeId(java.util.ArrayList<Integer> v){
		this.prizeId=v;
	}
	
	/**
	 * 道具数量
	 */
	public java.util.ArrayList<Integer> prizeNum  ;
	
	public java.util.ArrayList<Integer> getPrizeNum(){
		return this.prizeNum;
	}
	
	public void setPrizeNum(java.util.ArrayList<Integer> v){
		this.prizeNum=v;
	}
	
	/**
	 * 描述
	 */
	public String desc  = null  ;
	
	public String getDesc(){
		return this.desc;
	}
	
	public void setDesc(String v){
		this.desc=v;
	}
	
	
};