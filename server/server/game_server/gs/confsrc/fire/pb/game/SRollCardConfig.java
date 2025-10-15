package fire.pb.game;


public class SRollCardConfig implements mytools.ConvMain.Checkable ,Comparable<SRollCardConfig>{

	public int compareTo(SRollCardConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SRollCardConfig(){
		super();
	}
	public SRollCardConfig(SRollCardConfig arg){
		this.id=arg.id ;
		this.objs=arg.objs ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 大转盘的id,会有多种大转盘,奖励不一样
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
	public java.util.ArrayList<String> objs  ;
	
	public java.util.ArrayList<String> getObjs(){
		return this.objs;
	}
	
	public void setObjs(java.util.ArrayList<String> v){
		this.objs=v;
	}
	
	
};