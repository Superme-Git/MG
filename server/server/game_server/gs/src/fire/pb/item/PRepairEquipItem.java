package fire.pb.item;

import java.util.List;

import fire.log.YYLogger;
import fire.pb.DataInit;
import fire.pb.buff.BuffConstant;
import fire.pb.common.SCommon;
import fire.pb.main.ConfigManager;
import fire.pb.product.Commontext;
import fire.pb.product.SErrorCode;
import fire.pb.talk.MessageMgr;
import fire.pb.util.Misc;
import mkdb.Procedure;

import org.apache.log4j.Logger;

public class PRepairEquipItem extends Procedure {
	private long roleId;
	private int equipKey;
	private int bagId; // 只有 1 和 3才能修理 (背包和装备栏)

	private int repairType;

	private ItemMaps bag = null;
	private Pack beibao = null;
	protected ItemShuXing attr;

	private static Logger logger = Logger.getLogger("ITEM");

	public PRepairEquipItem(long roleId, int equipId, int bagId, int repairType) {
		this.roleId = roleId;
		this.equipKey = equipId;
		this.bagId = bagId;
		this.repairType = repairType;
	}

	@Override
	public boolean process() {
		if (bagId != fire.pb.item.BagTypes.BAG && bagId != fire.pb.item.BagTypes.EQUIP)
		{
			Module.logger.debug("背包类型不正确");
			return false;
		}
		
		if (bagId == fire.pb.item.BagTypes.EQUIP && fire.pb.buff.Module.existState(roleId, BuffConstant.StateType.STATE_BATTLE_FIGHTER)) {
			return false;
		}
		
		// 如果有队伍,先得到team锁,避免死锁发生			add by yebin
		fire.pb.team.TeamManager.getTeamByRoleId(roleId);
		
		switch(bagId) {
		case fire.pb.item.BagTypes.BAG:
			bag = (Pack)fire.pb.item.Module.getInstance().getItemMaps(roleId, bagId, false);
			break;
		case fire.pb.item.BagTypes.EQUIP:
			bag = (Equip)fire.pb.item.Module.getInstance().getItemMaps(roleId, bagId, false);
			break;
		default: 
			Module.logger.debug("背包类型不正确");
			return false;
		}
		
		beibao = (Pack)fire.pb.item.Module.getInstance().getItemMaps(roleId, fire.pb.item.BagTypes.BAG, false);
		
		final SErrorCode sErrorCode = new SErrorCode();
		
		ItemBase bi = bag.getItem(equipKey);
		if (!(bi instanceof EquipItem)) {
			Module.logger.debug("修理的物品不是装备");
			return false;
		}		

		attr = Module.getInstance().getItemManager().getAttr(bi.getItemId());
		if (!(attr instanceof EquipItemShuXing)){
			Module.logger.debug("修理的物品不是装备");
			return false;
		}

		EquipItemShuXing equipattr = (EquipItemShuXing)attr;
		EquipItem ei = (EquipItem)bi;

		//检查最大耐久
		if (ei.getCurMaxEndure() == 0) {
			logger.error("Role: " + roleId + "修理装备： " + equipattr.id
					+ "最大耐久度为0");
			return false;
		}
		
		//检查失败次数
		int nfailTimes = ei.getFailTimes();
		if (repairType == 0) { //失败次数大于2次不能特殊修理
			if (nfailTimes > 2) {
				logger.error("Role: " + roleId + "修理装备： " + equipattr.id
						+ "修理次数大于3次");
				MessageMgr.psendMsgNotifyWhileRollback(roleId, 160199, null);
				return false;
			}
		}
		
		
		//判断当前耐久是否需要修理
		final SCommon cc = fire.pb.main.ConfigManager.getInstance().getConf(SCommon.class).get(DataInit.EQUIP_CAN_REPAIR);
		if (cc == null) {
			return false;
		}
		if (ei.getEndure() > (ei.getCurMaxEndure() * Double.parseDouble(cc.getValue()))) {
			//不需要修理
			MessageMgr.psendMsgNotifyWhileRollback(roleId, 160031, null);
			return false;
		}
		
		//检查最大耐久和当前耐久
		if (ei.getCurMaxEndure() == ei.getEndure()) {
			logger.error("Role: " + roleId + "修理装备： " + equipattr.id
					+ "耐久度等于最大耐久");
			return false;
		}
		
		//1.检查材料是否足够           3.普通修理成功机率计算
		int nIndex = checkMaterialListIsOk(equipattr.commonidlist, equipattr.commonnumlist);
		
		if (nIndex == -1) {
			sErrorCode.errorcode = Commontext.XIU_LI_CAI_LIAO_NOT_ENOUGH;
			Procedure.psendWhileCommit(roleId, sErrorCode);
			return true;
		}
		
		if (repairType == 1) {
			if (!checkMaterialIsOk(equipattr.tsxlcailiaoid, 
					equipattr.tsxlcailiaonum)) {
				sErrorCode.errorcode = Commontext.XIU_LI_CAI_LIAO_NOT_ENOUGH;
				Procedure.psendWhileCommit(roleId, sErrorCode);
				return true;
			}
		}
		
		//2.扣钱
		switch(repairType)
		{
		case 0:
			{
				int money = equipattr.ptxlmoneynum;		
				if (money > beibao.getMoney()) {
					sErrorCode.errorcode = Commontext.MONEY_NOT_ENOUGH;
					Procedure.psendWhileCommit(roleId, sErrorCode);
					return true;
				}
				//扣钱
				if(beibao.subMoney(-money, Commontext.REASON_STRING,
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_xiuli, 0) != -money){
					
					return false;
				}
			}
			break;
		case 1:
			{
				int money = equipattr.ptxlmoneynum;		
				if (money > beibao.getMoney()) {
					sErrorCode.errorcode = Commontext.MONEY_NOT_ENOUGH;
					Procedure.psendWhileCommit(roleId, sErrorCode);
					return true;
				}
				//扣钱
				if(beibao.subMoney(-money, Commontext.REASON_STRING,
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_xiuli, 0) != -money){
					
					return false;
				}
			}
			break;
		default:
			break;
		}
		
		
		fire.pb.item.ItemBase cailiao = getItemKey(equipattr.commonidlist.get(nIndex));
		if (cailiao == null) {
			fire.pb.talk.MessageMgr.psendMsgNotify(roleId, ItemBase.ITEM_LOCK_MSG, null);			
			return false;
		}		
		beibao.removeItemWithKey(cailiao.getKey(), equipattr.commonnumlist.get(nIndex),
				fire.log.enums.YYLoggerTuJingEnum.tujing_Value_xiuli, 5,
				Commontext.REASON_STRING);
		logger.debug("Role: " + roleId + "背包栏删除道具： " + equipattr.commonnumlist.get(nIndex)
				+ "用于" + Commontext.REASON_STRING);
		
		if (repairType == 1)
		{
			fire.pb.item.ItemBase tsCaiLiao = getItemKey(equipattr.tsxlcailiaoid);
			if (tsCaiLiao == null) {
				fire.pb.talk.MessageMgr.psendMsgNotify(roleId, ItemBase.ITEM_LOCK_MSG, null);			
				return false;
			}		
			beibao.removeItemWithKey(tsCaiLiao.getKey(), equipattr.tsxlcailiaonum,
					fire.log.enums.YYLoggerTuJingEnum.tujing_Value_xiuli, 5,
					Commontext.REASON_STRING);
			logger.debug("Role: " + roleId + "背包栏删除道具： " + equipattr.tsxlcailiaonum
					+ "用于" + Commontext.REASON_STRING);
		}
		
		//开始修理了吧
		if (repairType == 0)
		{
			//随机普通修理成功机率
			if (Misc.getRatePercent() < equipattr.ptxlfailrate)
			{
				
				//1.增加失败次数				
				ei.setFailTimes(nfailTimes + 1);
				//2.减少耐久上限
				switch (nfailTimes + 1)
				{
				case 1:					
					ei.setCurMaxEndure((int)(equipattr.maxnaijiu*0.8));
					break;
				case 2:					
					ei.setCurMaxEndure((int)(equipattr.maxnaijiu*0.6));
					break;
				case 3:
					ei.setCurMaxEndure((int)(equipattr.maxnaijiu*0.4));
					break;
				default:
					ei.setCurMaxEndure(0);
					break;
				}
				
				//成功
				ei.setEndure(ei.getCurMaxEndure());
				SRepairResult repairResult = new SRepairResult();
				repairResult.ret = 1;
				Procedure.psendWhileCommit(roleId, repairResult);
				
				//修理失败也加耐久
				if (bagId == fire.pb.item.BagTypes.EQUIP) {
					fire.pb.skill.SceneSkillRole role = fire.pb.skill.SkillManager.getSceneSkillRole(roleId);
					role.addEquipEffectAndSkillWithSP(ei);
				}
				
				fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 160030, null);
			}
			else {
				//成功
				ei.setEndure(ei.getCurMaxEndure());
				SRepairResult repairResult = new SRepairResult();
				repairResult.ret = 1;
				Procedure.psendWhileCommit(roleId, repairResult);
				
				if (bagId == fire.pb.item.BagTypes.EQUIP) {
					fire.pb.skill.SceneSkillRole role = fire.pb.skill.SkillManager.getSceneSkillRole(roleId);
					role.addEquipEffectAndSkillWithSP(ei);
				}
				
				fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 160029, null);
			}
		}
		else if (repairType == 1) {
			//直接成功
			int addEndure = 0;
			fire.pb.common.SCommon common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(215);
			if (common != null)
				addEndure = Integer.parseInt(common.getValue());
			else
				addEndure = 100;
			
			if ((addEndure + ei.getEndure()) > ei.getCurMaxEndure()) {
				ei.setEndure(ei.getCurMaxEndure());
			}
			else {
				ei.setEndure(addEndure + ei.getEndure());
			}
			
			if (bagId == fire.pb.item.BagTypes.EQUIP) {
				fire.pb.skill.SceneSkillRole role = fire.pb.skill.SkillManager.getSceneSkillRole(roleId);
				role.addEquipEffectAndSkillWithSP(ei);
			}
			
			SRepairResult repairResult = new SRepairResult();
			repairResult.ret = 1;
			Procedure.psendWhileCommit(roleId, repairResult);
			fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 160029, null);
		}
		
		// 运营日志
		writeYYLogger(equipattr, ei);
		return true;
	}

	/**
	 * 运营日志
	 * @param equipattr
	 * @param ei
	 */
	private void writeYYLogger(EquipItemShuXing equipattr, EquipItem ei) {
		if(equipattr == null || ei == null) return;
		
		fire.log.beans.OpEquiRepBean opEquiRepBean = 
				new fire.log.beans.OpEquiRepBean(equipattr.getId(), 
						equipattr.getLevel(), equipattr.getRare(), ei.getItemId(), 
						ei.getEndure(), repairType);
		YYLogger.equiRepLog(roleId, opEquiRepBean);
	}

	private boolean checkMaterialIsOk(int materialID, int num) {
		int currentNum = beibao.getItemNum(materialID, 0);
		if (currentNum >= num) {
			return true;
		}

		return false;
	}

	private int checkMaterialListIsOk(List<Integer> materialIDList, List<Integer> numList) {
		int iIndex = 0;
		for (Integer materialID : materialIDList) {
			int currentNum = beibao.getItemNum(materialID, 0);
			if (currentNum >= numList.get(iIndex)) {
				return iIndex;
			}

			iIndex++;
		}

		return -1;
	}

	private fire.pb.item.ItemBase getItemKey(int itemId) {
		for (fire.pb.item.ItemBase item : beibao) {
			if (itemId == item.getItemId()) {
				return item;
			}
		}
		return null;
	}

}
