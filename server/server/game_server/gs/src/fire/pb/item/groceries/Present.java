package fire.pb.item.groceries;
import java.util.ArrayList;
import java.util.Arrays;

import fire.log.YYLogger;
import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.PAddExpProc;
import fire.pb.PropConf;
import fire.pb.PropRole;
import fire.pb.activity.award.RewardMgr;
import fire.pb.fushi.PAddFuShi;
import fire.pb.fushi.FushiConst;
import fire.pb.fushi.PAddVipExp;
import fire.pb.game.MoneyType;
import fire.pb.item.Commontext;
import fire.pb.item.GroceryItem;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMgr;
import fire.pb.item.ItemMgrImp;
import fire.pb.item.ItemShuXing;
import fire.pb.item.UseItemHandler;
import fire.pb.item.Commontext.UseResult;
import fire.pb.map.SActivityAwardItems;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.talk.MessageMgr;
import fire.pb.util.MessageUtil;
import fire.pb.util.Misc;
import fire.pb.util.RolePropConf;

public class Present extends GroceryItem {

	public Present(ItemMgr im, int itemid) {
		super(im, itemid);
	}

	public Present(ItemMgr im, final xbean.Item item) {
		super(im, item);
	}

	@Override
	protected UseItemHandler getUseItemHandler() {
		return new UsePresentHandler();
	}

	private static class UsePresentHandler implements UseItemHandler {

		@Override
		public UseResult onUse(long roleId, ItemBase bi, int usednum) {
			if (usednum != 1)
				return Commontext.UseResult.FAIL;

			final xbean.Properties prop = xtable.Properties.select(roleId);
			final int level = prop.getLevel();
			final int sex = prop.getSex();
			final int school = prop.getSchool();
			
			final int shape = RolePropConf
					.getShapeidByXshapeid(prop.getShape());
			final fire.pb.item.ItemMgr im = fire.pb.item.Module
					.getInstance().getItemManager();
			fire.pb.item.ItemMgrImp.LiBao conf = im.getLiBao(
					bi.getItemId(), sex, shape, school);
			final int needLevel = im.getAttr(bi.getItemId()).level;
			if (needLevel > level) {
				fire.pb.item.Module.getInstance().getLogger().error("等级不足");
				ArrayList<String> params = new ArrayList<String>();
				params.add(String.valueOf(needLevel));
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleId,
						142535, params);
				return Commontext.UseResult.FAIL;
			}
			if (conf == null) {
				fire.pb.item.Module
						.getInstance()
						.getLogger()
						.error("未找到礼包配置,present=" + bi.getItemId() + " sex="
								+ sex);
				return Commontext.UseResult.FAIL;
			}
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleId,
					conf.getMsgid(), null);
			final fire.pb.item.Pack bag = new fire.pb.item.Pack(roleId, false);
			if (conf.getMoney() > 0) {
				bag.addSysMoney(conf.getMoney(), "礼包赠送",
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_libaoduihuan,
						0);
				MessageMgr.psendMsgNotifyWhileCommit(roleId, 144774,
						Arrays.asList(String.valueOf(conf.getMoney())));
			}
			
			switch (conf.getMoneyType()) {
			case MoneyType.MoneyType_SilverCoin:
			case MoneyType.MoneyType_GoldCoin:
			case MoneyType.MoneyType_ProfContribute:
			case MoneyType.MoneyType_RongYu:
			case MoneyType.MoneyType_FactionContribute:
			case MoneyType.MoneyType_ShengWang:
			case MoneyType.MoneyType_FestivalPoint:
			case MoneyType.MoneyType_GoodTeacherVal:
			case MoneyType.MoneyType_EreditPoint:
				if (conf.getMoneyValue() > 0) {
					bag.addSysCurrency(
							conf.getMoneyValue(),
							conf.getMoneyType(),
							"礼包赠送",
							fire.log.enums.YYLoggerTuJingEnum.tujing_Value_libaoduihuan,
							0);
					MessageUtil.pSendAddOrRemoveCurrency(roleId, conf.getMoneyValue(), conf.getMoneyType());
					if(conf.getMoneyType() == MoneyType.MoneyType_GoldCoin )
					{
						if(bi.getItemId() == PropConf.FuShi.SHOP_GOLD_BOX_ID)
						{
							fire.pb.item.Pack bag2 = new fire.pb.item.Pack(roleId, false);
							bag2.addSysCurrency((long)(conf.getMoneyValue()*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_IN_BUYGOLDBOX))
									, MoneyType.MoneyType_EreditPoint, "使用道具", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_use, 0);
						}
						else if(bi.getItemId() == PropConf.FuShi.HYD_GOLD_BOX_ID)
						{
							fire.pb.item.Pack bag2 = new fire.pb.item.Pack(roleId, false);
							bag2.addSysCurrency((long)(conf.getMoneyValue()*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_IN_GOLD_HYD_BONUS))
									, MoneyType.MoneyType_EreditPoint, "使用道具", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_use, 0);
						}
					}
				}
					
				break;
			case MoneyType.MoneyType_RoleExp:
				if (conf.getMoneyValue() > 0) {
					PAddExpProc proc = new PAddExpProc(roleId,
							conf.getMoneyValue(), true,
							PAddExpProc.USE_MONEY_ITEM, "");
					proc.call();
					
					// 运营日志
					YYLogger.OpTokenGetLog(roleId, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_present,
							conf.getMoneyType(), conf.getMoneyValue(), prop.getExp(), new fire.log.beans.ItemBean());
				}
				break;
			case MoneyType.MoneyType_Activity:
				if (conf.getMoneyValue() > 0) {
					RoleLiveness actrole = RoleLiveness
							.getRoleLiveness(roleId);
					if (actrole != null && conf.getMoneyValue() > 0) {
						actrole.addLiveness((int) conf.getMoneyValue());
						
						// 运营日志
						YYLogger.OpTokenGetLog(roleId, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_present,
								conf.getMoneyType(), conf.getMoneyValue(), (long) actrole.getLiveness(), 
								new fire.log.beans.ItemBean());
					}
				}
				break;
			case MoneyType.MoneyType_HearthStone: {
				int userId = new PropRole(roleId, true).getUserid();
				new PAddFuShi(userId, roleId, (int)conf.getMoneyValue(), FushiConst.SYS_FUSHI, YYLoggerTuJingEnum.tujing_Value_present).call();
				break;
			}
			case MoneyType.MoneyType_Energy: {
				PropRole pRole = new PropRole(roleId, false);
				if (pRole.checkLimitEnergy()) {
					pRole.addEnergy((int)conf.getMoneyValue());
					
					// 运营日志
					YYLogger.OpTokenGetLog(roleId, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_present,
							conf.getMoneyType(), conf.getMoneyValue(), pRole.getEnergy(), new fire.log.beans.ItemBean());
					
					fire.pb.attr.SRefreshRoleData refreshdata = new fire.pb.attr.SRefreshRoleData();
					refreshdata.datas.put(fire.pb.attr.AttrType.ENERGY, (float) pRole.getEnergy());
					mkdb.Procedure.psendWhileCommit(roleId, refreshdata);
					
					MessageUtil.pSendAddOrRemoveCurrency(roleId, conf.getMoneyValue(), conf.getMoneyType());
				}
				else {
					MessageMgr.psendMsgNotify(roleId, 168003, null);
					return Commontext.UseResult.FAIL;
				}
				
				break;
			}
			default:
				break;
			}

			if (!conf.getItems().isEmpty()) {
				int need = 0;
				ItemShuXing attr;
				for (ItemMgrImp.LiBao.Item item : conf.getItems()) {
					if (item.getItemid() > 9999999) {
						need += item.getItemnum();
						continue;
					}
					attr = fire.pb.item.Module.getInstance().getItemManager()
							.getAttr(item.getItemid());
					if (attr == null || attr.maxstack == 0)
						continue;
					if (item.getItemnum() % attr.maxstack == 0) {
						need += item.getItemnum() / attr.maxstack;
					} else {
						need += item.getItemnum() / attr.maxstack + 1;
					}
				}
				if (need > bag.getRemainSize()) {
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleId,
							160062, null);
					return Commontext.UseResult.FAIL;
				}
				int[] params = new int[conf.getItems().size() * 2];
				int i = -1;
				for (final fire.pb.item.ItemMgrImp.LiBao.Item e : conf.getItems()) {
					final int itemid;
					if (e.getItemid() > 9999999) {
						SActivityAwardItems itemtype = RewardMgr.getInstance().getAward2Map().get(e.getItemid());
						if (itemtype == null || itemtype.items.size() <= 0) {
							return Commontext.UseResult.FAIL;
						}
						int index = Misc.getProbability(itemtype.itemsrate);
						itemid = itemtype.items.get(index);
					} else {
						itemid = e.getItemid();
					}
					if (bag.addItem(itemid, e.getItemnum(), 0,
							e.isBind() || bi.isBind() ? fire.pb.Item.BIND : 0,
							"礼包赠送",
							fire.log.enums.YYLoggerTuJingEnum.tujing_Value_present,
							bi.getItemId(), false) != e.getItemnum()) {
						fire.pb.item.Module.getInstance().getLogger().debug("礼包赠送时背包已满");
						return Commontext.UseResult.FAIL;
					}

					params[++i] = itemid;
					params[++i] = e.getItemnum();
					MessageUtil.psendAddItemWhileCommit(roleId, itemid, e.getItemnum());
				}
			}

			if (conf.getFuShi() > 0) {
				Integer userId = xtable.Properties.selectUserid(roleId);
				if (userId != null) {
					if (!new PAddFuShi(userId, roleId, conf.getFuShi(),
							FushiConst.SYS_FUSHI, YYLoggerTuJingEnum.tujing_Value_present).call())
						return Commontext.UseResult.FAIL;
				}
			}
			
			if (conf.getVipExp() > 0) {
				Integer userId = xtable.Properties.selectUserid(roleId);
				if (userId != null) {
					if (!new PAddVipExp(roleId,conf.getVipExp(), PAddVipExp.REASON_ADD_VIP_EXP_PRESENT).call())
						return Commontext.UseResult.FAIL;
				}
			}
			
			if (conf.getRewardid() > 0) {

				RewardMgr.getInstance().distributeAllAward(roleId,
						conf.getRewardid(), null,
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_present,
						conf.getRewardid(), PAddExpProc.OTHER, "礼包赠送", true);
			}

			return Commontext.UseResult.SUCC;
		}
	}
}
