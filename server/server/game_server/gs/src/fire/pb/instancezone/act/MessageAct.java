package fire.pb.instancezone.act;

import java.util.Map;

import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.conf.ActionConfig;
import fire.pb.instancezone.conf.ConfigParamName;
import fire.pb.talk.MessageMgr;

public class MessageAct extends ServiceAct
{
	public final int messageId;

	
	public MessageAct(int actId, Map<String, Object> params)
	{
		super(actId, params);
		this.messageId = (Integer)params.get(ConfigParamName.MSG_ID);
	}

	@Override
	public int getType()
	{
		return ActionConfig.TYPE_MESSAGE;
	}
	
	@Override
	public boolean defaultprocess(InstanceZone instzone)
	{
		Integer npcbaseId = (Integer)getParams().get(ConfigParamName.NPC_ID);
		if(npcbaseId == null)
			npcbaseId = -1;
		if(mkdb.Transaction.current() == null)
			MessageMgr.sendMsgNotify(roleId, messageId, npcbaseId, null);
		else
			MessageMgr.psendMsgNotifyWhileCommit(roleId, messageId, npcbaseId, null);
		
		return true;
	}
}
