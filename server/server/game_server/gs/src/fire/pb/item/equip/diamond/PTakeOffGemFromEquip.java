package fire.pb.item.equip.diamond;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fire.pb.event.AddGemToEquip;
import fire.pb.item.BagTypes;
import fire.pb.item.Equip;
import fire.pb.item.EquipItem;
import fire.pb.item.ItemBase;
import fire.pb.item.Module;
import fire.pb.item.SAllEquipScore;
import fire.pb.item.SGetItemTips;
import fire.pb.skill.SceneSkillRole;
import fire.pb.talk.MessageMgr;
import mkdb.Procedure;

import com.locojoy.base.Octets;

public class PTakeOffGemFromEquip extends Procedure {

	private final long roleId;

	private final int equipKey;

	private final int diamondIdx;

	private final int equipbag;

	public PTakeOffGemFromEquip(long roleId, int equipKey, int diamondIdx,
			int equipbag) {

		super();
		this.roleId = roleId;
		this.equipKey = equipKey;
		this.diamondIdx = diamondIdx;
		this.equipbag = equipbag;
	}

	@Override
	protected boolean process() throws Exception {

		// 拿到背包的锁
		fire.pb.item.Pack bag = null;
		Equip equip = null;
		ItemBase equipItem = null;
		bag = new fire.pb.item.Pack(roleId, false);

		if (fire.pb.buff.Module.existState(roleId,
				fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER))
			return false;

		if (equipbag == 0) {
			equipItem = bag.getItem(equipKey);
		} else if (equipbag == 1) {
			equip = (Equip) Module.getInstance().getItemMaps(roleId,
					BagTypes.EQUIP, false);
			equipItem = equip.getItem(equipKey);
		} else {
			return true;
		}
		if (equipItem == null)
			return false;
		if (!(equipItem instanceof EquipItem))
			return false;
		xbean.Equip equipAttr = ((EquipItem) equipItem).getEquipAttr();
		List<Integer> diamonds = equipAttr.getDiamonds();
		// 判断宝石上是否有同类型的宝石,同类型的不能再镶嵌.不同类型的直接顶掉
		if (diamonds.size() <= diamondIdx)
			return false;
		Integer itemid = diamonds.get(diamondIdx);
		if (itemid == null)
			return false;
		diamonds.remove(diamondIdx);
		if (bag.doAddItem(itemid, 1, "remove gem from equip",
				fire.log.enums.YYLoggerTuJingEnum.tujing_Value_baoshi,
				equipItem.getItemId()) != 1) {
			fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 144915, null);
			return false;
		}
		Set<Integer> diamondTypes = new HashSet<Integer>();
		for (Integer diamond : diamonds) {
			diamondTypes.add(EquipDiamondMgr.getDiamondPropMap().get(
					diamond).shape);
		}
		if (diamondTypes.size() > 3) {
			fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 142558, null);
			return false;
		}

		int score = fire.pb.item.Module.getInstance().getEquipScore(equipItem);
		((EquipItem) equipItem).getEquipAttr().setEquipscore(score);
		if (score >= ((EquipItem) equipItem).getItemAttr().getTreasureScore()) {
			((EquipItem) equipItem).getEquipAttr().setTreasure(1);
		} else
			((EquipItem) equipItem).getEquipAttr().setTreasure(0);
		// 发消息
		Octets tips = ((EquipItem) equipItem).getTips();
		int bagType = equipbag == 0 ? BagTypes.BAG : BagTypes.EQUIP;
		SGetItemTips send = new SGetItemTips(bagType, equipKey, tips);
		psendWhileCommit(roleId, send);
		MessageMgr.psendMsgNotifyWhileCommit(roleId, DiamondConst.NEST_SUCC,
				null);

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
		MessageMgr.psendMsgNotifyWhileCommit(roleId, 160478, null);

		fire.pb.event.Poster.getPoster().dispatchEvent(
				new AddGemToEquip(roleId, equipItem.getItemId()));

		return true;
	}
}
