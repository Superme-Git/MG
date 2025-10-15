package fire.pb.instancezone.act;

import java.util.Map;

import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.conf.ActionConfig;
import fire.pb.instancezone.conf.ConfigParamName;
import fire.pb.main.ConfigManager;
import fire.pb.map.DuplicateHelper;
import fire.pb.map.MapConfig;
import fire.pb.map.Transfer;
import fire.pb.move.DynamicSceneType;
import fire.pb.move.SRoleEnterScene;

public class GotoAct extends ServiceAct {

	public final int mapId;

	public final int posx;

	public final int posy;

	public GotoAct(int actId, Map<String, Object> params) {
		super(actId, params);
		this.mapId = (Integer) params.get(ConfigParamName.MAP_ID);
		this.posx = (Integer) params.get(ConfigParamName.POSX);
		this.posy = (Integer) params.get(ConfigParamName.POSY);
	}

	@Override
	public int getType() {
		return ActionConfig.TYPE_GOTO;
	}

	@Override
	public boolean defaultprocess(InstanceZone instzone) {
		MapConfig mapcfg = ConfigManager.getInstance().getConf(MapConfig.class)
				.get(mapId);
		if (mapcfg == null)
			return false;
		boolean isStatic = (mapcfg.dynamic == 0);
		boolean inProcedure = (mkdb.Transaction.current() != null);
		if (isStatic) {
			if (inProcedure)
				Transfer.justGotoWhileCommit(roleId, mapId, posx, posy,
						SRoleEnterScene.CHEFU);
			else
				Transfer.justGoto(roleId, mapId, posx, posy,
						SRoleEnterScene.CHEFU);
		} else {
			if (inProcedure)
				DuplicateHelper.enterDynamicMap(roleId, mapId, posx, posy,
						instzone.getLandkey(), "", true,
						DynamicSceneType.NORMAL_SCENE, SRoleEnterScene.CHEFU);
			else
				DuplicateHelper.enterDynamicSceneWhileCommit(roleId, mapId,
						posx, posy, instzone.getLandkey(), "", true,
						DynamicSceneType.NORMAL_SCENE, SRoleEnterScene.CHEFU);
		}
		return true;
	}
}
