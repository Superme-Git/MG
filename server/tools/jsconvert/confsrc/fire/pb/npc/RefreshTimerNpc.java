package fire.pb.npc;


public class RefreshTimerNpc implements mytools.ConvMain.Checkable {


	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public RefreshTimerNpc(){
		super();
	}
	public RefreshTimerNpc(RefreshTimerNpc arg){
		this.id=arg.id ;
		this.startTime=arg.startTime ;
		this.firstRefresh=arg.firstRefresh ;
		this.intervalRefresh=arg.intervalRefresh ;
		this.messageId=arg.messageId ;
		this.freshType=arg.freshType ;
		this.maxNpcCount=arg.maxNpcCount ;
		this.addNpcTime=arg.addNpcTime ;
		this.npcList=arg.npcList ;
		this.npcGroup=arg.npcGroup ;
		this.npcCount=arg.npcCount ;
		this.npcAllCount=arg.npcAllCount ;
		this.freshControl=arg.freshControl ;
		this.sceneList=arg.sceneList ;
		this.sceneCnt=arg.sceneCnt ;
		this.npcLastTime=arg.npcLastTime ;
		this.awardCnt=arg.awardCnt ;
		this.awardId=arg.awardId ;
		this.awardOverStepNotice=arg.awardOverStepNotice ;
		this.teamRequire=arg.teamRequire ;
		this.teamCount=arg.teamCount ;
		this.levelMin=arg.levelMin ;
		this.levelMax=arg.levelMax ;
		this.battletime=arg.battletime ;
		this.addnpctype=arg.addnpctype ;
		this.eventbiaoid=arg.eventbiaoid ;
		this.canwatchbattle=arg.canwatchbattle ;
		this.xiaoshi=arg.xiaoshi ;
		this.randomtrigger=arg.randomtrigger ;
		this.actid=arg.actid ;
		this.levelaward=arg.levelaward ;
		this.matchlevel=arg.matchlevel ;
		this.matchtime=arg.matchtime ;
		this.match=arg.match ;
		this.havetimes=arg.havetimes ;
		this.leveltimes=arg.leveltimes ;
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
	public String startTime  = null  ;
	
	public String getStartTime(){
		return this.startTime;
	}
	
	public void setStartTime(String v){
		this.startTime=v;
	}
	
	/**
	 * 
	 */
	public int firstRefresh  = 0  ;
	
	public int getFirstRefresh(){
		return this.firstRefresh;
	}
	
	public void setFirstRefresh(int v){
		this.firstRefresh=v;
	}
	
	/**
	 * 
	 */
	public int intervalRefresh  = 0  ;
	
	public int getIntervalRefresh(){
		return this.intervalRefresh;
	}
	
	public void setIntervalRefresh(int v){
		this.intervalRefresh=v;
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
	public int freshType  = 0  ;
	
	public int getFreshType(){
		return this.freshType;
	}
	
	public void setFreshType(int v){
		this.freshType=v;
	}
	
	/**
	 * 活动中能最多出现的怪物数量
	 */
	public int maxNpcCount  = 0  ;
	
	public int getMaxNpcCount(){
		return this.maxNpcCount;
	}
	
	public void setMaxNpcCount(int v){
		this.maxNpcCount=v;
	}
	
	/**
	 * 活动开始一段时间之后就不再补怪了
	 */
	public int addNpcTime  = 0  ;
	
	public int getAddNpcTime(){
		return this.addNpcTime;
	}
	
	public void setAddNpcTime(int v){
		this.addNpcTime=v;
	}
	
	/**
	 * 
	 */
	public String npcList  = null  ;
	
	public String getNpcList(){
		return this.npcList;
	}
	
	public void setNpcList(String v){
		this.npcList=v;
	}
	
	/**
	 * 
	 */
	public int npcGroup  = 0  ;
	
	public int getNpcGroup(){
		return this.npcGroup;
	}
	
	public void setNpcGroup(int v){
		this.npcGroup=v;
	}
	
	/**
	 * 
	 */
	public String npcCount  = null  ;
	
	public String getNpcCount(){
		return this.npcCount;
	}
	
	public void setNpcCount(String v){
		this.npcCount=v;
	}
	
	/**
	 * 
	 */
	public String npcAllCount  = null  ;
	
	public String getNpcAllCount(){
		return this.npcAllCount;
	}
	
	public void setNpcAllCount(String v){
		this.npcAllCount=v;
	}
	
	/**
	 * 1 为个数刷新; 2 为总数刷新
	 */
	public int freshControl  = 0  ;
	
	public int getFreshControl(){
		return this.freshControl;
	}
	
	public void setFreshControl(int v){
		this.freshControl=v;
	}
	
	/**
	 * 
	 */
	public String sceneList  = null  ;
	
	public String getSceneList(){
		return this.sceneList;
	}
	
	public void setSceneList(String v){
		this.sceneList=v;
	}
	
	/**
	 * 
	 */
	public int sceneCnt  = 0  ;
	
	public int getSceneCnt(){
		return this.sceneCnt;
	}
	
	public void setSceneCnt(int v){
		this.sceneCnt=v;
	}
	
	/**
	 * 单位:秒
	 */
	public int npcLastTime  = 0  ;
	
	public int getNpcLastTime(){
		return this.npcLastTime;
	}
	
	public void setNpcLastTime(int v){
		this.npcLastTime=v;
	}
	
	/**
	 * 
	 */
	public int awardCnt  = 0  ;
	
	public int getAwardCnt(){
		return this.awardCnt;
	}
	
	public void setAwardCnt(int v){
		this.awardCnt=v;
	}
	
	/**
	 * 
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
	public int awardOverStepNotice  = 0  ;
	
	public int getAwardOverStepNotice(){
		return this.awardOverStepNotice;
	}
	
	public void setAwardOverStepNotice(int v){
		this.awardOverStepNotice=v;
	}
	
	/**
	 * 1 需要组队; 0 不需要组队
	 */
	public int teamRequire  = 0  ;
	
	public int getTeamRequire(){
		return this.teamRequire;
	}
	
	public void setTeamRequire(int v){
		this.teamRequire=v;
	}
	
	/**
	 * 
	 */
	public int teamCount  = 0  ;
	
	public int getTeamCount(){
		return this.teamCount;
	}
	
	public void setTeamCount(int v){
		this.teamCount=v;
	}
	
	/**
	 * 
	 */
	public int levelMin  = 0  ;
	
	public int getLevelMin(){
		return this.levelMin;
	}
	
	public void setLevelMin(int v){
		this.levelMin=v;
	}
	
	/**
	 * 
	 */
	public int levelMax  = 0  ;
	
	public int getLevelMax(){
		return this.levelMax;
	}
	
	public void setLevelMax(int v){
		this.levelMax=v;
	}
	
	/**
	 * 
	 */
	public int battletime  = 0  ;
	
	public int getBattletime(){
		return this.battletime;
	}
	
	public void setBattletime(int v){
		this.battletime=v;
	}
	
	/**
	 * 
	 */
	public int addnpctype  = 0  ;
	
	public int getAddnpctype(){
		return this.addnpctype;
	}
	
	public void setAddnpctype(int v){
		this.addnpctype=v;
	}
	
	/**
	 * 
	 */
	public int eventbiaoid  = 0  ;
	
	public int getEventbiaoid(){
		return this.eventbiaoid;
	}
	
	public void setEventbiaoid(int v){
		this.eventbiaoid=v;
	}
	
	/**
	 * 
	 */
	public int canwatchbattle  = 0  ;
	
	public int getCanwatchbattle(){
		return this.canwatchbattle;
	}
	
	public void setCanwatchbattle(int v){
		this.canwatchbattle=v;
	}
	
	/**
	 * 
	 */
	public int xiaoshi  = 0  ;
	
	public int getXiaoshi(){
		return this.xiaoshi;
	}
	
	public void setXiaoshi(int v){
		this.xiaoshi=v;
	}
	
	/**
	 * 
	 */
	public int randomtrigger  = 0  ;
	
	public int getRandomtrigger(){
		return this.randomtrigger;
	}
	
	public void setRandomtrigger(int v){
		this.randomtrigger=v;
	}
	
	/**
	 * 
	 */
	public int actid  = 0  ;
	
	public int getActid(){
		return this.actid;
	}
	
	public void setActid(int v){
		this.actid=v;
	}
	
	/**
	 * 
	 */
	public String levelaward  = null  ;
	
	public String getLevelaward(){
		return this.levelaward;
	}
	
	public void setLevelaward(String v){
		this.levelaward=v;
	}
	
	/**
	 * 1 按照人物等级匹配; 2 按照队伍等级匹配
	 */
	public int matchlevel  = 0  ;
	
	public int getMatchlevel(){
		return this.matchlevel;
	}
	
	public void setMatchlevel(int v){
		this.matchlevel=v;
	}
	
	/**
	 * 多少时间内点击npc需要匹配
	 */
	public int matchtime  = 0  ;
	
	public int getMatchtime(){
		return this.matchtime;
	}
	
	public void setMatchtime(int v){
		this.matchtime=v;
	}
	
	/**
	 * 打怪时是否需要跟其他玩家随机
	 */
	public int match  = 0  ;
	
	public int getMatch(){
		return this.match;
	}
	
	public void setMatch(int v){
		this.match=v;
	}
	
	/**
	 * 进战斗前是否需要判断还有奖励次数
	 */
	public int havetimes  = 0  ;
	
	public int getHavetimes(){
		return this.havetimes;
	}
	
	public void setHavetimes(int v){
		this.havetimes=v;
	}
	
	/**
	 * 
	 */
	public String leveltimes  = null  ;
	
	public String getLeveltimes(){
		return this.leveltimes;
	}
	
	public void setLeveltimes(String v){
		this.leveltimes=v;
	}
	
	
};