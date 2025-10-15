

package fire.pb.item.equip.diamond;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fire.log.LogManager;
import fire.log.YYLogger;
import fire.pb.event.AddGemToEquip;
import fire.pb.item.BagTypes;
import fire.pb.item.Equip;
import fire.pb.item.EquipItem;
import fire.pb.item.EquipItemShuXing;
import fire.pb.item.GemItem;
import fire.pb.item.ItemBase;
import fire.pb.item.Module;
import fire.pb.item.SAllEquipScore;
import fire.pb.item.SEquipLvGemInfo;
import fire.pb.item.SGetItemTips;
import fire.pb.ranklist.proc.PRoleZongheRankProc;
import fire.pb.skill.SceneSkillRole;
import fire.pb.talk.MessageMgr;
import mkdb.Procedure;

import com.locojoy.base.Octets;


public class PAttachGemToEquip extends Procedure {

	private final long roleId;

	private final int equipKey;

	private final int diamondKey;

	private final int equipbag;

	public PAttachGemToEquip(long roleId, int equipKey, int diamondKey,
			int equipbag) {

		super();
		this.roleId = roleId;
		this.equipKey = equipKey;
		this.diamondKey = diamondKey;
		this.equipbag = equipbag;
	}

	@Override
	protected boolean process() throws Exception {
		// 拿到背包的锁
		fire.pb.item.Pack bag = null;
		Equip equip = null;
		ItemBase equipItem = null;
		ItemBase diamondItem = null;
		bag = new fire.pb.item.Pack(roleId, false);

		if (fire.pb.buff.Module.existState(roleId,
				fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER)) {
			MessageMgr.psendMsgNotify(roleId, 150163, null);
			return false;
		}

		if (equipbag == 0) {
			equipItem = bag.getItem(equipKey);
			diamondItem = bag.getItem(diamondKey);
		} else if (equipbag == 1) {
			equip = (Equip) Module.getInstance().getItemMaps(roleId,
					BagTypes.EQUIP, false);
			equipItem = equip.getItem(equipKey);
			diamondItem = bag.getItem(diamondKey);
		} else {
			MessageMgr.psendMsgNotify(roleId, 150163, null);
			return false;
		}
		if (equipItem == null || diamondItem == null) {
			MessageMgr.psendMsgNotify(roleId, 150163, null);
			return false;
		}
		if (!(equipItem instanceof EquipItem)
				|| !(diamondItem instanceof GemItem)) {
			MessageMgr.psendMsgNotify(roleId, 150163, null);
			return false;
		}
		if ((equipItem.getFlags() & fire.pb.Item.ONSTALL) != 0
				|| (diamondItem.getFlags() & fire.pb.Item.ONSTALL) != 0) {
			MessageMgr.sendMsgNotify(roleId, DiamondConst.CANT_OPERATE, null);
			return false;
		}
		xbean.Equip equipAttr = ((EquipItem) equipItem).getEquipAttr();
		EquipItemShuXing eiAttr = (EquipItemShuXing) equipItem.getItemAttr();
		// 判断宝石镶嵌的部位
		int gemType = EquipDiamondMgr.getDiamondPropMap().get(
				diamondItem.getItemId()).type;

		List<Integer> gems = eiAttr.getCanGems();
		if (!gems.contains(gemType)) {
			MessageMgr.psendMsgNotify(roleId, 150163, null);
			return false;
		}

		int diamondLevel = diamondItem.getItemAttr().getLevel();

		SEquipLvGemInfo equipLvGemInfo = EquipDiamondMgr.getEquipLvGemInfoByLv(eiAttr.level);
		if(equipLvGemInfo == null) {
			LogManager.logger.error("error equipLv in PAttachGemToEquip itemId=" + eiAttr.id);
			return false;
		}
		
		int equipGemsLevel = equipLvGemInfo.gemsLevel;
		if (equipGemsLevel < diamondLevel) {
			MessageMgr.psendMsgNotify(roleId, 150163, null);
			return false;
		}

		// 取得洞数
		int holeNum = equipLvGemInfo.hols;
		if (holeNum == 0) {
			MessageMgr.psendMsgNotify(roleId, 150163, null);
			return false;
		}

		List<Integer> diamonds = equipAttr.getDiamonds();
		if (diamonds.size() >= holeNum) {
			MessageMgr.psendMsgNotify(roleId, 150163, null);
			return false;
		}

		int nHole = diamonds.size();
		if (equipLvGemInfo.holsLevel.get(nHole) > eiAttr.level) {
			MessageMgr.psendMsgNotify(roleId, 150163, null);
			return false;
		}

		int realRemove = bag.removeItemWithKey(diamondKey, 1,
				fire.log.enums.YYLoggerTuJingEnum.tujing_Value_xiangqian, 0,
				"add diamond to equip succ");
		if (realRemove != 1) {
			MessageMgr.psendMsgNotify(roleId, 150163, null);
			return false;
		}
		diamonds.add(diamondItem.getItemId());
		Set<Integer> diamondTypes = new HashSet<Integer>();
		for (Integer diamond : diamonds) {
			diamondTypes.add(EquipDiamondMgr.getDiamondPropMap().get(diamond).shape);
		}
		if (diamondTypes.size() > 3) {
			fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 142558, null);
			return false;
		}
		int score = fire.pb.item.Module.getInstance().getEquipScore(equipItem);
		((EquipItem) equipItem).getEquipAttr().setEquipscore(score);
		if (score >= ((EquipItem) equipItem).getItemAttr().getTreasureScore()) {
			((EquipItem) equipItem).getEquipAttr().setTreasure(1);
		}
		else
			((EquipItem) equipItem).getEquipAttr().setTreasure(0);

		// 发消息
		Octets tips = ((EquipItem) equipItem).getTips();
		int bagType = equipbag == 0 ? BagTypes.BAG : BagTypes.EQUIP;
		SGetItemTips send = new SGetItemTips(bagType, equipKey, tips);
		psendWhileCommit(roleId, send);
		MessageMgr.psendMsgNotifyWhileCommit(roleId, DiamondConst.NEST_SUCC, null);
		
		int totalScore = 0;
		if (bagType == BagTypes.EQUIP) {
			totalScore = Module.getInstance().getEquipTotalScore(roleId);
			SAllEquipScore equipTotalScore = new SAllEquipScore();
			equipTotalScore.score = totalScore;
			psendWhileCommit(roleId, equipTotalScore);
		}

		if (equipbag == 1) {
			SceneSkillRole sceneSkillRole = new SceneSkillRole(roleId);
			sceneSkillRole.addEquipEffectAndSkillWithSP((EquipItem) equipItem);
		}

		MessageMgr.psendMsgNotify(roleId, 150164, null);

		fire.pb.event.Poster.getPoster().dispatchEvent(
				new AddGemToEquip(roleId, equipItem.getItemId()));
		
		// 更新历程信息 (第二个参数为0)
		fire.pb.course.CourseManager.achieveCourse(roleId, fire.pb.course.CourseType.XIANGQIAN_COURSE, diamondLevel, 0);
		//更新历程
		fire.pb.course.CourseManager.achieveCourse(roleId, fire.pb.course.CourseType.EQUIP_ADDGEM_COURSE, equipItem.getItemAttr().level, diamonds.size());
		
		fire.pb.course.CourseManager.checkAchieveCourse(roleId, fire.pb.course.CourseType.XIANG_QIAN_GEM, 4, 0, diamondLevel);
		fire.pb.course.CourseManager.checkAchieveCourse(roleId, fire.pb.course.CourseType.XIANG_QIAN_GEM, 5, 0, diamondLevel);
		fire.pb.course.CourseManager.checkAchieveCourse(roleId, fire.pb.course.CourseType.XIANG_QIAN_GEM, 7, 0, diamondLevel);
		//更新历程全部装备镶嵌宝石
		if (equipbag == 1) {
			Equip.checkEquipDiamondCourse(roleId);
		}
		
		// 更新玩家综合实力排行榜
		mkdb.Procedure.pexecuteWhileCommit(new PRoleZongheRankProc(this.roleId));
		// 运营日志
		writeYYLogger(equip);
		return true;
	}

	/**
	 * 运营日志
	 * @param equip
	 */
	private void writeYYLogger(Equip equip) {
		if(equip == null) return;
		YYLogger.equiEmbLog(roleId, equip.getEquipsGemInfo().entrySet());
	}
}
