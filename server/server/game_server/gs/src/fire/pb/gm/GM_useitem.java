package fire.pb.gm;

import java.util.concurrent.ExecutionException;

import fire.pb.battle.BattleField;
import fire.pb.battle.OperationType;
import fire.pb.battle.PPlayScript;
import fire.pb.item.ItemBase;
import xbean.BattleInfo;

public class GM_useitem extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		final Long battleID = xtable.Roleid2battleid.select(getGmroleid());
		if (null == battleID) {
			return true;
		} else {
			if (args.length < 2) {
				sendToGM("战斗内使用物品，参数格式错误：" + usage());
				return false;
			}
			final int itempos = Integer.valueOf(args[0]);
			final int aim = Integer.valueOf(args[1]);
			try {
				int phase = xtable.Battle.selectPhase(battleID);
				if (phase != BattleInfo.PHASE_WAITOPERATION) {
					sendToGM("战斗不在等待操作阶段，不能使用此GM指令");
					return false;
				}

				new mkdb.Procedure() {

					@Override
					protected boolean process() {
						xbean.BattleInfo battle = xtable.Battle.get(battleID);
						BattleField.cancelTimeOutCheck(battle);
						int fighterId = battle.getRoleids().get(getGmroleid());
						xbean.Decision decision = xbean.Pod.newDecision();
						decision.setOperator(fighterId);
						if (BattleField.checkOutBattle(battle, aim)) {
							sendToGM("ERROR:战斗内使用物品，目标不存在" + usage());
							return false;
						} else
							decision.setAim(aim);
						fire.pb.item.ItemMaps bag = fire.pb.item.Module
								.getInstance().getItemMaps(getGmroleid(),
										fire.pb.item.BagTypes.BAG, false);
						ItemBase item = null;
						for (ItemBase i : bag) {
							if (i.getPosition() == itempos) {
								item = i;
								break;
							}
						}
						if (item != null) {
							decision.setOperatetype(OperationType.ACTION_USEITEM);
							decision.setOperateid(item.getKey());
							battle.getDecisions().put(fighterId, decision);
						} else {
							sendToGM("ERROR:战斗内使用物品，位置上的物品不存在" + usage());
						}
						return true;
					}
				}.submit().get();
				new PPlayScript(battleID).submit();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			return true;
		}
	}

	@Override
	String usage() {
		return null;
	}

}
