package fire.pb.item;


public class SEquipAddattributerandomlib implements mytools.ConvMain.Checkable ,Comparable<SEquipAddattributerandomlib>{

	public int compareTo(SEquipAddattributerandomlib o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SEquipAddattributerandomlib(){
		super();
	}
	public SEquipAddattributerandomlib(SEquipAddattributerandomlib arg){
		this.id=arg.id ;
		this.addattributer=arg.addattributer ;
		this.addattributerquanzhong=arg.addattributerquanzhong ;
		this.allquanzhong=arg.allquanzhong ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * id
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
	public java.util.ArrayList<Integer> addattributer  ;
	
	public java.util.ArrayList<Integer> getAddattributer(){
		return this.addattributer;
	}
	
	public void setAddattributer(java.util.ArrayList<Integer> v){
		this.addattributer=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> addattributerquanzhong  ;
	
	public java.util.ArrayList<Integer> getAddattributerquanzhong(){
		return this.addattributerquanzhong;
	}
	
	public void setAddattributerquanzhong(java.util.ArrayList<Integer> v){
		this.addattributerquanzhong=v;
	}
	
	/**
	 * 
	 */
	public int allquanzhong  = 0  ;
	
	public int getAllquanzhong(){
		return this.allquanzhong;
	}
	
	public void setAllquanzhong(int v){
		this.allquanzhong=v;
	}
	
	
};