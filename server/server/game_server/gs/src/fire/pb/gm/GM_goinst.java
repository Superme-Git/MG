package fire.pb.gm;

import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.InstanceZoneFactory;
import fire.pb.instancezone.Module;
import fire.pb.instancezone.PCreateInstZone;
import fire.pb.instancezone.PInstNpcService;
import fire.pb.instancezone.conf.InstanceZoneConfig;
import fire.pb.scene.Scene;

public class GM_goinst extends GMCommand {

	@Override
	boolean exec(String[] args) {
		long roleId = getGmroleid();
		if (args.length == 0) {
			sendToGM("参数错误：" + usage());
			return false;
		} else if (args.length == 1) {
			Integer instid = Integer.parseInt(args[0]);
			InstanceZoneConfig zoneconfig = Module.getInstance()
					.getInstanceZoneConfigs().get(instid.intValue());
			if (zoneconfig == null) {
				this.sendToGM("副本id:" + instid + "没有找到配置！");
				return false;
			}
			new PInstNpcService(instid, roleId, true).submit();

		} else if (args.length == 2) {
			if (args[0].equals("key")) {
				Long instkey = Long.parseLong(args[1]);
				if (instkey.longValue() > 0) {
					InstanceZone oldzone = InstanceZoneFactory.getInstanceZone(
							instkey.longValue(), false);
					if (null == oldzone)
						return false;
					return oldzone.enter(roleId, true);
				}
			} else if (args[0].equals("create")) {
				Integer instid = Integer.parseInt(args[1]);
				if (instid.intValue() > 0) {
					new PCreateInstZone(roleId, instid, 0, true).call();
				}
			} else if (args[0].equals("getkey")) {
				InstanceZone izone = InstanceZoneFactory
						.getRoleCurInstanceZone(roleId, true);
				if (null != izone) {
					long instkey = izone.getLandkey();
					this.sendToGM("当前副本key:" + instkey);
					Scene.LOG.debug("角色[" + roleId + "]当前副本key:" + instkey);
				}
			}
		}
		return true;
	}

	@Override
	String usage() {
		return "goinst [instid]";
	}
}
