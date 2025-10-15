package fire.pb.fushi;


public class SVipInfoConfig implements mytools.ConvMain.Checkable ,Comparable<SVipInfoConfig>{

	public int compareTo(SVipInfoConfig o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SVipInfoConfig(){
		super();
	}
	public SVipInfoConfig(SVipInfoConfig arg){
		this.id=arg.id ;
		this.exp=arg.exp ;
		this.itemids=arg.itemids ;
		this.itemcounts=arg.itemcounts ;
		this.viprights=arg.viprights ;
		this.bagsize=arg.bagsize ;
		this.depotsize=arg.depotsize ;
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
	public int exp  = 0  ;
	
	public int getExp(){
		return this.exp;
	}
	
	public void setExp(int v){
		this.exp=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> itemids  ;
	
	public java.util.ArrayList<Integer> getItemids(){
		return this.itemids;
	}
	
	public void setItemids(java.util.ArrayList<Integer> v){
		this.itemids=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> itemcounts  ;
	
	public java.util.ArrayList<Integer> getItemcounts(){
		return this.itemcounts;
	}
	
	public void setItemcounts(java.util.ArrayList<Integer> v){
		this.itemcounts=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> viprights  ;
	
	public java.util.ArrayList<Integer> getViprights(){
		return this.viprights;
	}
	
	public void setViprights(java.util.ArrayList<Integer> v){
		this.viprights=v;
	}
	
	/**
	 * 
	 */
	public int bagsize  = 0  ;
	
	public int getBagsize(){
		return this.bagsize;
	}
	
	public void setBagsize(int v){
		this.bagsize=v;
	}
	
	/**
	 * 
	 */
	public int depotsize  = 0  ;
	
	public int getDepotsize(){
		return this.depotsize;
	}
	
	public void setDepotsize(int v){
		this.depotsize=v;
	}
	
	
};