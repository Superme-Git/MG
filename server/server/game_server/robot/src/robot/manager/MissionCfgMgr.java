package robot.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fire.pb.mission.SMissionData;
import fire.pb.mission.SSpecialScenarioQuestConfig;

public class MissionCfgMgr {
	public static class MissionActiveInfo {
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
		public final int teamState; // 0 不可以,1可以,2必须
		public final int timelimit;
		public final int transformID;
		public final int followID;
		public final long giveBackMoney;
		public final int giveBackPetID;
		public final int useItemID;
		public final int hasOtherLimit;
		public final int share; // 队伍共享

		public MissionActiveInfo(final SMissionData missioninfo) {
			missionType = missioninfo.Type;
			npcID = missioninfo.ExeNpcID;
			mapID = missioninfo.ExeMapID;

			leftPos = missioninfo.ExeLeftPos;
			rightPos = missioninfo.ExeRightPos;
			topPos = missioninfo.ExeTopPos;
			bottomPos = missioninfo.ExeBottomPos;

			targetID = missioninfo.ExeTargetID;
			targetNum = missioninfo.ExeTargetNum;
			miniStep = missioninfo.ExeMiniStep;
			stepProbability = missioninfo.ExeStepProbability;
			maxStep = missioninfo.ExeMaxStep;
			teamState = missioninfo.ExeTeamState;
			timelimit = missioninfo.ExeTimeLimit;
			giveBackMoney = missioninfo.ExeGiveBackMoney;
			giveBackPetID = missioninfo.ExeGiveBackPetID;
			useItemID = missioninfo.ExeUseItemID;
			transformID = missioninfo.TransformID;
			followID = missioninfo.FollowID;
			hasOtherLimit = missioninfo.ExeOtherType;

			share = missioninfo.share;
		}
	}

	public static class MissionAIInfo {
		public final int aIID;
		public final int battleResult; // 1为要求胜利,0为都可以
		public final int teamState;

		public MissionAIInfo(final SMissionData missioninfo) {
			aIID = missioninfo.BattleAIID;
			battleResult = missioninfo.BattleAIResult;
			teamState = missioninfo.BattleAITeamSteate;
		}
	}

	public static class MissionBattleInfo {
		public final int battleMapID;
		public final int battleZoneID;
		public final int drop;
		public final int battleTimes;
		public final List<Integer> monsterList;
		public final int monsterNum;
		public final int dropItemID;
		public final int dropItemNum;

		public MissionBattleInfo(final SMissionData missioninfo) {
			battleMapID = missioninfo.BattleMapType;
			battleZoneID = missioninfo.BattleZoneID;
			drop = missioninfo.BattleDrop;
			battleTimes = missioninfo.BattleTimes;
			monsterList = missioninfo.BattleMonsterList;
			monsterNum = missioninfo.BattleMonsterNum;
			dropItemID = missioninfo.BattleDropItemID;
			dropItemNum = missioninfo.BattleDropItemNum;
		}
	}

	static class MissionLimitInfo {
		public final java.util.Set<Integer> brotherMissions = new java.util.HashSet<Integer>(); // 兄弟任务
		// public int preMissionID; // 前置任务ID
		public final int minLv; // 最低等级
		public final int maxLv; // 最高等级
		public final int transMinLv; // 最低转生等级
		public final int transMaxLv; // 最高转生等级
		public final Set<Integer> requiredMissionList = new HashSet<Integer>(); // 任务需求
		public final Set<Long> requiredRoleIDList = new HashSet<Long>(); // 人物需求

		MissionLimitInfo(final SMissionData missioninfo) {
			maxLv = missioninfo.LevelMax;
			minLv = missioninfo.LevelMin;
			transMaxLv = missioninfo.LevelMaxTrans;
			transMinLv = missioninfo.LevelMinTrans;
			if (missioninfo.NeedMissionList != null)
				requiredMissionList.addAll(missioninfo.NeedMissionList);
			if (missioninfo.NeedRoleIDList != null)
				requiredRoleIDList.addAll(missioninfo.NeedRoleIDList);
		}
	}

	static class MissionQuestionInfo {
		public final int MAX_ANSWERS_NUM = 6; // 最大答案数量
		public final int npcID;
		public final int answerID;// QuestionInfo
		public final String conversion;
		public final int answersNum;
		public final String[] answers = new String[MAX_ANSWERS_NUM];

		public MissionQuestionInfo(final SMissionData missioninfo) {
			answerID = 0;
			npcID = missioninfo.AnswerNpcID;
			conversion = missioninfo.AnswerConversion;
			answers[answerID] = missioninfo.AnswerCorrect;
			if (missioninfo.AnswerWrongList != null) {
				int i = 0, j = 0;
				for (; i < MAX_ANSWERS_NUM
						&& j < missioninfo.AnswerWrongList.size(); i++) {
					if (i == answerID)
						continue;
					answers[i] = missioninfo.AnswerWrongList.get(j);
					j++;
				}
				answersNum = i;
			} else {
				answersNum = 0;
			}
		}
	}

	static class MissionRewardInfo {
		public enum JUMP_TYPE {
			NULL, // 不跳转
			GOTO, FLY, DREAMLAND
		};

		public final JUMP_TYPE jumpType;
		public final int jumpmapID;
		public final int jumpXpos;
		public final int jumpYpos;
		public final ArrayList<Integer> postMissionList = new ArrayList<Integer>(); // 后置任务
		public final ArrayList<Integer> displayNpcIDs = new ArrayList<Integer>(); // 显示npcID
		public final long expReward; // 经验奖励
		public final long moneyReward; // 金钱奖励
		public final long petExpReward; // 宠物经验奖励
		public final long sMoney; // 储备金
		public final int rewardType; // 奖励类型1：道具 2：宠物
		public final int rewardOption; // 奖励选择1：全部获取 2：选择后期
		public final Set<ScenarioMissionRewardItem> rewardItemList = new HashSet<ScenarioMissionRewardItem>();// 奖励物品
		public final boolean hasNote; // 是否有对话
		public final int lilianSW; // 历练声望奖励

		public MissionRewardInfo(final SMissionData missioninfo) {
			if (missioninfo.DisplayNpc != null)
				displayNpcIDs.addAll(missioninfo.DisplayNpc);
			expReward = missioninfo.RewardExp;
			moneyReward = missioninfo.RewardMoney;
			petExpReward = missioninfo.RewardPetExp;
			rewardType = missioninfo.RewardType;
			rewardOption = missioninfo.RewardOption;
			if (missioninfo.NextMissionList != null)
				postMissionList.addAll(missioninfo.NextMissionList);
			if (missioninfo.RewardItemIDList != null) {
				for (int i = 0; i < missioninfo.RewardItemIDList.size(); i++) {
					boolean isBind = true;
					if (missioninfo.RewardItemIsBindList != null
							&& i < missioninfo.RewardItemIsBindList.size()) {
						isBind = missioninfo.RewardItemIsBindList.get(i) == 1;
					}
					int needShape = 0;
					if (missioninfo.RewardItemShapeIDList != null
							&& i < missioninfo.RewardItemShapeIDList.size()) {
						needShape = missioninfo.RewardItemShapeIDList.get(i);
					}
					rewardItemList.add(new ScenarioMissionRewardItem(
							missioninfo.RewardItemIDList.get(i),
							missioninfo.RewardItemNumList.get(i), isBind,
							needShape));
				}
			}
			sMoney = missioninfo.SMoney;
			switch (missioninfo.RewardMapJumpType) {
			case 0:
				jumpType = JUMP_TYPE.NULL;
				break;
			case 1:
				jumpType = JUMP_TYPE.GOTO;
				break;
			case 2:
				jumpType = JUMP_TYPE.FLY;
				break;
			case 3:
				jumpType = JUMP_TYPE.DREAMLAND;
				break;
			default:
				throw new IllegalArgumentException("错误的跳转地图参数");
			}
			jumpmapID = missioninfo.RewardMapID;
			jumpXpos = missioninfo.RewardMapXPos;
			jumpYpos = missioninfo.RewardMapYPos;
			hasNote = missioninfo.FinishConversationList != null
					&& !missioninfo.FinishConversationList
							.isEmpty();
			lilianSW = missioninfo.ShengWang;
		}
	}

	public static class MissionScenarioInfo {
		public final int animationID;

		MissionScenarioInfo(final SMissionData missioninfo) {
			animationID = missioninfo.AnimationID;
		}
	}

	static class MissionTaskInfo {
	}

	static class ScenarioMissionRewardItem {
		public int itemid;
		public int itemnum;
		public boolean isBind;
		public int needShape;

		ScenarioMissionRewardItem() {
		}

		ScenarioMissionRewardItem(final int itemid, final int itemnum,
				final boolean isBind, final int needShape) {
			this.itemid = itemid;
			this.itemnum = itemnum;
			this.isBind = isBind;
			this.needShape = needShape;
		}
	}

	public final int missionId;
	public final String missionName;
	public final String missionTypeString;

	// 任务限定需求设定
	public final MissionLimitInfo missionLimitedInfo;
	// 任务触发方式设定
	public final MissionActiveInfo missionActiveInfo;
	// 任务奖励
	public final MissionRewardInfo missionRewardsInfo;
	// 答题
	public final MissionQuestionInfo questionMissionInfo;
	// 任务目的信息
	public final MissionTaskInfo missionTargetInfo = null;
	// 任务剧情对话信息
	public final MissionScenarioInfo zhuxianMissionInfo;
	// AI 编辑器
	public final MissionAIInfo aiInfo;
	// 战斗区域编辑器
	public final MissionBattleInfo battleInfo;

	MissionCfgMgr(final SMissionData missioninfo,
			final Map<Integer, SSpecialScenarioQuestConfig> specailconfs) {

		if (missioninfo == null)
			throw new RuntimeException("任务信息为空");

		missionId = missioninfo.id;
		if (missioninfo.Name != null) {
			missionName = missioninfo.Name;
		} else {
			missionName = "";
		}

		if (missioninfo.TypeStr != null) {
			missionTypeString = missioninfo.TypeStr;
		} else {
			missionTypeString = "";
		}

		missionLimitedInfo = new MissionLimitInfo(missioninfo);
		missionRewardsInfo = new MissionRewardInfo(missioninfo);
		missionActiveInfo = new MissionActiveInfo(missioninfo);
		questionMissionInfo = new MissionQuestionInfo(missioninfo);
		aiInfo = new MissionAIInfo(missioninfo);
		battleInfo = new MissionBattleInfo(missioninfo);
		zhuxianMissionInfo = new MissionScenarioInfo(missioninfo);
	}

	boolean hasAni() {
		return zhuxianMissionInfo.animationID != 0
				&& zhuxianMissionInfo.animationID != -1;
	}

	boolean isHasGoto() {
		return missionRewardsInfo.jumpType != MissionCfgMgr.MissionRewardInfo.JUMP_TYPE.NULL;
	}

	boolean isHasNote() {
		return missionRewardsInfo.hasNote;
	}
}
