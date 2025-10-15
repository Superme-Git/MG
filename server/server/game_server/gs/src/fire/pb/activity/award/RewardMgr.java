package fire.pb.activity.award;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.script.ScriptException;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.HelpCountManage;
import fire.pb.PAddClanPointProc;
import fire.pb.PAddExpProc;
import fire.pb.PLevelUpProc;
import fire.pb.PropRole;
import fire.pb.SSendServerMultiExp;
import fire.pb.battle.BattleEndHandler;
import fire.pb.game.ActivityAward;
import fire.pb.game.MoneyType;
import fire.pb.item.AddItemResult;
import fire.pb.item.BagTypes;
import fire.pb.item.EquipItem;
import fire.pb.item.ItemMaps;
import fire.pb.item.ItemShuXing;
import fire.pb.item.Module;
import fire.pb.item.make.ItemMakeFactory;
import fire.pb.main.ConfigManager;
import fire.pb.map.SActivityAwardItems;
import fire.pb.map.SMineArea;
import fire.pb.map.SShowAreainfo;
import fire.pb.map.SceneManager;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.pet.PAddPetExpProc;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.role.NextExpAndResMoney;
import fire.pb.talk.MessageMgr;
import fire.pb.talk.STransChatMessageNotify2Client;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.BagUtil;
import fire.pb.util.HighQualityRandom;
import fire.pb.util.MessageUtil;
import fire.pb.util.Misc;
import fire.script.AwardJSEngine;
import fire.script.JavaScript;
import fire.script.ParseStringExpression;

import org.apache.log4j.Logger;

public class RewardMgr {

	public class MsgInfos {

		final int sendToTeam;

		final int msgid;

		final String msgitemid;

		final String msg2itemid;

		final int msg2id;

		final int msg2type;

		private MsgInfos(String msgitemid, int msgid, int sendToTeam,
				String msg2itemid, int msg2id, int msg2type) {

			this.msgitemid = msgitemid;
			this.msgid = msgid;
			this.sendToTeam = sendToTeam;
			this.msg2itemid = msg2itemid;
			this.msg2id = msg2id;
			this.msg2type = msg2type;
		}

		public int getSendToTeam() {
			return sendToTeam;
		}

		public int getMsgid() {
			return msgid;
		}

		public String getMsgitemid() {
			return msgitemid;
		}

		public String getMsg2itemid() {
			return msg2itemid;
		}

		public int getMsg2id() {
			return msg2id;
		}

		public int getMsg2type() {
			return msg2type;
		}

	}

	private static Random random2 = new HighQualityRandom();

	private static Random random3 = new HighQualityRandom();

	private static Random random4 = new HighQualityRandom();

	public static final String MODULE_NAME = "award";

	public static Logger logger = Logger.getLogger("AWARD");

	public static final int FIRSTC_AWARD = 1; // 必给物品奖励
	public static final int SECONDC_AWARD = 2; // 随机物品奖励
	public static final int MONEY_AWARD = 3; // 金钱奖励
	public static final int EXP_AWARD = 5; // 经验奖励
	public static final int PETEXP_AWARD = 6; // 宠物经验奖励
	public static final int FESCREDIT_AWARD = 9; // 节日积分奖励
	public static final int FACTION_CONTRI = 10; // 帮贡奖励
	public static final int GOLD_AWARD = 11; // 金币奖励
	public static final int CONTRIBUTION_AWARD = 12; // 职业(师门)贡献
	public static final int FACTION_MONEY = 13; // 公会资金
	public static final int FACTION_BONUS = 14; // 公会分红
	public static final int SHENGWANG = 15; // 声望
	public static final int EXP_ADD_AWARD = 16; // 经验等级加成或衰减
	public static final int EXP_YF_AWARD = 17; // 盈福经验
	public static final String TEAM_LEVEL = "TeamLv"; // 队伍平均等级
	public static final String TEAM_NUM = "TeamNum"; // 队伍人数
	public static final String ROLE_LEVEL = "RoleLv"; // 角色等级
	public static final String TIME = "Time"; // 次数
	public static final String RING = "Ring"; // 环数
	public static final String PET_LEVEL = "PetLv"; // 宠物等级
	public static final String CUR_EXP = "CurExp"; // 当前经验
	public static final String MAP_LEVEL = "MapLv"; // 地图等级(暂时没用)
	public static final String MONSTER_NUM = "MonsterNum"; // 怪物数量
	public static final String MASTER_NUM = "MasterNum"; // 主怪数量(暂时没用)
	public static final String MAIN_MONSTER_LEVEL = "MonsterLv"; // 怪物等级
	public static final String IS_NO_MULTI_EXP = "IsNoMul"; // 是否允许有多倍经验
	public static final String IS_DOUBLE_EXP = "IsDouble"; // 是否有双倍经验
	public static final String IS_SYS_TRIPLE_EXP = "IsSTrible"; // 是否有系统三倍经验
	public static final String IS_TRIPLE_EXP = "IsTrible"; // 是否有三倍经验
	public static final String IS_LEADER = "IsTL"; // 是否在队长
	public static final String IS_PET_TRIBLE = "IsPetTrible"; // 宠物是否有三倍经验
	public static final String SCORE = "Score"; // 得分
	public static final String SAVE_ID = "Saveid"; // 进度id
	public static final String BINGFENG_DISCOUNT = "Discount"; // 折算率
	public static final String RANK = "Rank"; // 排行榜中的排名
	public static final String MAX_ROLE_LV = "MaxLv"; // 最大角色等级
	public static final String MIN_ROLE_LV = "MinLv"; // 最小角色等级
	public static final String IWEB_MULTI_EXP = "IsSerMul"; // 是否有多倍经验
	public static final String BROTHER_NUM = "BNum"; // 兄弟数量,暂时都为0
	public static final String VIP_LV = "vipLv"; // vip等级
	public static final String DKTRIBLE = "DkTrible"; // 阵营的倍数
	public static final String TJTRIBLE = "TjTrible"; // 阵营的倍数
	public static final String MERGE_SERVER = "MergeServer"; // 是否合服
	public static final String STDEXP = "StdExp"; // 基准经验
	public static final String STDMONEY = "StdMoney"; // 基准金钱
	public static final String ISDBPOINT = "IsDbPoint"; // 是否开启双倍点数
	public static final String TASKLV = "TaskLv"; // x循环任务配置表.xlsx 任务难度
	public static final String SWXS = "SwXs"; // 声望系数
	public static final String YINGFU = "YingFu"; // 盈福经验
	public static final String FUBEN_LEVEL = "FuBenLv"; // 副本进入等级
	public static final String ANSWER_RIGHT_TIMES = "AnswerCnt"; // 答题正确次数
	public static final String PVP_TIMES = "PVPCnt"; // 竞技场连胜次数
	public static final String PVP_TARGET_TIMES = "PVPTargetCnt"; // 竞技场对手连胜次数
	public static final String FUBEN_ID = "FuBenId"; // 副本id

	public static int iwebMultiExp = 0;// 默认是0，即没有双倍或多倍

	public static int dkTribleExp = 0;// 默认是0，即没有双倍或多倍

	public static int tjTribleExp = 0;// 默认是0，即没有双倍或多倍

	public static long mergeServerStartTime;
	public static long mergeServerEndTime;

	private int baseMsgid = 141682;

	// 奖励一级表的原始数据
	private java.util.Map<Integer, ActivityAward> award1Map = new HashMap<Integer, ActivityAward>();

	// 奖励二级表的原始数据
	private java.util.NavigableMap<Integer, SActivityAwardItems> award2Map = null;

	// 转换后的奖励:必给奖励
	private java.util.Map<Integer, List<FirstClassAwardItem>> firstCAwardMap = new HashMap<Integer, List<FirstClassAwardItem>>();

	// 转换后的奖励:随机奖励
	private java.util.Map<Integer, SecondClassAwardItems> secondCAwardMap = new HashMap<Integer, SecondClassAwardItems>();

	// 转换后的奖励:金钱奖励
	private java.util.Map<Integer, JavaScript> moneyAwardMap = new HashMap<Integer, JavaScript>();
	
	// 转换后的奖励:金钱奖励
	private java.util.Map<Integer, JavaScript> goldAwardMap = new HashMap<Integer, JavaScript>();
	
	//信用值比例
	private java.util.Map<Integer, Integer> creditBili = new HashMap<Integer, Integer>();
	//信用值
	private java.util.Map<Integer, JavaScript> creditMap = new HashMap<Integer, JavaScript>();

	// 转换后的奖励:贡献奖励
	private java.util.Map<Integer, JavaScript> contributionAwardMap = new HashMap<Integer, JavaScript>();

	// 转换后的奖励:经验奖励
	private java.util.Map<Integer, JavaScript> expAwardMap = new HashMap<Integer, JavaScript>();

	// 转换后的奖励：节日积分奖励
	private java.util.Map<Integer, JavaScript> fesCreditAwardMap = new HashMap<Integer, JavaScript>();

	// 转换后的奖励：节日帮贡
	private java.util.Map<Integer, JavaScript> faction_crontri = new HashMap<Integer, JavaScript>();

	// 转换后的奖励：公会任务贡献资金
	private java.util.Map<Integer, JavaScript> factionTask_crontri = new HashMap<Integer, JavaScript>();

	// 转换后的奖励：公会任务个人分红
	private java.util.Map<Integer, JavaScript> factionTaskProfit_crontri = new HashMap<Integer, JavaScript>();

	// 转换后的奖励：公会任务个人分红
	private java.util.Map<Integer, JavaScript> shengWangAwardMap = new HashMap<Integer, JavaScript>();

	// 转换后的奖励:宠物经验奖励
	private java.util.Map<Integer, JavaScript> petExpAwardMap = new HashMap<Integer, JavaScript>();

	// 特定时间的额外奖励
	private java.util.Map<Integer, ExAward> externalAwardMap = null;

	// 给予的物品是否绑定
	private java.util.Map<Integer, Boolean> bindMap = null;

	// 如果背包栏满了是否放入临时背包
	private java.util.Map<Integer, Boolean> tempBagAvailableMap = null;

	// 给予的物品是否需要发消息
	private java.util.Map<Integer, MsgInfos> msgInfosMap = null;

	// 是否会触发盈福经验
	private java.util.Map<Integer, Integer> yingFuExpMap = new HashMap<Integer, Integer>();

	// 是否需要清上限
	private java.util.Map<Integer, Set<Integer>> clearLimitMap = new ConcurrentHashMap<Integer, Set<Integer>>();

	// 次数概率 by changhao
	private java.util.Map<Integer, ArrayList<Integer>> timesProb = new HashMap<Integer, ArrayList<Integer>>();

	private java.util.Map<Integer, Integer> isShared = new HashMap<Integer, Integer>();
	//服务器等级经验比例
	private java.util.Map<Integer, ExpBili> expBili = new HashMap<Integer, ExpBili>();
	
	private java.util.Map<Integer, Integer> shengwangTips = new HashMap<Integer, Integer>();

	public static final RewardMgr instance = new RewardMgr();

	private RewardMgr() {

	}

	public static RewardMgr getInstance() {

		return instance;
	}

	private void initData() throws Exception {
		if (!new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				
				if (fire.pb.fushi.Module.GetPayServiceType() == 0) { //普通服
					award1Map.putAll(ConfigManager.getInstance().getConf(
						fire.pb.game.SActivityAward.class));
				}
				else {
					award1Map.putAll(ConfigManager.getInstance().getConf(
							fire.pb.game.SPointCardActivityAward.class));
				}

				award2Map = ConfigManager.getInstance().getConf(
						fire.pb.map.SActivityAwardItems.class);

				externalAwardMap = new HashMap<Integer, ExAward>();

				// 给予的物品是否绑定
				bindMap = new HashMap<Integer, Boolean>();

				// 如果背包栏满了是否放入临时背包
				tempBagAvailableMap = new HashMap<Integer, Boolean>();

				// 给予的物品是否需要发消息
				msgInfosMap = new HashMap<Integer, MsgInfos>();

				firstCAwardMap.clear();

				secondCAwardMap.clear();

				moneyAwardMap.clear();
				goldAwardMap.clear();
				creditBili.clear();
				contributionAwardMap.clear();
				expAwardMap.clear();
				fesCreditAwardMap.clear();
				petExpAwardMap.clear();
				clearLimitMap.clear();
				faction_crontri.clear();
				factionTask_crontri.clear();
				factionTaskProfit_crontri.clear();
				shengWangAwardMap.clear();
				yingFuExpMap.clear();
				shengwangTips.clear();

				for (ActivityAward award : award1Map.values()) {
					int id = award.getId();

					// 建立次数概率数据 by changhao
					ArrayList<Integer> times = new ArrayList<Integer>();
					times.add(award.getDrop1timesprob());
					times.add(award.getDrop1timesprob()
							+ award.getDrop2timesprob());
					times.add(award.getDrop1timesprob()
							+ award.getDrop2timesprob()
							+ award.getDrop3timesprob());

					timesProb.put(id, times);

					isShared.put(id, award.getShared());

					if (award.getBind() == 1)
						bindMap.put(id, true);
					else
						bindMap.put(id, false);
					MsgInfos msgInfos = new MsgInfos(award.getMsgitemid(),
							award.getMsgid(), award.getTeammsg(),
							award.getMsg2itemid(), award.getMsg2id(),
							award.getMsg2type());
					msgInfosMap.put(id, msgInfos);
					if (award.getTempBag() == 1)
						tempBagAvailableMap.put(id, true);
					else
						tempBagAvailableMap.put(id, false);

					// 随机物品的分母
					List<Integer> fcAwardRawData = award.getFirstClassAward();
					List<Integer> scAwardRawData = award.getSecondClassAward();
					List<String> scAwardRawDataProbs = award
							.getSecondClassAwardProb();
					List<FirstClassAwardItem> items = new ArrayList<FirstClassAwardItem>();
					List<SecondClassAwardItem> sItems = new ArrayList<SecondClassAwardItem>();
					int i = 0;
					if (fcAwardRawData != null) {
						while (i < fcAwardRawData.size()) {
							// 每四个int构成一个必给的奖励物品,但是如果第一个int是0，则跳过
							if (fcAwardRawData.get(i) == 0) {
								i = i + 4;
								continue;
							} else {
								try {
									int itemID = fcAwardRawData.get(i);
									int itemNum = fcAwardRawData.get(i + 1);
									int itemProp = fcAwardRawData.get(i + 2);
									int itemPropValue = fcAwardRawData
											.get(i + 3);
									items.add(new FirstClassAwardItem(itemID,
											itemNum, itemProp, itemPropValue));
									i = i + 4;
								} catch (RuntimeException e) {
									throw new RuntimeException(
											"init first class award table failed,awardid:"
													+ id, e);
								}
							}
						}
						firstCAwardMap.put(id, items);
					}
					if (scAwardRawData != null) {
						SecondClassAwardItems scClassAwardItems = new SecondClassAwardItems();
						scClassAwardItems.setBase(award
								.getSecondClassDenominator());
						scClassAwardItems.setRandomType(award.getRandomType());
						scClassAwardItems.setTotalProb(award.getTotalProb());
						int j = 0;
						while (j < scAwardRawData.size()) {
							// 每三个int构成一个随机的奖励物品,但是如果第一个int是0，则跳过
							if (scAwardRawData.get(j) == 0) {
								j = j + 2;
								continue;
							} else {
								try {
									int itemID = scAwardRawData.get(j);
									int itemNum = scAwardRawData.get(j + 1);
									String itemRatio = scAwardRawDataProbs
											.get(j / 2);

									sItems.add(new SecondClassAwardItem(itemID,
											itemNum, itemRatio));
									j = j + 2;
								} catch (RuntimeException e) {
									throw new RuntimeException(
											"init second class award table failed,awardid:"
													+ id, e);
								}
							}
						}
						scClassAwardItems.setItems(sItems);
						secondCAwardMap.put(id, scClassAwardItems);
					}
					if (award.getMoney() != null)
						moneyAwardMap.put(id, new JavaScript(award.getMoney()));
					if (award.getGold() != null) {
						goldAwardMap.put(id, new JavaScript(award.getGold()));
					}
					
					if (award.getCredit() != 0) {
						creditBili.put(id, award.getCredit());
					}
					if (award.getCreditValue() != null) {
						creditMap.put(id, new JavaScript(award.getCreditValue()));
					}
					if (award.getSchoolContribute() != null) {
						contributionAwardMap.put(id,
								new JavaScript(award.getSchoolContribute()));
					}
					if (award.getExp() != null)
						expAwardMap.put(id, new JavaScript(award.getExp()));
					if (award.getFestivalCredit() != null) {
						fesCreditAwardMap.put(id,
								new JavaScript(award.getFestivalCredit()));
					}

					if (award.getGangContribution() != null) {
						faction_crontri.put(id,
								new JavaScript(award.getGangContribution()));
					}

					if (award.getGangTaskContribution() != null) {
						factionTask_crontri.put(id,	new JavaScript(award.getGangTaskContribution()));
					}

					if (award.getGangTaskProfit() != null) {
						factionTaskProfit_crontri.put(id,
								new JavaScript(award.getGangTaskProfit()));
					}

					if (award.getShengwang() != null) {
						shengWangAwardMap.put(id,
								new JavaScript(award.getShengwang()));
					}

					if (award.getPetExp() != null)
						petExpAwardMap.put(id,
								new JavaScript(award.getPetExp()));

					yingFuExpMap.put(id, award.getYingfu());
					shengwangTips.put(id, award.getShengwangtips());
					// 是否有物品上限,如果有的话,加入map中
					if (award.getItemLimitString().length() > 2) {
						xbean.ActivityItemLimit itemLimit = xtable.Awarditemlimit
								.get(id);
						if (itemLimit == null) {
							itemLimit = xbean.Pod.newActivityItemLimit();
							xtable.Awarditemlimit.insert(id, itemLimit);
						}

						String str = award.getItemLimitString();
						String[] strs1 = str.split(";");
						List<Integer> newIdList = new ArrayList<Integer>();
						for (String itemidAndNum : strs1) {
							String[] strs2 = itemidAndNum.split("@");
							if (strs2.length == 2) {
								String itemidStr = strs2[0];
								String itemNum = strs2[1];
								int itemid = Integer.parseInt(itemidStr);
								int itemMaxNum = Integer.parseInt(itemNum);
								xbean.LimitItemInfo limitItemInfo = itemLimit
										.getLimititemmap().get(itemid);
								if (limitItemInfo != null) {// 如果之前就有这个限制,说明不是第一次启服务器了,只更新上限,已投放的数量不更新
									limitItemInfo.setMaxnum(itemMaxNum);
								} else {
									limitItemInfo = xbean.Pod
											.newLimitItemInfo();
									limitItemInfo.setMaxnum(itemMaxNum);
									limitItemInfo.setNum(0);
									itemLimit.getLimititemmap().put(itemid,
											limitItemInfo);
								}
								newIdList.add(itemid);
							}
						}
						int clearType = award.getCleartype();
						if (clearType == 1 || clearType == 2 || clearType == 3
								|| clearType == 5) {
							Set<Integer> awardRetIDs = clearLimitMap
									.get(clearType);
							if (awardRetIDs == null) {
								awardRetIDs = new HashSet<Integer>();
								clearLimitMap.put(clearType, awardRetIDs);
							}
							awardRetIDs.add(id);
						}
						List<Integer> removeIdList = new ArrayList<Integer>();
						for (Entry<Integer, xbean.LimitItemInfo> entry : itemLimit
								.getLimititemmap().entrySet()) {
							if (!newIdList.contains(entry.getKey()))
								removeIdList.add(entry.getKey());
						}
						for (Integer key : removeIdList) {
							itemLimit.getLimititemmap().remove(key);
						}
					}
					
					ExpBili bili = new ExpBili(award.expweaken, award.expaddition);
					expBili.put(id, bili);
				}
			
				
//				// 合服加buff的处理
//				String str = ConfigManager.getInstance().getPropConf("sys")
//						.getProperty("sys.merge.server");
//				SimpleDateFormat sdf2 = new SimpleDateFormat(
//						"yyyy-MM-dd-HH-mm-ss");
//				String[] tmpStrs = str.split(";");
//				for (String serverStr : tmpStrs) {
//					String[] serveridAndTimeStrings = serverStr.split(",");
//					if (serveridAndTimeStrings[0].equals(String
//							.valueOf(ConfigManager.getGsZoneId()))) {
//						mergeServerStartTime = sdf2.parse(
//								serveridAndTimeStrings[1]).getTime();
//						mergeServerEndTime = sdf2.parse(
//								serveridAndTimeStrings[2]).getTime();
//						break;
//					}
//				}
				return true;
			}

		}.call())
			throw new RuntimeException("init award table failed");

	}

	public int getTimes(int lootid) {
		int index = Misc.getRandomBetween(0, 999);

		java.util.List<Integer> prob = timesProb.get(lootid);

		int count = 0;
		for (Integer times : prob) {
			count++;

			if (index < times) {
				return count;
			}
		}

		if (count == prob.size()) {
			return 0;
		}

		return count;
	}

	public boolean isShared(int lootid) {
		Integer v = isShared.get(lootid);
		if (v == null) {
			return false;
		}

		return v != 0 ? true : false;
	}

	public Map<Integer, RewardObjs> distributeAllAward(long roleId,
			List<Integer> awardRetIDs, Map<String, Object> parameters, YYLoggerTuJingEnum getType,
			int unionId, int jingYanId, String jingYanStr, boolean showTips) {
		return distributeAllAward(roleId, awardRetIDs, parameters, false, getType,
				unionId, jingYanId, jingYanStr, showTips);
	}

	public Map<Integer, RewardObjs> distributeAllAward(long roleId,
			List<Integer> awardRetIDs, Map<String, Object> parameters, boolean petDead,
			YYLoggerTuJingEnum getType, int unionId, int jingYanId, String jingYanStr,
			boolean showTips) {

		Map<Integer, RewardObjs> result = new HashMap<Integer, RewardObjs>();
		for (Integer awardid : awardRetIDs) {
			Map<Integer, RewardObjs> add = distributeAllAward(roleId, awardid,
					parameters, petDead, getType, unionId, jingYanId,
					jingYanStr, showTips);
			UniteAwardsResult(result, add);
		}
		return result;
	}

	public Map<Integer, RewardObjs> distributeBattleAward(long roleId,
			int awardID, xbean.BattleInfo battleInfo, YYLoggerTuJingEnum getType,
			int unionId, int jingYanId, String jingYanStr, boolean showTips) {

		Map<String, Object> parameters = new HashMap<String, Object>();
		putBattleParas(parameters, battleInfo);
		return distributeAllAward(roleId, awardID, parameters, false, getType,
				unionId, jingYanId, jingYanStr, showTips);
	}

	public Map<Integer, RewardObjs> distributeAllAward(long roleId, int awardID,
			Map<String, Object> parameters, YYLoggerTuJingEnum getType, int unionId,
			int jingYanId, String jingYanStr, boolean showTips) {

		return distributeAllAward(roleId, awardID, parameters, false, getType,
				unionId, jingYanId, jingYanStr, showTips);
	}

	public Map<Integer, RewardObjs> distributeAllAward(long roleId, int awardID,
			Map<String, Object> parameters, YYLoggerTuJingEnum getType, int unionId,
			int jingYanId, String jingYanStr) {

		return distributeAllAward(roleId, awardID, parameters, false, getType,
				unionId, jingYanId, jingYanStr, true);
	}

	public Map<Integer, RewardObjs> distributeAllAward(long roleId, int awardID,
			Map<String, Object> parameters, boolean petDead, YYLoggerTuJingEnum getType,
			int unionId, int jingYanId, String jingYanStr, boolean showTips) {
		return distributeAllAward(roleId, awardID, parameters, petDead, getType,
				unionId, jingYanId, jingYanStr, showTips, true, false, null, false);
	}

	public Map<Integer, RewardObjs> distributeAllAward(long roleId, int awardID,
			Map<String, Object> parameters, YYLoggerTuJingEnum getType, int unionId,
			int jingYanId, String jingYanStr, boolean showTips,
			boolean calculateOnly, ArrayList<RolledAwardItem> rollawarditems, boolean distributeotheraward) {

		return distributeAllAward(roleId, awardID, parameters, false, getType,
				unionId, jingYanId, jingYanStr, showTips, calculateOnly,
				rollawarditems, distributeotheraward);
	}

	public Map<Integer, RewardObjs> distributeAllAward(long roleId, int awardID,
			Map<String, Object> parameters, boolean petDead, YYLoggerTuJingEnum getType,
			int unionId, int jingYanId, String jingYanStr, boolean showTips,
			boolean calculateOnly, ArrayList<RolledAwardItem> rollawarditems, boolean distributeotheraward) {
		return distributeAllAward(roleId, awardID, parameters, petDead, getType,
				unionId, jingYanId, jingYanStr, showTips, true,
				calculateOnly, rollawarditems,distributeotheraward);
	}

	private Map<Integer, RewardObjs> distributeAllAward(long roleId,
			int awardID, Map<String, Object> parameters, boolean petDead,
			YYLoggerTuJingEnum getType, int unionId, int jingYanId, String jingYanStr,
			boolean showTips, boolean showTeamMsg, boolean calculateOnly,
			ArrayList<RolledAwardItem> rollawarditems, boolean distributeotheraward) {

		ParseStringExpression pse = new ParseStringExpression();
		Map<Integer, RewardObjs> result = new HashMap<Integer, RewardObjs>();
		Map<Integer, Long> resultMoneyAndExp = new HashMap<Integer, Long>(2);
		try {
			if (parameters == null)
				parameters = new HashMap<String, Object>();

			putNeededParas(roleId, parameters);
			MsgInfos msgInfos = msgInfosMap.get(awardID);

			long money = distributeMoneyAward(roleId, awardID, parameters, pse,
					getType, unionId, false, calculateOnly && !distributeotheraward,
					resultMoneyAndExp);
			if (money != 0) {
				result.put(MONEY_AWARD, new RewardObjs(money));
			}
			long gold = distributeGoldAward(roleId, awardID, parameters, pse,
					getType, unionId, showTips,calculateOnly && !distributeotheraward);
			if (gold != 0) {
				result.put(GOLD_AWARD, new RewardObjs(gold));
			}
			//信用值奖励
			distributeCreditValueAward(roleId,awardID,parameters,getType,gold);
			long contribution = distributeContributionAward(roleId, awardID,
					parameters, pse, getType, unionId, showTips,
					calculateOnly && !distributeotheraward);
			if (contribution != 0) {
				result.put(CONTRIBUTION_AWARD, new RewardObjs(contribution));
			}
			long exp = distributeExpAward(roleId, awardID, parameters, pse,
					jingYanId, jingYanStr, showTips, calculateOnly && !distributeotheraward,
					resultMoneyAndExp);
			if (exp != 0) {
				result.put(EXP_AWARD, new RewardObjs(exp));
				//经验等级加成衰减
				Long addDecExp =resultMoneyAndExp.get(EXP_ADD_AWARD);
				if(addDecExp!=null)
					result.put(EXP_ADD_AWARD, new RewardObjs(addDecExp));
				//盈福经验
				Long yingfuExpVal =resultMoneyAndExp.get(EXP_YF_AWARD);
				if(yingfuExpVal!=null)
					result.put(EXP_YF_AWARD, new RewardObjs(yingfuExpVal));
			}
			int facionContri = distributeFactionContriAward(roleId, awardID,
					parameters, pse, showTips, calculateOnly && !distributeotheraward);
			if (facionContri != 0) {
				result.put(FACTION_CONTRI, new RewardObjs(facionContri));
			}

			// 公会资金
			int facionMoney = distributeFactionTaskContriAward(roleId, awardID,
					parameters, pse, showTips, calculateOnly && !distributeotheraward);
			if (facionMoney != 0) {
				result.put(FACTION_MONEY, new RewardObjs(facionMoney));
			}

			// 公会分红
			int facionBonus = distributeFactionTaskProfitContriAward(roleId,
					awardID, parameters, pse, showTips, calculateOnly && !distributeotheraward);
			if (facionBonus != 0) {
				result.put(FACTION_BONUS, new RewardObjs(facionBonus));
			}

			// 声望奖励
			int shengwang = distributeShengWangAward(roleId, awardID, getType, parameters,
					pse, showTips, calculateOnly && !distributeotheraward);
			if (shengwang != 0) {
				result.put(SHENGWANG, new RewardObjs(shengwang));
			}

			if (!petDead) {
				long petexp = distributePetExpAward(roleId, awardID, parameters,
						pse, showTips, calculateOnly && !distributeotheraward);
				if (petexp != 0) {
					result.put(PETEXP_AWARD, new RewardObjs(petexp));
				}
			}

			Map<Integer, RewardObjs> fAward = distributeFirstClassAward(roleId,
					awardID, true, parameters, getType, unionId, showTips,
					calculateOnly, rollawarditems);
			if (!fAward.isEmpty()
					&& fAward.get(RewardMgr.FIRSTC_AWARD) != null) {
				if (showTeamMsg && !calculateOnly) {
					for (Items item : fAward.get(RewardMgr.FIRSTC_AWARD)
							.getItems()) {
						if (getType == fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gonghuifuben
								|| getType == fire.log.enums.YYLoggerTuJingEnum.tujing_Value_fubenjiangli) {
							sendAwardMsg(roleId, item.getItemID(),
									item.getItemNum(), msgInfos, jingYanStr);
						} else {
							sendAwardMsg(roleId, item.getItemID(),
									item.getItemNum(), msgInfos);
						}
					}
				}
			}

			Map<Integer, RewardObjs> sAwardMap = distributeSecondClassAward(
					roleId, awardID, true, parameters, pse, getType,
					unionId, showTips, calculateOnly, rollawarditems);
			if (!sAwardMap.isEmpty()
					&& sAwardMap.get(RewardMgr.SECONDC_AWARD) != null) {
				UniteAwardsResult(result, sAwardMap);
				RewardObjs awardItem = result.get(RewardMgr.SECONDC_AWARD);
				if (awardItem != null && awardItem.itemNotEmpty()
						&& showTeamMsg && !calculateOnly)
					for (Items item : awardItem.getItems()) {
						if (getType == fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gonghuifuben
								|| getType == fire.log.enums.YYLoggerTuJingEnum.tujing_Value_fubenjiangli) {
							sendAwardMsg(roleId, item.getItemID(),
									item.getItemNum(), msgInfos, jingYanStr);
						} else {
							sendAwardMsg(roleId, item.getItemID(),
									item.getItemNum(), msgInfos);
						}
					}
			}
			// 看看有没有额外奖励
			int externalAwardid = getExternalAwardid(awardID);
			if (externalAwardid > 0) {
				Map<Integer, RewardObjs> exAwardMap = distributeAllAward(roleId,
						externalAwardid, parameters, petDead, getType,
						unionId, jingYanId, jingYanStr, showTips,
						showTeamMsg, calculateOnly, rollawarditems, distributeotheraward);
				UniteAwardsResult(result, exAwardMap);
			}

			if (!calculateOnly && showTips) {
				List<String> para = null;
				Long submoney = 0L;
				Long subExp = 0L;

				if (resultMoneyAndExp.containsKey(MONEY_AWARD)) {
					submoney = resultMoneyAndExp.get(MONEY_AWARD);
				}

				if (resultMoneyAndExp.containsKey(EXP_AWARD)) {
					subExp = resultMoneyAndExp.get(EXP_AWARD);
				}

				if (submoney > 0 && subExp > 0) {
					para = MessageUtil.getMsgParaList(Long.toString(submoney), Long.toString(Math.abs(exp)));
					MessageMgr.psendMsgNotifyWhileCommit(roleId, 162055, para);
//					MessageMgr.psendMsgNotifyWhileCommit(roleId, 180034, para);
				} else if (submoney > 0) {
					MessageUtil.psendAddorRemoveMoney(roleId, submoney);
				} else if (subExp > 0) {
					para = MessageUtil.getMsgParaList(Long.toString(Math.abs(subExp)));
					MessageMgr.psendMsgNotifyWhileCommit(roleId, MessageMgr.ADD_EXP, para);
				}
			}
		} catch (Exception e) {
			StringBuffer sb = new StringBuffer();
			sb.append("Distribute award failed:").append("roleId ")
					.append(roleId).append("awardid ").append(awardID);
			String str = sb.toString();
			logger.error(str, e);
			throw new RuntimeException(e);// 这里还是抛一下异常,这样可以导致procedure回滚,防止给奖励失败但其他表却记录玩家已经获得奖励的情况
		}
		return result;
	}

	private int getExternalAwardid(int awardID) {

		ExAward externalAward = externalAwardMap.get(awardID);
		if (externalAward != null) {

			try {
				long curTime = System.currentTimeMillis();
				int size = externalAward.getStartTimes().size();
				for (int i = 0; i < size; i++) {
					long startTime = externalAward.getStartTimes().get(i);
					long endTime = externalAward.getEndTimes().get(i);
					String zoneid = externalAward.getZoneids().get(i);
					if (startTime < curTime && curTime < endTime)
						if (zoneid.equals("0")
								|| zoneid.indexOf(String.valueOf(ConfigManager
										.getGsZoneId())) != -1) {
							return externalAward.getExawardid();
						}
				}
			} catch (Exception e) {
				StringBuffer sb = new StringBuffer();
				sb.append("find External award failed:").append("awardid ")
						.append(awardID);
				String str = sb.toString();
				logger.error(str, e);
			}
		}
		return 0;
	}

	private void sendAwardMsg(long roleId, int itemID, int itemNum,
			MsgInfos msgInfos) {
		if (msgInfos == null)
			return;
		if (itemNum <= 0)
			return;
		ItemShuXing attr = Module.getInstance().getItemManager()
				.getAttr(itemID);
		if (attr == null) {
			return;
		}
		if (msgInfos.getMsgid() > 0) {
			if (attr.rare == 1) {
				List<String> parameters = new ArrayList<String>();
				PropRole pRole = new PropRole(roleId, true);
				parameters.add(pRole.getName());
				parameters.addAll(MessageUtil.getItemMsgParas(itemID, itemNum));
				STransChatMessageNotify2Client ssmn = MessageMgr.getMsgNotify(
						msgInfos.getMsgid(), 0, parameters);
				SceneManager.sendAll(ssmn);
			}
		}
		if (msgInfos.getSendToTeam() == 1 && attr.rare == 1) {
			sendTeamMsg(roleId, itemID, itemNum);
		}
		if (msgInfos.getMsg2id() > 0) {
			if (attr.rare == 1) {
				List<String> parameters = new ArrayList<String>();
				PropRole pRole = new PropRole(roleId, true);
				parameters.add(pRole.getName());
				parameters.addAll(MessageUtil.getItemMsgParas(itemID, itemNum));
			}
		}
	}

	private void sendAwardMsg(long roleId, int itemID, int itemNum,
			MsgInfos msgInfos, String wanfamingzi) {

		if (msgInfos == null)
			return;
		if (itemNum <= 0)
			return;
		ItemShuXing attr = Module.getInstance().getItemManager()
				.getAttr(itemID);
		if (attr == null) {
			return;
		}
		if (msgInfos.getMsgid() > 0) {
			if (attr.rare == 1) {
				List<String> parameters = new ArrayList<String>();
				PropRole pRole = new PropRole(roleId, true);
				parameters.add(pRole.getName());
				parameters.add(wanfamingzi);
				parameters.addAll(MessageUtil.getItemMsgParas(itemID, itemNum));
				STransChatMessageNotify2Client ssmn = MessageMgr.getMsgNotify(
						msgInfos.getMsgid(), 0, parameters);
				SceneManager.sendAll(ssmn);
			}
		}
		if (msgInfos.getSendToTeam() == 1 && attr.rare == 1) {
			sendTeamMsg(roleId, itemID, itemNum);
		}
		if (msgInfos.getMsg2id() > 0) {
			if (attr.rare == 1) {
				List<String> parameters = new ArrayList<String>();
				PropRole pRole = new PropRole(roleId, true);
				parameters.add(pRole.getName());
				parameters.add(wanfamingzi);
				parameters.addAll(MessageUtil.getItemMsgParas(itemID, itemNum));
			}
		}
	}

	protected void sendTeamMsg(Long roleId, int itemid, int num) {

		if (TeamManager.isInTeam(roleId)) {
			Team team = TeamManager.selectTeamByRoleId(roleId);
			List<Long> members = team.getNormalMemberIds();
			members.remove(roleId);
			for (Long mem : members) {

				List<String> parameters = MessageUtil.getItemMsgParas(itemid,
						num);
				int msgid = Misc.getRandomBetween(0, 2) + baseMsgid;
				MessageMgr.psendMsgNotifyWhileCommit(mem, msgid, parameters);
			}
		}

	}

	private void UniteAwardsResult(final Map<Integer, RewardObjs> result,
			final Map<Integer, RewardObjs> add) {

		if (result != null && add != null && !add.isEmpty()) {
			java.util.Iterator<Integer> iter = add.keySet().iterator();
			while (iter.hasNext()) {
				Integer key = iter.next();
				if (result.containsKey(key)) {
					switch (key) {
					case FIRSTC_AWARD:
					case SECONDC_AWARD: {
						result.get(key).getItems()
								.addAll(add.get(key).getItems());
					}
						break;
					case MONEY_AWARD:
					case EXP_AWARD:
					case PETEXP_AWARD: {
						long oldValue = result.get(key).getValue();
						long addValue = add.get(key).getValue();
						result.get(key).setValue(oldValue + addValue);
					}
						break;
					}
				} else
					result.put(key, add.get(key));
			}
		}
		return;

	}

	long distributeMoneyAward(long roleId, int awardID,
			Map<String, Object> parameters, ParseStringExpression pse,
			YYLoggerTuJingEnum getType, int unionId, boolean showTips,
			boolean calculateOnly, Map<Integer, Long> resultMoneyAndExp) {
		long awardMoney = 0;
		do {
			if (moneyAwardMap.get(awardID) == null)
				return 0;
			Double value2 = moneyAwardMap.get(awardID).GetDoubleValue();
			if (value2 != null) {
				awardMoney = value2.longValue();
				break;
			}
			AwardJSEngine aw = new AwardJSEngine();
			for (Entry<String, Object> entry : parameters.entrySet()) {
				aw.put(entry.getKey(), entry.getValue());
			}
			Double value = moneyAwardMap.get(awardID).eval(aw, null, null);
			awardMoney = value.longValue();
			
			
			/*String str = moneyAwardMap.get(awardID).getJavaScriptString();
			if (str == null) {
				return 0;
			}

			try {
				Double value = pse.ParseStr(str, parameters);
				awardMoney = value.longValue();
			} catch (ScriptException e) {
				logger.error("error when parsing money award", e);
				return 0;
			}*/
			if (calculateOnly)
				return awardMoney;
		} while (false);

		fire.pb.item.Pack pack = new fire.pb.item.Pack(roleId, false);
		long realAdd = pack.addSysMoney(awardMoney, "awardID:" + awardID,
				getType, awardID);
		if (showTips && realAdd > 0)
			MessageUtil.psendAddorRemoveMoney(roleId, realAdd);
		if (realAdd != awardMoney) {
			logger.error("add money failed!reason:" + "awardID:" + awardID);

		}
		resultMoneyAndExp.put(MONEY_AWARD, realAdd);
		return realAdd;
	}

	long distributeGoldAward(long roleId, int awardID,
			Map<String, Object> parameters, ParseStringExpression pse,
			YYLoggerTuJingEnum getType, int unionId, boolean showTips,boolean calculateOnly) {
		long awardGold = 0;
		long creditValue=0;
		do {
			if (goldAwardMap.get(awardID) == null)
				return 0;
			Double value2 = goldAwardMap.get(awardID).GetDoubleValue();
			if (value2 != null) {
				awardGold = value2.longValue();
				break;
			}
			
			AwardJSEngine aw = new AwardJSEngine();
			for (Entry<String, Object> entry : parameters.entrySet()) {
				aw.put(entry.getKey(), entry.getValue());
			}
			Double value = goldAwardMap.get(awardID).eval(aw, null, null);
			awardGold = value.longValue();
			
			
			/*String str = goldAwardMap.get(awardID).getJavaScriptString();
			if (str == null) {
				return 0;
			}

			try {
				Double value = pse.ParseStr(str, parameters);
				awardGold = value.longValue();
			} catch (ScriptException e) {
				logger.error("error when parsing money award", e);
				return 0;
			}*/
			if (calculateOnly)
				return awardGold;
		} while (false);

		fire.pb.item.Pack pack = new fire.pb.item.Pack(roleId, false);
		long realAdd = pack.addSysGold(awardGold, "awardID:" + awardID,
				getType, awardID);
		if (showTips && realAdd > 0)
			MessageUtil.psendAddorRemoveGold(roleId, realAdd);
		if (realAdd != awardGold) {
			logger.error("add money failed!reason:" + "awardID:" + awardID);

		}
		return realAdd;
	}
	
	/**
	 * 信用值奖励
	 * @author yangtao
	 * @dateTime 2016年8月8日 下午10:25:57
	 * @version 1.0
	 * @param roleId
	 * @param awardID
	 * @param parameters
	 * @param getType
	 * @param realAddGold
	 */
	void distributeCreditValueAward(long roleId, int awardID, Map<String, Object> parameters,
			YYLoggerTuJingEnum getType, long realAddGold) {
		long creditValue = 0;
		// 信用值比例
		Integer creditbili = 0;
		if (creditBili.containsKey(awardID)) {
			creditbili = creditBili.get(awardID);
			// if (realAdd > 0 && creditbili > 0) {
			// pack.addSysCurrency((long) (realAdd * creditbili),
			// MoneyType.MoneyType_EreditPoint, "awardID:" + awardID,
			// getType, 0);
			// }
		}
		// 信用值额外
		if (creditMap.containsKey(awardID)) {
			AwardJSEngine aw1 = new AwardJSEngine();
			for (Entry<String, Object> entry : parameters.entrySet()) {
				aw1.put(entry.getKey(), entry.getValue());
			}
			Double credit = creditMap.get(awardID).eval(aw1, null, null);
			creditValue = credit.longValue();
		}
		long lastValue = 0l;
		// 计算最后的信用值
		if (realAddGold > 0) {
			// 信用值比例
			if (creditbili > 0) {
				lastValue = realAddGold * creditbili;
			}
		}
		// 信用值额外
		if (creditValue > 0) {
			lastValue += creditValue;
		}
		if (lastValue > 0) {
			fire.pb.item.Pack pack = new fire.pb.item.Pack(roleId, false);
			pack.addSysCurrency(lastValue, MoneyType.MoneyType_EreditPoint, "awardID:" + awardID, getType, 0);
		}
	}

	long distributeContributionAward(long roleId, int awardID,
			Map<String, Object> parameters, ParseStringExpression pse,
			YYLoggerTuJingEnum getType, int unionId, boolean showTips,
			boolean calculateOnly) {
		long awardContribution = 0;
		do {
			if (contributionAwardMap.get(awardID) == null)
				return 0;
			Double value2 = contributionAwardMap.get(awardID).GetDoubleValue();
			if (value2 != null) {
				awardContribution = value2.longValue();
				break;
			}
			
			AwardJSEngine aw = new AwardJSEngine();
			for (Entry<String, Object> entry : parameters.entrySet()) {
				aw.put(entry.getKey(), entry.getValue());
			}
			Double value = contributionAwardMap.get(awardID).eval(aw, null, null);
			awardContribution = value.longValue();
			
			/*String str = contributionAwardMap.get(awardID)
					.getJavaScriptString();
			if (str == null) {
				return 0;
			}

			try {
				Double value = pse.ParseStr(str, parameters);
				awardContribution = value.longValue();
			} catch (ScriptException e) {
				logger.error("error when parsing contribution award", e);
				return 0;
			}*/
			if (calculateOnly)
				return awardContribution;
		} while (false);

		fire.pb.item.Pack pack = new fire.pb.item.Pack(roleId, false);
		long realAdd = pack.addSysContribution(awardContribution, "awardID:"
				+ awardID, getType, awardID);
		if (showTips && realAdd > 0)
			MessageUtil.psendAddorRemoveContribution(roleId, realAdd);
		if (realAdd != awardContribution) {
			logger.error("add contribution failed!reason:" + "awardID:"
					+ awardID);

		}
		return realAdd;
	}

	long distributePetExpAward(long roleId, int awardID,
			Map<String, Object> parameters, ParseStringExpression pse,
			boolean showTips, boolean calculateOnly) {
		long exp = 0;
		do {
			if (petExpAwardMap.get(awardID) == null)
				return 0;
			Double value2 = petExpAwardMap.get(awardID).GetDoubleValue();
			if (value2 != null) {
				exp = value2.longValue();
				break;
			}
			
			AwardJSEngine aw = new AwardJSEngine();
			for (Entry<String, Object> entry : parameters.entrySet()) {
				aw.put(entry.getKey(), entry.getValue());
			}
			Double valuedb = petExpAwardMap.get(awardID).eval(aw, null, null);
			exp = valuedb.intValue();
			
			/*String str = petExpAwardMap.get(awardID).getJavaScriptString();
			if (str == null) {
				return 0;
			}

			try {
				Double value = pse.ParseStr(str, parameters);
				exp = value.longValue();
			} catch (ScriptException e) {
				logger.error("error when parsing petexp award", e);
				return 0;
			}*/
			if (calculateOnly)
				return exp;
		} while (false);

		PropRole propRole = new PropRole(roleId, true);
		PAddPetExpProc addexpproc = new PAddPetExpProc(roleId,
				propRole.getFightpetkey(), exp, showTips);
		boolean res = addexpproc.call();
		if (res) {
			return exp;
		} else {
			return 0;
		}
	}

	long distributeExpAward(long roleId, int awardID,
			Map<String, Object> parameters, ParseStringExpression pse, int reason,
			String hint, boolean showTips, boolean calculateOnly,
			Map<Integer, Long> resultMoneyAndExp) {
		/* 传进来的经验*/
		long exp = 0;
		/* 修正经验*/
		long reviseExp = 0;
		/*服务器加成和衰减*/
		long addDecExp = 0;
		/* 盈福经验*/
		long yingfuExpVal = 0;
		/* 停等级经验*/
		long pileExp = 0;
		String str = null;

		if (resultMoneyAndExp == null)
			resultMoneyAndExp = new HashMap<Integer, Long>(1);
		do {
			if (expAwardMap.get(awardID) == null)
				return 0;
			Double value2 = expAwardMap.get(awardID).GetDoubleValue();
			if (value2 != null) {
				exp = value2.longValue();
				if (calculateOnly) return exp;
				
				break;
			}
			
			AwardJSEngine aw = new AwardJSEngine();
			for (Entry<String, Object> entry : parameters.entrySet()) {
				aw.put(entry.getKey(), entry.getValue());
			}
			Double value = expAwardMap.get(awardID).eval(aw, null, null);
			exp = value.longValue();
			
			str = expAwardMap.get(awardID).getJavaScriptString();
			/*if (str == null) {
				return 0;
			}

			try {
				Double value = pse.ParseStr(str, parameters);
				exp = value.longValue();
			} catch (ScriptException e) {
				logger.error(new StringBuilder().append("error when parsing exp award").append(e).append("str is: ").append(str));
				return 0;
			}*/
			if (hint == null)
				hint = "";
			if (calculateOnly)
				return exp;
		} while (false);

		boolean res = false;
		
		//角色等级和服务器的等级差
		final xbean.Properties prop = xtable.Properties.select(roleId);
		int roleLv = prop.getLevel();
		int hideLv = 0;
		
		if (roleLv == 69 || roleLv == 89) {
			hideLv = getHideLevel(roleId);
		}
		
		int midLv = fire.pb.timer.Module.getMidLv(roleLv + hideLv, awardID);
		
		RoleLiveness actrole = RoleLiveness.getRoleLiveness(roleId, true);
		if (yingFuExpMap.get(awardID) == 1 && actrole != null && actrole.getYingFuExp() > 0) {
			int yingfuxishu = prop.getYingfuxishu();
			if (yingfuxishu > 0) {
				yingfuExpVal = exp * fire.pb.mission.activelist.RoleLivenessManager.YingFuXishu * yingfuxishu / 1000000
						 + exp * fire.pb.mission.activelist.RoleLivenessManager.YingFuXishu / 100;
			}
			else
				yingfuExpVal = exp * fire.pb.mission.activelist.RoleLivenessManager.YingFuXishu / 100;
			exp += yingfuExpVal;
		}
		
		//修正正常经验和盈福经验
		if (midLv != 0) {
			reviseExp = fire.pb.timer.Module.getExpRevise(midLv, exp);
			if (yingfuExpVal > 0)
				yingfuExpVal = fire.pb.timer.Module.getExpRevise(midLv, yingfuExpVal);
			addDecExp = reviseExp - exp;
		}
		else {
			reviseExp = exp;
		}
		
		fire.pb.effect.Role roleImpl = new fire.pb.effect.RoleImpl(roleId, true);
		long nextExp = roleImpl.getNextExp();
		if (prop.getExp() / nextExp == 10) {
			pileExp = exp - (exp * 80 / 100);
			reviseExp = reviseExp - pileExp;
		}
		else if (prop.getExp() / nextExp == 20) {
			pileExp = exp - (exp * 50 / 100);
			reviseExp = reviseExp - pileExp;
		}
		
		if (reviseExp < 0) reviseExp = 0;
		
		do {
			boolean addok = false;
			do {
				if (str == null) break;
				if ((str.indexOf(IS_LEADER) == -1)) break;
				if (((Integer) parameters.get(IS_LEADER)) != 1) break;
				
				PAddExpProc proc = new PAddExpProc(roleId, reviseExp,
						showTips, reason, hint, true, yingfuExpVal, reviseExp - exp, pileExp);
				res = proc.call();
				addok = true;
				break;
				
			} while (false);

			//上一个do while 里已经加过经验,所以不走下面的逻辑了
			if (addok) break;
			
			if (yingfuExpVal > 0 || addDecExp != 0 || pileExp > 0) {
				PAddExpProc proc = new PAddExpProc(roleId, reviseExp, showTips,
						reason, hint, false, yingfuExpVal, addDecExp, pileExp);
				res = proc.call();
//				resultMoneyAndExp.put(EXP_AWARD, reviseExp);
				if(addDecExp!=0)
				   resultMoneyAndExp.put(EXP_ADD_AWARD, addDecExp);
				if(yingfuExpVal>0)
				   resultMoneyAndExp.put(EXP_YF_AWARD, yingfuExpVal);
			} else {
				// 改到外面提示
				PAddExpProc proc = new PAddExpProc(roleId, reviseExp, false, reason, hint);
				res = proc.call();
				resultMoneyAndExp.put(EXP_AWARD, reviseExp);
			}
		} while (false);

		if (res) {
			return reviseExp;
		} else {
			return 0;
		}
	}

	int distributeFactionContriAward(long roleId, int awardID,
			Map<String, Object> parameters, ParseStringExpression pse,
			boolean showTips, boolean calculateOnly) {
		int factionContri = 0;
		do {
			if (faction_crontri.get(awardID) == null)
				return 0;
			Double value2 = faction_crontri.get(awardID).GetDoubleValue();
			if (value2 != null) {
				factionContri = value2.intValue();
				break;
			}
			
			AwardJSEngine aw = new AwardJSEngine();
			for (Entry<String, Object> entry : parameters.entrySet()) {
				aw.put(entry.getKey(), entry.getValue());
			}
			Double value = faction_crontri.get(awardID).eval(aw, null, null);
			factionContri = value.intValue();
			
			/*String str = faction_crontri.get(awardID).getJavaScriptString();
			if (str == null) {
				return 0;
			}

			try {
				Double value = pse.ParseStr(str, parameters);
				factionContri = value.intValue();
			} catch (ScriptException e) {
				logger.error("error when parsing exp award", e);
				return 0;
			}*/
			if (calculateOnly)
				return factionContri;
		} while (false);

		PAddClanPointProc addFaction = new PAddClanPointProc(roleId,
				factionContri, YYLoggerTuJingEnum.tujing_Value_npcjiangli,showTips, "奖励获得");
		boolean res = addFaction.call();
		if (res) {
			return factionContri;
		} else {
			return 0;
		}
	}

	int distributeFactionTaskContriAward(final long roleId, final int awardID,
			final Map<String, Object> parameters, final ParseStringExpression pse,
			final boolean showTips, final boolean calculateOnly) {
		int factionTaskMoney = 0;
		do {
			if (factionTask_crontri.get(awardID) == null)
				return 0;
			Double value2 = factionTask_crontri.get(awardID).GetDoubleValue();
			if (value2 != null) {
				factionTaskMoney = value2.intValue();
				break;
			}
			
			AwardJSEngine aw = new AwardJSEngine();
			for (Entry<String, Object> entry : parameters.entrySet()) {
				aw.put(entry.getKey(), entry.getValue());
			}
			Double value = factionTask_crontri.get(awardID).eval(aw, null, null);
			factionTaskMoney = value.intValue();
			
			/*String str = factionTask_crontri.get(awardID).getJavaScriptString();
			if (str == null) {
				return 0;
			}

			try {
				Double value = pse.ParseStr(str, parameters);
				factionTaskMoney = value.intValue();
			} catch (ScriptException e) {
				logger.error("error when parsing exp award", e);
				return 0;
			}*/
			if (calculateOnly)
				return factionTaskMoney;
		} while (false);

		fire.pb.clan.PAddClanMoney addFaction = new fire.pb.clan.PAddClanMoney(
				roleId, factionTaskMoney, showTips, "公会任务奖励");
		mkdb.Procedure.pexecuteWhileCommit(addFaction);
        return factionTaskMoney;
	}

	int distributeFactionTaskProfitContriAward(final long roleId,
			final int awardID, final Map<String, Object> parameters,
			final ParseStringExpression pse, final boolean showTips,
			final boolean calculateOnly) {
		int factionTaskProfit = 0;
		do {
			if (factionTaskProfit_crontri.get(awardID) == null)
				return 0;
			Double value2 = factionTaskProfit_crontri.get(awardID)
					.GetDoubleValue();
			if (value2 != null) {
				factionTaskProfit = value2.intValue();
				break;
			}
			
			AwardJSEngine aw = new AwardJSEngine();
			for (Entry<String, Object> entry : parameters.entrySet()) {
				aw.put(entry.getKey(), entry.getValue());
			}
			Double value = factionTaskProfit_crontri.get(awardID).eval(aw, null, null);
			factionTaskProfit = value.intValue();

			/*String str = factionTaskProfit_crontri.get(awardID)
					.getJavaScriptString();
			if (str == null) {
				return 0;
			}

			try {
				Double value = pse.ParseStr(str, parameters);
				factionTaskProfit = value.intValue();
			} catch (ScriptException e) {
				logger.error("error when parsing exp award", e);
				return 0;
			}*/
			if (calculateOnly)
				return factionTaskProfit;
		} while (false);

		fire.pb.PAddBonusProc addFaction = new fire.pb.PAddBonusProc(roleId, factionTaskProfit, showTips, "公会任务奖励");
		mkdb.Procedure.pexecuteWhileCommit(addFaction);
		return factionTaskProfit;
	}

	int distributeShengWangAward(final long roleId, final int awardID, YYLoggerTuJingEnum getType,
			final Map<String, Object> parameters, final ParseStringExpression pse,
			final boolean showTips, final boolean calculateOnly) {
		int shengWang = 0;
		do {
			if (shengWangAwardMap.get(awardID) == null)
				return 0;
			Double value = shengWangAwardMap.get(awardID).GetDoubleValue();
			if (value != null) {
				shengWang = value.intValue();
				break;
			}
			
			AwardJSEngine aw = new AwardJSEngine();
			for (Entry<String, Object> entry : parameters.entrySet()) {
				aw.put(entry.getKey(), entry.getValue());
			}
			Double valuedb = shengWangAwardMap.get(awardID).eval(aw, null, null);
			shengWang = valuedb.intValue();

			/*String str = shengWangAwardMap.get(awardID).getJavaScriptString();
			if (str == null) {
				shengWang = 0;
			}

			try {
				value = pse.ParseStr(str, parameters);
				shengWang = value.intValue();
			} catch (ScriptException e) {
				logger.error("error when parsing exp award", e);
				return 0;
			}*/
			if (calculateOnly)
				return shengWang;
		} while (false);

		int addShengWang = shengWang;
		if (getType != fire.log.enums.YYLoggerTuJingEnum.tujing_Value_chengjiu) {
			addShengWang = HelpCountManage.getInstance().addShengWangValue(roleId, shengWang, "援助奖励");
		}
		
		if (addShengWang > 0) {
			fire.pb.item.Pack pack = new fire.pb.item.Pack(roleId, false);
			long realAdd = pack.addSysCurrency(addShengWang,
					MoneyType.MoneyType_ShengWang, "awardID:" + awardID,
					getType, awardID);

			if (showTips && realAdd > 0) {
				if (shengwangTips.get(awardID) != 0) {
					String str = Long.toString(Math.abs(realAdd));
					List<String> paras = MessageUtil.getMsgParaList(str);
					MessageMgr.psendMsgNotifyWhileCommit(roleId, shengwangTips.get(awardID), paras);
				}
				else {
					MessageUtil.pSendAddOrRemoveCurrency(roleId, realAdd, MoneyType.MoneyType_ShengWang);
				}
				
				return addShengWang;
			}
		}

		return 0;
	}

	Map<Integer, RewardObjs> distributeFirstClassAward(long roleId, int awardID,
			boolean playCG, Map<String, Object> parameters, YYLoggerTuJingEnum getType,
			int unionId, boolean showTips, boolean calculateOnly,
			ArrayList<RolledAwardItem> out) {
		Map<Integer, RewardObjs> result = new HashMap<Integer, RewardObjs>();
		List<FirstClassAwardItem> awardItems = firstCAwardMap.get(awardID);
		if (awardItems != null)
			for (FirstClassAwardItem awardItem : awardItems) {
				// 拿到必给物品的id
				int id = awardItem.getItemID();
				if (id >= 100000) {
					// 根据这个id去奖励的二级表找物品
					SActivityAwardItems items = award2Map.get(id);
					if (items == null)
						continue;
					// 从items中随机一个物品,随机到的物品是必给的

					int randomValue = Misc.getProbability(items.itemsrate);
					int itemID = items.items.get(randomValue);
					// 要给多少个
					int itemNum = awardItem.getItemNum();

					RewardObjs awardItemAdd = result
							.get(RewardMgr.FIRSTC_AWARD);
					if (awardItemAdd == null) {
						awardItemAdd = new RewardObjs();
						result.put(RewardMgr.FIRSTC_AWARD, awardItemAdd);
					}

					if (calculateOnly) {
						awardItemAdd.getItems().add(
								new Items(itemID, itemNum, awardItem.getProp(),
										awardItem.getPropValue()));

						if (out != null) {
							RolledAwardItem item = new RolledAwardItem(awardID,
									itemID, itemNum, getType, unionId);
							out.add(item);
						}
						continue;
					}

					// 给玩家物品
					int realAddNum = giveItem(roleId, awardID, itemID, itemNum,
							getType, unionId, showTips);
					if (realAddNum > 0)
						awardItemAdd.getItems().add(
								new Items(itemID, realAddNum, awardItem
										.getProp(), awardItem.getPropValue()));
				} else {
					Map<Integer, RewardObjs> tmp = distributeAllAward(roleId,
							awardItem.getItemID(), parameters, playCG, getType,
							unionId, 0, "", showTips, false, calculateOnly,
							out, false);// 这个奖励不可能给经验,所以最后两个参数无所谓
					UniteAwardsResult(result, tmp);
					continue;
				}
			}
		return result;
	}

	public Map<Integer, RewardObjs> distributeSecondClassAward(long roleId,
			int awardID, boolean playCG, Map<String, Object> parameters,
			ParseStringExpression pse, YYLoggerTuJingEnum getType, int unionId,
			boolean showTips, boolean calculateOnly,
			ArrayList<RolledAwardItem> out) {

		Map<Integer, RewardObjs> result = new HashMap<Integer, RewardObjs>();
		SecondClassAwardItems secondClassAwardItems = secondCAwardMap
				.get(awardID);
		if (secondClassAwardItems == null)
			return result;

		List<SecondClassAwardItem> awardItems = secondClassAwardItems
				.getItems();
		if (awardItems == null || awardItems.size() == 0) {
			return result;
		}

		if (secondClassAwardItems.getRandomType() == SecondClassAwardItems.RELATED_RANDOM) {
			// 判断给不给的总开关
			int totalProb = 0;
			try {
				Double value = pse.ParseStr(
						secondClassAwardItems.getTotalProb(), parameters);
				totalProb = value.intValue();
			} catch (ScriptException e) {
				logger.error(
						"error when parsing second class award prob.awardid:"
								+ awardID, e);
			}

			int base = secondClassAwardItems.getBase();
			if (Misc.checkRate(base - 1, totalProb, random2))
				giveRelatedRandomItems(roleId, awardID, awardItems, playCG,
						result, parameters, pse, getType, unionId, showTips,
						calculateOnly, out);
		} else if (secondClassAwardItems.getRandomType() == SecondClassAwardItems.UNRELATED_RANDOM) {
			giveUnrelatedRandomItems(roleId, awardID, awardItems,
					secondClassAwardItems, playCG, result, parameters, pse,
					getType, unionId, showTips, calculateOnly, out);
		}
		return result;
	}

	private void giveUnrelatedRandomItems(long roleId, int awardid,
			List<SecondClassAwardItem> awardItems,
			SecondClassAwardItems secondClassAwardItems, boolean playCG,
			Map<Integer, RewardObjs> result, Map<String, Object> parameters,
			ParseStringExpression pse, YYLoggerTuJingEnum getType, int unionId,
			boolean showTips, boolean calculateOnly,
			ArrayList<RolledAwardItem> out) {

		int base = secondClassAwardItems.getBase();

		for (SecondClassAwardItem item : awardItems) {
			if (overMaxNum(awardid, item))
				continue;
			int prob = 0;
			try {
				Double value = pse.ParseStr(item.getRatio(), parameters);
				prob = value.intValue();
			} catch (ScriptException e) {
				logger.error(
						"error when parsing second class award prob.awardid:"
								+ awardid + "ratio:" + item.getRatio(), e);
			}

			if (Misc.checkRate(base - 1, prob, random3)) {
				if (item.getItemID() >= 100000)// 大于100000表示不是递归的
					giveRoleItem(roleId, awardid, item, playCG, result,
							getType, unionId, showTips, calculateOnly,
							out);
				else {// 否则是递归的,还要去奖励表中找下一行
					Map<Integer, RewardObjs> tmp = distributeAllAward(roleId,
							item.getItemID(), parameters, playCG, getType,
							unionId, 0, "", showTips, false, calculateOnly,
							out, false);
					UniteAwardsResult(result, tmp);
				}
				updateLimitNum(roleId, awardid, item);
			}
		}
	}

	private void giveRelatedRandomItems(long roleId, int awardID,
			List<SecondClassAwardItem> awardItems, boolean playCG,
			Map<Integer, RewardObjs> result, Map<String, Object> parameters,
			ParseStringExpression pse, YYLoggerTuJingEnum getType, int unionId,
			boolean showTips, boolean calculateOnly,
			ArrayList<RolledAwardItem> out) {

		// 随机物品中根据概率选出一个
		List<Integer> pros = new ArrayList<Integer>();
		for (int i = 0; i < awardItems.size(); i++) {
			try {
				if (overMaxNum(awardID, awardItems.get(i))) {
					pros.add(0);
				} else {
					Double value = pse.ParseStr(awardItems.get(i).getRatio(),
							parameters);
					pros.add(value.intValue());
				}

			} catch (ScriptException e) {
				logger.error(
						"error when parsing second class award prob.awardid:"
								+ awardID, e);
				pros.add(0);
			}

		}
		int index = Misc.getProbability(pros, random4);
		if (index == -1) {
			return;
		}
		SecondClassAwardItem awardItem = awardItems.get(index);
		if (awardItem.getItemID() >= 100000)// 大于100000表示不是递归的
			giveRoleItem(roleId, awardID, awardItem, playCG, result,
					getType, unionId, showTips, calculateOnly, out);
		else {// 否则是递归的,还要去奖励表中找下一行
			Map<Integer, RewardObjs> tmp = distributeAllAward(roleId,
					awardItem.getItemID(), parameters, playCG, getType,
					unionId, 0, "", showTips, false, calculateOnly, out, false);// 这个奖励不可能给经验,所以最后两个参数无所谓
			UniteAwardsResult(result, tmp);
		}
		updateLimitNum(roleId, awardID, awardItem);
	}

	private void updateLimitNum(long roleId, int awardID,
			SecondClassAwardItem awardItem) {
		if (xtable.Awarditemlimit.select(awardID) == null)
			return;
		xbean.ActivityItemLimit activityItemLimit = xtable.Awarditemlimit
				.get(awardID);
		if (activityItemLimit != null) {
			xbean.LimitItemInfo limitItemInfo = activityItemLimit
					.getLimititemmap().get(awardItem.getItemID());
			if (limitItemInfo != null) {
				if (limitItemInfo.getNum() + awardItem.getItemNum() > limitItemInfo
						.getMaxnum())
					throw new RuntimeException(
							"over award itemnum limit!roleId:" + roleId
									+ "awardid:" + awardID + "secondclass id:"
									+ awardItem.getItemID());
				limitItemInfo.setNum(limitItemInfo.getNum()
						+ awardItem.getItemNum());
			}
		}
	}

	private boolean overMaxNum(int awardID, SecondClassAwardItem awardItem) {

		xbean.ActivityItemLimit itemLimit = xtable.Awarditemlimit
				.select(awardID);
		if (itemLimit != null) {
			xbean.LimitItemInfo limitItemInfo = itemLimit.getLimititemmap()
					.get(awardItem.getItemID());
			if (limitItemInfo != null) {
				if (limitItemInfo.getNum() + awardItem.getItemNum() > limitItemInfo
						.getMaxnum())
					return true;
			}
		}
		return false;
	}

	private int giveRoleItem(long roleId, int awardID,
			SecondClassAwardItem awardItem, boolean playCG,
			Map<Integer, RewardObjs> result, YYLoggerTuJingEnum getType, int unionId,
			boolean showTips, boolean calculateOnly,
			ArrayList<RolledAwardItem> out) {

		// 拿到随机物品的id
		int id = awardItem.getItemID();
		// 根据这个id去奖励的二级表找物品
		SActivityAwardItems items = award2Map.get(id);
		if (items == null)
			return 0;
		// 从items中随机一个物品,随机到的物品是必给的
		int randomValue = Misc.getProbability(items.itemsrate);
		int itemID = items.items.get(randomValue);
		// 要给多少个
		int itemNum = awardItem.getItemNum();
		if (calculateOnly) {
			RewardObjs aItem = result.get(RewardMgr.SECONDC_AWARD);
			if (aItem == null) {
				aItem = new RewardObjs();
				result.put(RewardMgr.SECONDC_AWARD, aItem);
			}
			aItem.getItems().add(new Items(itemID, itemNum, 0, 0));

			if (out != null) {
				RolledAwardItem item = new RolledAwardItem(awardID, itemID,
						itemNum, getType, unionId);
				out.add(item);
			}

			return itemNum;
		}
		int realAddNum = giveItem(roleId, awardID, itemID, itemNum,
				getType, unionId, showTips);
		if (realAddNum > 0) {
			RewardObjs aItem = result.get(RewardMgr.SECONDC_AWARD);
			if (aItem == null) {
				aItem = new RewardObjs();
				result.put(RewardMgr.SECONDC_AWARD, aItem);
			}
			aItem.getItems().add(new Items(itemID, realAddNum, 0, 0));
		}

		return realAddNum;
	}

	public int giveItem(long roleId, int awardID, int itemID, int itemNum,
			YYLoggerTuJingEnum getType, int unionId, boolean showTips) {
		// 给玩家物品
		int realAddNum = 0;
		boolean isEquip = false;
		ItemShuXing attr = Module.getInstance().getItemManager()
				.getAttr(itemID);
		if (attr == null) {
			logger.error("item has no attr.itemid:" + itemID);
			return 0;
		}
		if (attr.getTypeid() % 16 == 8)
			isEquip = true;

		fire.pb.item.Pack pack = new fire.pb.item.Pack(roleId, false);
		realAddNum = addItemToBag(roleId, pack, awardID, isEquip, itemID,
				itemNum, getType, unionId);
		if (showTips && realAddNum > 0)
			MessageUtil.psendAddItemWhileCommit(roleId, itemID, realAddNum);
		if (realAddNum != itemNum) {
			if (!tempBagAvailableMap.get(awardID)) {
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleId,
						144949, null);
				throw new RuntimeException("玩家背包栏已满,roleId:" + roleId
						+ "awardid:" + awardID);
			}

			if (realAddNum < 0 || realAddNum > itemNum) {
				logger.error("add more items!itemid:" + itemID);
				return 0;
			}

			if (tempBagAvailableMap.get(awardID)) {
				int tempAdd = 0;
				fire.pb.item.ItemMaps tempBag = fire.pb.item.Module
						.getInstance()
						.getItemMaps(roleId, BagTypes.TEMP, false);
				if (pack.isFull()) {
					tempAdd = addItemToBag(roleId, tempBag, awardID, isEquip,
							itemID, itemNum - realAddNum, getType,
							unionId);
					if (showTips && tempAdd > 0) {
						MessageUtil.psendAddItemWhileCommit(roleId, itemID,
								tempAdd);
					}
				}

				realAddNum = realAddNum + tempAdd;
			} else {
				if (pack.isFull())
					if (realAddNum == 0)
						fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 142856,
								null);
					else
						fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 142857,
								null);

			}
			if (realAddNum != itemNum) {
				logger.error("add item failed: itemid:" + itemID + " number:"
						+ itemNum + ".pls check your pack capacity");
			}
		}

		return realAddNum;
	}

	private int addItemToBag(long roleId, ItemMaps pack, int awardID,
			boolean isEquip, int itemID, int itemNum, YYLoggerTuJingEnum getType,
			int unionId) {
		int added = 0;
		if (!bindMap.get(awardID)) {
			if (isEquip) {
				EquipItem equipItem = ItemMakeFactory.getFactory()
						.genItemMakeByWay(itemID, "");
				if (pack.doAddItem(equipItem, -1, "activity award:" + roleId,
						getType, unionId) == AddItemResult.SUCC)
					added = 1;
			} else
				added = pack.doAddItem(itemID, itemNum, "activity award",
						getType, unionId);
		} else {
			if (isEquip) {
				EquipItem equipItem = ItemMakeFactory.getFactory()
						.genItemMakeByWay(itemID, "");
				if (pack.doAddItem(equipItem, -1, "activity award:" + roleId,
						getType, unionId) == AddItemResult.SUCC)
					added = 1;
			} else
				added = BagUtil.addBindItem(roleId, itemID, itemNum,
						"activity award", getType, unionId,
						tempBagAvailableMap.get(awardID));
		}
		return added;
	}

	public java.util.NavigableMap<Integer, SActivityAwardItems> getSActivityAwardItemMap() {

		return award2Map;
	}

	// 转换后的奖励:金钱奖励
	public java.util.Map<Integer, JavaScript> getMoneyAwardMap() {
		return moneyAwardMap;
	}

	// 转换后的奖励:金钱奖励
	public java.util.Map<Integer, JavaScript> getGoldAwardMap() {
		return goldAwardMap;
	}
	
	// 转换后的奖励:信用值比例
	public java.util.Map<Integer, Integer> getCreditBili() {
		return creditBili;
	}

	// 转换后的奖励:贡献奖励
	public java.util.Map<Integer, JavaScript> getContributionAwardMap() {
		return contributionAwardMap;
	}

	// 转换后的奖励:经验奖励
	public java.util.Map<Integer, JavaScript> getExpAwardMap() {
		return expAwardMap;
	}
	
	// 有盈福经验的奖励
	public java.util.Map<Integer, Integer> getYingFuExpMap () {
		return yingFuExpMap;
	}

	// 转换后的奖励：节日积分奖励
	public java.util.Map<Integer, JavaScript> getFesCreditAwardMap() {
		return fesCreditAwardMap;
	}

	// 转换后的奖励：节日帮贡
	public java.util.Map<Integer, JavaScript> getFaction_crontri() {
		return faction_crontri;
	}

	// 转换后的奖励：公会任务贡献资金
	public java.util.Map<Integer, JavaScript> getFactionTask_crontri() {
		return factionTask_crontri;
	}

	// 转换后的奖励：公会任务个人分红
	public java.util.Map<Integer, JavaScript> getFactionTaskProfit_crontri() {
		return factionTaskProfit_crontri;
	}

	// 转换后的奖励：公会任务个人分红
	public java.util.Map<Integer, JavaScript> getShengWangAwardMap() {
		return shengWangAwardMap;
	}

	// 转换后的奖励:宠物经验奖励
	public java.util.Map<Integer, JavaScript> getPetExpAwardMap() {
		return petExpAwardMap;
	}

	// 一些有没有双倍,三倍时间的参数,可以调这个方法统一存入
	public static void putNeededParas(long roleId, Map<String, Object> parameters) {

		boolean hasMultiExpBuff = false;
		parameters.put(IS_SYS_TRIPLE_EXP, 0);
		parameters.put(IS_TRIPLE_EXP, 0);
		parameters.put(IS_DOUBLE_EXP, 0);
		if (hasMultiExpBuff)
			parameters.put(IS_NO_MULTI_EXP, 0);
		else
			parameters.put(IS_NO_MULTI_EXP, 1);

		parameters.put(IWEB_MULTI_EXP, iwebMultiExp);
		parameters.put(BROTHER_NUM, 0);

		PropRole role = new PropRole(roleId, true);
		// 阵营三倍处理,默认为0,根据阵营判断倍数
		parameters.put(DKTRIBLE, 0);
		parameters.put(TJTRIBLE, 0);
		parameters.put(ROLE_LEVEL, role.getLevel());
		int petkey = role.getFightpetkey();
		if (petkey <= 0)
			parameters.put(PET_LEVEL, 0);
		else {
			final PetColumn petcol = new PetColumn(roleId, PetColumnTypes.PET,
					true);
			final xbean.PetInfo xpetinfo = petcol.getPetInfo(petkey);
			if (xpetinfo != null) {
				int level = xpetinfo.getLevel();
				parameters.put(PET_LEVEL, level);
			}
		}

//		// 合服可以获得一定经验奖励
//		long curTime = System.currentTimeMillis();
//		if (mergeServerStartTime <= curTime && curTime <= mergeServerEndTime)
//			parameters.put(MERGE_SERVER, 1);
//		else
//			parameters.put(MERGE_SERVER, 0);

		NextExpAndResMoney cfg = ConfigManager.getInstance()
				.getConf(NextExpAndResMoney.class).get(role.getLevel());
		parameters.put(STDEXP, cfg.standardexp);
		parameters.put(STDMONEY, cfg.standardmoney);

	}

	public java.util.NavigableMap<Integer, SActivityAwardItems> getAward2Map() {

		return award2Map;
	}

	public void putBattleParas(Map<String, Object> parameters,
			xbean.BattleInfo battle) {

		int mapLevel = 0;
		if (battle.getBattletype() % 10 == 0) {
			SMineArea hideAreaInfo = null;
			SShowAreainfo showAreainfo = null;

			if (battle.getAreatype() == xbean.BattleInfo.AREA_BATTLEHIDE) {
				hideAreaInfo = BattleEndHandler.hideMapConfigs.get(battle
						.getAreaconf());
				mapLevel = hideAreaInfo.getLevel();

			} else if (battle.getAreatype() == xbean.BattleInfo.AREA_BATTLESHOW) {
				showAreainfo = BattleEndHandler.showMapConfigs.get(battle
						.getAreaconf());
				mapLevel = showAreainfo.getTypelevel();

			}
		}
		parameters.put("MapLv", mapLevel);
		parameters.put(
				"MasterNum",
				battle.getBattledatas()
						.get(xbean.BattleInfo.DATA_MONSTER_CHIEF_NUM)
						.intValue());
		parameters.put("MonsterNum",
				battle.getBattledatas().get(xbean.BattleInfo.DATA_MONSTER_NUM)
						.intValue());
		parameters.put("TeamNum",
				battle.getBattledatas()
						.get(xbean.BattleInfo.DATA_HOST_ROLE_NUM).intValue());
		parameters.put(
				"TeamLv",
				battle.getBattledatas().get(
						xbean.BattleInfo.DATA_HOST_ROLE_AVERAGE_LEVEL));
		parameters.put(
				"MonsterLv",
				battle.getBattledatas().get(
						xbean.BattleInfo.DATA_MONSTER_AVERAGE_LEVEL));
		parameters.put(BROTHER_NUM, 0);
	}

	public void init() throws Exception {
		initData();
	}

	public long calculateExpOrMoney(int awardID, int type,
			Map<String, Object> parameters) {
		long result = 0;
		AwardJSEngine aw = new AwardJSEngine();
		//ParseStringExpression pse = new ParseStringExpression();
		if (type == RewardMgr.EXP_AWARD) {
			if (expAwardMap.get(awardID) == null)
				return 0;
			
			for (Entry<String, Object> entry : parameters.entrySet()) {
				aw.put(entry.getKey(), entry.getValue());
			}
			Double value = expAwardMap.get(awardID).eval(aw, null, null);
			result = value.longValue();
/*			String str = expAwardMap.get(awardID).getJavaScriptString();
			if (str == null) {
				return 0;
			}
			try {
				Double value = pse.ParseStr(str, parameters);
				result = value.longValue();
			} catch (ScriptException e) {
				return 0;
			}
*/			
			
		} else if (type == RewardMgr.MONEY_AWARD) {
			for (Entry<String, Object> entry : parameters.entrySet()) {
				aw.put(entry.getKey(), entry.getValue());
			}
			Double value = moneyAwardMap.get(awardID).eval(aw, null, null);
			result = value.longValue();
				
			/*if (moneyAwardMap.get(awardID) == null)
				return 0;
			String str = moneyAwardMap.get(awardID).getJavaScriptString();
			if (str == null) {
				return 0;
			}
			try {
				Double value = pse.ParseStr(str, parameters);
				result = value.longValue();
			} catch (ScriptException e) {
				return 0;
			}*/

		}
		return result;
	}

	public static boolean setIwebMultiExp(int multiExp) {

		if (multiExp < 0 || multiExp > 3)
			return false;
		if (iwebMultiExp == multiExp)
			return false;
		iwebMultiExp = multiExp;
		gnet.link.Onlines.getInstance().broadcast(
				new SSendServerMultiExp(multiExp), 999);
		return true;
	}

	public static boolean setCampMultiExp(int camp, int multiExp) {
		if (multiExp < 0 || multiExp > 1)
			return false;
		if (camp != 1 && camp != 2) {
			return false;
		}
		return true;
	}

	public java.util.Map<Integer, Set<Integer>> getClearLimitMap() {

		return clearLimitMap;
	}

	public static List<RewardObjs> getItemAwards(Map<Integer, RewardObjs> awardMap) {
		List<RewardObjs> awardItemsList = new ArrayList<RewardObjs>();
		for (Entry<Integer, RewardObjs> awarditem : awardMap.entrySet()) {
			switch (awarditem.getKey()) {
			case RewardMgr.FIRSTC_AWARD:
			case RewardMgr.SECONDC_AWARD:
				awardItemsList.add(awarditem.getValue());
				break;

			default:
				break;
			}
		}
		return awardItemsList;
	}

	public Map<Integer, RewardObjs> calAllAward(long roleId, int awardID,
			Map<String, Object> parameters) {

		return distributeAllAward(roleId, awardID, parameters, false,
				fire.log.enums.YYLoggerTuJingEnum.NO_USED, awardID,
				PAddExpProc.OTHER, "", false, false, true, null, false);
	}

	public void dealCatchItTaskRoll(final long roleId, final int questid,
			final int state, final long teamid, final int awardid,
			final List<Long> roleids, Map<String, Object> parameters) // 处理ROLL点 by
																	// changhao
	{

	}

	/**
	 * 计算经验
	 * @param roleId
	 * @param awardID
	 * @param parameters
	 * @param calculateOnly  true 只是计算值，不添加到背包
	 * @return
	 */
	public long getAwardExpdistributeAllAward(long roleId, int awardID,
			Map<String, Object> parameters, boolean calculateOnly) {
		ParseStringExpression pse = new ParseStringExpression();
		try {
			if (parameters == null)
				parameters = new HashMap<String, Object>();
			putNeededParas(roleId, parameters);

			Map<Integer, Long> resultMoneyAndExp = new HashMap<Integer, Long>(1);

			long exp = distributeExpAward(roleId, awardID, parameters, pse, 0, "",
					false, calculateOnly, resultMoneyAndExp);
			if (exp != 0) {
				return exp;
			}
		} catch (Exception e) {
			StringBuffer sb = new StringBuffer();
			sb.append("Distribute award failed:").append("roleId ")
					.append(roleId).append("awardid ").append(awardID);
			String str = sb.toString();
			logger.error(str, e);
			throw new RuntimeException(e);// 这里还是抛一下异常,这样可以导致procedure回滚,防止给奖励失败但其他表却记录玩家已经获得奖励的情况
		}
		return 0;
	}
	/**
	 * 计算金钱
	 * @param roleId
	 * @param awardID
	 * @param parameters
	 * @param calculateOnly  true 只是计算值，不添加到背包
	 * @return
	 */
	public long getAwardMoneydistributeAllAward(long roleId, int awardID,
			Map<String, Object> parameters, boolean calculateOnly) {
		ParseStringExpression pse = new ParseStringExpression();
		try {
			if (parameters == null)
				parameters = new HashMap<String, Object>();
			putNeededParas(roleId, parameters);
			
			Map<Integer, Long> resultMoneyAndExp = new HashMap<Integer, Long>(1);
			
			long money = distributeMoneyAward(roleId, awardID, parameters, pse,
					fire.log.enums.YYLoggerTuJingEnum.tujing_Value_mailget, 0, false, calculateOnly,
					resultMoneyAndExp);
			
			if (money != 0) {
				return money;
			}
		} catch (Exception e) {
			StringBuffer sb = new StringBuffer();
			sb.append("Distribute award failed:").append("roleId ")
			.append(roleId).append("awardid ").append(awardID);
			String str = sb.toString();
			logger.error(str, e);
			throw new RuntimeException(e);// 这里还是抛一下异常,这样可以导致procedure回滚,防止给奖励失败但其他表却记录玩家已经获得奖励的情况
		}
		return 0;
	}
	
	/**
	 * 计算金币
	 * @param roleId
	 * @param awardID
	 * @param parameters
	 * @param calculateOnly  true 只是计算值，不添加到背包
	 * @return
	 */
	public long getAwardGolddistributeAllAward(long roleId, int awardID,
			Map<String, Object> parameters, boolean calculateOnly) {
		ParseStringExpression pse = new ParseStringExpression();
		try {
			if (parameters == null)
				parameters = new HashMap<String, Object>();
			putNeededParas(roleId, parameters);
			long gold = distributeGoldAward(roleId, awardID, parameters, pse,
					fire.log.enums.YYLoggerTuJingEnum.tujing_Value_mailget, 0, false,calculateOnly);
			if (gold != 0) {
				return gold;
			}
		} catch (Exception e) {
			StringBuffer sb = new StringBuffer();
			sb.append("Distribute award failed:").append("roleId ")
			.append(roleId).append("awardid ").append(awardID);
			String str = sb.toString();
			logger.error(str, e);
			throw new RuntimeException(e);// 这里还是抛一下异常,这样可以导致procedure回滚,防止给奖励失败但其他表却记录玩家已经获得奖励的情况
		}
		return 0;
	}

	public MsgInfos getMsgInfos(int awardID) {
		if (msgInfosMap.containsKey(awardID))
			return msgInfosMap.get(awardID);
		else {
			return null;
		}
	}
	
	public ExpBili getBili(int awardId) {
		if (expBili.containsKey(awardId))
			return expBili.get(awardId);
		return null;
	}
	
	/**
	 * 获取隐藏等级
	 * @param roleid
	 * @return
	 */
	public int getHideLevel(final long roleid) {
		final xbean.Properties prop = xtable.Properties.select(roleid);
		int roleLv = prop.getLevel();
		int hideLv = 0;
		//计算出隐藏等级
		if (roleLv == 69 || roleLv == 89) {
			do {
				Integer tupotips = prop.getTupotips().get(prop.getLevel());
				if (tupotips == null) break;
				if (tupotips == 2) break;
				
				Long hideExp = prop.getExp();
				Map<Integer, NextExpAndResMoney> expMap = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.role.NextExpAndResMoney.class);
				for (int i = roleLv; i < PLevelUpProc.getMaxLevel(); i ++) {
					long nextLvExp = expMap.get(i).exp;
					if ((hideExp - nextLvExp) < 0) break;
					
					hideExp -= nextLvExp;
					hideLv ++;
				}
			}
			while (false);
			
			return hideLv;
		}
		
		return 0;
	}
}
