package fire.pb.gm;

import fire.pb.activity.award.RewardMgr;
import fire.pb.compensation.CompensationManager;
import fire.pb.main.ConfigManager;
import fire.pb.mbean.beans.Reload;
import fire.pb.npc.Module;

public class GM_reload extends GMCommand {

	@Override
	boolean exec(String[] args) {

		try {
			if (args.length >= 1) {
				sendToGM(new Reload().reload());
				return true;
			}

			ConfigManager.getInstance().init();

			fire.pb.effect.Module.getInstance().init();
			fire.pb.buff.Module.getInstance().init();
			fire.pb.skill.Module.getInstance().init();
			fire.pb.battle.Module.getInstance().init();
			fire.pb.mission.Module.getInstance().init();
			fire.pb.instancezone.Module.getInstance().reload();
			RewardMgr.getInstance().init();
			CompensationManager.getInstance().init();
			Module.getInstance().reload();
		} catch (Exception e) {
			sendToGM("reload error");
			e.printStackTrace();
		}

		sendToGM("Reload 成功。重载的模块有：Buff，技能，战场，战斗AI，战斗怪物数值，暗雷区域,奖励表。");

		return true;
	}

	@Override
	String usage() {
		return "//reload hot ： 根据hotfix文件来热加载   ||  //reload 全重载模块：Buff，技能，战场，战斗AI，战斗怪物数值，暗雷区域,奖励表";
	}

}
