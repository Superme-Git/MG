package fire.pb.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fire.pb.main.ConfigManager;
import fire.pb.talk.MessageMgr;
import fire.pb.team.TeamManager;
import fire.pb.util.Misc;
import mkdb.Procedure;

public class PCombineItem extends Procedure {

	static Map<Integer, SEquipHeCheng> combineConfs = null;
	static{
		if(fire.pb.fushi.Module.GetPayServiceType() == 1){
			Map<Integer, DSEquipHeCheng> dSEquipHeCheng = ConfigManager.getInstance().getConf(DSEquipHeCheng.class);
			combineConfs = new TreeMap<>(dSEquipHeCheng);
		}else {
			combineConfs = ConfigManager.getInstance().getConf(SEquipHeCheng.class);
		}
	}

	private final long roleId;

	private final int keyinpack;

	private final byte usemoney;
	
	private final boolean usehammer;

	private final boolean combineall;

	public PCombineItem(long roleId, int keyinpack, boolean combineall,
			byte usemoney, boolean usehammer) {

		super();
		this.roleId = roleId;
		this.keyinpack = keyinpack;
		this.combineall = combineall;
		this.usemoney = usemoney;
		this.usehammer = usehammer;
	}

	@Override
	protected boolean process() throws Exception {
		fire.pb.item.Pack bag = new fire.pb.item.Pack(roleId, false);
		ItemBase bi = bag.getItem(keyinpack);
		if (bi == null)
			return false;

		int combinItemNum = bag.getBagItemNum(bi.getItemId());

		SEquipHeCheng sEquipCombin = combineConfs.get(bi.getItemId());
		if (sEquipCombin == null) {
			MessageMgr.psendMsgNotify(roleId, 150167, null);
			return false;
		}

		ItemShuXing itemittr = Module.getInstance().getItemManager()
				.getAttr(sEquipCombin.nextid);
		if (itemittr == null)
			return false;

		if (sEquipCombin.money < 0) {
			MessageMgr.psendMsgNotify(roleId, 150167, null);
			return false;
		}
		
		if (usehammer) {
			if (sEquipCombin.getHammerid() <= 0) {
				Module.logger.error(new StringBuffer().append("roleId:").append(roleId)
						.append(",合成物品:").append(sEquipCombin.nextid).append(",宝石锤id配置错误"));
				return false;
			}
			if (sEquipCombin.getHammernum() <= 0) {
				Module.logger.error(new StringBuffer().append("roleId:").append(roleId)
						.append(",合成物品:").append(sEquipCombin.nextid).append(",宝石锤数量配置错误"));
				return false;
			}
		}

		int num = sEquipCombin.getNums();
		if (num > combinItemNum) {
			MessageMgr.psendMsgNotify(roleId, 160304, null);
			return false;
		}
		int newitemnum = 1;
		long costMoney = sEquipCombin.money;

		// 当为合成全部时改成循环处理
		if (combineall) {
			int nNewItemadd = 0;
			int nRetItem = 0; // 返还的宝石数量
			newitemnum = combinItemNum / num;

			if (usemoney == 1) {
				// 减钱
				costMoney = sEquipCombin.money * newitemnum;
				long rmvMoney = bag.subMoney(-costMoney, "道具合成",
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_hecheng,
						-sEquipCombin.money);
				if (rmvMoney != -costMoney) {
					fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 144801, 0,
							null);
					return false;
				}
			}

			// 减合成物品的道具
			int decItemNum = newitemnum * num;
			int rmv = bag.removeItemById(bi.getItemId(), decItemNum,
					fire.log.enums.YYLoggerTuJingEnum.tujing_Value_hecheng, bi.getItemId(), "道具合成");
			if (rmv != decItemNum) {
				MessageMgr.psendMsgNotify(roleId, 150167, null);
				return false;
			}
			
			//减少宝石锤数量
			if (usehammer) {
				int decHammerNum = newitemnum * sEquipCombin.getHammernum();
				int rmvHammmer = bag.removeItemById(sEquipCombin.getHammerid(), decHammerNum, 
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_hecheng, sEquipCombin.getHammerid(), "道具合成");
				if (rmvHammmer != decHammerNum)
					return false;
			}

			if (bag.isFull()) {
				fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 144875, 0, null);
				return false;
			}

			for (int i = 0; i < newitemnum; i++) {
				// 添加合成机率
				int rate = usehammer ? sEquipCombin.getHammerrate() : sEquipCombin.hechengrate;
				if (!Misc.checkRatePercent(rate)) {
					// 处理返还
					nRetItem++;
					Module.logger.error(new StringBuffer().append("roleId:").append(roleId)
							.append(",合成物品:").append(sEquipCombin.nextid).append(",合成概率是:").append(rate));
					continue;
				}

				nNewItemadd++;
			}

			if (nRetItem > 0) {
				bag.doAddItem(sEquipCombin.hechengfailreturn,
						sEquipCombin.failreturnnum * nRetItem, "道具合成失败返还",
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_hecheng,
						sEquipCombin.nextid);
				SHeChengItem combineItemResult = new SHeChengItem(
						sEquipCombin.failreturnnum * nRetItem,
						sEquipCombin.hechengfailreturn);
				psendWhileCommit(roleId, combineItemResult);
			}

			if (nNewItemadd > 0) {
				bag.doAddItem(sEquipCombin.nextid, nNewItemadd, "道具合成",
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_hecheng,
						sEquipCombin.nextid);

				SHeChengItem sCombineItem = new SHeChengItem(nNewItemadd,
						sEquipCombin.nextid);
				psendWhileCommit(roleId, sCombineItem);
				fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 150168, 0, null);
				if (nRetItem > 0) {
					List<String> param = new ArrayList<String>();

					param.add(itemittr.getName());
					param.add(String.valueOf(nNewItemadd));
					param.add(String.valueOf(sEquipCombin.failreturnnum
							* nRetItem));
					MessageMgr.psendMsgNotify(roleId, 160138, param);
				}
				fire.pb.event.Poster.getPoster().dispatchEvent(
						new CombineItemEvent(roleId));
			} else {
				if (nRetItem > 0) {
					List<String> param = new ArrayList<String>();
					ItemShuXing iAttr = Module.getInstance().getItemManager()
							.getAttr(bi.getItemId());
					if (iAttr == null) {
						return false;
					}

					param.add(iAttr.getName());
					param.add(String.valueOf(sEquipCombin.failreturnnum));
					MessageMgr.psendMsgNotify(roleId, 160114, param);
				}

				SHeChengItem combineItemResult = new SHeChengItem(0, 0);
				psendWhileCommit(roleId, combineItemResult);
				fire.pb.event.Poster.getPoster().dispatchEvent(
						new CombineItemEvent(roleId));
				return true;
			}
		} else {
			int rmv = bag.removeItemById(bi.getItemId(), num,
					fire.log.enums.YYLoggerTuJingEnum.tujing_Value_hecheng, bi.getItemId(), "道具合成");
			if (rmv != num) {
				MessageMgr.psendMsgNotify(roleId, 150167, null);
				return false;
			}
			
			if (usehammer) {
				int decHammerNum = sEquipCombin.getHammernum();
				int rmvHammmer = bag.removeItemById(sEquipCombin.getHammerid(), decHammerNum, 
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_hecheng, sEquipCombin.getHammerid(), "道具合成");
				if (rmvHammmer != decHammerNum)
					return false;
			}

			if (bag.isFull()) {
				fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 144875, 0, null);
				return false;
			}
			if (usemoney == 1) {
				long rmvMoney = bag.subMoney(-costMoney, "道具合成",
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_hecheng,
						-sEquipCombin.money);
				if (rmvMoney != -costMoney) {
					fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 144801, 0,
							null);
					return false;
				}
			}

			// 添加合成机率
			int rate =  usehammer ? sEquipCombin.getHammerrate() : sEquipCombin.hechengrate;
			if (!Misc.checkRatePercent(rate)) {
				Module.logger.error(new StringBuffer().append("roleId:").append(roleId)
						.append(",合成物品:").append(sEquipCombin.nextid).append(",合成概率是:").append(rate));
				// 处理返还
				bag.doAddItem(sEquipCombin.hechengfailreturn,
						sEquipCombin.failreturnnum, "道具合成失败返还",
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_hecheng,
						sEquipCombin.nextid);

				SHeChengItem combineItemResult = new SHeChengItem(0, 0);
				psendWhileCommit(roleId, combineItemResult);

				List<String> param = new ArrayList<String>();
				ItemShuXing iAttr = Module.getInstance().getItemManager()
						.getAttr(bi.getItemId());
				if (iAttr == null) {
					return true;
				}

				param.add(iAttr.getName());
				param.add(String.valueOf(sEquipCombin.failreturnnum));
				MessageMgr.psendMsgNotify(roleId, 160114, param);

				return true;
			}

			bag.doAddItem(sEquipCombin.nextid, newitemnum, "道具合成",
					fire.log.enums.YYLoggerTuJingEnum.tujing_Value_hecheng,
					sEquipCombin.nextid);

			mkdb.Procedure.pexecuteWhileCommit(
					new mkdb.Procedure() {
						@Override
						protected boolean process() {
							TeamManager.getTeamByRoleId(roleId);//强行锁队伍
							fire.pb.event.Poster.getPoster().dispatchEvent(new CombineItemEvent(roleId));
							return true;
						}
				} );

			SHeChengItem sCombineItem = new SHeChengItem(newitemnum,
					sEquipCombin.nextid);
			psendWhileCommit(roleId, sCombineItem);

			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 150168, 0, null);
		}
		return true;
	}
}
