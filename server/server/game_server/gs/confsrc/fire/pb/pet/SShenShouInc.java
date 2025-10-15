package fire.pb.pet;


public class SShenShouInc implements mytools.ConvMain.Checkable ,Comparable<SShenShouInc>{

	public int compareTo(SShenShouInc o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SShenShouInc(){
		super();
	}
	public SShenShouInc(SShenShouInc arg){
		this.id=arg.id ;
		this.petid=arg.petid ;
		this.inccount=arg.inccount ;
		this.attinc=arg.attinc ;
		this.atkinc=arg.atkinc ;
		this.definc=arg.definc ;
		this.hpinc=arg.hpinc ;
		this.mpinc=arg.mpinc ;
		this.spdinc=arg.spdinc ;
		this.inclv=arg.inclv ;
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
	public int petid  = 0  ;
	
	public int getPetid(){
		return this.petid;
	}
	
	public void setPetid(int v){
		this.petid=v;
	}
	
	/**
	 * 
	 */
	public int inccount  = 0  ;
	
	public int getInccount(){
		return this.inccount;
	}
	
	public void setInccount(int v){
		this.inccount=v;
	}
	
	/**
	 * 
	 */
	public int attinc  = 0  ;
	
	public int getAttinc(){
		return this.attinc;
	}
	
	public void setAttinc(int v){
		this.attinc=v;
	}
	
	/**
	 * 
	 */
	public int atkinc  = 0  ;
	
	public int getAtkinc(){
		return this.atkinc;
	}
	
	public void setAtkinc(int v){
		this.atkinc=v;
	}
	
	/**
	 * 
	 */
	public int definc  = 0  ;
	
	public int getDefinc(){
		return this.definc;
	}
	
	public void setDefinc(int v){
		this.definc=v;
	}
	
	/**
	 * 
	 */
	public int hpinc  = 0  ;
	
	public int getHpinc(){
		return this.hpinc;
	}
	
	public void setHpinc(int v){
		this.hpinc=v;
	}
	
	/**
	 * 
	 */
	public int mpinc  = 0  ;
	
	public int getMpinc(){
		return this.mpinc;
	}
	
	public void setMpinc(int v){
		this.mpinc=v;
	}
	
	/**
	 * 
	 */
	public int spdinc  = 0  ;
	
	public int getSpdinc(){
		return this.spdinc;
	}
	
	public void setSpdinc(int v){
		this.spdinc=v;
	}
	
	/**
	 * 
	 */
	public int inclv  = 0  ;
	
	public int getInclv(){
		return this.inclv;
	}
	
	public void setInclv(int v){
		this.inclv=v;
	}
	
	
};