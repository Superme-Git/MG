package fire.pb.activity;


public class SFestivalConf  extends FestivalConf {

	public int compareTo(SFestivalConf o){
		return this.id-o.id;
	}

	
	public SFestivalConf(FestivalConf arg){
		super(arg);
	}
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SFestivalConf(){
		super();
	}
	public SFestivalConf(SFestivalConf arg){
		super(arg);
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
			super.checkValid(objs);
	}
	
};