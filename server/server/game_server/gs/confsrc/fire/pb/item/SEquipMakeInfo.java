package fire.pb.item;


public class SEquipMakeInfo implements mytools.ConvMain.Checkable ,Comparable<SEquipMakeInfo>{

	public int compareTo(SEquipMakeInfo o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SEquipMakeInfo(){
		super();
	}
	public SEquipMakeInfo(SEquipMakeInfo arg){
		this.id=arg.id ;
		this.type=arg.type ;
		this.tuzhiid=arg.tuzhiid ;
		this.tuzhinum=arg.tuzhinum ;
		this.hantieid=arg.hantieid ;
		this.hantienum=arg.hantienum ;
		this.zhizaofuid=arg.zhizaofuid ;
		this.zhizaofunum=arg.zhizaofunum ;
		this.qianghuaid=arg.qianghuaid ;
		this.qianghuanum=arg.qianghuanum ;
		this.moneynum=arg.moneynum ;
		this.moneytype=arg.moneytype ;
		this.chanchuequipid=arg.chanchuequipid ;
		this.ptdazhaoid=arg.ptdazhaoid ;
		this.ptdazhaorate=arg.ptdazhaorate ;
		this.qhdazhaoid=arg.qhdazhaoid ;
		this.qhdazhaorate=arg.qhdazhaorate ;
		this.vcailiaotie=arg.vcailiaotie ;
		this.vcailiaotienum=arg.vcailiaotienum ;
		this.vcailiaozhizaofu=arg.vcailiaozhizaofu ;
		this.vcailiaozhizaofunum=arg.vcailiaozhizaofunum ;
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
	public int type  = 0  ;
	
	public int getType(){
		return this.type;
	}
	
	public void setType(int v){
		this.type=v;
	}
	
	/**
	 * 
	 */
	public int tuzhiid  = 0  ;
	
	public int getTuzhiid(){
		return this.tuzhiid;
	}
	
	public void setTuzhiid(int v){
		this.tuzhiid=v;
	}
	
	/**
	 * 
	 */
	public int tuzhinum  = 0  ;
	
	public int getTuzhinum(){
		return this.tuzhinum;
	}
	
	public void setTuzhinum(int v){
		this.tuzhinum=v;
	}
	
	/**
	 * 
	 */
	public int hantieid  = 0  ;
	
	public int getHantieid(){
		return this.hantieid;
	}
	
	public void setHantieid(int v){
		this.hantieid=v;
	}
	
	/**
	 * 
	 */
	public int hantienum  = 0  ;
	
	public int getHantienum(){
		return this.hantienum;
	}
	
	public void setHantienum(int v){
		this.hantienum=v;
	}
	
	/**
	 * 
	 */
	public int zhizaofuid  = 0  ;
	
	public int getZhizaofuid(){
		return this.zhizaofuid;
	}
	
	public void setZhizaofuid(int v){
		this.zhizaofuid=v;
	}
	
	/**
	 * 
	 */
	public int zhizaofunum  = 0  ;
	
	public int getZhizaofunum(){
		return this.zhizaofunum;
	}
	
	public void setZhizaofunum(int v){
		this.zhizaofunum=v;
	}
	
	/**
	 * 
	 */
	public int qianghuaid  = 0  ;
	
	public int getQianghuaid(){
		return this.qianghuaid;
	}
	
	public void setQianghuaid(int v){
		this.qianghuaid=v;
	}
	
	/**
	 * 
	 */
	public int qianghuanum  = 0  ;
	
	public int getQianghuanum(){
		return this.qianghuanum;
	}
	
	public void setQianghuanum(int v){
		this.qianghuanum=v;
	}
	
	/**
	 * 
	 */
	public int moneynum  = 0  ;
	
	public int getMoneynum(){
		return this.moneynum;
	}
	
	public void setMoneynum(int v){
		this.moneynum=v;
	}
	
	/**
	 * 
	 */
	public int moneytype  = 0  ;
	
	public int getMoneytype(){
		return this.moneytype;
	}
	
	public void setMoneytype(int v){
		this.moneytype=v;
	}
	
	/**
	 * 
	 */
	public int chanchuequipid  = 0  ;
	
	public int getChanchuequipid(){
		return this.chanchuequipid;
	}
	
	public void setChanchuequipid(int v){
		this.chanchuequipid=v;
	}
	
	/**
	 * 普通打造产出装备id
	 */
	public java.util.ArrayList<Integer> ptdazhaoid  ;
	
	public java.util.ArrayList<Integer> getPtdazhaoid(){
		return this.ptdazhaoid;
	}
	
	public void setPtdazhaoid(java.util.ArrayList<Integer> v){
		this.ptdazhaoid=v;
	}
	
	/**
	 * 普通打造产出装备概率
	 */
	public java.util.ArrayList<Integer> ptdazhaorate  ;
	
	public java.util.ArrayList<Integer> getPtdazhaorate(){
		return this.ptdazhaorate;
	}
	
	public void setPtdazhaorate(java.util.ArrayList<Integer> v){
		this.ptdazhaorate=v;
	}
	
	/**
	 * 强化打造产出装备id
	 */
	public java.util.ArrayList<Integer> qhdazhaoid  ;
	
	public java.util.ArrayList<Integer> getQhdazhaoid(){
		return this.qhdazhaoid;
	}
	
	public void setQhdazhaoid(java.util.ArrayList<Integer> v){
		this.qhdazhaoid=v;
	}
	
	/**
	 * 强化打造产出装备概率
	 */
	public java.util.ArrayList<Integer> qhdazhaorate  ;
	
	public java.util.ArrayList<Integer> getQhdazhaorate(){
		return this.qhdazhaorate;
	}
	
	public void setQhdazhaorate(java.util.ArrayList<Integer> v){
		this.qhdazhaorate=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> vcailiaotie  ;
	
	public java.util.ArrayList<Integer> getVcailiaotie(){
		return this.vcailiaotie;
	}
	
	public void setVcailiaotie(java.util.ArrayList<Integer> v){
		this.vcailiaotie=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> vcailiaotienum  ;
	
	public java.util.ArrayList<Integer> getVcailiaotienum(){
		return this.vcailiaotienum;
	}
	
	public void setVcailiaotienum(java.util.ArrayList<Integer> v){
		this.vcailiaotienum=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> vcailiaozhizaofu  ;
	
	public java.util.ArrayList<Integer> getVcailiaozhizaofu(){
		return this.vcailiaozhizaofu;
	}
	
	public void setVcailiaozhizaofu(java.util.ArrayList<Integer> v){
		this.vcailiaozhizaofu=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> vcailiaozhizaofunum  ;
	
	public java.util.ArrayList<Integer> getVcailiaozhizaofunum(){
		return this.vcailiaozhizaofunum;
	}
	
	public void setVcailiaozhizaofunum(java.util.ArrayList<Integer> v){
		this.vcailiaozhizaofunum=v;
	}
	
	
};