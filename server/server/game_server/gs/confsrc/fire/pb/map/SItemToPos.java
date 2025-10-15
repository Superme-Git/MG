package fire.pb.map;


public class SItemToPos implements mytools.ConvMain.Checkable ,Comparable<SItemToPos>{

	public int compareTo(SItemToPos o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SItemToPos(){
		super();
	}
	public SItemToPos(SItemToPos arg){
		this.id=arg.id ;
		this.mapList=arg.mapList ;
		this.mapRemotePos=arg.mapRemotePos ;
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
	public String mapList  = null  ;
	
	public String getMapList(){
		return this.mapList;
	}
	
	public void setMapList(String v){
		this.mapList=v;
	}
	
	/**
	 * 
	 */
	public int mapRemotePos  = 0  ;
	
	public int getMapRemotePos(){
		return this.mapRemotePos;
	}
	
	public void setMapRemotePos(int v){
		this.mapRemotePos=v;
	}
	
	
};