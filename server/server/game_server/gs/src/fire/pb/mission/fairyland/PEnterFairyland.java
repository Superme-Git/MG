

package fire.pb.mission.fairyland;

import java.util.ArrayList;
import java.util.List;

import mkdb.Procedure;
import fire.pb.PropRole;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.map.SceneManager;
import fire.pb.map.Transfer;
import fire.pb.mission.FairylandStatus;
import fire.pb.mission.SFairylandStatus;
import fire.pb.move.SRoleEnterScene;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.MapUtil;
import fire.pb.util.MessageUtil;
import xbean.RolePos;


public class PEnterFairyland extends Procedure {

	private int mapid;

	private int posx;

	private int posy;
	
	private boolean playCG;

	long roleid;
	
	@SuppressWarnings("unused")
	private boolean isLeader;

	public PEnterFairyland(long roleid, int mapid, int posx, int posy,boolean playCG,boolean isLeader) {

		super();
		this.roleid = roleid;
		this.mapid = mapid;
		this.posx = posx;
		this.posy = posy;
        this.playCG = playCG;
        this.isLeader = isLeader;
	}

	@Override
	protected boolean process() throws Exception {
		Team team = TeamManager.getTeamByRoleId(roleid);
		//欧阳可以保证只有跟队长有同样梦境任务的人才会调这个方法,所以先把dynamic map坐标存下来
		saveDynamicMapPos();
      if (!Transfer.canChangeMap(roleid,(playCG)?SRoleEnterScene.QUEST_CG : SRoleEnterScene.QUEST, mapid)) 
    	  return true;//return true是为了保证队员的dynamic pos能保存下来,下同 11.09.17
      //做个限制,只有队长,或者单人,才能返回梦境,暂离队员不可以
      if (team!=null&&!team.isTeamLeader(roleid)) {
    	 fire.pb.talk.MessageMgr.psendMsgNotify(roleid, 144846, 0, null);     	  
    	  return true;
      }
		Role role = RoleManager.getInstance().getRoleByID(roleid);
		if (role == null)
			return true;
		// 保存之前的位置,由于存在梦中梦的情况,如果原坐标是在梦境中,则不保存
		if (!FairylandMgr.isInDreamMap(role.getScene())) {
			final int roleMap = MapUtil.getBaseMapIdBySceneId(role.getScene());
			final int positionx = role.getPos().toGridPos().getX();
			final int positiony = role.getPos().toGridPos().getY();
			saveOriginalPos(roleid, roleMap, positionx, positiony);
		}
		

		List<Long> roleids = null;
		if (TeamManager.isTeamLeader(roleid)) {
			roleids = TeamManager.getTeamByRoleId(roleid).getNormalMemberIds();
		} else {
			roleids = new ArrayList<Long>();
			roleids.add(roleid);
		}
		//int leaderDreamTaskid = Module.getInstance().hasUnfinishedDreamlandQuest(roleid);
		for (Long rid : roleids) {
			xbean.RolePos rolePos = xtable.Roleposes.get(rid);
			if (rolePos == null) {
				rolePos = xbean.Pod.newRolePos();
				xtable.Roleposes.insert(rid, rolePos);
			}
			rolePos.setOwnerid(roleid);//保存副本的owner,当team变化要踢人的时候有用
		//	if (rid==roleid||Module.getInstance().hasUnfinishedDreamlandQuest(rid)==leaderDreamTaskid) {   //只有owner才存副本的坐标,其他人都不存,这是因为其他队员可能有自己的梦境,如果存了的话他们自己的梦境的起始点就错了
//				rolePos.setDynamicmap(mapid);
//				rolePos.setDynamicposx(this.posx);
//				rolePos.setDynamicposy(this.posy);
		//	}
			SFairylandStatus sds = new SFairylandStatus(FairylandStatus.LANDIN);
			psendWhileCommit(rid, sds);
		}
		String mapName=SceneManager.getMapNameByMapID(mapid);
		Transfer.justGotoWhileCommit(roleid, mapid, posx, posy, (playCG)?SRoleEnterScene.QUEST_CG:SRoleEnterScene.QUEST);
		// 跳转 副本设置可以重用,因为可能会频繁暂离又返回.梦境副本用mapid直接做ownerid,这样可以保证只有一个副本存在
		//DuplicateHelper.enterDynamicSceneWhileCommit(roleid, mapid, posx, posy, mapid, mapName, true, DynamicSceneType.DREAMLAND_SCENE, (playCG)?SRoleEnterScene.QUEST_CG:SRoleEnterScene.QUEST);
		//DuplicateHelper.enterDynamicMapWhileCommit(roleid, mapid, posx, posy, roleid, mapName, true, true, playCG, 0);
		
		//send msg
		String leaderName = new PropRole(roleid, true).getName();
		List<String> paras = MessageUtil.getMsgParaList(leaderName,mapName);
        for (Long rid : roleids) {
        	if (rid == roleid) {
        		MessageMgr.psendMsgNotifyWhileCommit(rid, 142065, MessageUtil.getMsgParaList(mapName));
			}else {
				MessageMgr.psendMsgNotifyWhileCommit(rid, 142066, paras);
			}
		}
		return true;
	}

	
	
	private void saveDynamicMapPos() {
		xbean.RolePos rolePos = xtable.Roleposes.get(roleid);
		if (rolePos == null) {
			rolePos = xbean.Pod.newRolePos();
			xtable.Roleposes.insert(roleid, rolePos);
		}
		rolePos.setDynamicmap(mapid);
		rolePos.setDynamicposx(this.posx);
		rolePos.setDynamicposy(this.posy);
	}
	private void saveOriginalPos(long roleid, int roleMap, int posx, int posy) {

		List<Long> roleids = null;
		if (TeamManager.isTeamLeader(roleid)) {
			Team team = TeamManager.getTeamByRoleId(roleid);
			if (team != null)
				roleids = team.getNormalMemberIds();
			else {
				roleids = new ArrayList<Long>();
				roleids.add(roleid);
			}
		} else {
			roleids = new ArrayList<Long>();
			roleids.add(roleid);
		}
		for (Long rid : roleids) {
			xbean.RolePos rolePos = xtable.Roleposes.get(rid);
			if (rolePos == null) {
				rolePos = xbean.Pod.newRolePos();
				xtable.Roleposes.insert(rid, rolePos);
			}
			rolePos.setStatus(RolePos.INDREAM);
			rolePos.setMapid(roleMap);
			rolePos.setPosx(posx);
			rolePos.setPosy(posy);
			// 跟队长有一样任务的玩家要置1.如果有一个玩家自己有别的梦境,又帮队长做梦境,则他不应该出现在mems中.dynamic坐标也不用保存了,返回梦境的时候由
			// 剧情任务指定坐标
			// if ((mems!=null&&mems.contains(rid))||roleid==rid)

			
			// rolePos.setHastask(1); 通过有没有梦境任务来判断,不需要setHasTask了

		}

	}
}
