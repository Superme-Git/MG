package fire.pb.mission;


public class GuideCourse implements mytools.ConvMain.Checkable {


	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public GuideCourse(){
		super();
	}
	public GuideCourse(GuideCourse arg){
		this.id=arg.id ;
		this.finish=arg.finish ;
		this.ref1=arg.ref1 ;
		this.ref2=arg.ref2 ;
		this.itemid=arg.itemid ;
		this.itemnum=arg.itemnum ;
		this.awardid=arg.awardid ;
		this.petid1=arg.petid1 ;
		this.petnum1=arg.petnum1 ;
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
	public int finish  = 0  ;
	
	public int getFinish(){
		return this.finish;
	}
	
	public void setFinish(int v){
		this.finish=v;
	}
	
	/**
	 * 
	 */
	public String ref1  = null  ;
	
	public String getRef1(){
		return this.ref1;
	}
	
	public void setRef1(String v){
		this.ref1=v;
	}
	
	/**
	 * 
	 */
	public String ref2  = null  ;
	
	public String getRef2(){
		return this.ref2;
	}
	
	public void setRef2(String v){
		this.ref2=v;
	}
	
	/**
	 * 
	 */
	public int itemid  = 0  ;
	
	public int getItemid(){
		return this.itemid;
	}
	
	public void setItemid(int v){
		this.itemid=v;
	}
	
	/**
	 * 
	 */
	public int itemnum  = 0  ;
	
	public int getItemnum(){
		return this.itemnum;
	}
	
	public void setItemnum(int v){
		this.itemnum=v;
	}
	
	/**
	 * 
	 */
	public int awardid  = 0  ;
	
	public int getAwardid(){
		return this.awardid;
	}
	
	public void setAwardid(int v){
		this.awardid=v;
	}
	
	/**
	 * 
	 */
	public int petid1  = 0  ;
	
	public int getPetid1(){
		return this.petid1;
	}
	
	public void setPetid1(int v){
		this.petid1=v;
	}
	
	/**
	 * 
	 */
	public int petnum1  = 0  ;
	
	public int getPetnum1(){
		return this.petnum1;
	}
	
	public void setPetnum1(int v){
		this.petnum1=v;
	}
	
	
};