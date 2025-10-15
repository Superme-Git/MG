package fire.pb.role;


public class Redirect implements mytools.ConvMain.Checkable ,Comparable<Redirect>{

	public int compareTo(Redirect o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public Redirect(){
		super();
	}
	public Redirect(Redirect arg){
		this.id=arg.id ;
		this.remapid=arg.remapid ;
		this.reposx=arg.reposx ;
		this.reposy=arg.reposy ;
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
	public int remapid  = 0  ;
	
	public int getRemapid(){
		return this.remapid;
	}
	
	public void setRemapid(int v){
		this.remapid=v;
	}
	
	/**
	 * 
	 */
	public int reposx  = 0  ;
	
	public int getReposx(){
		return this.reposx;
	}
	
	public void setReposx(int v){
		this.reposx=v;
	}
	
	/**
	 * 
	 */
	public int reposy  = 0  ;
	
	public int getReposy(){
		return this.reposy;
	}
	
	public void setReposy(int v){
		this.reposy=v;
	}
	
	
};