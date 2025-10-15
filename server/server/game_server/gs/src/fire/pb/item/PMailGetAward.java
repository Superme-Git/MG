package fire.pb.item;

import fire.pb.compensation.CompensationRole;
import fire.pb.compensation.PReceiveCompensationAward;

public class PMailGetAward extends mkdb.Procedure {

	private final long roleId;
	private final byte kind;
	private final long id;

	public PMailGetAward(final long roleId, final byte kind, final long id) {
		this.roleId = roleId;
		this.kind = kind;
		this.id = id;
	}

	@Override
	public boolean process() {

		boolean ret = false;

		if (kind == 0) {
			// 直接领奖
			if (new PReceiveCompensationAward(roleId, (int) id).call()) {
				ret = true;
			}
		} else if (kind == 1) {
			CompensationRole compensRole = CompensationRole.getSingleCompensation(roleId, false);
			if (compensRole != null) {
				if (compensRole.receiveAward(id)) {
					ret = true;
				}
			}
		}

		if (ret) {
			// 发送领取状态
			SMailState send = new SMailState();
			send.kind = kind;
			send.id = id;
			send.statetype = 1;
			send.statevalue = 1;
			mkdb.Procedure.psendWhileCommit(roleId, send);
		}
		
		return ret;
	}
}
