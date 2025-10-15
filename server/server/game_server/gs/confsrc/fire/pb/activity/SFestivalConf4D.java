package fire.pb.activity;


public class SFestivalConf4D  extends FestivalConf {

	public int compareTo(SFestivalConf4D o){
		return this.id-o.id;
	}

	
	public SFestivalConf4D(FestivalConf arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SFestivalConf4D(){
		super();
	}
	public SFestivalConf4D(SFestivalConf4D arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};