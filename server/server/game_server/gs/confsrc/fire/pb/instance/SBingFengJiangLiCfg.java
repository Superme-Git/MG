package fire.pb.instance;


public class SBingFengJiangLiCfg implements mytools.ConvMain.Checkable ,Comparable<SBingFengJiangLiCfg>{

	public int compareTo(SBingFengJiangLiCfg o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SBingFengJiangLiCfg(){
		super();
	}
	public SBingFengJiangLiCfg(SBingFengJiangLiCfg arg){
		this.id=arg.id ;
		this.rankid=arg.rankid ;
		this.fubenid=arg.fubenid ;
		this.ranklv=arg.ranklv ;
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
	public int rankid  = 0  ;
	
	public int getRankid(){
		return this.rankid;
	}
	
	public void setRankid(int v){
		this.rankid=v;
	}
	
	/**
	 * 
	 */
	public int fubenid  = 0  ;
	
	public int getFubenid(){
		return this.fubenid;
	}
	
	public void setFubenid(int v){
		this.fubenid=v;
	}
	
	/**
	 * 
	 */
	public int ranklv  = 0  ;
	
	public int getRanklv(){
		return this.ranklv;
	}
	
	public void setRanklv(int v){
		this.ranklv=v;
	}
	
	
};