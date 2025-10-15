package fire.pb.game;


public class Smuldayloginreward implements mytools.ConvMain.Checkable ,Comparable<Smuldayloginreward>{

	public int compareTo(Smuldayloginreward o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public Smuldayloginreward(){
		super();
	}
	public Smuldayloginreward(Smuldayloginreward arg){
		this.id=arg.id ;
		this.rewardvec=arg.rewardvec ;
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
	public java.util.ArrayList<MulDayLoginGiftData> rewardvec  ;
	
	public java.util.ArrayList<MulDayLoginGiftData> getRewardvec(){
		return this.rewardvec;
	}
	
	public void setRewardvec(java.util.ArrayList<MulDayLoginGiftData> v){
		this.rewardvec=v;
	}
	
	
};