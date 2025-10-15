package fire.pb.gm;

import fire.pb.main.ConfigManager;
import fire.pb.map.DuplicateHelper;
import fire.pb.map.MapConfig;
import fire.pb.map.Transfer;
import fire.pb.move.DynamicSceneType;
import fire.pb.move.SRoleEnterScene;
import fire.pb.util.MapUtil;

public class GM_goto extends GMCommand {

	@Override
	boolean exec(String[] args) {

		if (args.length < 2) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		final fire.pb.map.Role role = fire.pb.map.RoleManager.getInstance()
				.getRoleByID(getGmroleid());
		if (null == role)
			return false;

		final int x = Integer.parseInt(args[0]);
		final int y = Integer.parseInt(args[1]);

		int mapID = MapUtil.getBaseMapIdBySceneId(role.getScene());
		if (3 == args.length)
			mapID = Integer.parseInt(args[2]);
		long roleid = getGmroleid();
		MapConfig cfg = ConfigManager.getInstance().getConf(MapConfig.class)
				.get(mapID);
		if (cfg == null) {
			sendToGM("没有该地图：" + args[0]);
			return false;
		}

		boolean isStatic = (cfg.dynamic == 0);
		if (isStatic)
			Transfer.justGoto(roleid, mapID, x, y, SRoleEnterScene.QUEST);
		else {
			if (mapID == (int) role.getScene())
				Transfer.justGoto(roleid, role.getScene(), x, y,
						SRoleEnterScene.QUEST);
			else
				DuplicateHelper.enterDynamicMap(roleid, mapID, x, y, roleid,
						cfg.mapName, false, DynamicSceneType.NORMAL_SCENE,
						SRoleEnterScene.QUEST);
		}
		StringBuilder content = new StringBuilder("//goto ");
		content.append(args[0]);
		content.append(" ");
		content.append(args[1]);
		content.append(" ");
		if (args.length > 2)
			content.append(args[2]);
		return true;
	}

	@Override
	String usage() {
		return "goto x y or goto x y mapid";
	}

}
