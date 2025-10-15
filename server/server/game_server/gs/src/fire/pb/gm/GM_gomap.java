package fire.pb.gm;

import fire.pb.main.ConfigManager;
import fire.pb.map.DuplicateHelper;
import fire.pb.map.MapConfig;
import fire.pb.map.Transfer;
import fire.pb.move.DynamicSceneType;
import fire.pb.move.SRoleEnterScene;

public class GM_gomap extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}

		Integer mapid = null;
		try {
			mapid = Integer.parseInt(args[0]);

		} catch (java.lang.NumberFormatException e) {
			mapid = getObjectIdByName(args[0]);
		}

		if (null == mapid)
			return false;
		long roleid = getGmroleid();

		MapConfig cfg = ConfigManager.getInstance().getConf(MapConfig.class)
				.get(mapid);
		if (cfg == null) {
			sendToGM("没有该地图：" + args[0]);
			return false;
		}
		boolean isStatic = (cfg.dynamic == 0);
		if (isStatic)
		{
			xbean.BuffRole buffRoleBean = xtable.Buffroles.select(roleid);
			xbean.StoredBuffRole storedBuffRole = xtable.Buffrolestodisk.select(roleid);
			
			if (buffRoleBean == null || storedBuffRole == null)
			{
				return false;
			}
				
			Transfer.justGotoRandom(roleid, mapid, SRoleEnterScene.QUEST);
		}
		else
			DuplicateHelper.enterDynamicMap(roleid, mapid, 50, 50, roleid,
					cfg.mapName, false, DynamicSceneType.NORMAL_SCENE,
					SRoleEnterScene.QUEST);

		return true;
	}

	@Override
	String usage() {
		return "gomap mapid";
	}
}
