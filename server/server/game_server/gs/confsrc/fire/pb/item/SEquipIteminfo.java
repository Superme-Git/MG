package fire.pb.item;


public class SEquipIteminfo implements mytools.ConvMain.Checkable ,Comparable<SEquipIteminfo>{

	public int compareTo(SEquipIteminfo o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SEquipIteminfo(){
		super();
	}
	public SEquipIteminfo(SEquipIteminfo arg){
		this.id=arg.id ;
		this.shuxing1name=arg.shuxing1name ;
		this.shuxing1bodongduanmin=arg.shuxing1bodongduanmin ;
		this.shuxing1bodongduanmax=arg.shuxing1bodongduanmax ;
		this.shuxing1bodongquanzhong=arg.shuxing1bodongquanzhong ;
		this.shuxing2name=arg.shuxing2name ;
		this.shuxing2bodongduanmin=arg.shuxing2bodongduanmin ;
		this.shuxing2bodongduanmax=arg.shuxing2bodongduanmax ;
		this.shuxing2bodongquanzhong=arg.shuxing2bodongquanzhong ;
		this.shuxing3name=arg.shuxing3name ;
		this.shuxing3bodongduanmin=arg.shuxing3bodongduanmin ;
		this.shuxing3bodongduanmax=arg.shuxing3bodongduanmax ;
		this.shuxing3bodongquanzhong=arg.shuxing3bodongquanzhong ;
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
	public String shuxing1name  = null  ;
	
	public String getShuxing1name(){
		return this.shuxing1name;
	}
	
	public void setShuxing1name(String v){
		this.shuxing1name=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> shuxing1bodongduanmin  ;
	
	public java.util.ArrayList<Integer> getShuxing1bodongduanmin(){
		return this.shuxing1bodongduanmin;
	}
	
	public void setShuxing1bodongduanmin(java.util.ArrayList<Integer> v){
		this.shuxing1bodongduanmin=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> shuxing1bodongduanmax  ;
	
	public java.util.ArrayList<Integer> getShuxing1bodongduanmax(){
		return this.shuxing1bodongduanmax;
	}
	
	public void setShuxing1bodongduanmax(java.util.ArrayList<Integer> v){
		this.shuxing1bodongduanmax=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> shuxing1bodongquanzhong  ;
	
	public java.util.ArrayList<Integer> getShuxing1bodongquanzhong(){
		return this.shuxing1bodongquanzhong;
	}
	
	public void setShuxing1bodongquanzhong(java.util.ArrayList<Integer> v){
		this.shuxing1bodongquanzhong=v;
	}
	
	/**
	 * 
	 */
	public String shuxing2name  = null  ;
	
	public String getShuxing2name(){
		return this.shuxing2name;
	}
	
	public void setShuxing2name(String v){
		this.shuxing2name=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> shuxing2bodongduanmin  ;
	
	public java.util.ArrayList<Integer> getShuxing2bodongduanmin(){
		return this.shuxing2bodongduanmin;
	}
	
	public void setShuxing2bodongduanmin(java.util.ArrayList<Integer> v){
		this.shuxing2bodongduanmin=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> shuxing2bodongduanmax  ;
	
	public java.util.ArrayList<Integer> getShuxing2bodongduanmax(){
		return this.shuxing2bodongduanmax;
	}
	
	public void setShuxing2bodongduanmax(java.util.ArrayList<Integer> v){
		this.shuxing2bodongduanmax=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> shuxing2bodongquanzhong  ;
	
	public java.util.ArrayList<Integer> getShuxing2bodongquanzhong(){
		return this.shuxing2bodongquanzhong;
	}
	
	public void setShuxing2bodongquanzhong(java.util.ArrayList<Integer> v){
		this.shuxing2bodongquanzhong=v;
	}
	
	/**
	 * 
	 */
	public String shuxing3name  = null  ;
	
	public String getShuxing3name(){
		return this.shuxing3name;
	}
	
	public void setShuxing3name(String v){
		this.shuxing3name=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> shuxing3bodongduanmin  ;
	
	public java.util.ArrayList<Integer> getShuxing3bodongduanmin(){
		return this.shuxing3bodongduanmin;
	}
	
	public void setShuxing3bodongduanmin(java.util.ArrayList<Integer> v){
		this.shuxing3bodongduanmin=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> shuxing3bodongduanmax  ;
	
	public java.util.ArrayList<Integer> getShuxing3bodongduanmax(){
		return this.shuxing3bodongduanmax;
	}
	
	public void setShuxing3bodongduanmax(java.util.ArrayList<Integer> v){
		this.shuxing3bodongduanmax=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> shuxing3bodongquanzhong  ;
	
	public java.util.ArrayList<Integer> getShuxing3bodongquanzhong(){
		return this.shuxing3bodongquanzhong;
	}
	
	public void setShuxing3bodongquanzhong(java.util.ArrayList<Integer> v){
		this.shuxing3bodongquanzhong=v;
	}
	
	
};