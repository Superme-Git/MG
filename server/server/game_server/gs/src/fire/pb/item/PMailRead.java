package fire.pb.item;

import fire.pb.compensation.CompensationRole;

public class PMailRead extends mkdb.Procedure {

	private final long roleId;
	private final byte kind;
	private final long id;

	public PMailRead(final long roleId, final byte kind, final long id) {
		this.roleId = roleId;
		this.kind = kind;
		this.id = id;
	}

	@Override
	public boolean process() {

		boolean ret = false;

		if (kind == 0) {
			// 设置已读
			int compensationid = (int)id;

			xbean.CompensationInfo cInfo = null;
			xbean.Compensation cTable = xtable.Compensation.get(roleId);
			if (cTable != null) {
				cInfo = cTable.getCompensationmap().get(compensationid);
				if (cInfo != null) {
					if (cInfo.getReadflag() == 0) {
						cInfo.setReadflag(1);
					}
				}
			}
			// 通知客户端刷新
			ret = true;
		} else if (kind == 1) {
			// 设置已读
			long compensKey = id;
			CompensationRole compensRole = CompensationRole.getSingleCompensation(roleId, false);
			compensRole.setReadflag(compensKey);
			// 通知客户端刷新
			ret = true;
		}

		if (ret) {
			// 发送读取状态
			SMailState send = new SMailState();
			send.kind = kind;
			send.id = id;
			send.statetype = 0;
			send.statevalue = 1;
			mkdb.Procedure.psendWhileCommit(roleId, send);
		}

		return ret;
	}
}
