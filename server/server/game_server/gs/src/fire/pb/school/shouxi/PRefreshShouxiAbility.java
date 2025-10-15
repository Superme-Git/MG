
package fire.pb.school.shouxi;

import fire.pb.PropRole;
import fire.pb.map.SceneNpcManager;
import fire.pb.school.School;
import fire.pb.school.SchoolConst;
import fire.pb.talk.MessageMgr;
import fire.pb.util.DateValidate;
import fire.pb.util.FireProp;
import mkdb.Procedure;

public class PRefreshShouxiAbility extends Procedure {

	private final long roleid;

	private final long npckey;

	public PRefreshShouxiAbility(long roleid, long npckey) {

		this.roleid = roleid;
		this.npckey = npckey;
	}

	@Override
	protected boolean process() throws Exception {

		PropRole propRole = new PropRole(roleid, true);
		int school = propRole.getSchool();
		int npcid = SceneNpcManager.selectNpcByKey(npckey).getNpcID();
		School npcSchool = School.getSchoolByNpcid(npcid);
		// 门派不对,直接返回
		if (npcSchool == null || npcSchool.getValue() != school)
			return false;

		xbean.ProfessionLeaderInfo shouxiInfo = xtable.Professionleader.get(school);
		if (shouxiInfo == null || shouxiInfo.getRoleid() != roleid) {
			return false;
		} else {
			long currentTime = System.currentTimeMillis();
			if (shouxiInfo.getRefreshtimes() >= 5) {
				if (DateValidate.inTheSameDay(currentTime, shouxiInfo.getLastrefreshtime())) {
					MessageMgr.sendMsgNotify(roleid, FireProp.getIntValue("school", "mtgserv.shouxi.cantRefresh"),npcid, null);
					return false;
				} else {
					shouxiInfo.setRefreshtimes(0);
				}
			}
			// 如果玩家真的是首席弟子,则刷新首席npc的各项能力值,并且刷新次数加1
			int refreshTimes = shouxiInfo.getRefreshtimes();
			shouxiInfo.setRefreshtimes(refreshTimes + 1);
			shouxiInfo.setLastrefreshtime(currentTime);
			xbean.Monster monsterBean = fire.pb.battle.Monster.copyMonsterFromRoleOrPet(roleid, School.getBattleNpc(npcSchool),true,0);
			monsterBean.setFightnpctype(5);
			// 理论上来说,此时getMonsterbean()返回的list里面一定有一个xbean.Monster,否则是不能用刷新这个功能的
			shouxiInfo.getMonsterbean().set(0, monsterBean);
			MessageMgr.psendMsgNotifyWhileCommit(roleid, SchoolConst.REFRESH_SUCC, null);

		}
		return true;
	}

}
