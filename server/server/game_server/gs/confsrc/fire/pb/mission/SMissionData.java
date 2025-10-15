package fire.pb.mission;


public class SMissionData implements mytools.ConvMain.Checkable ,Comparable<SMissionData>{

	public int compareTo(SMissionData o){
		return this.id-o.id;
	}

	
	
	static class NeedId extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	public SMissionData(){
		super();
	}
	public SMissionData(SMissionData arg){
		this.id=arg.id ;
		this.Name=arg.Name ;
		this.TypeStr=arg.TypeStr ;
		this.LevelMin=arg.LevelMin ;
		this.LevelMax=arg.LevelMax ;
		this.CruiseId=arg.CruiseId ;
		this.LevelMinTrans=arg.LevelMinTrans ;
		this.LevelMaxTrans=arg.LevelMaxTrans ;
		this.NeedMissionList=arg.NeedMissionList ;
		this.NeedRoleIDList=arg.NeedRoleIDList ;
		this.NextMissionList=arg.NextMissionList ;
		this.TransformID=arg.TransformID ;
		this.FollowID=arg.FollowID ;
		this.BuShuaGuaiMaps=arg.BuShuaGuaiMaps ;
		this.Note=arg.Note ;
		this.RewardExp=arg.RewardExp ;
		this.RewardMoney=arg.RewardMoney ;
		this.RewardPetExp=arg.RewardPetExp ;
		this.ShengWang=arg.ShengWang ;
		this.SMoney=arg.SMoney ;
		this.RewardMapJumpType=arg.RewardMapJumpType ;
		this.RewardMapID=arg.RewardMapID ;
		this.RewardMapXPos=arg.RewardMapXPos ;
		this.RewardMapYPos=arg.RewardMapYPos ;
		this.ProcessBarTime=arg.ProcessBarTime ;
		this.ProcessBarText=arg.ProcessBarText ;
		this.ProcessBarColor=arg.ProcessBarColor ;
		this.DisplayNpc=arg.DisplayNpc ;
		this.RewardType=arg.RewardType ;
		this.RewardOption=arg.RewardOption ;
		this.RewardItemIDList=arg.RewardItemIDList ;
		this.RewardItemNumList=arg.RewardItemNumList ;
		this.RewardItemShapeIDList=arg.RewardItemShapeIDList ;
		this.RewardItemIsBindList=arg.RewardItemIsBindList ;
		this.Type=arg.Type ;
		this.share=arg.share ;
		this.ExeNpcID=arg.ExeNpcID ;
		this.ExeMapID=arg.ExeMapID ;
		this.ExeLeftPos=arg.ExeLeftPos ;
		this.ExeTopPos=arg.ExeTopPos ;
		this.ExeRightPos=arg.ExeRightPos ;
		this.ExeBottomPos=arg.ExeBottomPos ;
		this.ExeTargetID=arg.ExeTargetID ;
		this.ExeTargetNum=arg.ExeTargetNum ;
		this.ExeMiniStep=arg.ExeMiniStep ;
		this.ExeStepProbability=arg.ExeStepProbability ;
		this.ExeMaxStep=arg.ExeMaxStep ;
		this.ExeTeamState=arg.ExeTeamState ;
		this.ExeTimeLimit=arg.ExeTimeLimit ;
		this.ExeIsRestartTimer=arg.ExeIsRestartTimer ;
		this.ExeGiveBackMoney=arg.ExeGiveBackMoney ;
		this.ExeGiveBackPetID=arg.ExeGiveBackPetID ;
		this.ExeUseItemID=arg.ExeUseItemID ;
		this.ExeOtherType=arg.ExeOtherType ;
		this.AnswerCorrect=arg.AnswerCorrect ;
		this.AnswerWrongList=arg.AnswerWrongList ;
		this.AnswerNpcID=arg.AnswerNpcID ;
		this.AnswerConversion=arg.AnswerConversion ;
		this.MissionDescriptionListA=arg.MissionDescriptionListA ;
		this.MissionPurposeListA=arg.MissionPurposeListA ;
		this.MissionTraceListA=arg.MissionTraceListA ;
		this.BattleAIID=arg.BattleAIID ;
		this.BattleAIResult=arg.BattleAIResult ;
		this.BattleAIDeathPunish=arg.BattleAIDeathPunish ;
		this.BattleAITeamSteate=arg.BattleAITeamSteate ;
		this.BattleAILevel=arg.BattleAILevel ;
		this.BattleMapType=arg.BattleMapType ;
		this.BattleZoneID=arg.BattleZoneID ;
		this.BattleDrop=arg.BattleDrop ;
		this.BattleTimes=arg.BattleTimes ;
		this.BattleMonsterList=arg.BattleMonsterList ;
		this.BattleMonsterNum=arg.BattleMonsterNum ;
		this.BattleDropItemID=arg.BattleDropItemID ;
		this.BattleDropItemNum=arg.BattleDropItemNum ;
		this.BattleVideo=arg.BattleVideo ;
		this.AnimationID=arg.AnimationID ;
		this.BranchNpcID=arg.BranchNpcID ;
		this.BranchNote=arg.BranchNote ;
		this.BranchOptionList=arg.BranchOptionList ;
		this.NpcConversationList=arg.NpcConversationList ;
		this.NpcID=arg.NpcID ;
		this.FinishConversationList=arg.FinishConversationList ;
		this.FinishNpcID=arg.FinishNpcID ;
		this.UnionSeekHelp=arg.UnionSeekHelp ;
		this.WorldSeekHelp=arg.WorldSeekHelp ;
		this.HelpBonus=arg.HelpBonus ;
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
	public String Name  = null  ;
	
	public String getName(){
		return this.Name;
	}
	
	public void setName(String v){
		this.Name=v;
	}
	
	/**
	 * 
	 */
	public String TypeStr  = null  ;
	
	public String getTypeStr(){
		return this.TypeStr;
	}
	
	public void setTypeStr(String v){
		this.TypeStr=v;
	}
	
	/**
	 * 
	 */
	public int LevelMin  = 0  ;
	
	public int getLevelMin(){
		return this.LevelMin;
	}
	
	public void setLevelMin(int v){
		this.LevelMin=v;
	}
	
	/**
	 * 
	 */
	public int LevelMax  = 0  ;
	
	public int getLevelMax(){
		return this.LevelMax;
	}
	
	public void setLevelMax(int v){
		this.LevelMax=v;
	}
	
	/**
	 * 
	 */
	public int CruiseId  = 0  ;
	
	public int getCruiseId(){
		return this.CruiseId;
	}
	
	public void setCruiseId(int v){
		this.CruiseId=v;
	}
	
	/**
	 * 
	 */
	public int LevelMinTrans  = 0  ;
	
	public int getLevelMinTrans(){
		return this.LevelMinTrans;
	}
	
	public void setLevelMinTrans(int v){
		this.LevelMinTrans=v;
	}
	
	/**
	 * 
	 */
	public int LevelMaxTrans  = 0  ;
	
	public int getLevelMaxTrans(){
		return this.LevelMaxTrans;
	}
	
	public void setLevelMaxTrans(int v){
		this.LevelMaxTrans=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> NeedMissionList  ;
	
	public java.util.ArrayList<Integer> getNeedMissionList(){
		return this.NeedMissionList;
	}
	
	public void setNeedMissionList(java.util.ArrayList<Integer> v){
		this.NeedMissionList=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Long> NeedRoleIDList  ;
	
	public java.util.ArrayList<Long> getNeedRoleIDList(){
		return this.NeedRoleIDList;
	}
	
	public void setNeedRoleIDList(java.util.ArrayList<Long> v){
		this.NeedRoleIDList=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> NextMissionList  ;
	
	public java.util.ArrayList<Integer> getNextMissionList(){
		return this.NextMissionList;
	}
	
	public void setNextMissionList(java.util.ArrayList<Integer> v){
		this.NextMissionList=v;
	}
	
	/**
	 * 
	 */
	public int TransformID  = 0  ;
	
	public int getTransformID(){
		return this.TransformID;
	}
	
	public void setTransformID(int v){
		this.TransformID=v;
	}
	
	/**
	 * 
	 */
	public int FollowID  = 0  ;
	
	public int getFollowID(){
		return this.FollowID;
	}
	
	public void setFollowID(int v){
		this.FollowID=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> BuShuaGuaiMaps  ;
	
	public java.util.ArrayList<Integer> getBuShuaGuaiMaps(){
		return this.BuShuaGuaiMaps;
	}
	
	public void setBuShuaGuaiMaps(java.util.ArrayList<Integer> v){
		this.BuShuaGuaiMaps=v;
	}
	
	/**
	 * 
	 */
	public String Note  = null  ;
	
	public String getNote(){
		return this.Note;
	}
	
	public void setNote(String v){
		this.Note=v;
	}
	
	/**
	 * 
	 */
	public long RewardExp  = 0L  ;
	
	public long getRewardExp(){
		return this.RewardExp;
	}
	
	public void setRewardExp(long v){
		this.RewardExp=v;
	}
	
	/**
	 * 
	 */
	public long RewardMoney  = 0L  ;
	
	public long getRewardMoney(){
		return this.RewardMoney;
	}
	
	public void setRewardMoney(long v){
		this.RewardMoney=v;
	}
	
	/**
	 * 
	 */
	public long RewardPetExp  = 0L  ;
	
	public long getRewardPetExp(){
		return this.RewardPetExp;
	}
	
	public void setRewardPetExp(long v){
		this.RewardPetExp=v;
	}
	
	/**
	 * 
	 */
	public int ShengWang  = 0  ;
	
	public int getShengWang(){
		return this.ShengWang;
	}
	
	public void setShengWang(int v){
		this.ShengWang=v;
	}
	
	/**
	 * 
	 */
	public long SMoney  = 0L  ;
	
	public long getSMoney(){
		return this.SMoney;
	}
	
	public void setSMoney(long v){
		this.SMoney=v;
	}
	
	/**
	 * 
	 */
	public int RewardMapJumpType  = 0  ;
	
	public int getRewardMapJumpType(){
		return this.RewardMapJumpType;
	}
	
	public void setRewardMapJumpType(int v){
		this.RewardMapJumpType=v;
	}
	
	/**
	 * 
	 */
	public int RewardMapID  = 0  ;
	
	public int getRewardMapID(){
		return this.RewardMapID;
	}
	
	public void setRewardMapID(int v){
		this.RewardMapID=v;
	}
	
	/**
	 * 
	 */
	public int RewardMapXPos  = 0  ;
	
	public int getRewardMapXPos(){
		return this.RewardMapXPos;
	}
	
	public void setRewardMapXPos(int v){
		this.RewardMapXPos=v;
	}
	
	/**
	 * 
	 */
	public int RewardMapYPos  = 0  ;
	
	public int getRewardMapYPos(){
		return this.RewardMapYPos;
	}
	
	public void setRewardMapYPos(int v){
		this.RewardMapYPos=v;
	}
	
	/**
	 * 
	 */
	public int ProcessBarTime  = 0  ;
	
	public int getProcessBarTime(){
		return this.ProcessBarTime;
	}
	
	public void setProcessBarTime(int v){
		this.ProcessBarTime=v;
	}
	
	/**
	 * 
	 */
	public String ProcessBarText  = null  ;
	
	public String getProcessBarText(){
		return this.ProcessBarText;
	}
	
	public void setProcessBarText(String v){
		this.ProcessBarText=v;
	}
	
	/**
	 * 
	 */
	public String ProcessBarColor  = null  ;
	
	public String getProcessBarColor(){
		return this.ProcessBarColor;
	}
	
	public void setProcessBarColor(String v){
		this.ProcessBarColor=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> DisplayNpc  ;
	
	public java.util.ArrayList<Integer> getDisplayNpc(){
		return this.DisplayNpc;
	}
	
	public void setDisplayNpc(java.util.ArrayList<Integer> v){
		this.DisplayNpc=v;
	}
	
	/**
	 * 
	 */
	public int RewardType  = 0  ;
	
	public int getRewardType(){
		return this.RewardType;
	}
	
	public void setRewardType(int v){
		this.RewardType=v;
	}
	
	/**
	 * 
	 */
	public int RewardOption  = 0  ;
	
	public int getRewardOption(){
		return this.RewardOption;
	}
	
	public void setRewardOption(int v){
		this.RewardOption=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> RewardItemIDList  ;
	
	public java.util.ArrayList<Integer> getRewardItemIDList(){
		return this.RewardItemIDList;
	}
	
	public void setRewardItemIDList(java.util.ArrayList<Integer> v){
		this.RewardItemIDList=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> RewardItemNumList  ;
	
	public java.util.ArrayList<Integer> getRewardItemNumList(){
		return this.RewardItemNumList;
	}
	
	public void setRewardItemNumList(java.util.ArrayList<Integer> v){
		this.RewardItemNumList=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> RewardItemShapeIDList  ;
	
	public java.util.ArrayList<Integer> getRewardItemShapeIDList(){
		return this.RewardItemShapeIDList;
	}
	
	public void setRewardItemShapeIDList(java.util.ArrayList<Integer> v){
		this.RewardItemShapeIDList=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> RewardItemIsBindList  ;
	
	public java.util.ArrayList<Integer> getRewardItemIsBindList(){
		return this.RewardItemIsBindList;
	}
	
	public void setRewardItemIsBindList(java.util.ArrayList<Integer> v){
		this.RewardItemIsBindList=v;
	}
	
	/**
	 * 
	 */
	public int Type  = 0  ;
	
	public int getType(){
		return this.Type;
	}
	
	public void setType(int v){
		this.Type=v;
	}
	
	/**
	 * 
	 */
	public int share  = 0  ;
	
	public int getShare(){
		return this.share;
	}
	
	public void setShare(int v){
		this.share=v;
	}
	
	/**
	 * 
	 */
	public int ExeNpcID  = 0  ;
	
	public int getExeNpcID(){
		return this.ExeNpcID;
	}
	
	public void setExeNpcID(int v){
		this.ExeNpcID=v;
	}
	
	/**
	 * 
	 */
	public int ExeMapID  = 0  ;
	
	public int getExeMapID(){
		return this.ExeMapID;
	}
	
	public void setExeMapID(int v){
		this.ExeMapID=v;
	}
	
	/**
	 * 
	 */
	public int ExeLeftPos  = 0  ;
	
	public int getExeLeftPos(){
		return this.ExeLeftPos;
	}
	
	public void setExeLeftPos(int v){
		this.ExeLeftPos=v;
	}
	
	/**
	 * 
	 */
	public int ExeTopPos  = 0  ;
	
	public int getExeTopPos(){
		return this.ExeTopPos;
	}
	
	public void setExeTopPos(int v){
		this.ExeTopPos=v;
	}
	
	/**
	 * 
	 */
	public int ExeRightPos  = 0  ;
	
	public int getExeRightPos(){
		return this.ExeRightPos;
	}
	
	public void setExeRightPos(int v){
		this.ExeRightPos=v;
	}
	
	/**
	 * 
	 */
	public int ExeBottomPos  = 0  ;
	
	public int getExeBottomPos(){
		return this.ExeBottomPos;
	}
	
	public void setExeBottomPos(int v){
		this.ExeBottomPos=v;
	}
	
	/**
	 * 
	 */
	public int ExeTargetID  = 0  ;
	
	public int getExeTargetID(){
		return this.ExeTargetID;
	}
	
	public void setExeTargetID(int v){
		this.ExeTargetID=v;
	}
	
	/**
	 * 
	 */
	public int ExeTargetNum  = 0  ;
	
	public int getExeTargetNum(){
		return this.ExeTargetNum;
	}
	
	public void setExeTargetNum(int v){
		this.ExeTargetNum=v;
	}
	
	/**
	 * 
	 */
	public int ExeMiniStep  = 0  ;
	
	public int getExeMiniStep(){
		return this.ExeMiniStep;
	}
	
	public void setExeMiniStep(int v){
		this.ExeMiniStep=v;
	}
	
	/**
	 * 
	 */
	public int ExeStepProbability  = 0  ;
	
	public int getExeStepProbability(){
		return this.ExeStepProbability;
	}
	
	public void setExeStepProbability(int v){
		this.ExeStepProbability=v;
	}
	
	/**
	 * 
	 */
	public int ExeMaxStep  = 0  ;
	
	public int getExeMaxStep(){
		return this.ExeMaxStep;
	}
	
	public void setExeMaxStep(int v){
		this.ExeMaxStep=v;
	}
	
	/**
	 * 
	 */
	public int ExeTeamState  = 0  ;
	
	public int getExeTeamState(){
		return this.ExeTeamState;
	}
	
	public void setExeTeamState(int v){
		this.ExeTeamState=v;
	}
	
	/**
	 * 
	 */
	public int ExeTimeLimit  = 0  ;
	
	public int getExeTimeLimit(){
		return this.ExeTimeLimit;
	}
	
	public void setExeTimeLimit(int v){
		this.ExeTimeLimit=v;
	}
	
	/**
	 * 
	 */
	public int ExeIsRestartTimer  = 0  ;
	
	public int getExeIsRestartTimer(){
		return this.ExeIsRestartTimer;
	}
	
	public void setExeIsRestartTimer(int v){
		this.ExeIsRestartTimer=v;
	}
	
	/**
	 * 
	 */
	public long ExeGiveBackMoney  = 0L  ;
	
	public long getExeGiveBackMoney(){
		return this.ExeGiveBackMoney;
	}
	
	public void setExeGiveBackMoney(long v){
		this.ExeGiveBackMoney=v;
	}
	
	/**
	 * 
	 */
	public int ExeGiveBackPetID  = 0  ;
	
	public int getExeGiveBackPetID(){
		return this.ExeGiveBackPetID;
	}
	
	public void setExeGiveBackPetID(int v){
		this.ExeGiveBackPetID=v;
	}
	
	/**
	 * 
	 */
	public int ExeUseItemID  = 0  ;
	
	public int getExeUseItemID(){
		return this.ExeUseItemID;
	}
	
	public void setExeUseItemID(int v){
		this.ExeUseItemID=v;
	}
	
	/**
	 * 
	 */
	public int ExeOtherType  = 0  ;
	
	public int getExeOtherType(){
		return this.ExeOtherType;
	}
	
	public void setExeOtherType(int v){
		this.ExeOtherType=v;
	}
	
	/**
	 * 
	 */
	public String AnswerCorrect  = null  ;
	
	public String getAnswerCorrect(){
		return this.AnswerCorrect;
	}
	
	public void setAnswerCorrect(String v){
		this.AnswerCorrect=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<String> AnswerWrongList  ;
	
	public java.util.ArrayList<String> getAnswerWrongList(){
		return this.AnswerWrongList;
	}
	
	public void setAnswerWrongList(java.util.ArrayList<String> v){
		this.AnswerWrongList=v;
	}
	
	/**
	 * 
	 */
	public int AnswerNpcID  = 0  ;
	
	public int getAnswerNpcID(){
		return this.AnswerNpcID;
	}
	
	public void setAnswerNpcID(int v){
		this.AnswerNpcID=v;
	}
	
	/**
	 * 
	 */
	public String AnswerConversion  = null  ;
	
	public String getAnswerConversion(){
		return this.AnswerConversion;
	}
	
	public void setAnswerConversion(String v){
		this.AnswerConversion=v;
	}
	
	/**
	 * 
	 */
	public String MissionDescriptionListA  = null  ;
	
	public String getMissionDescriptionListA(){
		return this.MissionDescriptionListA;
	}
	
	public void setMissionDescriptionListA(String v){
		this.MissionDescriptionListA=v;
	}
	
	/**
	 * 
	 */
	public String MissionPurposeListA  = null  ;
	
	public String getMissionPurposeListA(){
		return this.MissionPurposeListA;
	}
	
	public void setMissionPurposeListA(String v){
		this.MissionPurposeListA=v;
	}
	
	/**
	 * 
	 */
	public String MissionTraceListA  = null  ;
	
	public String getMissionTraceListA(){
		return this.MissionTraceListA;
	}
	
	public void setMissionTraceListA(String v){
		this.MissionTraceListA=v;
	}
	
	/**
	 * 
	 */
	public int BattleAIID  = 0  ;
	
	public int getBattleAIID(){
		return this.BattleAIID;
	}
	
	public void setBattleAIID(int v){
		this.BattleAIID=v;
	}
	
	/**
	 * 
	 */
	public int BattleAIResult  = 0  ;
	
	public int getBattleAIResult(){
		return this.BattleAIResult;
	}
	
	public void setBattleAIResult(int v){
		this.BattleAIResult=v;
	}
	
	/**
	 * 
	 */
	public int BattleAIDeathPunish  = 0  ;
	
	public int getBattleAIDeathPunish(){
		return this.BattleAIDeathPunish;
	}
	
	public void setBattleAIDeathPunish(int v){
		this.BattleAIDeathPunish=v;
	}
	
	/**
	 * 
	 */
	public int BattleAITeamSteate  = 0  ;
	
	public int getBattleAITeamSteate(){
		return this.BattleAITeamSteate;
	}
	
	public void setBattleAITeamSteate(int v){
		this.BattleAITeamSteate=v;
	}
	
	/**
	 * 
	 */
	public String BattleAILevel  = null  ;
	
	public String getBattleAILevel(){
		return this.BattleAILevel;
	}
	
	public void setBattleAILevel(String v){
		this.BattleAILevel=v;
	}
	
	/**
	 * 
	 */
	public int BattleMapType  = 0  ;
	
	public int getBattleMapType(){
		return this.BattleMapType;
	}
	
	public void setBattleMapType(int v){
		this.BattleMapType=v;
	}
	
	/**
	 * 
	 */
	public int BattleZoneID  = 0  ;
	
	public int getBattleZoneID(){
		return this.BattleZoneID;
	}
	
	public void setBattleZoneID(int v){
		this.BattleZoneID=v;
	}
	
	/**
	 * 
	 */
	public int BattleDrop  = 0  ;
	
	public int getBattleDrop(){
		return this.BattleDrop;
	}
	
	public void setBattleDrop(int v){
		this.BattleDrop=v;
	}
	
	/**
	 * 
	 */
	public int BattleTimes  = 0  ;
	
	public int getBattleTimes(){
		return this.BattleTimes;
	}
	
	public void setBattleTimes(int v){
		this.BattleTimes=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> BattleMonsterList  ;
	
	public java.util.ArrayList<Integer> getBattleMonsterList(){
		return this.BattleMonsterList;
	}
	
	public void setBattleMonsterList(java.util.ArrayList<Integer> v){
		this.BattleMonsterList=v;
	}
	
	/**
	 * 
	 */
	public int BattleMonsterNum  = 0  ;
	
	public int getBattleMonsterNum(){
		return this.BattleMonsterNum;
	}
	
	public void setBattleMonsterNum(int v){
		this.BattleMonsterNum=v;
	}
	
	/**
	 * 
	 */
	public int BattleDropItemID  = 0  ;
	
	public int getBattleDropItemID(){
		return this.BattleDropItemID;
	}
	
	public void setBattleDropItemID(int v){
		this.BattleDropItemID=v;
	}
	
	/**
	 * 
	 */
	public int BattleDropItemNum  = 0  ;
	
	public int getBattleDropItemNum(){
		return this.BattleDropItemNum;
	}
	
	public void setBattleDropItemNum(int v){
		this.BattleDropItemNum=v;
	}
	
	/**
	 * 
	 */
	public int BattleVideo  = 0  ;
	
	public int getBattleVideo(){
		return this.BattleVideo;
	}
	
	public void setBattleVideo(int v){
		this.BattleVideo=v;
	}
	
	/**
	 * 
	 */
	public int AnimationID  = 0  ;
	
	public int getAnimationID(){
		return this.AnimationID;
	}
	
	public void setAnimationID(int v){
		this.AnimationID=v;
	}
	
	/**
	 * 
	 */
	public int BranchNpcID  = 0  ;
	
	public int getBranchNpcID(){
		return this.BranchNpcID;
	}
	
	public void setBranchNpcID(int v){
		this.BranchNpcID=v;
	}
	
	/**
	 * 
	 */
	public String BranchNote  = null  ;
	
	public String getBranchNote(){
		return this.BranchNote;
	}
	
	public void setBranchNote(String v){
		this.BranchNote=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<String> BranchOptionList  ;
	
	public java.util.ArrayList<String> getBranchOptionList(){
		return this.BranchOptionList;
	}
	
	public void setBranchOptionList(java.util.ArrayList<String> v){
		this.BranchOptionList=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<String> NpcConversationList  ;
	
	public java.util.ArrayList<String> getNpcConversationList(){
		return this.NpcConversationList;
	}
	
	public void setNpcConversationList(java.util.ArrayList<String> v){
		this.NpcConversationList=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> NpcID  ;
	
	public java.util.ArrayList<Integer> getNpcID(){
		return this.NpcID;
	}
	
	public void setNpcID(java.util.ArrayList<Integer> v){
		this.NpcID=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<String> FinishConversationList  ;
	
	public java.util.ArrayList<String> getFinishConversationList(){
		return this.FinishConversationList;
	}
	
	public void setFinishConversationList(java.util.ArrayList<String> v){
		this.FinishConversationList=v;
	}
	
	/**
	 * 
	 */
	public java.util.ArrayList<Integer> FinishNpcID  ;
	
	public java.util.ArrayList<Integer> getFinishNpcID(){
		return this.FinishNpcID;
	}
	
	public void setFinishNpcID(java.util.ArrayList<Integer> v){
		this.FinishNpcID=v;
	}
	
	/**
	 * 
	 */
	public int UnionSeekHelp  = 0  ;
	
	public int getUnionSeekHelp(){
		return this.UnionSeekHelp;
	}
	
	public void setUnionSeekHelp(int v){
		this.UnionSeekHelp=v;
	}
	
	/**
	 * 
	 */
	public int WorldSeekHelp  = 0  ;
	
	public int getWorldSeekHelp(){
		return this.WorldSeekHelp;
	}
	
	public void setWorldSeekHelp(int v){
		this.WorldSeekHelp=v;
	}
	
	/**
	 * 
	 */
	public int HelpBonus  = 0  ;
	
	public int getHelpBonus(){
		return this.HelpBonus;
	}
	
	public void setHelpBonus(int v){
		this.HelpBonus=v;
	}
	
	
};