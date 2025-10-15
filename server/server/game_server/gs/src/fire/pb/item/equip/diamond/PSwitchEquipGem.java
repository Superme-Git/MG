

package fire.pb.item.equip.diamond;

import java.util.ArrayList;
import java.util.List;

import com.locojoy.base.Octets;

import fire.log.LogManager;
import fire.pb.event.AddGemToEquip;
import fire.pb.item.BagTypes;
import fire.pb.item.Equip;
import fire.pb.item.EquipItem;
import fire.pb.item.EquipItemShuXing;
import fire.pb.item.ItemBase;
import fire.pb.item.Module;
import fire.pb.item.Pack;
import fire.pb.item.SAllEquipScore;
import fire.pb.item.SEquipLvGemInfo;
import fire.pb.item.SGetItemTips;
import fire.pb.ranklist.proc.PRoleZongheRankProc;
import fire.pb.skill.SceneSkillRole;
import fire.pb.talk.MessageMgr;
import mkdb.Procedure;

public class PSwitchEquipGem extends Procedure {
	private final long roleId;
	private final int srcEquipKey;
	private final int desEquipKey;

	public PSwitchEquipGem(final long roleId, final int srcEquipKey, final int desEquipKey) {
		this.roleId = roleId;
		this.srcEquipKey = srcEquipKey;
		this.desEquipKey = desEquipKey;
	}

	@Override
	protected boolean process() throws Exception {
		
		
		Pack equipSrc = null;
		ItemBase equipItemSrc = null;
		
		
		Equip equipDes = null;
		ItemBase equipItemDes = null;

		if (fire.pb.buff.Module.existState(roleId,
				fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER)) {
			MessageMgr.psendMsgNotify(roleId, 150163, null);
			return false;
		}

		//源装备必须在背包栏
		equipSrc = (Pack) Module.getInstance().getItemMaps(roleId, BagTypes.BAG, false);
		equipItemSrc = equipSrc.getItem(srcEquipKey);
		
		//目标装备必须在装备栏
		equipDes = (Equip) Module.getInstance().getItemMaps(roleId, BagTypes.EQUIP, false);
		equipItemDes = equipDes.getItem(desEquipKey);
		
		//背包栏
		if (equipItemSrc == null) {
			MessageMgr.psendMsgNotify(roleId, 150163, null);
			return false;
		}
		if (!(equipItemSrc instanceof EquipItem)) {
			MessageMgr.psendMsgNotify(roleId, 150163, null);
			return false;
		}
		if ((equipItemSrc.getFlags() & fire.pb.Item.ONSTALL) != 0) {
			MessageMgr.sendMsgNotify(roleId, DiamondConst.CANT_OPERATE, null);
			return false;
		}
		
		//装备栏
		if (equipItemDes == null) {
			MessageMgr.psendMsgNotify(roleId, 150163, null);
			return false;
		}
		if (!(equipItemDes instanceof EquipItem)) {
			MessageMgr.psendMsgNotify(roleId, 150163, null);
			return false;
		}
		if ((equipItemDes.getFlags() & fire.pb.Item.ONSTALL) != 0) {
			MessageMgr.sendMsgNotify(roleId, DiamondConst.CANT_OPERATE, null);
			return false;
		}
		
		//目标装备,在装备栏上
		xbean.Equip equipDesAttr = ((EquipItem) equipItemDes).getEquipAttr();
		EquipItemShuXing eiDesAttr = (EquipItemShuXing) equipItemDes.getItemAttr();
		SEquipLvGemInfo equipLvGemInfo = EquipDiamondMgr.getEquipLvGemInfoByLv(eiDesAttr.level);
		if(equipLvGemInfo == null) {
			LogManager.logger.error("error equipLv in PPutOnEquip itemId=" + eiDesAttr.id);
			return false;
		}
		
		// 取得洞数
		int holeNum = equipLvGemInfo.hols;
		if (holeNum == 0) {
			MessageMgr.psendMsgNotify(roleId, 150163, null);
			return false;
		}
		
		// 目标装备宝石列表
		List<Integer> diamondsDes = equipDesAttr.getDiamonds();
		if (diamondsDes.size() > 0)
			return true;
		
		// 判断宝石镶嵌的部位 获取源装备上的所有宝石
		xbean.Equip equipSrcAttr = ((EquipItem) equipItemSrc).getEquipAttr();
		// 源装备宝石列表
		List<Integer> diamonds = equipSrcAttr.getDiamonds();
		if (diamonds.size() == 0)
			return true;
		
		// 需要镶嵌到目标装备的宝石ID列表
		List<Integer> diamondDesId = new ArrayList<Integer>(diamonds.size());
		// 需要放到背包的宝石ID列表
		List<Integer> diamondRetId = new ArrayList<Integer>(diamonds.size());
		
		List<Integer> gems = eiDesAttr.getCanGems();
		//装备可镶嵌的宝石等级
		final int canGemLevel = equipLvGemInfo.gemsLevel;
		Integer hols = 0;
		for (Integer diamondId : diamonds) {
			int gemType = EquipDiamondMgr.getDiamondPropMap().get(diamondId).type;
			int gemLevel = EquipDiamondMgr.getDiamondPropMap().get(diamondId).level;
			
			boolean canGem = false;
			do {
				if (equipLvGemInfo.holsLevel.size() <= hols)
					break;
				if (equipLvGemInfo.holsLevel.get(hols) > eiDesAttr.level)
					break;
				
				if (!gems.contains(gemType))
					break;
				
				if (canGemLevel < gemLevel) {
					MessageMgr.psendMsgNotifyWhileRollback(roleId, 160067, null);
					return false;
				}
				
				diamondDesId.add(diamondId);
				hols ++;
				canGem = true;
				
			} while (false);
			
			if (!canGem)
				diamondRetId.add(diamondId);
		}
		
		if (diamondDesId.size() == 0)
			return false;
		
		//开始镶嵌宝石
		equipDesAttr.getDiamonds().addAll(diamondDesId);
		equipSrcAttr.getDiamonds().clear();
		equipSrcAttr.getDiamonds().addAll(diamondRetId);
		
		//源装备评分改变
		int score = fire.pb.item.Module.getInstance().getEquipScore(equipItemSrc);
		((EquipItem) equipItemSrc).getEquipAttr().setEquipscore(score);
		if (score >= ((EquipItem) equipItemSrc).getItemAttr().getTreasureScore()) {
			((EquipItem) equipItemSrc).getEquipAttr().setTreasure(1);
		}
		else
			((EquipItem) equipItemSrc).getEquipAttr().setTreasure(0);
		//目标装备评分改变
		int scoredes = fire.pb.item.Module.getInstance().getEquipScore(equipItemDes);
		((EquipItem) equipItemDes).getEquipAttr().setEquipscore(scoredes);
		if (score >= ((EquipItem) equipItemDes).getItemAttr().getTreasureScore()) {
			((EquipItem) equipItemDes).getEquipAttr().setTreasure(1);
		}
		else
			((EquipItem) equipItemDes).getEquipAttr().setTreasure(0);

		// 发消息
		Octets tips = ((EquipItem) equipItemSrc).getTips();
		//源装备tips改变
		SGetItemTips send = new SGetItemTips(BagTypes.BAG, equipItemSrc.getKey(), tips);
		psendWhileCommit(roleId, send);
		
		// 发消息
		Octets tips2 = ((EquipItem) equipItemDes).getTips();
		//目标装备tips改变
		SGetItemTips send2 = new SGetItemTips(BagTypes.EQUIP, equipItemDes.getKey(), tips2);
		psendWhileCommit(roleId, send2);
		MessageMgr.psendMsgNotifyWhileCommit(roleId, DiamondConst.NEST_SUCC, null);

		int totalScore = 0;
		totalScore = Module.getInstance().getEquipTotalScore(roleId);
		SAllEquipScore equipTotalScore = new SAllEquipScore();
		equipTotalScore.score = totalScore;
		psendWhileCommit(roleId, equipTotalScore);

		SceneSkillRole sceneSkillRole = new SceneSkillRole(roleId);
		sceneSkillRole.addEquipEffectAndSkillWithSP((EquipItem) equipItemDes);

		MessageMgr.psendMsgNotify(roleId, 160401, null);
		fire.pb.event.Poster.getPoster().dispatchEvent(
				new AddGemToEquip(roleId, equipItemDes.getItemId()));

		// 更新玩家综合实力排行榜
		mkdb.Procedure.pexecuteWhileCommit(new PRoleZongheRankProc(this.roleId));
		return true;
	}

}
