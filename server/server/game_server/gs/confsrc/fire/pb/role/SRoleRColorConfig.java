package fire.pb.role;


public class SRoleRColorConfig implements mytools.ConvMain.Checkable ,Comparable<SRoleRColorConfig>{

	public int compareTo(SRoleRColorConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SRoleRColorConfig(){
		super();
	}
	public SRoleRColorConfig(SRoleRColorConfig arg){
		this.id=arg.id ;
		this.rolepos=arg.rolepos ;
		this.modeltype=arg.modeltype ;
		this.res=arg.res ;
		this.itemcode=arg.itemcode ;
		this.itemnum=arg.itemnum ;
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
	 * 部位
	 */
	public int rolepos  = 0  ;
	
	public int getRolepos(){
		return this.rolepos;
	}
	
	public void setRolepos(int v){
		this.rolepos=v;
	}
	
	/**
	 * 方案
	 */
	public int modeltype  = 0  ;
	
	public int getModeltype(){
		return this.modeltype;
	}
	
	public void setModeltype(int v){
		this.modeltype=v;
	}
	
	/**
	 * 美术资源
	 */
	public String res  = null  ;
	
	public String getRes(){
		return this.res;
	}
	
	public void setRes(String v){
		this.res=v;
	}
	
	/**
	 * 道具1
	 */
	public int itemcode  = 0  ;
	
	public int getItemcode(){
		return this.itemcode;
	}
	
	public void setItemcode(int v){
		this.itemcode=v;
	}
	
	/**
	 * 数量1
	 */
	public int itemnum  = 0  ;
	
	public int getItemnum(){
		return this.itemnum;
	}
	
	public void setItemnum(int v){
		this.itemnum=v;
	}
	
	
};