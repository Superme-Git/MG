package fire.pb.battle;

import java.util.Map;

import fire.pb.attr.AttrType;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.effect.Role;
import fire.pb.effect.RoleImpl;
import mkdb.Procedure;

public class PBasicDeathPunish extends Procedure implements Runnable {

	private final long roleid;
	private final int battleid;

	public PBasicDeathPunish(final long roleid, final int battleid) {
		this.roleid = roleid;
		this.battleid = battleid;
	}

	@Override
	protected boolean process() throws Exception {
		// 回血回魔
		Role role = new RoleImpl(roleid);

		Map<Integer, Float> result = role.fullHpAndRecoverWound();
		role.setHp(1);
		//role.setMp(1);
		SRefreshRoleData sr = new SRefreshRoleData();
		sr.datas.putAll(result);
		sr.datas.put(AttrType.HP, (float) role.getHp());
		sr.datas.put(AttrType.MP, (float) role.getMp());
		Procedure.psendWhileCommit(roleid, sr);
		
		psendWhileCommit(roleid, new SDeadLess20(1));
		SBattleInfo battleinfo = fire.pb.main.ConfigManager.getInstance().getConf(SBattleInfo.class).get(battleid);
		if (battleinfo != null && battleinfo.getDeathinfo() == 1) {
			psendWhileCommit(roleid, new SDeadLess20(0));
		}
		
		if (battleinfo == null){
			psendWhileCommit(roleid, new SDeadLess20(0));
		}
		

		return true;
	}

	@Override
	public void run() {
		this.submit();
	}

}
