package fire.pb.game;


public class SCrossGlobal implements mytools.ConvMain.Checkable ,Comparable<SCrossGlobal>{

	public int compareTo(SCrossGlobal o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SCrossGlobal(){
		super();
	}
	public SCrossGlobal(SCrossGlobal arg){
		this.id=arg.id ;
		this.zoneid=arg.zoneid ;
		this.openornot=arg.openornot ;
		this.session=arg.session ;
		this.list=arg.list ;
		this.time=arg.time ;
		this.award=arg.award ;
		this.deadline=arg.deadline ;
		this.laba=arg.laba ;
		this.ip=arg.ip ;
		this.duankou=arg.duankou ;
		this.duankoushu=arg.duankoushu ;
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
	public int zoneid  = 0  ;
	
	public int getZoneid(){
		return this.zoneid;
	}
	
	public void setZoneid(int v){
		this.zoneid=v;
	}
	
	/**
	 * 
	 */
	public int openornot  = 0  ;
	
	public int getOpenornot(){
		return this.openornot;
	}
	
	public void setOpenornot(int v){
		this.openornot=v;
	}
	
	/**
	 * 
	 */
	public int session  = 0  ;
	
	public int getSession(){
		return this.session;
	}
	
	public void setSession(int v){
		this.session=v;
	}
	
	/**
	 * 
	 */
	public String list  = null  ;
	
	public String getList(){
		return this.list;
	}
	
	public void setList(String v){
		this.list=v;
	}
	
	/**
	 * 
	 */
	public String time  = null  ;
	
	public String getTime(){
		return this.time;
	}
	
	public void setTime(String v){
		this.time=v;
	}
	
	/**
	 * 
	 */
	public int award  = 0  ;
	
	public int getAward(){
		return this.award;
	}
	
	public void setAward(int v){
		this.award=v;
	}
	
	/**
	 * 
	 */
	public String deadline  = null  ;
	
	public String getDeadline(){
		return this.deadline;
	}
	
	public void setDeadline(String v){
		this.deadline=v;
	}
	
	/**
	 * 
	 */
	public int laba  = 0  ;
	
	public int getLaba(){
		return this.laba;
	}
	
	public void setLaba(int v){
		this.laba=v;
	}
	
	/**
	 * 
	 */
	public String ip  = null  ;
	
	public String getIp(){
		return this.ip;
	}
	
	public void setIp(String v){
		this.ip=v;
	}
	
	/**
	 * 
	 */
	public int duankou  = 0  ;
	
	public int getDuankou(){
		return this.duankou;
	}
	
	public void setDuankou(int v){
		this.duankou=v;
	}
	
	/**
	 * 
	 */
	public int duankoushu  = 0  ;
	
	public int getDuankoushu(){
		return this.duankoushu;
	}
	
	public void setDuankoushu(int v){
		this.duankoushu=v;
	}
	
	
};