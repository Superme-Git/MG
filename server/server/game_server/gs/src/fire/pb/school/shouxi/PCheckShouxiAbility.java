
package fire.pb.school.shouxi;

import fire.pb.PropRole;
import fire.pb.battle.Monster;
import fire.pb.effect.MonsterImpl;
import fire.pb.map.SceneNpcManager;
import fire.pb.school.SSendShouxiInfo;
import fire.pb.school.School;
import mkdb.Procedure;

public class PCheckShouxiAbility extends Procedure {

	private final long roleid;

	private final long npckey;

	public PCheckShouxiAbility(long roleid, long npckey) {

		this.roleid = roleid;
		this.npckey = npckey;
	}

	@Override
	protected boolean process() throws Exception {

		PropRole prop = new PropRole(roleid, true);
		int school = prop.getSchool();
		int npcid = SceneNpcManager.selectNpcByKey(npckey).getNpcID();
		School npcSchool = School.getSchoolByNpcid(npcid);
		// 门派不对,直接返回
		if (npcSchool == null || npcSchool.getValue() != school)
			return false;
		xbean.ProfessionLeaderInfo shouxiInfo = xtable.Professionleader.select(school);
		xbean.Monster monsterBean = null;
		// 如果没有首席,则直接给一个npc的monster作为首席
		if (shouxiInfo == null) {
			Monster monster = new Monster(School.getBattleNpc(npcSchool));
			monsterBean = monster.getMonsterbean(null);
		} else {
			monsterBean = shouxiInfo.getMonsterbean().get(0);
		}
		MonsterImpl monsterImpl = new MonsterImpl(monsterBean);
		SSendShouxiInfo sssi = new SSendShouxiInfo();

		fire.pb.school.ShouxiInfo sxi = new fire.pb.school.ShouxiInfo();
		sxi.maxhp = monsterImpl.getMaxHp();
		sxi.maxmp = monsterImpl.getMaxMp();
		sxi.hitrate = (int) monsterImpl.getHit();
		sxi.attack = monsterImpl.getAttack();
		sxi.defend = monsterImpl.getDefend();
		sxi.magicattack = monsterImpl.getMagicAttack();
		sxi.magicdef = monsterImpl.getMagicDef();
		sxi.speed = monsterImpl.getSpeed();
		sxi.dodge = (int) monsterImpl.getDodge();

		sssi.shouxi = sxi;
		sssi.shouxikey = npckey;
		psendWhileCommit(roleid, sssi);
		return true;
	}

}
