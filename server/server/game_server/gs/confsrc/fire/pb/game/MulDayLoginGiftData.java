package fire.pb.game;


public class MulDayLoginGiftData implements mytools.ConvMain.Checkable {


	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public MulDayLoginGiftData(){
		super();
	}
	public MulDayLoginGiftData(MulDayLoginGiftData arg){
		this.item1id=arg.item1id ;
		this.item1num=arg.item1num ;
		this.item1bind=arg.item1bind ;
		this.item2id=arg.item2id ;
		this.item2num=arg.item2num ;
		this.item2bind=arg.item2bind ;
		this.item3id=arg.item3id ;
		this.item3num=arg.item3num ;
		this.item3bind=arg.item3bind ;
		this.needbagvolume=arg.needbagvolume ;
		this.pet1id=arg.pet1id ;
		this.pet1bind=arg.pet1bind ;
		this.pet2id=arg.pet2id ;
		this.pet2bind=arg.pet2bind ;
		this.pet3id=arg.pet3id ;
		this.pet3bind=arg.pet3bind ;
		this.needpetbagvolume=arg.needpetbagvolume ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 
	 */
	public String item1id  = null  ;
	
	public String getItem1id(){
		return this.item1id;
	}
	
	public void setItem1id(String v){
		this.item1id=v;
	}
	
	/**
	 * 
	 */
	public int item1num  = 0  ;
	
	public int getItem1num(){
		return this.item1num;
	}
	
	public void setItem1num(int v){
		this.item1num=v;
	}
	
	/**
	 * 
	 */
	public int item1bind  = 0  ;
	
	public int getItem1bind(){
		return this.item1bind;
	}
	
	public void setItem1bind(int v){
		this.item1bind=v;
	}
	
	/**
	 * 
	 */
	public String item2id  = null  ;
	
	public String getItem2id(){
		return this.item2id;
	}
	
	public void setItem2id(String v){
		this.item2id=v;
	}
	
	/**
	 * 
	 */
	public int item2num  = 0  ;
	
	public int getItem2num(){
		return this.item2num;
	}
	
	public void setItem2num(int v){
		this.item2num=v;
	}
	
	/**
	 * 
	 */
	public int item2bind  = 0  ;
	
	public int getItem2bind(){
		return this.item2bind;
	}
	
	public void setItem2bind(int v){
		this.item2bind=v;
	}
	
	/**
	 * 
	 */
	public String item3id  = null  ;
	
	public String getItem3id(){
		return this.item3id;
	}
	
	public void setItem3id(String v){
		this.item3id=v;
	}
	
	/**
	 * 
	 */
	public int item3num  = 0  ;
	
	public int getItem3num(){
		return this.item3num;
	}
	
	public void setItem3num(int v){
		this.item3num=v;
	}
	
	/**
	 * 
	 */
	public int item3bind  = 0  ;
	
	public int getItem3bind(){
		return this.item3bind;
	}
	
	public void setItem3bind(int v){
		this.item3bind=v;
	}
	
	/**
	 * 
	 */
	public int needbagvolume  = 0  ;
	
	public int getNeedbagvolume(){
		return this.needbagvolume;
	}
	
	public void setNeedbagvolume(int v){
		this.needbagvolume=v;
	}
	
	/**
	 * 
	 */
	public int pet1id  = 0  ;
	
	public int getPet1id(){
		return this.pet1id;
	}
	
	public void setPet1id(int v){
		this.pet1id=v;
	}
	
	/**
	 * 
	 */
	public int pet1bind  = 0  ;
	
	public int getPet1bind(){
		return this.pet1bind;
	}
	
	public void setPet1bind(int v){
		this.pet1bind=v;
	}
	
	/**
	 * 
	 */
	public int pet2id  = 0  ;
	
	public int getPet2id(){
		return this.pet2id;
	}
	
	public void setPet2id(int v){
		this.pet2id=v;
	}
	
	/**
	 * 
	 */
	public int pet2bind  = 0  ;
	
	public int getPet2bind(){
		return this.pet2bind;
	}
	
	public void setPet2bind(int v){
		this.pet2bind=v;
	}
	
	/**
	 * 
	 */
	public int pet3id  = 0  ;
	
	public int getPet3id(){
		return this.pet3id;
	}
	
	public void setPet3id(int v){
		this.pet3id=v;
	}
	
	/**
	 * 
	 */
	public int pet3bind  = 0  ;
	
	public int getPet3bind(){
		return this.pet3bind;
	}
	
	public void setPet3bind(int v){
		this.pet3bind=v;
	}
	
	/**
	 * 
	 */
	public int needpetbagvolume  = 0  ;
	
	public int getNeedpetbagvolume(){
		return this.needpetbagvolume;
	}
	
	public void setNeedpetbagvolume(int v){
		this.needpetbagvolume=v;
	}
	
	
};