

package fire.pb.mission.fairyland;

import java.util.List;

import fire.pb.battle.PlayPKManage;
import fire.pb.map.MapConfig;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.map.Transfer;
import fire.pb.mission.Module;
import fire.pb.mission.SFairylandStatus;
import fire.pb.move.SRoleEnterScene;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.team.TeamMemberState;
import mkdb.Procedure;
import xbean.RolePos;


public class PAbsentFairyland extends Procedure {

	private long roleid;

	private boolean hasDreamTask;

	public PAbsentFairyland(long roleid, boolean hasDreamTask) {

		super();
		this.roleid = roleid;
		this.hasDreamTask = hasDreamTask;
	}

	@Override
	protected boolean process() throws Exception {
		Role role = RoleManager.getInstance().getRoleByID(roleid);
		if (role == null)
			return false;
		long originalScene = role.getScene();
		if (!TeamManager.isInTeam(roleid)) {// 如果不在team里,由于现在梦境跟队伍无关,所以
			xbean.RolePos rolePos = xtable.Roleposes.get(roleid);
			int mapid = rolePos.getMapid();
			int posx = rolePos.getPosx();
			int posy = rolePos.getPosy();
			//如果mapid没有(比如从家族副本进入梦境,进入的时候是不记录副本坐标的),则默认回到临安
			if (mapid == 0){
				MapConfig conf = PlayPKManage.getMapConfig(1007);
				mapid = 1007;
				posx = conf.getXjPos();
				posy = conf.getYjPos();
			}
            //这里不能存dynamic pos,因为玩家可能没有跟队长一样的梦境任务,他只是有自己的另一个梦境任务,然后他退队了,点暂离梦境.这时是不能存的--
			//后果就是下次回来的时候回到原点,可以接受 2011.09.17
			// saveDynamicMapPos(rolePos,roleid);

			// 跳转
			Transfer.justGotoWhileCommit(roleid, mapid, posx, posy, SRoleEnterScene.QUEST);
			// 设状态
			SFairylandStatus sds = new SFairylandStatus();
			// 理论上,单个人暂离梦境的时候,hasdreamtask一定是true
			if (hasDreamTask) {
				sds.status = RolePos.ABSENTDREAM;
				rolePos.setStatus(RolePos.ABSENTDREAM);
			} else {
				sds.status = RolePos.OUTDREAM;
				rolePos.setStatus(RolePos.OUTDREAM);
			}
			psendWhileCommit(roleid, sds);
			MessageMgr.psendMsgNotifyWhileCommit(roleid, 142069, null);

		} else {// 如果是在team中
			Team team = TeamManager.getTeamByRoleId(roleid);
			if (team == null)
				return true;
			if (team.isTeamLeader(roleid)) {// 如果是队长,全队都要离开梦境
				// 如果是暂离,更新队长的副本内位置就可以了,因为只有队长可以带队回归梦境,梦境副本的坐标以队长为准
				// if (status==DreamStatus.ABSENTDREAM){
				// xbean.RolePos rolePos = xtable.Roleposes.get(roleid);
				// //saveDynamicMapPos(rolePos);
				// }
                
				xbean.RolePos leaderPos = xtable.Roleposes.get(roleid);
				int mapid = leaderPos.getMapid();
				int posx = leaderPos.getPosx();
				int posy = leaderPos.getPosy();
				//如果mapid没有(比如从家族副本进入梦境,进入的时候是不记录副本坐标的),则默认回到临安
				if (mapid == 0){
					MapConfig conf = PlayPKManage.getMapConfig(1007);
					mapid = 1007;
					posx = conf.getXjPos();
					posy = conf.getYjPos();
				}
 //               long ownerid = leaderPos.getOwnerid();
				List<Long> roleids = team.getNormalMemberIds();
				for (Long mem : roleids) {
					xbean.RolePos rolePos = xtable.Roleposes.get(mem);
					if (rolePos == null)
						continue;
					Role memRole = RoleManager.getInstance().getRoleByID(mem);
					if (memRole == null || memRole.getScene() != originalScene)// 暂离队员可能不跟队长在同一个副本,不用管他
						continue;
					//之前因为梦境跟队伍关系很紧密,所以有这段代码,现在注释掉
//					if (ownerid==mem){ //有种情况,队长在自己梦境的时候,主动退队或交换队长,这时候他应该留在自己的梦境中
//						//如果进入这个if,说明是在交换队长,把自己设为暂离,这样可以留在梦境中
//						team.setTeamMemberState(mem, TeamMemberState.eTeamAbsent);
//						continue;
//					}
					if (Module.getInstance().hasUnfinishedFairylandMission(mem)>0)
						rolePos.setStatus(RolePos.ABSENTDREAM);
					else {
						rolePos.setStatus(RolePos.OUTDREAM);
						xtable.Roleposes.remove(mem);
					}
					SFairylandStatus sds = new SFairylandStatus(rolePos.getStatus());
					psendWhileCommit(mem, sds);
					MessageMgr.psendMsgNotifyWhileCommit(mem, 142069, null);
				}
//				BuffAgent roleagent = new BuffRoleImpl(roleid, false);
//				if (roleagent.existBuff(CGPlayUtil.cgBuffID)){//如果在播动画,推迟到动画播完再跳
//					ContinualBuff cb = roleagent.getBuff(CGPlayUtil.cgBuffID);
//					xbean.Buff buffbean=cb.getBuffBean();
//					buffbean.getEffects().put(PlayCGBuff.GOTO_EVENT, 1f);
//					buffbean.getEffects().put(PlayCGBuff.MAPID, (float) mapid);
//					buffbean.getEffects().put(PlayCGBuff.POSX, (float) posx);
//					buffbean.getEffects().put(PlayCGBuff.POSY, (float) posy);
//				}else {
					Transfer.justGotoWhileCommit(roleid, mapid, posx, posy, SRoleEnterScene.QUEST);
			//	}
				// 如果有暂离的队员,也把他们踢出副本(除非暂离的队员是副本的owner),这里不用置状态了,因为上面已经设了
				//这段代码页可以注掉,因为暂离的队员,现在可以留在梦境中,等他自己暂离出来就可以了.11.09.17
//				List<Long> absentids = team.getAbsentMemberIds();
//				for (Long mem : absentids) {
//					if (ownerid==mem)//如果他是副本的owner,不做处理,留在副本中
//						continue;
//					Role r = RoleManager.selectRoleByID(mem);
//					if (r != null && r.getScene() == originalScene) {
//						Long battleId = xtable.Roleid2battleid.select(mem);
//						if (battleId != null) {
//							BuffAgent agent = new BuffRoleImpl(mem);
//							AfterBattleBuff cb = (AfterBattleBuff) fire.pb.buff.Module.getInstance().createContinualBuff(MissionConst.AFTER_BATTLE_BUFF);
//							xbean.Buff buff = cb.getBuffBean();
//							buff.getEffects().put(AfterBattleBuff.EVENT_TYPE,(float) AfterBattleBuff.DREAMLAND);
//							buff.getEffects().put(AfterBattleBuff.DREAMLAND_MAPID, (float) mapid);
//							buff.getEffects().put(AfterBattleBuff.DREAMLAND_POSX, (float) posx);
//							buff.getEffects().put(AfterBattleBuff.DREAMLAND_POSY, (float) posy);
//							agent.addCBuffWithSP(cb);
//						} else {
//							Transfer.justGotoWhileCommit(mem, mapid, posx, posy, SRoleEnterScene.QUEST);
//							
//						}
//					}
//
//				}
			} else {// 如果只是普通队员
					// 设为暂离
				team.setTeamMemberStateWithSP(roleid, TeamMemberState.eTeamAbsent);

				xbean.RolePos rolePos = xtable.Roleposes.get(roleid);
				if (hasDreamTask){
				//	saveDynamicMapPos(rolePos, roleid);
					rolePos.setStatus(RolePos.ABSENTDREAM);
				}
				else {
					rolePos.setStatus(RolePos.OUTDREAM);
					xtable.Roleposes.remove(roleid);
				}
				int mapid = rolePos.getMapid();
				int posx = rolePos.getPosx();
				int posy = rolePos.getPosy();
				//如果mapid没有(比如从家族副本进入梦境,进入的时候是不记录副本坐标的),则默认回到临安
				if (mapid == 0){
					MapConfig conf = PlayPKManage.getMapConfig(1007);
					mapid = 1007;
					posx = conf.getXjPos();
					posy = conf.getYjPos();
				}
				// 普通队员不需要做这些处理
				// if (status==DreamStatus.ABSENTDREAM){
				// saveDynamicMapPos(rolePos);
				// }else {//如果是彻底离开梦境,则删除相应的记录
				// xtable.Roleposes.remove(roleid);
				// }
				Transfer.justGotoWhileCommit(roleid, mapid, posx, posy, SRoleEnterScene.FORCE_GOTO);
				SFairylandStatus sds = new SFairylandStatus(rolePos.getStatus());
				psendWhileCommit(roleid, sds);
				MessageMgr.psendMsgNotifyWhileCommit(roleid, 142069, null);
			}
		}
		return true;
	}

//	
//
//	 private void saveDynamicMapPos(RolePos rolePos,long rid) {
//	 Role role = RoleManager.selectRoleByID(rid);
//	 if (rolePos==null){
//		 rolePos = xbean.Pod.newRolePos();
//		 xtable.Roleposes.insert(rid, rolePos);
//	 }
//	 rolePos.setDynamicmap(MapUtil.getBaseMapIdBySceneId(role.getScene()));
//	 rolePos.setDynamicposx(role.getPos().toGridPos().getX());
//	 rolePos.setDynamicposy(role.getPos().toGridPos().getY());
//	 }

}
