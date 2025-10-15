package fire.pb.map;


public class FindTreasureGift implements mytools.ConvMain.Checkable {


	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public FindTreasureGift(){
		super();
	}
	public FindTreasureGift(FindTreasureGift arg){
		this.id=arg.id ;
		this.awardId=arg.awardId ;
		this.mapId=arg.mapId ;
		this.itemList=arg.itemList ;
		this.awardids=arg.awardids ;
		this.moneyAwardId=arg.moneyAwardId ;
		this.noticeItemList=arg.noticeItemList ;
		this.noticeId=arg.noticeId ;
		this.getItemMessageId=arg.getItemMessageId ;
		this.getMoneyMessageId=arg.getMoneyMessageId ;
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
	 * id
	 */
	public int awardId  = 0  ;
	
	public int getAwardId(){
		return this.awardId;
	}
	
	public void setAwardId(int v){
		this.awardId=v;
	}
	
	/**
	 * 
	 */
	public int mapId  = 0  ;
	
	public int getMapId(){
		return this.mapId;
	}
	
	public void setMapId(int v){
		this.mapId=v;
	}
	
	/**
	 * 
	 */
	public String itemList  = null  ;
	
	public String getItemList(){
		return this.itemList;
	}
	
	public void setItemList(String v){
		this.itemList=v;
	}
	
	/**
	 * 添加根据角色等级匹配不同的奖励id
	 */
	public String awardids  = null  ;
	
	public String getAwardids(){
		return this.awardids;
	}
	
	public void setAwardids(String v){
		this.awardids=v;
	}
	
	/**
	 * 
	 */
	public int moneyAwardId  = 0  ;
	
	public int getMoneyAwardId(){
		return this.moneyAwardId;
	}
	
	public void setMoneyAwardId(int v){
		this.moneyAwardId=v;
	}
	
	/**
	 * 
	 */
	public String noticeItemList  = null  ;
	
	public String getNoticeItemList(){
		return this.noticeItemList;
	}
	
	public void setNoticeItemList(String v){
		this.noticeItemList=v;
	}
	
	/**
	 * 
	 */
	public int noticeId  = 0  ;
	
	public int getNoticeId(){
		return this.noticeId;
	}
	
	public void setNoticeId(int v){
		this.noticeId=v;
	}
	
	/**
	 * 
	 */
	public int getItemMessageId  = 0  ;
	
	public int getGetItemMessageId(){
		return this.getItemMessageId;
	}
	
	public void setGetItemMessageId(int v){
		this.getItemMessageId=v;
	}
	
	/**
	 * 
	 */
	public int getMoneyMessageId  = 0  ;
	
	public int getGetMoneyMessageId(){
		return this.getMoneyMessageId;
	}
	
	public void setGetMoneyMessageId(int v){
		this.getMoneyMessageId=v;
	}
	
	
};