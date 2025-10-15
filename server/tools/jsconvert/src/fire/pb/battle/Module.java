package fire.pb.battle;

import java.util.HashMap;
import java.util.Map;

import fire.pb.battle.SBattleInfo;
import fire.pb.battle.ai.BattleAIMgr;
import fire.pb.main.IModule;
import fire.pb.main.ModuleMgr;
import fire.script.JavaScript;

public class Module implements IModule {

	public final static String MODULE_NAME = "battle";
	private final Map<Integer, BattleCfg> battleCfgs = new HashMap<Integer, BattleCfg>();

	public static Module getInstance() {
		return ((fire.pb.battle.Module) ModuleMgr.getInstance()
				.getModuleByName(MODULE_NAME));
	}

	@Override
	public void exit() {
	}

	@Override
	public void init() throws Exception {
		try {
			BattleAIMgr.getInstance().initBattleAI();
		} catch (Exception e) {
			System.out.println("Battle AI模块初始化错误。" + e);
		}

		// 战场信息
		try {
			Map<Integer, SBattleInfo> sBattleInfos = fire.pb.main.ConfigMgr
					.getInstance().getConf(SBattleInfo.class);
			battleCfgs.clear();
			for (SBattleInfo sbattleInfo : sBattleInfos.values()) {
				if (sbattleInfo == null || sbattleInfo.monsterNumScript == null
						|| sbattleInfo.positions == null) {
					System.out.println("ID为" + sbattleInfo.id + "的战场信息配置有问题");
					continue;
				}
				BattleCfg battleconf = new BattleCfg();
				battleconf.id = sbattleInfo.id;
				battleconf.monsterNumScript = new JavaScript(
						sbattleInfo.monsterNumScript);
				if (sbattleInfo.battlelevel != null)
					battleconf.leveljs = new JavaScript(sbattleInfo.battlelevel);
				if (sbattleInfo.positions.size() != BattleField.A_SIDE_FIGHTER_NUM * 2) {
					continue;
				}
				battleCfgs.put(battleconf.id, battleconf);
			}
		} catch (Exception e) {
			System.out.println("战场信息模块初始化错误。" + e);
		}
	}

	public Map<Integer, BattleCfg> getBattleConfigs() {
		return battleCfgs;
	}
}
