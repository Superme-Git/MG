package fire.pb.mission;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import fire.pb.GameSystemConfig;
import fire.pb.PropRole;
import fire.pb.STeamVote;
import fire.pb.SysConfigType;
import fire.pb.main.ConfigManager;
import fire.pb.map.MapConfig;
import fire.pb.map.Transfer;
import fire.pb.move.SRoleEnterScene;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;


public class PReqGotoProc extends mkdb.Procedure {
	
	private long roleId;
	private int mapid;
	private int xpos;
	private int ypos;
	
	public PReqGotoProc(long roleId, int mapId, int x, int y){
		this.roleId = roleId;
		this.mapid = mapId;
		this.xpos = x;
		this.ypos = y;
	}

	@Override
	protected boolean process() throws Exception {
		
		Team team = TeamManager.getTeamByRoleId(roleId);
		
		//如果没有队伍 ，或者玩家是暂离队员 直接传送
		if(team == null || team.isAbsentMember(roleId)){
			Transfer.justGoto(roleId, mapid, xpos, ypos, SRoleEnterScene.QUEST);
			return true;
		}
		
		if(!team.isTeamLeader(roleId)){
			return false;
		}
		List<Long> allRoleIDs = new LinkedList<Long>();
		allRoleIDs.addAll(team.getNormalMemberIds());
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, allRoleIDs));
		
		List<Long> notifyTouPiao = new ArrayList<Long>();
		List<Long> touPiao = new ArrayList<Long>();
		
		for(long rid : allRoleIDs){
			if(rid == roleId){
				notifyTouPiao.add(rid);
				continue;
			}
			
			Integer pvpNotifyVal = GameSystemConfig.getSysConfig(rid, SysConfigType.PVPNotify);
			if(pvpNotifyVal== null || pvpNotifyVal.intValue() == 0){
				notifyTouPiao.add(rid);
			}else{
				touPiao.add(rid);
			}
		}
		
		//不需要投票
		if(touPiao.size() == 0){
			Transfer.justGoto(roleId, mapid, xpos, ypos, SRoleEnterScene.QUEST);
			return true;
		}
		
		STeamVote send = new STeamVote();
		PropRole p = new PropRole(roleId, true);
		send.parms.add(p.getName());
		MapConfig cfg = ConfigManager.getInstance().getConf(MapConfig.class).get(mapid);
		if(cfg != null){
			send.parms.add(cfg.getMapName());
		}else{
			send.parms.add("");
		}
		mkdb.Procedure.psendWhileCommit(touPiao, send);
		
		//通知其他玩家 现在正在投票中
		if(notifyTouPiao.size() > 0){
			fire.pb.talk.MessageMgr.psendMsgNotify(notifyTouPiao, 145099, 0, null);
		}
		return true;
	}
}
