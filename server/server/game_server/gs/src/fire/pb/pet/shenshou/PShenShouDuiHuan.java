package fire.pb.pet.shenshou;

import java.util.Arrays;

import mkdb.Procedure;

import org.apache.log4j.Logger;

import xtable.Properties;
import fire.pb.RoleConfigManager;
import fire.pb.item.BagTypes;
import fire.pb.item.ItemMaps;
import fire.pb.item.ItemShuXing;
import fire.pb.item.Module;
import fire.pb.pet.PetAttr;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.talk.MessageMgr;

/**
 * 神兽兑换 author yebin @ 2016年3月10日
 */
public class PShenShouDuiHuan extends Procedure {
	public static final Logger logger = Logger.getLogger("SYSTEM");
	private final long roleId;

	public PShenShouDuiHuan(long roleId) {
		this.roleId = roleId;
	}

	@Override
	protected boolean process() throws Exception {
		fire.pb.pet.PetManager petManager = fire.pb.pet.Module.getInstance().getPetManager();
		if (null != petManager) {
			PetAttr petAttr = petManager.randGetOneShenShou();
			if (null == petAttr) {
				return false;
			}

			PetColumn petColumn = new PetColumn(roleId, PetColumnTypes.PET, false);
			if (petColumn.getRemainSize() < 1) {
				// 宠物栏已满
				MessageMgr.sendMsgNotify(roleId, 162101, null);
				logger.error("神兽兑换,宠物栏已满");
				return false;
			}

			fire.pb.PropRole pRole = new fire.pb.PropRole(roleId, true);
			if (petAttr.takelevel > pRole.getLevel()) {
				return false;
			}

			int itemId = Integer.parseInt(RoleConfigManager.getRoleCommonConfig(289).getValue());
			int needItemNum = Integer.parseInt(RoleConfigManager.getRoleCommonConfig(286).getValue());
			ItemMaps bagContainer = fire.pb.item.Module.getInstance().getItemMaps(roleId, BagTypes.BAG, false);
			if (bagContainer == null) {
				logger.info("角色id " + roleId + "兑换神兽" + "\t背包错误");
				return false;
			}

			int curNum = bagContainer.getItemNum(itemId, 0);
			ItemShuXing attr = Module.getInstance().getItemManager().getAttr(itemId);
			if(attr == null){
				logger.info("角色id " + roleId + "兑换神兽" + "\t道具错误");
				return false;
			}
			
			if (curNum < needItemNum) {
				fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 162093, Arrays.asList(attr.getName()));
				return false;
			}

			// 扣除道具
			int usedNum = bagContainer.removeItemById(itemId, needItemNum, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_shenshoucost, itemId,
					"兑换神兽");
			if (usedNum != needItemNum) {
				return false;
			}

			logger.info("角色id " + roleId + "兑换神兽" + "\t扣除道具，物品id" + itemId + "数量" + needItemNum);

			if (petColumn.addpet(petAttr.getId(), petAttr.getInitlevel(), petAttr.getKind(), null,
					PetColumn.ADD_REASON_SHENSHOUDUIHUAN, -1, false, null) < 0) {
				return false;
			}

			MessageMgr.sendMsgNotify(roleId, 162097, Arrays.asList(petAttr.getName()));
			MessageMgr.sendMsgNotify(roleId, 162109, Arrays.asList(petAttr.getName()));
			
			xbean.Properties prop = Properties.select(roleId);
			if(prop != null){
				MessageMgr.sendSystemMsg(162142, Arrays.asList(prop.getRolename(), petAttr.getName()));
			}
			
			return true;
		}
		return false;
	}
}
