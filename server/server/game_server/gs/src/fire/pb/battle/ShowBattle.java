package fire.pb.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;

import org.apache.log4j.Logger;

import fire.pb.GsClient;
import fire.pb.util.Misc;
import fire.script.ParseStringExpression;
import fire.msp.battle.GAddShowBattleLoser;

public class ShowBattle {
	public ShowBattle(final long roleID, final int teamsize,
			final long monsterid, final int monsterbaseid, final int areaconf) {
		this.roleID = roleID;
		this.teamsize = teamsize;
		this.monsterid = monsterid;
		conf = Module.getInstance().getMonstershowConfigManager()
				.getConfig(monsterbaseid);
		if (conf == null)
			throw new NullPointerException("明雷怪baseid=" + monsterbaseid
					+ "的配置为空,请检查配置表");
		this.areaconf = areaconf;
	}

	private final fire.pb.npc.SMonstershowConfig conf;
	private static ParseStringExpression pse = new ParseStringExpression();
	private static Map<String, Object> pseParas = new HashMap<String, Object>();
	private static Logger logger = Logger.getLogger("BATTLE");

	/**获取暗雷怪物个数*/
	public int getMonsterNum() {
		// 队伍人数 ≤ 出战怪物个数 ≤INT(25*(队伍人数+1)/14)
		return Misc.getRandomBetween(teamsize, 25 * (teamsize + 1) / 14);
	}

	private final long monsterid;
	private final int areaconf;
	private boolean isBossBattle;

	public void launch() {
		// final int monsterNum = getMonsterNum();

		final java.util.List<Monster> monsters = getMonsters();
		if (monsters.size() != 0) {
			if (isBossBattle) {// boss战在之前getMonsters方法里已经提交pnewbattle了,这里直接返回
				return;
			}
			PNewBattle batproc;
			batproc = new PNewBattle(roleID, monsters,
					new ShowBattleEndHandler());
			batproc.setAreaType(xbean.BattleInfo.AREA_BATTLESHOW);
			batproc.setAreaConf(areaconf);
			batproc.setBattleType(BattleType.BATTLE_SHOWAREA);
			batproc.setMonsterNpckey(monsterid);
			batproc.submit();
		}
		// new fire.pb.move.
		// ((Monstershow)monster).setBattlestatus( true );
	}

	private static final class ShowBattleEndHandler extends
			fire.pb.battle.BasicEndHandler {
		@Override
		public boolean handleOutBattle(int index,int petkey) throws Exception {
			xbean.Fighter f = battle.getFighters().get(index);
			if (f == null)
				return true;
			if (f.getFightertype() == xbean.Fighter.FIGHTER_ROLE) {
				GAddShowBattleLoser addloser = new GAddShowBattleLoser();
				addloser.npckey = battle.getMonsterid();
				addloser.losers.add(f.getUniqueid());
				GsClient.pSendWhileCommit(addloser);
			}
			return super.handleOutBattle(index, petkey);
		}

		@Override
		public boolean handleAward() {
			fire.msp.battle.GRemoveMonstershow send = new fire.msp.battle.GRemoveMonstershow();
			send.monsterkey = battle.getMonsterid();
			fire.pb.GsClient.sendToScene(send);
			return super.handleAward();
		}

		@Override
		protected boolean handleBattleFailed() {
			GAddShowBattleLoser addloser = new GAddShowBattleLoser();
			addloser.npckey = battle.getMonsterid();
			addloser.losers.addAll(battle.getRoleids().keySet());
			GsClient.pSendWhileCommit(addloser);
			return super.handleBattleFailed();
		}

		@Override
		protected boolean handleDrawBattle() {
			GAddShowBattleLoser addloser = new GAddShowBattleLoser();
			addloser.npckey = battle.getMonsterid();
			addloser.losers.addAll(battle.getRoleids().keySet());
			GsClient.pSendWhileCommit(addloser);
			return super.handleDrawBattle();
		}

	}

	private final long roleID; // 参战(队长)角色ID
	private final int teamsize; // 队伍人数,如果单人则为1

	private List<Monster> getMonsters() {
		// 判断是普通站,boss战

		List<Monster> monsters = new ArrayList<Monster>();

		final BattleConfigManager.MonsterNumConfig monsternumConf = Module
				.getInstance().getBattleConfigManager()
				.getMonsterNumConfig(teamsize);
		if (monsternumConf != null) {
			monsters.add(new Monster(conf.指定怪id));
			int hasGen = 1; // 生成了一个指定怪
			int random = fire.pb.util.Misc.getRandomBetween(1, 10000);
			int babyRate = calBattleTypeRate(conf.宝宝战斗概率);
			int bossRate = calBattleTypeRate(conf.BOSS战斗概率);
			if (random <= babyRate) {
				monsters.add(new Monster(getRandomPet()));
				hasGen++;
			} else if (random <= bossRate + babyRate) {
				isBossBattle = true;// boss战应策划要求修改实现,直接用battleid进入战斗
				int bossbattleid = conf.getBossbattleid();
				PNewBattle batproc;
				batproc = new PNewBattle(roleID, bossbattleid,
						new ShowBattleEndHandler());
				batproc.setAreaType(xbean.BattleInfo.AREA_BATTLESHOW);
				batproc.setAreaConf(areaconf);

				batproc.setMonsterType(HideBattle.HIDEBATTLE_TYPE_BOSS);
				batproc.setBattleType(BattleType.BATTLE_BOSS);

				batproc.setMonsterNpckey(monsterid);
				batproc.submit();
				return monsters;
			}
			for (int i = hasGen; i < monsternumConf.getNormalnum(); i++) {
				monsters.add(new Monster(getRandomMonster()));
			}
			int t = hasGen - monsternumConf.getNormalnum();
			int s = t > 0 ? t : 0;
			for (int i = s; i < monsternumConf.getHeadnum(); i++) {
				monsters.add(new Monster(getRandomHeadMonster()));
			}
		}

		return monsters;
	}

	private int calBattleTypeRate(String commonrate) {
		pseParas.put("TeamNum", teamsize);
		int rate = 0;
		try {
			Double value = pse.ParseStr(commonrate, pseParas);
			rate = value.intValue();
			return rate;
		} catch (ScriptException e) {
			logger.error("error when parsing battleTypeRate String:"
					+ commonrate, e);
			return 0;
		}

	}

	private int getRandomPet() {
		int totalprob = 0;
		for (Integer prob : conf.宝宝概率) {
			totalprob += prob;
		}
		if (totalprob != 1000)
			throw new RuntimeException(conf.id + ": 宝宝总概率不为1000");
		int random = fire.pb.util.Misc.getRandomBetween(1, 1000);
		for (int i = 0; i < conf.宝宝概率.size(); i++) {
			int prob = conf.宝宝概率.get(i);
			if (random <= prob) {
				return conf.宝宝.get(i);
			} else {
				random -= prob;
			}
		}
		throw new RuntimeException("获得明雷怪boss id的时候出错");
	}

	private int getRandomMonster() {
		if (conf.怪物概率 == null || conf.怪物概率.isEmpty()) {
			throw new RuntimeException("获得明雷怪普通战斗怪物id的时候出错");
		}
		int totalprob = 0;
		for (Integer prob : conf.怪物概率) {
			totalprob += prob;
		}
		if (totalprob != 1000)
			throw new RuntimeException(conf.id + ": 怪物总概率不为1000");
		int random = fire.pb.util.Misc.getRandomBetween(1, 1000);
		for (int i = 0; i < conf.怪物概率.size(); i++) {
			int prob = conf.怪物概率.get(i);
			if (random <= prob) {
				return conf.怪物.get(i);
			} else {
				random -= prob;
			}
		}
		throw new RuntimeException("获得明雷怪普通战斗怪物id的时候出错");
	}

	private int getRandomHeadMonster() {
		if (conf.精英概率 == null || conf.精英概率.isEmpty()) {
			return getRandomMonster();
		}
		int totalprob = 0;
		for (Integer prob : conf.精英概率) {
			totalprob += prob;
		}
		if (totalprob != 1000)
			throw new RuntimeException(conf.id + ": 头领总概率不为1000");
		int random = fire.pb.util.Misc.getRandomBetween(1, 1000);
		for (int i = 0; i < conf.精英概率.size(); i++) {
			int prob = conf.精英概率.get(i);
			if (random <= prob) {
				return conf.精英.get(i);
			} else {
				random -= prob;
			}
		}
		throw new RuntimeException("获得明雷怪boss id的时候出错");
	}

}
