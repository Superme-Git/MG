package fire.pb.circletask;

import java.util.List;
import java.util.Map;

import fire.pb.battle.BasicEndHandler;
import fire.pb.battle.ResultType;
import fire.pb.circletask.RefreshDataType;
import fire.pb.circletask.SpecialQuestState;

public class CircTaskPatrolBattleEndHandler extends BasicEndHandler {
	private int circletype;

	public CircTaskPatrolBattleEndHandler(int circletype) {
		super();
		this.circletype = circletype;
	}

	@Override
	public boolean handleAward() {
		return true;
	}

	@Override
	public boolean handleDeath() {
		super.handleDeath();
		super.handlePetFighters();
		List<xbean.Fighter> fighters = super.getDeadRoles();
		for (xbean.Fighter f : fighters) {
			fire.pb.effect.Role erole = new fire.pb.effect.RoleImpl(
					f.getUniqueid(), false);
			fire.pb.attr.SRefreshRoleData data = new fire.pb.attr.SRefreshRoleData();
			Map<Integer, Float> result1 = erole.fullHpAndRecoverWound();
			Map<Integer, Float> result2 = erole.fullMp();
			data.datas.putAll(result1);
			data.datas.putAll(result2);
			mkdb.Procedure.psendWhileCommit(f.getUniqueid(), data);
		}

		xbean.Fighter fightLeader = battle.getFighters().get(1);
		if (fightLeader != null) {
			CircleTask sq = new CircleTask(fightLeader.getUniqueid(), false);
			if (sq.getSpecialQuestState(circletype) != SpecialQuestState.UNDONE) {
				return true;
			}

			final Integer val = battle.getRoundresult().get(1);
			if (val != null) {
				if ((val & ResultType.RESULT_DEATH) != 0
						|| (val & ResultType.RESULT_RUNAWAY) != 0) {
					return true;
				}
			}

			if (sq.incCircTaskPatrolBattleNum(circletype)) {
				if (sq.getCircTaskPatrolBattleNum(circletype) >= 1) {
					new UpdateCircleTaskState(fightLeader.getUniqueid(), circletype,
							SpecialQuestState.DONE).call();
				}

				final SRefreshQuestData data = new SRefreshQuestData();
				data.questid = circletype;
				data.datas.put(RefreshDataType.DEST_ITEM_ID,
						(long) sq.getCircTaskPatrolBattleNum(circletype));
				mkdb.Procedure.psendWhileCommit(fightLeader.getUniqueid(), data);
			}
		}
		return true;
	}
}