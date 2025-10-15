package fire.pb.activity.award;

import java.util.HashSet;
import java.util.Set;

import fire.pb.game.SActivityAward;
import fire.pb.game.SPointCardActivityAward;
import fire.pb.main.ConfigMgr;
import fire.pb.npc.RefreshTimerNpc;
import fire.pb.npc.SRefreshTimerNpc;
import fire.pb.npc.SRefreshTimerNpcDianKa;
import fire.script.JavaScript;

public class AwardMgr {
	public static final String MODULE_NAME = "award";

	// 奖励一级表的原始数据
	private java.util.NavigableMap<Integer, SActivityAward> award1Map = null;

	// 点卡服奖励一级表的原始数据
	private java.util.NavigableMap<Integer, SPointCardActivityAward> daward1Map = null;

	// 金钱奖励
	private Set<JavaScript> moneyAwardMap = new HashSet<>();

	// 金钱奖励
	private Set<JavaScript> goldAwardMap = new HashSet<>();
	
	// 信用值
	private Set<JavaScript> creditMap = new HashSet<>();

	// 贡献奖励
	private Set<JavaScript> contributionAwardMap = new HashSet<>();

	// 经验奖励
	private Set<JavaScript> expAwardMap = new HashSet<>();

	// 节日积分奖励
	private Set<JavaScript> fesCreditAwardMap = new HashSet<>();

	// 节日帮贡
	private Set<JavaScript> faction_crontri = new HashSet<>();

	// 公会任务贡献资金
	private Set<JavaScript> factionTask_crontri = new HashSet<>();

	// 公会任务个人分红
	private Set<JavaScript> factionTaskProfit_crontri = new HashSet<>();

	// 公会任务个人分红
	private Set<JavaScript> shengWangAwardMap = new HashSet<>();

	// 宠物经验奖励
	private Set<JavaScript> petExpAwardMap = new HashSet<>();

	// NPC刷新表
	private Set<JavaScript> npcCountScriptList = new HashSet<>();
	
	// NPC刷新表
	private Set<JavaScript> npcControlCountScriptList = new HashSet<>();

	public static final AwardMgr instance = new AwardMgr();

	private AwardMgr() {

	}

	public static AwardMgr getInstance() {

		return instance;
	}

	private void initData() {
		award1Map = ConfigMgr.getInstance().getConf(fire.pb.game.SActivityAward.class);
		daward1Map = ConfigMgr.getInstance().getConf(fire.pb.game.SPointCardActivityAward.class);

		// 普通服奖励表getNpcCountScriptList
		for (SActivityAward award : award1Map.values()) {
			doJsInit((fire.pb.game.ActivityAward) award);
		}

		for (RefreshTimerNpc config : ConfigMgr.getInstance().getConf(SRefreshTimerNpc.class).values()) {
			JavaScript js = new JavaScript(config.getNpcCount());
			if (null != js.getJavaScriptString())
				npcCountScriptList.add(js);
			
			npcControlCountScriptList.add(new JavaScript(config.getNpcAllCount()));
		}

		// 点卡服奖励表
		for (SPointCardActivityAward award : daward1Map.values()) {
			doJsInit((fire.pb.game.ActivityAward) award);
		}

		for (RefreshTimerNpc config : ConfigMgr.getInstance().getConf(SRefreshTimerNpcDianKa.class).values()) {
			JavaScript js = new JavaScript(config.getNpcCount());
			if (null != js.getJavaScriptString())
				npcCountScriptList.add(js);
			
			npcControlCountScriptList.add(new JavaScript(config.getNpcAllCount()));
		}
	}

	private void doJsInit(fire.pb.game.ActivityAward award) {
		if (award.getMoney() != null)
			moneyAwardMap.add(new JavaScript(award.getMoney()));
		if (award.getGold() != null) {
			goldAwardMap.add(new JavaScript(award.getGold()));
		}
		if (award.getCreditValue() != null) {
			creditMap.add(new JavaScript(award.getCreditValue()));
		}
		if (award.getSchoolContribute() != null) {
			contributionAwardMap.add(new JavaScript(award.getSchoolContribute()));
		}
		if (award.getExp() != null)
			expAwardMap.add(new JavaScript(award.getExp()));
		if (award.getFestivalCredit() != null) {
			fesCreditAwardMap.add(new JavaScript(award.getFestivalCredit()));
		}

		if (award.getGangContribution() != null) {
			faction_crontri.add(new JavaScript(award.getGangContribution()));
		}

		if (award.getGangTaskContribution() != null) {
			factionTask_crontri.add(new JavaScript(award.getGangTaskContribution()));
		}

		if (award.getGangTaskProfit() != null) {
			factionTaskProfit_crontri.add(new JavaScript(award.getGangTaskProfit()));
		}

		if (award.getShengwang() != null) {
			shengWangAwardMap.add(new JavaScript(award.getShengwang()));
		}

		if (award.getPetExp() != null)
			petExpAwardMap.add(new JavaScript(award.getPetExp()));
	}

	// 金钱奖励
	public Set<JavaScript> getMoneyAwardList() {
		return moneyAwardMap;
	}

	// 金钱奖励
	public Set<JavaScript> getGoldAwardList() {
		return goldAwardMap;
	}

	// 信用值奖励
	public Set<JavaScript> getCreditAwardList() {
		return creditMap;
	}

	// 贡献奖励
	public Set<JavaScript> getContributionAwardList() {
		return contributionAwardMap;
	}

	// 经验奖励
	public Set<JavaScript> getExpAwardList() {
		return expAwardMap;
	}

	// 节日积分奖励
	public Set<JavaScript> getFesCreditAwardList() {
		return fesCreditAwardMap;
	}

	// 节日帮贡
	public Set<JavaScript> getFaction_crontriList() {
		return faction_crontri;
	}

	// 公会任务贡献资金
	public Set<JavaScript> getFactionTask_crontriList() {
		return factionTask_crontri;
	}

	// 公会任务个人分红
	public Set<JavaScript> getFactionTaskProfit_crontriList() {
		return factionTaskProfit_crontri;
	}

	// 公会任务个人分红
	public Set<JavaScript> getShengWangAwardList() {
		return shengWangAwardMap;
	}

	// 宠物经验奖励
	public Set<JavaScript> getPetExpAwardList() {
		return petExpAwardMap;
	}

	// NPC刷新表
	public Set<JavaScript> getNpcCountScriptList() {
		return npcCountScriptList;
	}

	public Set<JavaScript> getNpcControlCntScriptList() {
		return npcControlCountScriptList;
	}
	
	public void init() throws Exception {
		initData();
	}
}
