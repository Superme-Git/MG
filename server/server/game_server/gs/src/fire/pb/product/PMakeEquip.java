package fire.pb.product;

import java.util.ArrayList;
import java.util.List;

import fire.log.YYLogger;
import fire.pb.PropRole;
import fire.pb.game.MoneyType;
import fire.pb.item.AddItemResult;
import fire.pb.item.BagTypes;
import fire.pb.item.EquipItem;
import fire.pb.item.EquipMakeEvent;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMgr;
import fire.pb.item.ItemShuXing;
import fire.pb.item.Module;
import fire.pb.item.Pack;
import fire.pb.item.SEquipMakeInfo;
import fire.pb.item.SGetItemTips;
import fire.pb.item.make.ItemMakeFactory;
import fire.pb.item.make.ItemMakeUtil;
import fire.pb.product.Commontext;
import fire.pb.product.SErrorCode;
import fire.pb.product.SProductMadeUp;
import fire.pb.talk.MessageMgr;
import fire.pb.team.TeamManager;
import fire.pb.util.Misc;
import mkdb.Procedure;

import org.apache.log4j.Logger;

import com.locojoy.base.Octets;

public class PMakeEquip extends Procedure {

	private long roleId;
	private int equipId; // 要打造的装备ID
	private int dazaoType; // 打造类型 0 普通打造; 1 强化打造

	private Pack bag = null;
	protected ItemShuXing attr;

	// 成功的消息提示
	private static final int PRODUCE_SUCCESS = 140797;

	private static Logger logger = Logger.getLogger("ITEM");

	public PMakeEquip(long role, int equipid) {
		this.roleId = role;
		this.equipId = equipid;
		this.dazaoType = 0; // 普通打造
	}

	public PMakeEquip(long role, int equipid, int dazao_type) {
		this.roleId = role;
		this.equipId = equipid;
		this.dazaoType = dazao_type;
	}

	@Override
	public boolean process() {

		bag = (Pack) fire.pb.item.Module.getInstance().getItemMaps(roleId,
				fire.pb.item.BagTypes.BAG, false);
		final SErrorCode sErrorCode = new SErrorCode();

		ItemMgr itemManager = Module.getInstance().getItemManager();
		SEquipMakeInfo makeInfo = ItemMakeUtil.EQUIPMAKEINFO_CFGS.get(equipId);
		if (makeInfo == null)
			return false;

		attr = itemManager.getAttr(equipId);

		if (!checkMaterialIsEnough(makeInfo.getTuzhiid(),/* attr.get等级(), */
				makeInfo.getTuzhinum())) {
			sErrorCode.errorcode = Commontext.TUZHI_NOT_ENOUGH;
			Procedure.psendWhileCommit(roleId, sErrorCode);
			return true;
		}
		
		int nTieIndex = checkMaterialListIsOk(makeInfo.vcailiaotie, makeInfo.vcailiaotienum);
		if (nTieIndex == -1) {
			sErrorCode.errorcode = Commontext.TIE_NOT_ENOUGH;
			Procedure.psendWhileCommit(roleId, sErrorCode);
			return true;
		}
		
		int nZhizaofuIndex = checkMaterialListIsOk(makeInfo.vcailiaozhizaofu, makeInfo.vcailiaozhizaofunum);
		if (nZhizaofuIndex == -1) {
			sErrorCode.errorcode = Commontext.ZHI_ZHAO_FU_NOT_ENOUGH;
			Procedure.psendWhileCommit(roleId, sErrorCode);
			return true;
		}

		PropRole propRole = new PropRole(roleId, true);
		if (dazaoType == 1) {
			// 检测强化石是否足够
			if (!checkQiangHuaShiIsOk(makeInfo.getQianghuaid(),
					makeInfo.getQianghuanum())) {
				sErrorCode.errorcode = Commontext.QIANG_HUA_SHI_NOT_ENOUGH;
				Procedure.psendWhileCommit(roleId, sErrorCode);
				return true;
			}
		}

		// 检查钱是否够
		int money = makeInfo.moneynum;
		if (money > bag.getMoney()) {
			sErrorCode.errorcode = Commontext.MONEY_NOT_ENOUGH;
			Procedure.psendWhileCommit(roleId, sErrorCode);
			return true;
		}
		// 扣钱
		if (bag.subMoney(-money, Commontext.REASON_STRING,
				fire.log.enums.YYLoggerTuJingEnum.tujing_Value_dazao, 0) != -money) {
			sErrorCode.errorcode = Commontext.MONEY_NOT_ENOUGH;
			Procedure.psendWhileCommit(roleId, sErrorCode);
			return true;
		}

		/**
		 * 在背包中移除用于打造的各种材料
		 */
		// 图纸
		fire.pb.item.ItemBase tuzhi = getItemKey(makeInfo.getTuzhiid());
		if (tuzhi == null) {
			fire.pb.talk.MessageMgr.psendMsgNotify(roleId,
					ItemBase.ITEM_LOCK_MSG, null);
			return false;
		}
		int removeitemkey = bag.removeItemWithKey(tuzhi.getKey(), makeInfo.getTuzhinum(),
				fire.log.enums.YYLoggerTuJingEnum.tujing_Value_dazao, 5,
				Commontext.REASON_STRING);
		if (removeitemkey != makeInfo.getTuzhinum()) {
			fire.pb.talk.MessageMgr.psendMsgNotify(roleId,
					ItemBase.ITEM_LOCK_MSG, null);
			return false;
		}
		logger.debug("Role: " + roleId + "背包栏删除道具： " + makeInfo.getTuzhinum()
				+ "用于" + Commontext.REASON_STRING);
				
		// 铁
		fire.pb.item.ItemBase hantie = getItemKey(makeInfo.vcailiaotie.get(nTieIndex));
		if (hantie == null) {
			fire.pb.talk.MessageMgr.psendMsgNotify(roleId,
					ItemBase.ITEM_LOCK_MSG, null);
			return false;
		}
		removeitemkey = bag.removeItemWithKey(hantie.getKey(), makeInfo.vcailiaotienum.get(nTieIndex),
				fire.log.enums.YYLoggerTuJingEnum.tujing_Value_dazao, 5,
				Commontext.REASON_STRING);
		if (removeitemkey != makeInfo.vcailiaotienum.get(nTieIndex)) {
			fire.pb.talk.MessageMgr.psendMsgNotify(roleId,
					ItemBase.ITEM_LOCK_MSG, null);
			return false;
		}
		logger.debug("Role: " + roleId + "背包栏删除道具： " + makeInfo.getHantieid()
				+ "用于" + Commontext.REASON_STRING);

		// 制造符
		fire.pb.item.ItemBase zizhaofu = getItemKey(makeInfo.vcailiaozhizaofu.get(nZhizaofuIndex));
		if (zizhaofu == null) {
			fire.pb.talk.MessageMgr.psendMsgNotify(roleId,
					ItemBase.ITEM_LOCK_MSG, null);
			return false;
		}
		removeitemkey = bag.removeItemWithKey(zizhaofu.getKey(), makeInfo.vcailiaozhizaofunum.get(nZhizaofuIndex),
				fire.log.enums.YYLoggerTuJingEnum.tujing_Value_dazao, 5,
				Commontext.REASON_STRING);
		if (removeitemkey != makeInfo.vcailiaozhizaofunum.get(nZhizaofuIndex)) {
			fire.pb.talk.MessageMgr.psendMsgNotify(roleId,
					ItemBase.ITEM_LOCK_MSG, null);
			return false;
		}
		logger.debug("Role: " + roleId + "背包栏删除道具： "
				+ makeInfo.getZhizaofunum() + "用于" + Commontext.REASON_STRING);

		// 强化石
		int consuStoId = 0;
		int consuStoNum = 0;
		if (dazaoType == 1) {
			fire.pb.item.ItemBase qianghuashi = getItemKey(makeInfo.getQianghuaid());
			if (qianghuashi == null) {
				fire.pb.talk.MessageMgr.psendMsgNotify(roleId,
						ItemBase.ITEM_LOCK_MSG, null);
				return false;
			}
			int removenum = bag.removeItemById(makeInfo.getQianghuaid(),
					makeInfo.getQianghuanum(),
					fire.log.enums.YYLoggerTuJingEnum.tujing_Value_dazao, 5,
					Commontext.REASON_STRING);
			if (removenum != makeInfo.getQianghuanum()) {
				fire.pb.talk.MessageMgr.psendMsgNotify(roleId,
						ItemBase.ITEM_LOCK_MSG, null);
				return false;
			}
			consuStoId = makeInfo.getQianghuaid();
			consuStoNum = makeInfo.getQianghuanum();
			logger.debug("Role: " + roleId + "背包栏删除道具： "
					+ makeInfo.getQianghuanum() + "用于" + Commontext.REASON_STRING);
		}
		
		int chanchuId = 0;
		if (dazaoType == 0) {
			int nIndex = Misc.getProbability(makeInfo.getPtdazhaorate());
			if (nIndex == -1) {
				throw new IllegalArgumentException("pt: makeInfo rate is invalid");
			}
			chanchuId = makeInfo.getPtdazhaoid().get(nIndex);
		}
		else if (dazaoType == 1) {
			int nIndex = Misc.getProbability(makeInfo.getQhdazhaorate());
			if (nIndex == -1) {
				throw new IllegalArgumentException("qh: makeInfo rate is invalid");
			}
			chanchuId = makeInfo.getQhdazhaoid().get(nIndex);
		}
		
		if (chanchuId == 0) {
			throw new IllegalArgumentException("makeInfo: index is invalid");
		}

		// 创建装备和计算相关动态属性值
		EquipItem equipItem = createEquipItemWithAllAttr(chanchuId,	propRole.getName());
		if (equipItem == null) {
			logger.error("装备打造出错，检查配置文件");
			return false;
		}

		if (bag.doAddItem(equipItem, -1, Commontext.REASON_STRING,
				fire.log.enums.YYLoggerTuJingEnum.tujing_Value_daozaoget, 0) != AddItemResult.SUCC) {
			MessageMgr.psendMsgNotifyWhileRollback(roleId, 142338, null);
			return false;
		}
		logger.debug("Role: " + roleId + "背包栏增加装备： " + chanchuId + "通过"
				+ Commontext.REASON_STRING);
		
		Octets tips = ((EquipItem) equipItem).getTips();
		SGetItemTips send = new SGetItemTips(BagTypes.BAG, equipItem.getKey(), tips);
		psendWhileCommit(roleId, send);

		int itemId = equipItem.getItemId();
		SProductMadeUp productMadeUp = new SProductMadeUp();
		productMadeUp.itemkey = itemId;
		productMadeUp.maketype = Commontext.EQUIP;
		mkdb.Procedure.psendWhileCommit(roleId, productMadeUp);
		
		// 增加提示消息
		List<String> parameters = new ArrayList<String>(2);
		parameters.add(equipItem.getItemAttr().name);
		parameters.add(equipItem.getItemAttr().namecolor);
		MessageMgr.sendMsgNotify(roleId, PRODUCE_SUCCESS, parameters);
		
		fire.pb.course.CourseManager.achieveUpdate(roleId, fire.pb.course.CourseType.DAZHAO_COURSE);
		
		fire.pb.course.CourseManager.achieveCourse(roleId, fire.pb.course.CourseType.DA_ZHAO_EQUIP, equipItem.getItemAttr().nquality, 0);
		
		
		mkdb.Procedure.pexecuteWhileCommit(
				new mkdb.Procedure() {
					@Override
					protected boolean process() {
						TeamManager.getTeamByRoleId(roleId);//强行锁队伍
						fire.pb.event.Poster.getPoster().dispatchEvent(new EquipMakeEvent(roleId));
						return true;
					}
			} );

		// 运营日志
		writeYYLogger(makeInfo, money, consuStoId, consuStoNum, equipItem);
		return true;
	}

	/**
	 * 运营日志
	 * @param makeInfo
	 * @param money
	 * @param consuStoId
	 * @param consuStoNum
	 * @param equipItem
	 */
	private void writeYYLogger(SEquipMakeInfo makeInfo, int money, int consuStoId, int consuStoNum, EquipItem equipItem) {
		if(attr == null || makeInfo == null || equipItem == null) return;
		
		fire.log.beans.OpEquiMakBean opEquiMakBean = new fire.log.beans.OpEquiMakBean(equipId, 
				attr.getLevel(), attr.getRare(), equipItem.getUniqId(), makeInfo.getTuzhiid(), makeInfo.getTuzhinum(), 
				makeInfo.getHantieid(), makeInfo.getHantienum(), makeInfo.getZhizaofuid(), makeInfo.getZhizaofunum(), 
				consuStoId, consuStoNum, MoneyType.MoneyType_SilverCoin, money);
		YYLogger.equiMakLog(roleId, opEquiMakBean);
	}

	private EquipItem createEquipItemWithAllAttr(int equipid, String producer) {
		return ItemMakeFactory.getFactory().genItemMakeByWay(equipid, producer);
	}

	/**
	 * 获取装备ID
	 * 
	 * @param itemId
	 * @return
	 */
	private fire.pb.item.ItemBase getItemKey(int itemId) {
		for (fire.pb.item.ItemBase item : bag) {
			if (itemId == item.getItemId()) {
				return item;
			}
		}
		return null;
	}

	/**
	 * 检查强化石是否足够
	 */
	private boolean checkQiangHuaShiIsOk(int materialID, int num) {
		int currentNum = bag.getItemNum(materialID, 0);
		if (currentNum >= num) {
			return true;
		}
		return false;
	}

	/**
	 * 检查背包内所需物品是否足够
	 * 
	 * @param materialID
	 * @param num
	 * @return
	 */
	private boolean checkMaterialIsEnough(int materialID, int num) {
		int currentNum = bag.getItemNum(materialID, 0);
		if (currentNum >= num) {
			return true;
		}
		return false;
	}
	
	/**
	 * 检查背包内所需物品是否足够,与要修理的装备级别是否符合
	 * @param materialID
	 * @param num
	 * @return
	 */
	private int checkMaterialListIsOk(List<Integer> materialIDList, List<Integer> numList) {
		int iIndex = 0;
		for (Integer materialID : materialIDList) {
			int currentNum = bag.getItemNum(materialID, 0);
			if (currentNum >= numList.get(iIndex)) {
				return iIndex;
			}
			
			iIndex ++;
		}
		
		return -1;
	}
}
