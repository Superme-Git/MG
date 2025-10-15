package fire.pb.circletask;


public class CircTask implements mytools.ConvMain.Checkable ,Comparable<CircTask>{

	public int compareTo(CircTask o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public CircTask(){
		super();
	}
	public CircTask(CircTask arg){
		this.id=arg.id ;
		this.type=arg.type ;
		this.levelgroup=arg.levelgroup ;
		this.levelmin=arg.levelmin ;
		this.levelmax=arg.levelmax ;
		this.levelrate=arg.levelrate ;
		this.cycle=arg.cycle ;
		this.clearround=arg.clearround ;
		this.round=arg.round ;
		this.ring=arg.ring ;
		this.totaltime=arg.totaltime ;
		this.awardtime=arg.awardtime ;
		this.questrate=arg.questrate ;
		this.doublepoint=arg.doublepoint ;
		this.teamshare=arg.teamshare ;
		this.teamnum=arg.teamnum ;
		this.renxing=arg.renxing ;
		this.renxingfushi=arg.renxingfushi ;
		this.giveup=arg.giveup ;
		this.teamgiveup=arg.teamgiveup ;
		this.giveupreset=arg.giveupreset ;
		this.taskcnt=arg.taskcnt ;
		this.timeover=arg.timeover ;
		this.isopenaward=arg.isopenaward ;
		this.openawardid=arg.openawardid ;
		this.openawardnum=arg.openawardnum ;
		this.specialtask=arg.specialtask ;
		this.specialtaskrate=arg.specialtaskrate ;
		this.specialevent=arg.specialevent ;
		this.gjhuanshu=arg.gjhuanshu ;
		this.gjmsgid=arg.gjmsgid ;
		this.gjaward=arg.gjaward ;
		this.mlgjitemaward=arg.mlgjitemaward ;
		this.mldbitemaward=arg.mldbitemaward ;
		this.dbitemaward=arg.dbitemaward ;
		this.ptitemaward=arg.ptitemaward ;
		this.shareaward=arg.shareaward ;
		this.shengwangaward=arg.shengwangaward ;
		this.extraitemaward=arg.extraitemaward ;
		this.extraitemawardring=arg.extraitemawardring ;
		this.specialaward=arg.specialaward ;
		this.matchtype=arg.matchtype ;
		this.awardids=arg.awardids ;
		this.shareawardids=arg.shareawardids ;
		this.dbappendawardids=arg.dbappendawardids ;
		this.teamleaderaward=arg.teamleaderaward ;
		this.successtips=arg.successtips ;
		this.alreadytips=arg.alreadytips ;
		this.maxnumtips=arg.maxnumtips ;
		this.needguild=arg.needguild ;
		this.dayauto=arg.dayauto ;
		this.activeid=arg.activeid ;
		this.gaojiang1=arg.gaojiang1 ;
		this.gaojiang1quest=arg.gaojiang1quest ;
		this.gaojiang2=arg.gaojiang2 ;
		this.gaojiang2quest=arg.gaojiang2quest ;
		this.isoutrandom=arg.isoutrandom ;
	}
	public void checkValid(java.util.Map<String,java.util.Map<Integer,? extends Object> > objs){
	}
	/**
	 * 用于唯一标识一个任务id
	 */
	public int id  = 0  ;
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int v){
		this.id=v;
	}
	
	/**
	 * 循环类型,即师门、帮派、捉鬼之类
	 */
	public int type  = 0  ;
	
	public int getType(){
		return this.type;
	}
	
	public void setType(int v){
		this.type=v;
	}
	
	/**
	 * 确定一个等级组
	 */
	public int levelgroup  = 0  ;
	
	public int getLevelgroup(){
		return this.levelgroup;
	}
	
	public void setLevelgroup(int v){
		this.levelgroup=v;
	}
	
	/**
	 * 
	 */
	public int levelmin  = 0  ;
	
	public int getLevelmin(){
		return this.levelmin;
	}
	
	public void setLevelmin(int v){
		this.levelmin=v;
	}
	
	/**
	 * 
	 */
	public int levelmax  = 0  ;
	
	public int getLevelmax(){
		return this.levelmax;
	}
	
	public void setLevelmax(int v){
		this.levelmax=v;
	}
	
	/**
	 * 如果一个组里有好几个相同的等级段,则根据权重随机出一个
	 */
	public int levelrate  = 0  ;
	
	public int getLevelrate(){
		return this.levelrate;
	}
	
	public void setLevelrate(int v){
		this.levelrate=v;
	}
	
	/**
	 * 
	 */
	public int cycle  = 0  ;
	
	public int getCycle(){
		return this.cycle;
	}
	
	public void setCycle(int v){
		this.cycle=v;
	}
	
	/**
	 * 
	 */
	public int clearround  = 0  ;
	
	public int getClearround(){
		return this.clearround;
	}
	
	public void setClearround(int v){
		this.clearround=v;
	}
	
	/**
	 * 
	 */
	public int round  = 0  ;
	
	public int getRound(){
		return this.round;
	}
	
	public void setRound(int v){
		this.round=v;
	}
	
	/**
	 * 
	 */
	public int ring  = 0  ;
	
	public int getRing(){
		return this.ring;
	}
	
	public void setRing(int v){
		this.ring=v;
	}
	
	/**
	 * 
	 */
	public int totaltime  = 0  ;
	
	public int getTotaltime(){
		return this.totaltime;
	}
	
	public void setTotaltime(int v){
		this.totaltime=v;
	}
	
	/**
	 * 
	 */
	public int awardtime  = 0  ;
	
	public int getAwardtime(){
		return this.awardtime;
	}
	
	public void setAwardtime(int v){
		this.awardtime=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<String> questrate  ;
	
	public java.util.ArrayList<String> getQuestrate(){
		return this.questrate;
	}
	
	public void setQuestrate(java.util.ArrayList<String> v){
		this.questrate=v;
	}
	
	/**
	 * 
	 */
	public int doublepoint  = 0  ;
	
	public int getDoublepoint(){
		return this.doublepoint;
	}
	
	public void setDoublepoint(int v){
		this.doublepoint=v;
	}
	
	/**
	 * 
	 */
	public int teamshare  = 0  ;
	
	public int getTeamshare(){
		return this.teamshare;
	}
	
	public void setTeamshare(int v){
		this.teamshare=v;
	}
	
	/**
	 * 
	 */
	public int teamnum  = 0  ;
	
	public int getTeamnum(){
		return this.teamnum;
	}
	
	public void setTeamnum(int v){
		this.teamnum=v;
	}
	
	/**
	 * 
	 */
	public int renxing  = 0  ;
	
	public int getRenxing(){
		return this.renxing;
	}
	
	public void setRenxing(int v){
		this.renxing=v;
	}
	
	/**
	 * 
	 */
	public int renxingfushi  = 0  ;
	
	public int getRenxingfushi(){
		return this.renxingfushi;
	}
	
	public void setRenxingfushi(int v){
		this.renxingfushi=v;
	}
	
	/**
	 * 
	 */
	public int giveup  = 0  ;
	
	public int getGiveup(){
		return this.giveup;
	}
	
	public void setGiveup(int v){
		this.giveup=v;
	}
	
	/**
	 * 
	 */
	public int teamgiveup  = 0  ;
	
	public int getTeamgiveup(){
		return this.teamgiveup;
	}
	
	public void setTeamgiveup(int v){
		this.teamgiveup=v;
	}
	
	/**
	 * 
	 */
	public int giveupreset  = 0  ;
	
	public int getGiveupreset(){
		return this.giveupreset;
	}
	
	public void setGiveupreset(int v){
		this.giveupreset=v;
	}
	
	/**
	 * 
	 */
	public int taskcnt  = 0  ;
	
	public int getTaskcnt(){
		return this.taskcnt;
	}
	
	public void setTaskcnt(int v){
		this.taskcnt=v;
	}
	
	/**
	 * 
	 */
	public int timeover  = 0  ;
	
	public int getTimeover(){
		return this.timeover;
	}
	
	public void setTimeover(int v){
		this.timeover=v;
	}
	
	/**
	 * 
	 */
	public int isopenaward  = 0  ;
	
	public int getIsopenaward(){
		return this.isopenaward;
	}
	
	public void setIsopenaward(int v){
		this.isopenaward=v;
	}
	
	/**
	 * 
	 */
	public int openawardid  = 0  ;
	
	public int getOpenawardid(){
		return this.openawardid;
	}
	
	public void setOpenawardid(int v){
		this.openawardid=v;
	}
	
	/**
	 * 
	 */
	public int openawardnum  = 0  ;
	
	public int getOpenawardnum(){
		return this.openawardnum;
	}
	
	public void setOpenawardnum(int v){
		this.openawardnum=v;
	}
	
	/**
	 * 
	 */
	public int specialtask  = 0  ;
	
	public int getSpecialtask(){
		return this.specialtask;
	}
	
	public void setSpecialtask(int v){
		this.specialtask=v;
	}
	
	/**
	 * 
	 */
	public int specialtaskrate  = 0  ;
	
	public int getSpecialtaskrate(){
		return this.specialtaskrate;
	}
	
	public void setSpecialtaskrate(int v){
		this.specialtaskrate=v;
	}
	
	/**
	 * 
	 */
	public String specialevent  = null  ;
	
	public String getSpecialevent(){
		return this.specialevent;
	}
	
	public void setSpecialevent(String v){
		this.specialevent=v;
	}
	
	/**
	 * 
	 */
	public int gjhuanshu  = 0  ;
	
	public int getGjhuanshu(){
		return this.gjhuanshu;
	}
	
	public void setGjhuanshu(int v){
		this.gjhuanshu=v;
	}
	
	/**
	 * 
	 */
	public int gjmsgid  = 0  ;
	
	public int getGjmsgid(){
		return this.gjmsgid;
	}
	
	public void setGjmsgid(int v){
		this.gjmsgid=v;
	}
	
	/**
	 * 
	 */
	public int gjaward  = 0  ;
	
	public int getGjaward(){
		return this.gjaward;
	}
	
	public void setGjaward(int v){
		this.gjaward=v;
	}
	
	/**
	 * 
	 */
	public int mlgjitemaward  = 0  ;
	
	public int getMlgjitemaward(){
		return this.mlgjitemaward;
	}
	
	public void setMlgjitemaward(int v){
		this.mlgjitemaward=v;
	}
	
	/**
	 * 
	 */
	public int mldbitemaward  = 0  ;
	
	public int getMldbitemaward(){
		return this.mldbitemaward;
	}
	
	public void setMldbitemaward(int v){
		this.mldbitemaward=v;
	}
	
	/**
	 * 
	 */
	public int dbitemaward  = 0  ;
	
	public int getDbitemaward(){
		return this.dbitemaward;
	}
	
	public void setDbitemaward(int v){
		this.dbitemaward=v;
	}
	
	/**
	 * 
	 */
	public int ptitemaward  = 0  ;
	
	public int getPtitemaward(){
		return this.ptitemaward;
	}
	
	public void setPtitemaward(int v){
		this.ptitemaward=v;
	}
	
	/**
	 * 
	 */
	public int shareaward  = 0  ;
	
	public int getShareaward(){
		return this.shareaward;
	}
	
	public void setShareaward(int v){
		this.shareaward=v;
	}
	
	/**
	 * 
	 */
	public int shengwangaward  = 0  ;
	
	public int getShengwangaward(){
		return this.shengwangaward;
	}
	
	public void setShengwangaward(int v){
		this.shengwangaward=v;
	}
	
	/**
	 * 
	 */
	public int extraitemaward  = 0  ;
	
	public int getExtraitemaward(){
		return this.extraitemaward;
	}
	
	public void setExtraitemaward(int v){
		this.extraitemaward=v;
	}
	
	/**
	 * 
	 */
	public int extraitemawardring  = 0  ;
	
	public int getExtraitemawardring(){
		return this.extraitemawardring;
	}
	
	public void setExtraitemawardring(int v){
		this.extraitemawardring=v;
	}
	
	/**
	 * 
	 */
	public int specialaward  = 0  ;
	
	public int getSpecialaward(){
		return this.specialaward;
	}
	
	public void setSpecialaward(int v){
		this.specialaward=v;
	}
	
	/**
	 * 1 表示匹配个人等级 2 表示队伍平均等级
	 */
	public int matchtype  = 0  ;
	
	public int getMatchtype(){
		return this.matchtype;
	}
	
	public void setMatchtype(int v){
		this.matchtype=v;
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
	public String shareawardids  = null  ;
	
	public String getShareawardids(){
		return this.shareawardids;
	}
	
	public void setShareawardids(String v){
		this.shareawardids=v;
	}
	
	/**
	 * 
	 */
	public String dbappendawardids  = null  ;
	
	public String getDbappendawardids(){
		return this.dbappendawardids;
	}
	
	public void setDbappendawardids(String v){
		this.dbappendawardids=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<String> teamleaderaward  ;
	
	public java.util.ArrayList<String> getTeamleaderaward(){
		return this.teamleaderaward;
	}
	
	public void setTeamleaderaward(java.util.ArrayList<String> v){
		this.teamleaderaward=v;
	}
	
	/**
	 * 
	 */
	public int successtips  = 0  ;
	
	public int getSuccesstips(){
		return this.successtips;
	}
	
	public void setSuccesstips(int v){
		this.successtips=v;
	}
	
	/**
	 * 
	 */
	public int alreadytips  = 0  ;
	
	public int getAlreadytips(){
		return this.alreadytips;
	}
	
	public void setAlreadytips(int v){
		this.alreadytips=v;
	}
	
	/**
	 * 
	 */
	public int maxnumtips  = 0  ;
	
	public int getMaxnumtips(){
		return this.maxnumtips;
	}
	
	public void setMaxnumtips(int v){
		this.maxnumtips=v;
	}
	
	/**
	 * 
	 */
	public int needguild  = 0  ;
	
	public int getNeedguild(){
		return this.needguild;
	}
	
	public void setNeedguild(int v){
		this.needguild=v;
	}
	
	/**
	 * 
	 */
	public int dayauto  = 0  ;
	
	public int getDayauto(){
		return this.dayauto;
	}
	
	public void setDayauto(int v){
		this.dayauto=v;
	}
	
	/**
	 * 
	 */
	public int activeid  = 0  ;
	
	public int getActiveid(){
		return this.activeid;
	}
	
	public void setActiveid(int v){
		this.activeid=v;
	}
	
	/**
	 * 
	 */
	public int gaojiang1  = 0  ;
	
	public int getGaojiang1(){
		return this.gaojiang1;
	}
	
	public void setGaojiang1(int v){
		this.gaojiang1=v;
	}
	
	/**
	 * 
	 */
	public int gaojiang1quest  = 0  ;
	
	public int getGaojiang1quest(){
		return this.gaojiang1quest;
	}
	
	public void setGaojiang1quest(int v){
		this.gaojiang1quest=v;
	}
	
	/**
	 * 
	 */
	public int gaojiang2  = 0  ;
	
	public int getGaojiang2(){
		return this.gaojiang2;
	}
	
	public void setGaojiang2(int v){
		this.gaojiang2=v;
	}
	
	/**
	 * 
	 */
	public int gaojiang2quest  = 0  ;
	
	public int getGaojiang2quest(){
		return this.gaojiang2quest;
	}
	
	public void setGaojiang2quest(int v){
		this.gaojiang2quest=v;
	}
	
	/**
	 * 0调用，1不调用
	 */
	public int isoutrandom  = 0  ;
	
	public int getIsoutrandom(){
		return this.isoutrandom;
	}
	
	public void setIsoutrandom(int v){
		this.isoutrandom=v;
	}
	
	
};