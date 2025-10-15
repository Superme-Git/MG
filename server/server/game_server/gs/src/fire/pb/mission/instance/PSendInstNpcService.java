package fire.pb.mission.instance;

import mkdb.Procedure;
import fire.pb.npc.NpcServiceManager;
import fire.pb.npc.SVisitNpc;
import fire.pb.talk.MessageMgr;
import fire.pb.talk.STransChatMessageNotify2Client;

public class PSendInstNpcService extends Procedure {
	private final int npcid;
	private final long npckey;
	private final long roleid;
	
	public PSendInstNpcService(final int npcid, final long npckey, final long roleid) {
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
		
		ServiceConfig serviceConfig = InstanceManager.getInstance().getServiceConfig(services.get(0));
		if (serviceConfig == null)
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
		
		if (serviceConfig.msgid > 0) {
			final java.util.List<Long> members = team.getNormalMemberIds();
			for (Long rid : members) {
//				Message.psendMsgNotifyWhileCommit(rid, serviceConfig.msgid);
				STransChatMessageNotify2Client notify = MessageMgr.getMsgNotify(serviceConfig.msgid, npcid, null);
				gnet.link.Onlines.getInstance().send(rid, notify);
			}
		}
		
		return true;
	}

}
