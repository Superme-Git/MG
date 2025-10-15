package fire.pb.item.equip.diamond;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.locojoy.base.Octets;

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

public class PReplaceGemFromEquip extends Procedure {
	
	private final long roleId;
	private final int equipKey;
	private final int diamondKey;	
	private final int equipBag;
	private final int gemIndex; 
	
	public PReplaceGemFromEquip(long roleId, int equipkey, int diamondkey, int equipbag, int gemindex) {
		this.roleId = roleId;
		this.equipKey = equipkey;
		this.diamondKey = diamondkey;
		this.equipBag = equipbag;
		this.gemIndex = gemindex;
	}
	
	protected boolean process() throws Exception {
		// 拿到背包的锁
		fire.pb.item.Pack bag = null;
		Equip equip = null;
		ItemBase equipItem = null;
		ItemBase diamondItem = null;
		bag = new fire.pb.item.Pack(roleId, false);
		
		if(fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER))
			return false;
		
		if (equipBag == 0) {
			equipItem = bag.getItem(equipKey);
			diamondItem = bag.getItem(diamondKey);
		} else if (equipBag == 1) {
			equip = (Equip) Module.getInstance().getItemMaps(roleId, BagTypes.EQUIP, false);
			equipItem = equip.getItem(equipKey);
			diamondItem = bag.getItem(diamondKey);
		} else {
			return true;
		}
		if (equipItem == null || diamondItem == null)
			return false;
		if (!(equipItem instanceof EquipItem) || !(diamondItem instanceof GemItem))
			return false;
		
		if ((equipItem.getFlags() & fire.pb.Item.ONSTALL) != 0||
				(diamondItem.getFlags() & fire.pb.Item.ONSTALL) != 0) 
		{MessageMgr.sendMsgNotify(roleId, DiamondConst.CANT_OPERATE, null);
			return false;}
		
        xbean.Equip equipAttr = ((EquipItem)equipItem).getEquipAttr();
		EquipItemShuXing eiAttr = (EquipItemShuXing)equipItem.getItemAttr();
		//判断宝石镶嵌的部位        
        int gemType = EquipDiamondMgr.getDiamondPropMap().get(diamondItem.getItemId()).type;
        List<Integer> gems = eiAttr.getCanGems();
        if (!gems.contains(gemType)) {
        	MessageMgr.psendMsgNotify(roleId, 150165, null);
        	return false;
        }
        
        SEquipLvGemInfo equipLvGemInfo = EquipDiamondMgr.getEquipLvGemInfoByLv(eiAttr.level);
		if(equipLvGemInfo == null) {
			LogManager.logger.error("error equipLv in PReplaceGemFromEquip itemId=" + eiAttr.id);
			return false;
		}
		
        int diamondLevel = diamondItem.getItemAttr().getLevel();        
        int equipGemsLevel = equipLvGemInfo.gemsLevel;
        if (equipGemsLevel < diamondLevel) {  //宝石等级超过装备可镶嵌的等级
        	MessageMgr.psendMsgNotify(roleId, 150165, null);
	    	return false;
    	}        
        
        int holeNum = equipLvGemInfo.hols;
        if (holeNum == 0) {
        	MessageMgr.psendMsgNotify(roleId, 150165, null);
        	return false;
        }
        	
        //先把原来的宝石拿掉
        List<Integer> diamonds = equipAttr.getDiamonds();
        if (diamonds.size() <= gemIndex) {
        	MessageMgr.psendMsgNotify(roleId, 150165, null);
        	return false;
        }
        Integer itemid = diamonds.get(gemIndex);
        if (itemid == null) {
        	MessageMgr.psendMsgNotify(roleId, 150165, null);
        	return false;
        }
        diamonds.remove(gemIndex);
        if (bag.doAddItem(itemid, 1, "remove gem from equip", 
        		fire.log.enums.YYLoggerTuJingEnum.tujing_Value_xiangqian, 
        		equipItem.getItemId()) != 1) {
        	fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 144915, null);
        	return false;
        }
        
        //镶嵌新的宝石
        int nHole = diamonds.size();
        if (equipLvGemInfo.holsLevel.get(nHole) > eiAttr.level) {
        	MessageMgr.psendMsgNotify(roleId, 150165, null);
        	return false;  
        }
        
		
        int realRemove = bag.removeItemWithKey(diamondKey, 1, 
        		fire.log.enums.YYLoggerTuJingEnum.tujing_Value_xiangqian, 0, "add diamond to equip succ");
        if (realRemove != 1) {
        	MessageMgr.psendMsgNotify(roleId, 150165, null);
        	return false;  
        }
        
        //镶嵌        
        diamonds.add(diamondItem.getItemId());
        
        Set<Integer> diamondTypes = new HashSet<Integer>();
        for (Integer diamond : diamonds) {
			diamondTypes.add(EquipDiamondMgr.getDiamondPropMap().get(diamond).shape);
		}
        if (diamondTypes.size() > 3) {
        	fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 142558,  null);
        	return false;
        }
        
        int score = fire.pb.item.Module.getInstance().getEquipScore(equipItem);
        ((EquipItem)equipItem).getEquipAttr().setEquipscore(score);
        if (score >= ((EquipItem) equipItem).getItemAttr().getTreasureScore()) {
			((EquipItem) equipItem).getEquipAttr().setTreasure(1);
		}
        else
			((EquipItem) equipItem).getEquipAttr().setTreasure(0);

        //发消息
        Octets tips = ((EquipItem)equipItem).getTips();
        int bagType = equipBag == 0 ? BagTypes.BAG : BagTypes.EQUIP;
        SGetItemTips send = new SGetItemTips(bagType, equipKey, tips);
        psendWhileCommit(roleId, send);
        MessageMgr.psendMsgNotifyWhileCommit(roleId, DiamondConst.NEST_SUCC, null);
        //替换宝石成功
        MessageMgr.psendMsgNotifyWhileCommit(roleId, 160483, null);
        
        int totalScore  = 0;
        if (bagType == BagTypes.EQUIP) {
        	totalScore = Module.getInstance().getEquipTotalScore(roleId);;
			SAllEquipScore equipTotalScore = new SAllEquipScore();
			equipTotalScore.score = totalScore;
			psendWhileCommit(roleId, equipTotalScore);
        }
        
        if (equipBag == 1) {
        	SceneSkillRole sceneSkillRole = new SceneSkillRole(roleId);
        	sceneSkillRole.addEquipEffectAndSkillWithSP((EquipItem) equipItem);
        }
        fire.pb.event.Poster.getPoster().dispatchEvent(new AddGemToEquip(roleId, equipItem.getItemId()));
        //更新玩家综合实力排行榜
      	mkdb.Procedure.pexecuteWhileCommit(new PRoleZongheRankProc(this.roleId));
      	
      	// 运营日志
      	writeYYLogger(equip);
		return true;
	}

	private void writeYYLogger(Equip equip) {
		if(equip == null) return;
		YYLogger.equiEmbLog(roleId, equip.getEquipsGemInfo().entrySet());
	}
}
