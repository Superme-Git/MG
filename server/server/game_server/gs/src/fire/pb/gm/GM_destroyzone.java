package fire.pb.gm;

import fire.pb.clan.ClanUtils;
import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.InstanceZoneFactory;
import mkdb.Procedure;

public class GM_destroyzone extends GMCommand {

	@Override
	boolean exec(String[] args) {
		final long roleId;

		if (args.length == 0)
			roleId = getGmroleid();
		else if (args.length == 1)
			roleId = Long.valueOf(args[0]);
		else
			return false;
		Procedure procedure = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				destroyFactionZone();
				return true;
			}

			void destroyFactionZone() {
				xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleId, false);
				if (null == clanInfo)
					return;

				xbean.ClanInstances clanInst = xtable.Claninstances.select(clanInfo.getKey());

				for (long instkey : clanInst.getInstkey().values()) {
					if (instkey > 0) {
						InstanceZone instzone = InstanceZoneFactory.getInstanceZone(instkey, false);
						if (null == instzone) {
							continue;
						}
						boolean destroyed = instzone.destroy(true, false);
						if (destroyed)
							sendToGM("角色 " + roleId + " 的帮派副本 "
									+ instzone.getInstZoneId() + " 摧毁成功");
						else
							sendToGM("角色 " + roleId + " 的帮派副本 "
									+ instzone.getInstZoneId() + " 摧毁失败");
					}
				}
			}
		};
		procedure.submit();

		return true;
	}

	@Override
	String usage() {
		return "//destroyzone [roleid可选，不填默认为摧毁自己和自己队伍的所有副本] 注意：摧毁队伍副本时，此人必须为队长";
	}
}
