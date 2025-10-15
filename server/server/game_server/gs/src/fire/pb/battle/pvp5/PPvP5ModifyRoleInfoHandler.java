package fire.pb.battle.pvp5;

/**
 * 修改角色信息的事件处理
 * @author XGM
 */
public class PPvP5ModifyRoleInfoHandler extends mkdb.Procedure {

	private final long roleId;
	private final String newName;

	public PPvP5ModifyRoleInfoHandler(long roleId, String newName) {
		this.roleId = roleId;
		this.newName = newName;
	}

	@Override
	protected boolean process() {
		for (int grade : PvP5Helper.getAllRaceIds()) {
			updateLastRankList(grade);
			updateHistoryRankList(grade);
		}
		return true;
	}

	public void updateLastRankList(int grade) {
		mkdb.Procedure proc = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				return updateRankList(xtable.Pvp5scorelist.get(grade));
			}
		};
		mkdb.Procedure.pexecuteWhileCommit(proc);
	}

	public void updateHistoryRankList(int grade) {
		mkdb.Procedure proc = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				return updateRankList(xtable.Pvp5historyscorelist.get(grade));
			}
		};
		mkdb.Procedure.pexecuteWhileCommit(proc);
	}

	public boolean updateRankList(xbean.PvP5ScoreRankList rankList) {
		if (rankList != null) {
			for (xbean.PvP5ScoreRecord record : rankList.getRecords()) {
				if (record.getRoleid() == roleId) {
					if (record.getRolename().equals(newName) == false) {
						record.setRolename(newName);
					}
					return true;
				}
			}
		}
		return true;
	}
}
