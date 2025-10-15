package fire.msp;

import fire.pb.battle.pvp1.PvP1Control;
import fire.pb.battle.pvp1.PvP1Helper;
import fire.pb.battle.pvp3.PvP3Control;
import fire.pb.battle.pvp3.PvP3Helper;
import fire.pb.battle.pvp5.PvP5Control;
import fire.pb.battle.pvp5.PvP5Helper;
import fire.pb.instancezone.bingfeng.BingFengLandMgr;
import fire.pb.mission.fairyland.FairylandMgr;
import fire.pb.timer.AbstractScheduledActivity;
import fire.pb.util.MapUtil;

/**
 * 人物换地图通知逻辑，调用此Procedure
 * @author nobody
 *
 */
public class PRoleChangeMap extends mkdb.Procedure
{
	private long roleId;
	private long originSceneId;
	private long dstSceneId;

	public PRoleChangeMap(long roleId, long originSceneId,long dstSceneId)
	{
		this.roleId = roleId;
		this.originSceneId = originSceneId;
		this.dstSceneId = dstSceneId;
	}

	@Override
	protected boolean process()
	{
		AbstractScheduledActivity.sendActivityNpcToMiniMap(dstSceneId,roleId);
		int originalMapid = MapUtil.getBaseMapIdBySceneId(originSceneId);
		int newMapid = MapUtil.getBaseMapIdBySceneId(dstSceneId);
		FairylandMgr.gotoFromDreamland(roleId, originalMapid, newMapid);
		// 1v1
		if (PvP1Helper.isPvPMap((int)originSceneId) || PvP1Helper.isPvPMap((int)dstSceneId)) {
			PvP1Control.getInstance().onRoleLeaveScene(roleId, (int)originSceneId, true);
			PvP1Control.getInstance().onRoleEnterScene(roleId, (int)dstSceneId);
		}
		// 3v3
		if (PvP3Helper.isPvPMap((int)originSceneId) || PvP3Helper.isPvPMap((int)dstSceneId)) {
			PvP3Control.getInstance().onRoleLeaveScene(roleId, (int)originSceneId, true);
			PvP3Control.getInstance().onRoleEnterScene(roleId, (int)dstSceneId);
		}
		// 5v5
		if (PvP5Helper.isPvPMap((int)originSceneId) || PvP5Helper.isPvPMap((int)dstSceneId)) {
			pexecuteWhileCommit(new mkdb.Procedure() {
				@Override
				protected boolean process() {
					PvP5Control.getInstance().onRoleLeaveScene(roleId, (int)originSceneId, true);
					PvP5Control.getInstance().onRoleEnterScene(roleId, (int)dstSceneId);
					return true;
				}
			});
		}
		
		if (BingFengLandMgr.bingFengMaps.contains(newMapid)){
			BingFengLandMgr.getInstance().sendAfterEnterBingFengLand(roleId, false);
		}
		
		return true;
	}
}
