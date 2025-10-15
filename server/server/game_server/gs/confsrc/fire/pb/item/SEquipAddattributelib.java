package fire.pb.item;


public class SEquipAddattributelib implements mytools.ConvMain.Checkable ,Comparable<SEquipAddattributelib>{

	public int compareTo(SEquipAddattributelib o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SEquipAddattributelib(){
		super();
	}
	public SEquipAddattributelib(SEquipAddattributelib arg){
		this.id=arg.id ;
		this.namecolour=arg.namecolour ;
		this.attributename=arg.attributename ;
		this.attributeidinterval=arg.attributeidinterval ;
		this.buffid=arg.buffid ;
		this.skillid=arg.skillid ;
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
	public String namecolour  = null  ;
	
	public String getNamecolour(){
		return this.namecolour;
	}
	
	public void setNamecolour(String v){
		this.namecolour=v;
	}
	
	/**
	 * 
	 */
	public String attributename  = null  ;
	
	public String getAttributename(){
		return this.attributename;
	}
	
	public void setAttributename(String v){
		this.attributename=v;
	}
	
	/**
	 * 
	 */
	public String attributeidinterval  = null  ;
	
	public String getAttributeidinterval(){
		return this.attributeidinterval;
	}
	
	public void setAttributeidinterval(String v){
		this.attributeidinterval=v;
	}
	
	/**
	 * 
	 */
	public int buffid  = 0  ;
	
	public int getBuffid(){
		return this.buffid;
	}
	
	public void setBuffid(int v){
		this.buffid=v;
	}
	
	/**
	 * 
	 */
	public int skillid  = 0  ;
	
	public int getSkillid(){
		return this.skillid;
	}
	
	public void setSkillid(int v){
		this.skillid=v;
	}
	
	
};