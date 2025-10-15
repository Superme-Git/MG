package fire.pb.gm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GM_moverole extends GMCommand {
	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		final long roleId = Long.valueOf(args[0]);
		final int gmuserid = getGmUserid();

		new mkdb.Procedure() {
			protected boolean process() throws Exception {
				xbean.Properties targetprop = xtable.Properties.get(roleId);
				if (targetprop == null || targetprop.getDeletetime() > 0) {
					sendToGM("该角色不存在或已经删除");
					return false;
				}
				xbean.User user = xtable.User.get(gmuserid);
				clearInvalidRoleIds(user, gmuserid);
				for (long roleid : user.getIdlist()) {
					final xbean.Properties pro = xtable.Properties
							.select(roleid);
					if (null == pro)
						continue;
					if (pro.getDeletetime() > 0)
						continue;
				}
				int olduserid = targetprop.getUserid();
				if (!user.getIdlist().contains(roleId))
					user.getIdlist().add(roleId);
				targetprop.setUserid(gmuserid);

				xbean.User olduser = xtable.User.get(olduserid);
				if (olduser != null)
					olduser.getIdlist().remove((Long) roleId);
				clearInvalidRoleIds(olduser, olduserid);
				sendToGM("账号 " + roleId + " 已经与你的账号 " + gmuserid
						+ " 绑定，原来的账号为：" + olduserid);
				return true;
			}
		}.submit();

		return true;
	}

	private void clearInvalidRoleIds(xbean.User user, int userId) {
		Set<Long> alreadyhaveids = new HashSet<Long>();
		for (Iterator<Long> it = user.getIdlist().iterator(); it.hasNext();) {
			Long roleid = it.next();
			final xbean.Properties pro = xtable.Properties.select(roleid);
			if (null == pro) {
				it.remove();
				continue;
			}
			if (pro.getUserid() != userId) {
				it.remove();
				continue;
			}
			if (alreadyhaveids.contains(roleid)) {
				it.remove();
				continue;
			}
			alreadyhaveids.add(roleid);
		}
	}

	@Override
	String usage() {
		return "参数为roleid";
	}

}
