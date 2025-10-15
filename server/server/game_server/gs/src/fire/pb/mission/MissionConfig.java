package fire.pb.mission;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fire.pb.map.DuplicateHelper;
import fire.pb.map.Transfer;
import fire.pb.mission.SSpecialScenarioQuestConfig;
import fire.pb.mission.fairyland.FairylandMgr;
import fire.pb.move.DynamicSceneType;
import fire.pb.move.SRoleEnterScene;
import fire.pb.scene.DynamicTemplate;
import fire.pb.scene.SceneManager;

public class MissionConfig {
	public static class AIInfo {
    	public final int aIID;
    	public final int battleResult; // 1为要求胜利,0为都可以
    	public final int teamState;
    	public AIInfo( final SMissionData missioninfo ) {
    		aIID = missioninfo.BattleAIID;
    		battleResult = missioninfo.BattleAIResult;
    		teamState = missioninfo.BattleAITeamSteate;
    	}
    }
	
	static class AnswerInfo {
    	public final int MAX_ANSWERS = 6;
    	public final int npcID;
    	public final int correctAnswerID;
    	public final String conversion;
    	public final int answersNum;
    	public final String[] answers = new String[MAX_ANSWERS];
    	public AnswerInfo( final SMissionData missioninfo ) {
    		correctAnswerID = 0;
    		npcID = missioninfo.AnswerNpcID;
    		conversion = missioninfo.AnswerConversion;
    		answers[correctAnswerID] = missioninfo.AnswerCorrect;
    		if (  missioninfo.AnswerWrongList != null ) {
	    		int i = 0, j = 0;
	    		for ( ; i < MAX_ANSWERS && 
	    			j < missioninfo.AnswerWrongList.size(); i++ ) {
	    			if ( i == correctAnswerID )
	    				continue;
	    			answers[i] = missioninfo.AnswerWrongList.get( j );
	    			j++;
	    		}
	    		answersNum = i;
    		} else {
    			answersNum = 0;
    		}
    	}
    }
	
	public static class BattleInfo {
    	public final int battleMapID;
    	public final int battleZoneID;
    	public final int drop;
    	public final int battleTimes;
    	public final List<Integer> monsterList;
    	public final int monsterNum;
    	public final int dropItemID;
    	public final int dropItemNum;
    	public final int battleVideo;
    	public BattleInfo( final SMissionData missioninfo ) {
    		battleMapID = missioninfo.BattleMapType;
    		battleZoneID = missioninfo.BattleZoneID;
    		drop = missioninfo.BattleDrop;
    		battleTimes = missioninfo.BattleTimes;
    		monsterList = missioninfo.BattleMonsterList;
    		monsterNum = missioninfo.BattleMonsterNum;
    		dropItemID = missioninfo.BattleDropItemID;
    		dropItemNum = missioninfo.BattleDropItemNum;
    		battleVideo = missioninfo.BattleVideo;
    	}
    }
	
	public static class ExeInfo {
    	public final int missionType;
    	public final int npcID;
    	public final int mapID;
    	public final int leftPos;
    	public final int topPos;
    	public final int rightPos;
    	public final int bottomPos;
    	public final int targetID;
    	public final int targetNum;
    	public final int miniStep;
    	public final int stepProbability;
    	public final int maxStep;
    	public final int teamState;  // 0 不可以,1可以,2必须 
    	public final int timelimit;
    	public final int transformID;
    	public final int followID;
    	public final int cruiseID;
    	public final long giveBackMoney;
    	public final int giveBackPetID;
    	public final int useItemID;
    	public final int hasOtherLimit;
    	public final int share;			//队伍共享
    	public final ArrayList<Integer> ignoreMaps = new ArrayList<Integer>();	//忽略地图，这些地图不遇暗雷怪
    	public final int unionSeekHelp;
    	public final int worldSeekHelp;
    	public final int helpBonus;
    	public ExeInfo(  final SMissionData missioninfo ) {
    		missionType = missioninfo.Type;
    		npcID = missioninfo.ExeNpcID;
    		mapID = missioninfo.ExeMapID;
    		leftPos = missioninfo.ExeLeftPos;
    		topPos = missioninfo.ExeTopPos;
    		rightPos = missioninfo.ExeRightPos;
    		bottomPos = missioninfo.ExeBottomPos;
    		targetID = missioninfo.ExeTargetID;
    		targetNum = missioninfo.ExeTargetNum;
    		miniStep = missioninfo.ExeMiniStep;
    		stepProbability = missioninfo.ExeStepProbability;
    		maxStep = missioninfo.ExeMaxStep;
    		teamState = missioninfo.ExeTeamState;
    		timelimit = missioninfo.ExeTimeLimit;
    		giveBackMoney =  missioninfo.ExeGiveBackMoney;
    		giveBackPetID = missioninfo.ExeGiveBackPetID;
    		useItemID = missioninfo.ExeUseItemID;
    		transformID = missioninfo.TransformID;
    		followID = missioninfo.FollowID;
    		cruiseID = missioninfo.CruiseId;
    		hasOtherLimit = missioninfo.ExeOtherType;
    		share = missioninfo.share;
    		if (missioninfo.BuShuaGuaiMaps != null)
    			ignoreMaps.addAll(missioninfo.BuShuaGuaiMaps);
    		unionSeekHelp = missioninfo.UnionSeekHelp;
    		worldSeekHelp = missioninfo.WorldSeekHelp;
    		helpBonus = missioninfo.HelpBonus;
    	}
    }
	private static class ExtConf {
		private SSpecialScenarioQuestConfig sconf;
		ExtConf(final int taskid, final Map<Integer, SSpecialScenarioQuestConfig> specailconfs) {
			sconf = specailconfs.get(taskid);
		}
	}
	static class LimitInfo {
    	public final java.util.Set<Integer> brotherMissions =
    		new java.util.HashSet<Integer>();
    	public final int minLevel;
    	public final int maxLevel;
    	public final int transMinLevel;
    	public final int transMaxLevel;
    	public final Set<Integer> requireMissionList = new HashSet<Integer>();
    	public final Set<Long> requireRoleIDList = new HashSet<Long>();
    	LimitInfo( final SMissionData missioninfo ) {
    		maxLevel = missioninfo.LevelMax;
    		minLevel = missioninfo.LevelMin;
    		transMaxLevel = missioninfo.LevelMaxTrans;
    		transMinLevel = missioninfo.LevelMinTrans;
    		if ( missioninfo.NeedMissionList != null )
    			requireMissionList.addAll( missioninfo.NeedMissionList );
    		if ( missioninfo.NeedRoleIDList != null )
    			requireRoleIDList.addAll( missioninfo.NeedRoleIDList );
    	}
    }
	static class MissionTaskInfo{}
	 
    static class RewardInfo {
    	public enum JUMPTYPE { 
    		NULL,
    		GOTO, 
    		FLY,
    		DREAMLAND
    		};
    	public final JUMPTYPE jtype;
    	public final int jumpmapID;
    	public final int jumpXpos;
    	public final int jumpYpos;
    	public final ArrayList<Integer> postMissionList = new ArrayList<Integer>();
    	public final ArrayList<Integer> displayNpcIDs = new ArrayList<Integer>();
    	public final long expReward;
    	public final long moneyReward;
    	public final long petExpReward;
    	public final long sMoney;
    	public final int rewardType;										//奖励类型1：道具 2：宠物
    	public final int rewardOption;										//奖励选择1：全部获取 2：选择后期
    	public final Set<ScenarioMissionRewardItem> rewardItemList = new HashSet<ScenarioMissionRewardItem>();
    	public final boolean hasNote;
    	public final int lilianSW;
    	public RewardInfo( final SMissionData missioninfo ) {
    		if ( missioninfo.DisplayNpc != null )
    			displayNpcIDs.addAll( missioninfo.DisplayNpc );
    		expReward = missioninfo.RewardExp;
    		moneyReward = missioninfo.RewardMoney;
    		petExpReward = missioninfo.RewardPetExp;
    		rewardType = missioninfo.RewardType;
    		rewardOption = missioninfo.RewardOption;
    		if ( missioninfo.NextMissionList != null )
    			postMissionList.addAll( missioninfo.NextMissionList );
    		if (missioninfo.RewardItemIDList != null)
	    		for ( int i = 0; i < missioninfo.RewardItemIDList.size(); i++ ) {
	    			boolean isBind = true;
	    			if (missioninfo.RewardItemIsBindList != null && i < missioninfo.RewardItemIsBindList.size()) {
	    				isBind = missioninfo.RewardItemIsBindList.get(i) == 1;
	    			}
	    			int needShape = 0;
	    			if (missioninfo.RewardItemShapeIDList != null && i < missioninfo.RewardItemShapeIDList.size()) {
	    				needShape = missioninfo.RewardItemShapeIDList.get(i);
	    			}
	    			rewardItemList.add(new ScenarioMissionRewardItem(missioninfo.RewardItemIDList.get(i),
	    							missioninfo.RewardItemNumList.get(i),
	    							isBind,
	    							needShape) );
	    		}
    		sMoney = missioninfo.SMoney;
    		switch ( missioninfo.RewardMapJumpType ) {
	    		case 0: jtype = JUMPTYPE.NULL; break;
	    		case 1: jtype = JUMPTYPE.GOTO; break;
	    		case 2: jtype = JUMPTYPE.FLY;  break;
	    		case 3: jtype = JUMPTYPE.DREAMLAND; break;
	    		default: throw new IllegalArgumentException("错误的跳转地图参数");
    		}
    		jumpmapID = missioninfo.RewardMapID;
    		jumpXpos = missioninfo.RewardMapXPos;
    		jumpYpos = missioninfo.RewardMapYPos;
    		hasNote = missioninfo.FinishConversationList!=null&&!missioninfo.FinishConversationList.isEmpty();
    		lilianSW = missioninfo.ShengWang;
    	}
    }
    public static class ScenarioInfo{
    	public final int animationID;
    	ScenarioInfo( final SMissionData missioninfo ) {
    		animationID = missioninfo.AnimationID;
    	}
    }
    static class ScenarioMissionRewardItem {
    	public int itemid;
    	public int itemnum;
    	public boolean isBind;
    	public int needShape;
    	ScenarioMissionRewardItem() {}
    	ScenarioMissionRewardItem(final int itemid, final int itemnum, final boolean isBind, final int needShape) {
    		this.itemid = itemid;
    		this.itemnum = itemnum;
    		this.isBind = isBind;
    		this.needShape = needShape;
    	}
    }
    public final int missionId;
    public final String missionName;
    public final String missionTypeString;
    public final LimitInfo limitInfo;
    public final ExeInfo exeIndo;
    public final RewardInfo rewardInfo;
    
    public final AnswerInfo answerInfo;
  
    public final MissionTaskInfo taskInfo = null;
    public final ScenarioInfo scenarioInfo;
    
    public final AIInfo aiInfo;
    
    public final BattleInfo battleInfo;
    public final ExtConf extconf;
    MissionConfig(final SMissionData missioninfo,
			final Map<Integer, SSpecialScenarioQuestConfig> specailconfs) {
		if (missioninfo == null)
			throw new RuntimeException("Element should not be null");
		missionId = missioninfo.id;
		if (missioninfo.Name != null)
			missionName = missioninfo.Name;
		else
			missionName = "";
		if (missioninfo.TypeStr != null)
			missionTypeString = missioninfo.TypeStr; // e.getAttribute( "MissionTypeString" );
		else 
			missionTypeString = "";
		limitInfo = new LimitInfo(missioninfo);
		rewardInfo = new RewardInfo(missioninfo);
		exeIndo = new ExeInfo(missioninfo);
		answerInfo = new AnswerInfo(missioninfo);
		aiInfo = new AIInfo(missioninfo);
		battleInfo = new BattleInfo(missioninfo);
		scenarioInfo = new ScenarioInfo(missioninfo);
		extconf = new ExtConf(missionId, specailconfs);
	}
    public void addShowing(final long roleid, final boolean isTeamLeader) {
    	mkdb.Procedure p = new mkdb.Procedure() {
    		@Override
    		protected boolean process() {
    			xbean.ShowingMission st = xtable.Showingmission.get(roleid);
    			if (st == null) {
    				st = xbean.Pod.newShowingMission();
    				xtable.Showingmission.insert(roleid, st);
    			}
    			st.setMissionid(missionId);
    			st.setIsleader(isTeamLeader);
    			return true;
    		}
    	};
    	if (mkdb.Transaction.current() == null) {
    		p.submit();
    	} else {
    		p.call();
    	}
    	
    }
    void afterGoto( final long roleid ) {
    	if (hasAni()) {
			if ( UtilHelper.getAnimeType(scenarioInfo.animationID)== UtilHelper.AnimeType.ANIMATION) 
				fire.pb.util.CGPlayUtil.sendStartCGProtocal( roleid, scenarioInfo.animationID );
		}
    	removeShowing(roleid);
    }
    void afterNote(final long roleid, final boolean isTeamLeader) {
    	if (hasGoto()) {
    		jump(roleid, isTeamLeader);
    	} else {
    		afterGoto(roleid);
    	}
    }

	public SSpecialScenarioQuestConfig getSpecialScenarioQuestConfig() {
		return extconf.sconf;
	}
	
	boolean hasAni() {
		return scenarioInfo.animationID !=0 && scenarioInfo.animationID!=-1;
	}
	boolean hasCG() {
		return UtilHelper.getAnimeType(scenarioInfo.animationID)== UtilHelper.AnimeType.ANIMATION;
	}
    
    boolean hasGoto() {
		return rewardInfo.jtype != MissionConfig.RewardInfo.JUMPTYPE.NULL;
	}
    
    boolean hasNote() {
		return rewardInfo.hasNote;
	}
    
    private void jump( final long roleid, final boolean isTeamLeader ) {
		final boolean hasCG = hasCG();
		if (hasCG) {
			addShowing(roleid, isTeamLeader);
		}
		if (!isTeamLeader)
			return;
		if (rewardInfo.jtype == MissionConfig.RewardInfo.JUMPTYPE.DREAMLAND ) {
			if ( UtilHelper.isFairylandMap(rewardInfo.jumpmapID ) ) {
				FairylandMgr.getInstance().enterIntoDreamLand(roleid, rewardInfo.jumpmapID, rewardInfo.jumpXpos, rewardInfo.jumpYpos, hasCG ,isTeamLeader);
			} else {
				FairylandMgr.getInstance().finishDreamLandTask(roleid,rewardInfo.jumpmapID,rewardInfo.jumpXpos,rewardInfo.jumpYpos, hasCG );
			}
			return;
		}
		
		if (!hasCG) {
			DynamicTemplate dt = SceneManager.getInstance().getDynamicTemplate(rewardInfo.jumpmapID);
			if (dt!=null){
				DuplicateHelper.enterDynamicSceneWhileCommit(roleid, rewardInfo.jumpmapID, rewardInfo.jumpXpos, rewardInfo.jumpYpos, roleid, dt.getName(), false, DynamicSceneType.NORMAL_SCENE, SRoleEnterScene.QUEST);
			}else{
				Transfer.justGotoWhileCommit(roleid, (long)rewardInfo.jumpmapID, rewardInfo.jumpXpos, rewardInfo.jumpYpos, SRoleEnterScene.QUEST_CG);
			}
			removeShowing( roleid );
		} else {	
			DynamicTemplate dt = SceneManager.getInstance().getDynamicTemplate(rewardInfo.jumpmapID);
			if (dt!=null){
				DuplicateHelper.enterDynamicSceneWhileCommit(roleid, rewardInfo.jumpmapID, rewardInfo.jumpXpos, rewardInfo.jumpYpos, roleid, dt.getName(), false, DynamicSceneType.NORMAL_SCENE, SRoleEnterScene.QUEST_CG);
			}else
			    Transfer.justGotoWhileCommit(roleid, (long)rewardInfo.jumpmapID, rewardInfo.jumpXpos, rewardInfo.jumpYpos, SRoleEnterScene.QUEST_CG);
			
		}
	}
    
    private void removeShowing(final long roleid) {
    	final mkdb.Procedure p = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				final xbean.ShowingMission showing = xtable.Showingmission.get( roleid );
				if ( showing != null && showing.getMissionid() == missionId )
					xtable.Showingmission.remove( roleid );
				return true;
			}
		};
		if ( mkdb.Transaction.current() == null ) {
			p.submit();
		} else {
			p.call();
		}
    }
}
