

package fire.pb.mission.fairyland;

import java.util.List;

import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.map.Transfer;
import fire.pb.mission.Module;
import fire.pb.mission.SFairylandStatus;
import fire.pb.move.SRoleEnterScene;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import mkdb.Procedure;
import xbean.RolePos;


public class PLeaveFairyLand extends Procedure {

	private long roleid;
	private int mapid;
	private int posx;
	private int posy;
	private boolean playCG;

	// private List<Long> mems;//和队长有同样梦境任务的队员

	

	public PLeaveFairyLand(long roleid,int mapid,int posx,int posy) {

		this.roleid = roleid;
		this.mapid = mapid;
		this.posx = posx;
        this.posy = posy;
	}
	public PLeaveFairyLand(long roleid,int mapid,int posx,int posy,boolean playCG) {

		this.roleid = roleid;
		this.mapid = mapid;
		this.posx = posx;
        this.posy = posy;
        this.playCG = playCG;
	}
	@Override
	protected boolean process() throws Exception {
      Role role = RoleManager.getInstance().getRoleByID(roleid);
      if (role==null)
    	  return false;
      long originalScene =role.getScene();
		if (!TeamManager.isInTeam(roleid)) {// 如果不在team里
		//	xbean.RolePos rolePos = xtable.Roleposes.get(roleid);
//			int mapid = rolePos.getMapid();
//			int posx = rolePos.getPosx();
//			int posy = rolePos.getPosy();

			// 因为是彻底离开梦境,删除相应的记录
			xtable.Roleposes.remove(roleid);

			// 跳转
		//	Transfer.justGoto(roleid, mapid, posx, posy, false);
			leaveDreamLand();
			// 设状态
			SFairylandStatus sds = new SFairylandStatus(RolePos.OUTDREAM);
			psendWhileCommit(roleid, sds);
			MessageMgr.psendMsgNotifyWhileCommit(roleid, 142069, null);

		} else {// 如果是在team中
			Team team = TeamManager.getTeamByRoleId(roleid);
			if (team.isTeamLeader(roleid)) {// 如果是队长
//				xbean.RolePos leaderPos = xtable.Roleposes.get(roleid);
//				int mapid = leaderPos.getMapid();
//				int posx = leaderPos.getPosx();
//				int posy = leaderPos.getPosy();

				// xtable.Roleposes.remove(roleid);
				// SDreamlandStatus sds = new
				// SDreamlandStatus(RolePos.OUTDREAM);
				// psendWhileCommit(roleid, sds);
				// //处理队员
				List<Long> mems = TeamManager.getTeamByRoleId(roleid).getNormalMemberIds();
				if (mems != null) {
					for (long mem : mems) {
						Role memRole = RoleManager.getInstance().getRoleByID(mem);
						if (memRole==null||memRole.getScene()!=originalScene)//暂离队员可能不跟队长在同一个副本,不用管他
							continue;
						SFairylandStatus sds2 = new SFairylandStatus();
						if (Module.getInstance().hasUnfinishedFairylandMission(mem)>0) {
							sds2.status = RolePos.ABSENTDREAM;//队员如果还有自己的梦境,进入这个block
						} else {
							sds2.status = RolePos.OUTDREAM;
							xtable.Roleposes.remove(mem);
						}

						psendWhileCommit(mem, sds2);
						//Message.psendMsgNotifyWhileCommit(mem, 142069, null);
					}
				}
			//	Transfer.justGoto(roleid, mapid, posx, posy, true);
				leaveDreamLand();
				// 如果有暂离的队员,也把他们踢出副本
			//	List<Long> absentids = team.getAbsentMemberIds();
//				for (Long mem : absentids) {
//					Role r = RoleManager.getInstance().getRoleByID(mem);
//					if (r!=null&&r.getScene()==originalScene) {
//						Long battleId = xtable.Roleid2battleid.select(mem);
//						if (battleId != null) {
//							BuffAgent agent = new BuffRoleImpl(mem);
//							AfterBattleBuff cb = (AfterBattleBuff)fire.pb.buff.Module.getInstance().createContinualBuff(MissionConst.AFTER_BATTLE_BUFF);
//							xbean.Buff buff = cb.getBuffBean();
//							buff.getEffects().put(AfterBattleBuff.EVENT_TYPE,(float) AfterBattleBuff.DREAMLAND);
//							buff.getEffects().put(AfterBattleBuff.DREAMLAND_MAPID, (float) mapid);
//							buff.getEffects().put(AfterBattleBuff.DREAMLAND_POSX, (float) posx);
//							buff.getEffects().put(AfterBattleBuff.DREAMLAND_POSY, (float) posy);
//							agent.addCBuffWithSP(cb);
//						} else {
//							Transfer.justGotoWhileCommit(mem, mapid, posx, posy, SRoleEnterScene.FORCE_GOTO);
//						}
//					}
//				}
				if(mems != null)
					for (Long rid : mems) {
						MessageMgr.psendMsgNotifyWhileCommit(rid, 142069, null);
					}
			}
		}
		
     // DuplicateHelper.destroyDynamicSceneWhileCommit(MapUtil.getBaseMapIdBySceneId(originalScene), roleid);
		return true;
	}
	
	
	private void leaveDreamLand() {
		
		Transfer.justGotoWhileCommit(roleid, mapid, posx, posy, (playCG)? SRoleEnterScene.QUEST_CG:SRoleEnterScene.QUEST);
		
	}

}
