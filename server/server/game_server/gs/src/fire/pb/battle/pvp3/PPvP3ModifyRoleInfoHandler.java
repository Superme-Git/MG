package fire.pb.battle.pvp3;

/**
 * 修改角色信息的事件处理
 * @author XGM
 */
public class PPvP3ModifyRoleInfoHandler extends mkdb.Procedure {

	private final long roleId;
	private final String newName;

	public PPvP3ModifyRoleInfoHandler(long roleId, String newName) {
		this.roleId = roleId;
		this.newName = newName;
	}

	@Override
	protected boolean process() {
		for (int grade : PvP3Helper.getAllRaceIds()) {
			updateHistoryRankList(grade);
		}
		return true;
	}

	public void updateHistoryRankList(int grade) {
		mkdb.Procedure proc = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				return updateRankList(xtable.Pvp3historyscorelist.get(grade));
			}
		};
		mkdb.Procedure.pexecuteWhileCommit(proc);
	}

	public boolean updateRankList(xbean.PvP3ScoreRankList rankList) {
		if (rankList != null) {
			for (xbean.PvP3ScoreRecord record : rankList.getRecords()) {
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
