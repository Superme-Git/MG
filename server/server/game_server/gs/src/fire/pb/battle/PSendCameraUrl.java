package fire.pb.battle;
import fire.pb.instancezone.bingfeng.BingFengLandMgr;
import fire.pb.instancezone.bingfeng.BingFengWangZuoConfig;
import xbean.CameraInfo;

/**战斗内所有玩家发送消息 */
public class PSendCameraUrl extends mkdb.Procedure{
	
	private final long roleid;
	private long battleid;
	public PSendCameraUrl(long battleid, long roleid){
		this.battleid = battleid;
		this.roleid = roleid;
	}
	
	public boolean process(){
		BattleConfig battleConfig = fire.pb.battle.Module.getInstance().getBattleConfigs().get((int)battleid);
		if(battleConfig!=null && battleConfig.cameratype > 0)
		{
			CameraInfo cameraInfo = null;
			switch (battleConfig.cameratype) {
			case BattleCameraInfo.CAMERA_TYPE_MISSION: {
				cameraInfo = xtable.Missioncameras.select(battleid);
				break;
			}
			case BattleCameraInfo.CAMERA_TYPE_BINGFENG: {
				final int schoolId = xtable.Properties.selectSchool(roleid);
				
				BingFengWangZuoConfig bfg = BingFengLandMgr.getInstance().getBingFengWangZuoConfigByBattleId((int)battleid);
				int landid = bfg.getLandId();
				xbean.BingFengInfo bingFeng = xtable.Bingfenginfos.select(landid);
				if (bingFeng == null)
					break;
				
				if (bingFeng.getSchoolstage().containsKey(schoolId)) {
					xbean.SchoolStageTime schoolTime = bingFeng.getSchoolstage().get(schoolId);
					if (schoolTime.getStagebest().containsKey(bfg.getStage())) {
						battleid = battleid * 100 + schoolId;
						cameraInfo = xtable.Missioncameras.select(battleid);
						break;
					}
				}
				
				if (bingFeng.getStagebest().containsKey(bfg.getStage())) {
					int ssId = xtable.Properties.selectSchool(bingFeng.getStagebest().get(bfg.getStage()));
					battleid = battleid * 100 + ssId;
					cameraInfo = xtable.Missioncameras.select(battleid);
				}
				
				break;
			}
			}
			if (cameraInfo != null) {
				SSendCameraUrl send = new SSendCameraUrl(battleid, (byte) 1,
						cameraInfo.getSizebeforezip(),
						cameraInfo.getSizeafterzip(),
						cameraInfo.getCamerafileurl());
				psendWhileCommit(roleid, send);
			} else {
				SSendCameraUrl send = new SSendCameraUrl(battleid, (byte) 0, 0,	0, "");
				psendWhileCommit(roleid, send);
			}
		}
		return true;
	}
}
