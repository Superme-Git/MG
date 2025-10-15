
package fire.pb.school.change;

import mkdb.Procedure;

import org.apache.log4j.Logger;

import com.locojoy.base.Octets;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.RoleConfigManager;
import fire.pb.item.BagTypes;
import fire.pb.item.EquipItem;
import fire.pb.item.EquipUtils;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMaps;
import fire.pb.item.ItemShuXing;
import fire.pb.item.SAddItem;
import fire.pb.item.SGetItemTips;
import fire.pb.ranklist.proc.PRoleZongheRankProc;
import fire.pb.shop.utils.MarketUtils;
import fire.pb.talk.MessageMgr;

/**
 * 转职转武器
 * author yebin @ 2016年8月16日
 */
public class PChangeWeapon extends Procedure {
	private static Logger logger = Logger.getLogger("ITEM");

	private final long roleId;
	private final int equipKey;
	private final int newWeaponTypeId;

	public PChangeWeapon(long roleId, int equipKey, int newWeaponTypeId) {

		super();
		this.roleId = roleId;
		this.equipKey = equipKey;
		this.newWeaponTypeId = newWeaponTypeId;
	}

	@Override
	protected boolean process() throws Exception {
		xbean.Properties prop = xtable.Properties.get(roleId);
		if (null == prop) {
			return false;
		}

		// 战斗状态下不能使用该功能
		if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER)) {
			MessageMgr.psendMsgNotify(roleId, 150163, null);
			logger.error("战斗状态下无法使用转换武器功能");
			return false;
		}

		// 转职检测
		xbean.ChangeSchoolInfo changeSchoolInfo = xtable.Changeschool.get(roleId);
		if (null == changeSchoolInfo || changeSchoolInfo.getRecords().size() == 0) {
			logger.error("未转职无法使用转换武器功能");
			return false;
		}

		// 转换武器次数判断
		int maxChangeWeaponCount = ChangeSchoolUtils.getMaxChangeWeaponCount();
		int hasChangeWeaponCount = changeSchoolInfo.getChangeweaponcount();
		if (hasChangeWeaponCount >= maxChangeWeaponCount) {
			logger.error("转换武器功能使用次数已满");
			return false;
		}

		// 新的武器类型是否存在
		ItemShuXing newEquipAttr = fire.pb.item.Module.getInstance().getItemManager().getAttr(newWeaponTypeId);
		if (newEquipAttr == null) {
			logger.error("转换武器功能新武器类型不存在");
			return false;
		}

		if (!EquipUtils.isRoleCanEquip(newWeaponTypeId, prop)) {
			logger.error("转换武器功能新武器玩家无法装备");
			return false;
		}

		// 拿到背包的锁
		fire.pb.item.Pack bag = new fire.pb.item.Pack(roleId, false);
		ItemBase oldWeaponIB = bag.getItem(equipKey);

		if (oldWeaponIB == null || !(oldWeaponIB instanceof EquipItem)) {
			MessageMgr.psendMsgNotify(roleId, 150163, null);
			logger.error("转换武器功能旧武器错误!!!");
			return false;
		}

		EquipItem oldWeapon = ((EquipItem) oldWeaponIB);
		
		// 武器需要一定等级才能转换
		int needLv = Integer.parseInt(RoleConfigManager.getRoleCommonConfig(434).getValue());
		int oldWeaponLv = oldWeapon.getItemAttr().getLevel();
		if(newEquipAttr.getLevel() != oldWeaponLv || oldWeaponLv < needLv) {
			logger.error("转换武器功能新武器等级错误");
			return false;
		}
					
		// 是否拍卖中
		if ((oldWeaponIB.getFlags() & fire.pb.Item.ONSTALL) != 0) {
			logger.error("拍卖的武器无法使用转换武器功能备");
			return false;
		}

		// 检查原装备是否可以用,可以用则不能转换
		if (EquipUtils.isRoleCanEquip(oldWeaponIB.getItemId(), prop)) {
			logger.error("检查原装备是否可以用,可以用则不能转换武器");
			return false;
		}

		// 扣钱
		int confWeaponChangeCostMoney = Integer.parseInt(RoleConfigManager.getRoleCommonConfig(433).getValue());
		long ret = bag.subMoney(-confWeaponChangeCostMoney, "转职转武器消耗", YYLoggerTuJingEnum.tujing_Value_changeschoolweaponcost, 0);
		if (ret != -confWeaponChangeCostMoney) {
			return false;
		}

		// 消耗次数
		changeSchoolInfo.setChangeweaponcount(hasChangeWeaponCount + 1);
		
		// 转换
		oldWeapon.changeEquipID(fire.pb.item.Module.getInstance().getItemManager(), newWeaponTypeId);
		
		// 设置冷却时间
		int coolDownCostTime = Integer.parseInt(RoleConfigManager.getRoleCommonConfig(432).getValue());
		long targetTimeMillis = System.currentTimeMillis() + coolDownCostTime * MarketUtils.ONE_DAY;
		oldWeapon.getDataItem().setMarkettime(targetTimeMillis);
		
		// 是否珍品检测
		int score = fire.pb.item.Module.getInstance().getEquipScore(oldWeapon);
		oldWeapon.getEquipAttr().setEquipscore(score);
		if (score >= oldWeapon.getItemAttr().getTreasureScore()) {
			oldWeapon.getEquipAttr().setTreasure(1);
		} else {
			oldWeapon.getEquipAttr().setTreasure(0);
		}

		// 发消息
		SAddItem sAddItem = new SAddItem();
		sAddItem.packid = bag.getPackid();
		sAddItem.data.add(ItemMaps.transItemData2SendData(oldWeapon.getDataItem(), equipKey, 0));
		psendWhileCommit(roleId, sAddItem);

		Octets tips = oldWeapon.getTips();
		SGetItemTips send = new SGetItemTips(BagTypes.BAG, oldWeapon.getKey(), tips);
		psendWhileCommit(roleId, send);

		// 通知客户端转换武器成功
		SChangeWeapon sendResult = new SChangeWeapon();
		psendWhileCommit(roleId, sendResult);

		// 更新玩家综合实力排行榜
		mkdb.Procedure.pexecuteWhileCommit(new PRoleZongheRankProc(this.roleId));

		return true;
	}
}
