package fire.pb.instancezone.bingfeng;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fire.msp.IGetRolesCallBack;
import fire.pb.battle.BattleField;
import fire.pb.battle.PBattleEnd;
import fire.pb.map.RoleManager;
import fire.pb.map.Transfer;
import fire.pb.move.SRoleEnterScene;
import mkdb.Procedure;

public class PClearBingFengMap extends Procedure {

	@Override
	protected boolean process() throws Exception {
		for (Integer mapid : BingFengLandMgr.bingFengMaps) {
			clearBingFengMap(mapid);
		}

		return true;
	}

	private void clearBingFengMap(int mapid) {
		final List<Long> roleids = new ArrayList<Long>();
		RoleManager.getRolesByConditions(-1, -1, -1, mapid, -1,
				new IGetRolesCallBack() {

					@Override
					public void process(List<Long> roleIds) {
						roleids.addAll(roleIds);
					}
				});
		mkdb.Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleids));

		forceEndAllBattle(roleids);

		for (long roleid : roleids) {
			Transfer.justGoto(roleid, 1615, 102, 104, SRoleEnterScene.CHEFU);
		}
	}

	private void forceEndAllBattle(List<Long> roleids) {
		Set<Long> battleids = new HashSet<Long>();
		for (long roleId : roleids) {
			Long battleId = xtable.Roleid2battleid.select(roleId);
			if (battleId != null)
				battleids.add(battleId);
		}
		for (long battleId : battleids)
			new PBattleEnd(battleId, BattleField.BATTLE_DRAW).call();
	}

}
