package fire.pb.mission.instance.line;

import mkdb.Procedure;
import fire.pb.mission.SLineTaskFight;
import fire.pb.npc.NpcServiceManager;
import fire.pb.npc.SVisitNpc;
import fire.pb.talk.MessageMgr;
import fire.pb.talk.STransChatMessageNotify2Client;

public class PSendLineNpcService extends Procedure {
	private final int npcid;
	private final long npckey;
	private final long roleid;
	
	public PSendLineNpcService(final int npcid, final long npckey, final long roleid) {
		this.npcid = npcid;
		this.npckey = npckey;
		this.roleid = roleid;
	}

	@Override
	protected boolean process() throws Exception {
		final java.util.List<Integer> services = NpcServiceManager
				.getInstance().getServicesIDSByNpcKey(roleid, npckey);
		if (services == null || services.isEmpty())
			return false;
		
		SLineTaskFight fightCnf = LineInstManager.getInstance().getFightConfig(services.get(0));
		if (fightCnf == null)
			return false;
		
		//判断队伍人数
		fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleid);
		if (null == team || !team.isTeamLeader(roleid)) {
			STransChatMessageNotify2Client notify = MessageMgr.getMsgNotify(160115, npcid, null);
			gnet.link.Onlines.getInstance().send(roleid, notify);
			return false;
		}
		
		final SVisitNpc svisitNpc = new SVisitNpc();
		svisitNpc.npckey = npckey;
		for (int i = 0; i < services.size(); i++) {
			Integer serviceId = services.get(i);
			svisitNpc.services.add(serviceId);// 如果有
		}
		
		mkdb.Procedure.psendWhileCommit(roleid, svisitNpc);
		
		if (fightCnf.npcmsgid > 0) {
			final java.util.List<Long> members = team.getNormalMemberIds();
			for (Long rid : members) {
				STransChatMessageNotify2Client notify = MessageMgr.getMsgNotify(fightCnf.npcmsgid, npcid, null);
				gnet.link.Onlines.getInstance().send(rid, notify);
			}
		}
		
		return true;
	}

}
