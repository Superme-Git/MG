package fire.pb.role;


public class SCreateRoleConfig implements mytools.ConvMain.Checkable ,Comparable<SCreateRoleConfig>{

	public int compareTo(SCreateRoleConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SCreateRoleConfig(){
		super();
	}
	public SCreateRoleConfig(SCreateRoleConfig arg){
		this.id=arg.id ;
		this.sex=arg.sex ;
		this.schools=arg.schools ;
		this.initequip=arg.initequip ;
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
	 * 性别
	 */
	public int sex  = 0  ;
	
	public int getSex(){
		return this.sex;
	}
	
	public void setSex(int v){
		this.sex=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> schools  ;
	
	public java.util.ArrayList<Integer> getSchools(){
		return this.schools;
	}
	
	public void setSchools(java.util.ArrayList<Integer> v){
		this.schools=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> initequip  ;
	
	public java.util.ArrayList<Integer> getInitequip(){
		return this.initequip;
	}
	
	public void setInitequip(java.util.ArrayList<Integer> v){
		this.initequip=v;
	}
	
	
};