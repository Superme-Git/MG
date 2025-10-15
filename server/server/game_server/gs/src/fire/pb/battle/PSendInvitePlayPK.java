package fire.pb.battle;

import java.util.Arrays;
import java.util.List;

import fire.msp.npc.GCheckCanPlayPK;
import fire.pb.GsClient;
import fire.pb.PropRole;
import fire.pb.StateCommon;
import fire.pb.battle.PlayPKManage;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import mkdb.Procedure;

public class PSendInvitePlayPK extends Procedure {
	private final long hostid;
	private long guestid;
	
	public PSendInvitePlayPK(long roleid,long otherid){
		super();
		this.hostid = roleid;
		this.guestid = otherid;
	}
	
	@Override
	protected boolean process() throws Exception {
		
		if (-1 == hostid)
			return false;
		
		Role hostRole = RoleManager.getInstance().getRoleByID(hostid);
		if (hostRole == null)
			return false;
		
		Role guestRole = RoleManager.getInstance().getRoleByID(guestid);
		if (guestRole == null){
			fire.pb.talk.MessageMgr.sendMsgNotify(hostid, 145001, null);
			return false;
		}
		
		Team host_Team = TeamManager.selectTeamByRoleId(hostid);
		if (host_Team != null && host_Team.isNormalMember(hostid)){
			fire.pb.talk.MessageMgr.sendMsgNotify(hostid, 120061, 0, null);
			return false;
		}
		Team guest_team = TeamManager.selectTeamByRoleId(guestid);
		if (guest_team != null && guest_team.isNormalMember(guestid))
			guestid = guest_team.getTeamLeaderId();
		
		BuffAgent guestBuff = new BuffRoleImpl(guestid, true);
		if (guestBuff.existBuff(BuffConstant.StateType.STATE_BATTLE_FIGHTER)) {
			MessageMgr.sendMsgNotify(hostid,144987, null);
			return false;
		}
		//判断切磋等级限制
		if(host_Team!=null){
			List<Long> host_TeamMembers = host_Team.getNormalMemberIds();
			for (Long mem : host_TeamMembers) {
				PropRole role = new PropRole(mem, true);
				if (role.getLevel() < CSendInvitePlayPK.PVP_LEVEL) {
					fire.pb.talk.MessageMgr.sendMsgNotify(hostid, 160322 ,Arrays.asList(CSendInvitePlayPK.PVP_LEVEL+""));
					return false;
				}
			}
		}
		if(guest_team!=null){
			List<Long> guest_TeamMembers = guest_team.getNormalMemberIds();
			for (Long member : guest_TeamMembers) {
				PropRole role = new PropRole(member, true);
				if (role.getLevel() < CSendInvitePlayPK.PVP_LEVEL) {
					fire.pb.talk.MessageMgr.sendMsgNotify(hostid, 160322 ,Arrays.asList(CSendInvitePlayPK.PVP_LEVEL+""));
					return false;
				}
			}
		}
		if (guestBuff.existBuff(500343)) {
			fire.pb.talk.MessageMgr.sendMsgNotify(hostid, 144999, 0, null);
			return false;
		}
		if (!StateCommon.isOnlineBuffer(guestid)) {
			fire.pb.talk.MessageMgr.sendMsgNotify(hostid, 145001, null);
			return false;
		}
		
		// 判断两者之间的距离
		if (!checkRoleDistance(hostid, guestid)){
            fire.pb.talk.MessageMgr.sendMsgNotify(hostid, 120063, 0, null);			
            return false;
		}
		// 判断是否在擂台上,交给场景去做
		GCheckCanPlayPK gccq = new GCheckCanPlayPK(hostid, guestid, 0);
		GsClient.sendToScene(gccq);
		return true;
	}
	
	/**
	 * 检测目标距离
	 * @param hostid
	 * @param guestid
	 * @return
	 */
	public static boolean checkRoleDistance(long hostid, long guestid) {
		final fire.pb.map.Role role1 = fire.pb.map.RoleManager.getInstance().getRoleByID(hostid);
		final fire.pb.map.Role role2 = fire.pb.map.RoleManager.getInstance().getRoleByID(guestid);
		if (null == role1 || null == role2)
			return false;
		if (!role1.checkDistance(role2, PlayPKManage.MAZ_DISTANCE)){
			return false;
		}else{
			return true;
		}
	}

}
