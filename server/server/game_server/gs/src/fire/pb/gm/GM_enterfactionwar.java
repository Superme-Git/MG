package fire.pb.gm;

import fire.pb.map.DuplicateHelper;
import fire.pb.move.DynamicSceneType;
import fire.pb.move.SRoleEnterScene;

public class GM_enterfactionwar extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		final int duplicateMapkey = Integer.parseInt(args[0]);
		long roleid = getGmroleid();

		DuplicateHelper.enterDynamicMap(roleid, 1403, 100, 100,
				duplicateMapkey, "帮战副本", false, DynamicSceneType.NORMAL_SCENE,
				SRoleEnterScene.SYSTEM_DRAG);
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
