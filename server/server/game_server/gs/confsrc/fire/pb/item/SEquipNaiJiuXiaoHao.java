package fire.pb.item;


public class SEquipNaiJiuXiaoHao implements mytools.ConvMain.Checkable ,Comparable<SEquipNaiJiuXiaoHao>{

	public int compareTo(SEquipNaiJiuXiaoHao o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SEquipNaiJiuXiaoHao(){
		super();
	}
	public SEquipNaiJiuXiaoHao(SEquipNaiJiuXiaoHao arg){
		this.id=arg.id ;
		this.yuanyin=arg.yuanyin ;
		this.cishu=arg.cishu ;
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
	public java.util.ArrayList<Integer> yuanyin  ;
	
	public java.util.ArrayList<Integer> getYuanyin(){
		return this.yuanyin;
	}
	
	public void setYuanyin(java.util.ArrayList<Integer> v){
		this.yuanyin=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> cishu  ;
	
	public java.util.ArrayList<Integer> getCishu(){
		return this.cishu;
	}
	
	public void setCishu(java.util.ArrayList<Integer> v){
		this.cishu=v;
	}
	
	
};