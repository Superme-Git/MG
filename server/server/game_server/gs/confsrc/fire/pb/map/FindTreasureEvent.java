package fire.pb.map;


public class FindTreasureEvent implements mytools.ConvMain.Checkable {


	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public FindTreasureEvent(){
		super();
	}
	public FindTreasureEvent(FindTreasureEvent arg){
		this.id=arg.id ;
		this.name=arg.name ;
		this.type=arg.type ;
		this.group=arg.group ;
		this.battleId=arg.battleId ;
		this.battleAward=arg.battleAward ;
		this.skillId=arg.skillId ;
		this.messageId=arg.messageId ;
		this.noticeId=arg.noticeId ;
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
	public String name  = null  ;
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String v){
		this.name=v;
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
	public int group  = 0  ;
	
	public int getGroup(){
		return this.group;
	}
	
	public void setGroup(int v){
		this.group=v;
	}
	
	/**
	 * 
	 */
	public int battleId  = 0  ;
	
	public int getBattleId(){
		return this.battleId;
	}
	
	public void setBattleId(int v){
		this.battleId=v;
	}
	
	/**
	 * 
	 */
	public String battleAward  = null  ;
	
	public String getBattleAward(){
		return this.battleAward;
	}
	
	public void setBattleAward(String v){
		this.battleAward=v;
	}
	
	/**
	 * 
	 */
	public int skillId  = 0  ;
	
	public int getSkillId(){
		return this.skillId;
	}
	
	public void setSkillId(int v){
		this.skillId=v;
	}
	
	/**
	 * 
	 */
	public int messageId  = 0  ;
	
	public int getMessageId(){
		return this.messageId;
	}
	
	public void setMessageId(int v){
		this.messageId=v;
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
	
	
};