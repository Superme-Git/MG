

package fire.pb.mission.fairyland;

import gnet.link.Onlines;
import mkdb.Procedure;
import mkdb.Transaction;
import fire.pb.mission.FairylandStatus;
import fire.pb.mission.Module;
import fire.pb.mission.SFairylandStatus;
import fire.pb.scene.Scene;
import fire.pb.scene.SceneManager;
import fire.pb.scene.StaticScene;

public class FairylandMgr {

	private static FairylandMgr instance = new FairylandMgr();

	public static FairylandMgr getInstance() {

		return instance;
	}

	public static void gotoFromDreamland(long roleid,int originalMapid,int newMapid) {
		if (isInDreamMap(originalMapid)){
			if (Module.getInstance().hasUnfinishedFairylandMission(roleid)>0) {
				if (!isInDreamMap(newMapid)){//如果跳转的目的地不是梦境,要通知客户端
					SFairylandStatus sds = new SFairylandStatus();
					sds.status = FairylandStatus.LANDABSENT;
					Procedure.psendWhileCommit(roleid, sds);
				}
			}else {//没有梦境任务
				SFairylandStatus sds = new SFairylandStatus();
				sds.status = FairylandStatus.LANDOUT;
				Procedure.psendWhileCommit(roleid, sds);
			}
		}
	}
	
	public static boolean isInDreamMap(long sceneid) {
		Scene scene = SceneManager.getInstance().getSceneByID(sceneid);
		if (scene!=null){
			if (scene instanceof StaticScene) {
//					return ((StaticScene) scene).isDreamland();
			}
		}
		return false;
	}
	
	private FairylandMgr() {
	}

	// 暂离梦境
	public boolean absentDreamLand(long roleid, boolean hasDreamTask) {

		xbean.RolePos rolePos = xtable.Roleposes.select(roleid);
		if (rolePos == null)
			return false;

		if (Transaction.current() != null) {
			Procedure.pexecuteWhileCommit(new PAbsentFairyland(roleid, hasDreamTask));
			return true;
		} else {
			new PAbsentFairyland(roleid, hasDreamTask).submit();
		}
		return true;
	}
	
	public boolean enterIntoDreamLand(final long roleid, int mapid, int positionx, int positiony,boolean playCG,boolean isLeader) {

		if (Transaction.current() != null) {
			Procedure.pexecuteWhileCommit(new PEnterFairyland(roleid, mapid, positionx, positiony,playCG,isLeader));
		} else {
			new PEnterFairyland(roleid, mapid, positionx, positiony,playCG,isLeader).submit();
		}
		return true;
	}
	

	// 队伍或个人完成任务,需要跳出梦境的时候调用
	public boolean finishDreamLandTask(long roleid,int mapid,int posx,int posy) {

		xbean.RolePos rolePos = xtable.Roleposes.select(roleid);
		if (rolePos == null)
			return false;

		if (Transaction.current() != null) {
			return new PLeaveFairyLand(roleid,mapid,posx,posy).call();
		} else {
			new PLeaveFairyLand(roleid,mapid,posx,posy).submit();
		}
		return true;
	}

	public boolean finishDreamLandTask(long roleid,int mapid,int posx,int posy,boolean playCG) {

		xbean.RolePos rolePos = xtable.Roleposes.select(roleid);
		if (rolePos == null)
			return false;

		if (Transaction.current() != null) {
			return new PLeaveFairyLand(roleid,mapid,posx,posy).call();
		} else {
			new PLeaveFairyLand(roleid,mapid,posx,posy).submit();
		}
		return true;
	}

	// 如果玩家在上线的时候发现有未完成的梦境任务,则调用这个方法,它会通知客户端加上返回梦境按钮
	public void roleHasDreamTask(long roleid) {

		SFairylandStatus sds = new SFairylandStatus(FairylandStatus.LANDABSENT);
		Onlines.getInstance().send(roleid, sds);
	}
}
