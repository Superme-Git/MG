package fire.pb.circletask.catchit;

import fire.pb.circletask.CircTaskClass;
import fire.pb.npc.NpcServiceManager;
import fire.pb.npc.SVisitNpc;
import mkdb.Procedure;

public class PSendCatchItNpcService extends Procedure {
	private final int npcid;
	private final long npckey;
	private final long roleid;
	
	private final int QUESTID = 1030000;
	
	public PSendCatchItNpcService(final int npcid, final long npckey, final long roleid) {
		this.npcid = npcid;
		this.npckey = npckey;
		this.roleid = roleid;
	}

	@Override
	protected boolean process() throws Exception {
		xbean.CircleTaskMap questMap = xtable.Rolecircletask.select(roleid);
		if (null == questMap) {
			final SVisitNpc svisitNpc = new SVisitNpc();
			svisitNpc.npckey = npckey;
			mkdb.Procedure.psendWhileCommit(roleid, svisitNpc);
			return true;
		}
		
		xbean.CircleTaskInfo questInfo = questMap.getTaskmap().get(QUESTID);
		if (questInfo == null) {
			final SVisitNpc svisitNpc = new SVisitNpc();
			svisitNpc.npckey = npckey;
			mkdb.Procedure.psendWhileCommit(roleid, svisitNpc);
			return true;
		}
		
		if ((questInfo.getQuestclass() == CircTaskClass.CircTask_ChallengeNpc || questInfo.getQuestclass() == CircTaskClass.CircTask_CatchIt) 
				&& questInfo.getDstnpcid() == npcid) {
			final java.util.List<Integer> services = NpcServiceManager.getInstance().getServicesIDSByNpcKey(roleid, npckey);
			if (services == null || services.isEmpty())
				return false;
			
			final SVisitNpc svisitNpc = new SVisitNpc();
			svisitNpc.npckey = npckey;
			for (int i = 0; i < services.size(); i++) {
				Integer serviceId = services.get(i);
				svisitNpc.services.add(serviceId);// 如果有
			}
			
			mkdb.Procedure.psendWhileCommit(roleid, svisitNpc);
			return true;
		}
		else {
			final SVisitNpc svisitNpc = new SVisitNpc();
			svisitNpc.npckey = npckey;
			mkdb.Procedure.psendWhileCommit(roleid, svisitNpc);
		}
		return true;
	}
	
	

}
