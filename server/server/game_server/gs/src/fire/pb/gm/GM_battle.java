package fire.pb.gm;

import java.util.ArrayList;
import java.util.List;

import xbean.BattleInfo;
import fire.pb.battle.BattleEndHandler;
import fire.pb.battle.BattleField;
import fire.pb.battle.Monster;
import fire.pb.battle.PNewBattle;
import fire.pb.battle.specialevent.RunawaySpyBattleHandler;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;

public class GM_battle extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1)
			return false;

		final String oper = args[0];
		if (oper.equals("end")) {
			Long battleID = xtable.Roleid2battleid.select(getGmroleid());
			if (null == battleID)
				return false;
			int result = BattleField.BATTLE_DRAW;
			if (args.length >= 2) {
				try {
					result = Integer.valueOf(args[1]);
				} catch (Exception e) {
				}
			}
			new fire.pb.battle.PBattleEnd(battleID, result).submit();
		} else if (oper.equals("start")) {
			if (args.length == 1)
				new fire.pb.battle.HideBattle(getGmroleid(), 1, 1).launch();
			else {
				Team team = TeamManager.selectTeamByRoleId(getGmroleid());
				if (null != team && team.isNormalMember(getGmroleid())) {
					sendToGM("你是正常队员，不能start battle，由队长start。");
					return true;
				}
				if (args.length == 2 && !args[1].contains("@")) {
					int battleConfId = Integer.valueOf(args[1]);
					BattleEndHandler handler = null;
					if (battleConfId >= 9116 && battleConfId <= 9199) {
						handler = new RunawaySpyBattleHandler();
						PNewBattle newbattle = new PNewBattle(getGmroleid(),
								battleConfId, handler);
						newbattle.setAreaConf(1);
						newbattle.setAreaType(xbean.BattleInfo.AREA_BATTLEHIDE);
						newbattle.setMonsterType(3);
						newbattle.submit();
						return true;
					}
					new PNewBattle(getGmroleid(), battleConfId).submit();
					return true;
				}

				List<Integer> monsterIDs = new ArrayList<Integer>();
				int num = 0;
				for (int i = 1; i < args.length && num < 10; i++) {
					String[] strs = args[i].split("@");
					if (strs.length != 2)
						return false;
					int monsterID = 0;
					int monsterNum = 0;
					try {
						monsterID = Integer.parseInt(strs[0].trim());
						monsterNum = Integer.parseInt(strs[1].trim());
					} catch (NumberFormatException e) {
						sendToGM("格式错误,@的左边是怪物id,右边是怪物数量");
						return false;
					}
					for (int j = 0; j < monsterNum && num < 10; j++) {
						monsterIDs.add(monsterID);
						num++;
					}
				}
				List<Monster> monsters = BattleField.genMonsters(monsterIDs);
				new PNewBattle(getGmroleid(), monsters).submit();
			}
		} else if (oper.equals("test")) {
			new fire.pb.battle.HideBattle(getGmroleid(), 1, 2).launch();
		} else if (oper.equals("kill")) {
			Long battleID = xtable.Roleid2battleid.select(getGmroleid());
			if (null == battleID)
				return false;
			int phase = xtable.Battle.selectPhase(battleID);
			if (phase != BattleInfo.PHASE_WAITOPERATION) {
				sendToGM("战斗不在等待操作阶段，不能使用此GM指令");
				return false;
			}
			new fire.pb.battle.PGmKillAllScript(battleID, getGmroleid())
					.submit();
		}

		return true;

	}

	@Override
	String usage() {
		return null;
	}

}
