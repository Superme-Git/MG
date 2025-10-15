package fire.pb.gm;

import fire.pb.main.ConfigManager;
import fire.pb.map.DuplicateHelper;
import fire.pb.map.MapConfig;
import fire.pb.util.MapUtil;

public class GM_unloadmap extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1)
			return false;

		final String type = args[0];
		long roleid = getGmroleid();

		if (type.equals("self")) {

			final fire.pb.map.Role role = fire.pb.map.RoleManager
					.getInstance().getRoleByID(roleid);
			if (null == role)
				return false;

			int mapID = MapUtil.getBaseMapIdBySceneId(role.getScene());
			MapConfig cfg = ConfigManager.getInstance()
					.getConf(MapConfig.class).get(mapID);
			if (cfg == null) {
				sendToGM("没有该地图");
				return false;
			}
			if (cfg.dynamic == 1) {
				DuplicateHelper.destroyDynamicSceneById(role.getScene());
			}
		}

		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
