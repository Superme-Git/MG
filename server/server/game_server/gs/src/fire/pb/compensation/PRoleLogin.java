package fire.pb.compensation;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 角色上线发邮件奖励
 * @author XGM
 */
public class PRoleLogin extends mkdb.Procedure {

	private final long roleId;

	public PRoleLogin(long roleId) {
		this.roleId = roleId;
	}

	@Override
	protected boolean process() {

		// 按条件发送的邮件上线发一次
		Map<Long, Set<Long>> giveKeyRecord = Module.getGiveKeyRecord();
		for (Map.Entry<Long, Set<Long>> e : giveKeyRecord.entrySet()) {
			if (e.getValue().contains(roleId)) {
				continue;
			}
			List<CompensationCondition> conditions = PGiveSingleCompensition.getCondition(e.getKey());
			if (CompensationManager.getInstance().checkCondition(conditions, roleId) == false) {
				// 添加到排除列表
				mkdb.Procedure proc = Helper.roleToExcept(e.getKey(), roleId);
				mkdb.Procedure.pexecuteWhileCommit(proc);
				continue;
			}
			mkdb.Procedure proc = PGiveSingleCompensition.giveOfRole(roleId, e.getKey(), Helper.GIVETYPE_CONDITION, true);
			mkdb.Procedure.pexecuteWhileCommit(proc);
		}

		// 发送邮件信息给客户端
		new PCheckSendCompensationMsg(roleId, System.currentTimeMillis()).call();
		return true;
	}

}
