package fire.pb.game;


public class Snpcjianglifafang implements mytools.ConvMain.Checkable ,Comparable<Snpcjianglifafang>{

	public int compareTo(Snpcjianglifafang o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public Snpcjianglifafang(){
		super();
	}
	public Snpcjianglifafang(Snpcjianglifafang arg){
		this.id=arg.id ;
		this.jiangliid=arg.jiangliid ;
		this.jianglicishu=arg.jianglicishu ;
		this.lvxianzhi=arg.lvxianzhi ;
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
	public int jiangliid  = 0  ;
	
	public int getJiangliid(){
		return this.jiangliid;
	}
	
	public void setJiangliid(int v){
		this.jiangliid=v;
	}
	
	/**
	 * 
	 */
	public int jianglicishu  = 0  ;
	
	public int getJianglicishu(){
		return this.jianglicishu;
	}
	
	public void setJianglicishu(int v){
		this.jianglicishu=v;
	}
	
	/**
	 * 
	 */
	public int lvxianzhi  = 0  ;
	
	public int getLvxianzhi(){
		return this.lvxianzhi;
	}
	
	public void setLvxianzhi(int v){
		this.lvxianzhi=v;
	}
	
	
};