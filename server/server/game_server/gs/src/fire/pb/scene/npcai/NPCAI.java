package fire.pb.scene.npcai;

import fire.msp.npc.MNpcAIOver;
import fire.pb.GsClient;
import fire.pb.scene.movable.NPC;

public final class NPCAI 
{
	NPCAI(final NPC n, AIConfig ac)
	{
		npc = n;
		
		for(Config c : ac.getConfigs())
		{
			actions.add(c.generateAction(npc));
		}
	}
	
	final void start()
	{
		if(actions.isEmpty())
			return;
		
		curAction = actions.remove(0);
		if(null == curAction)
			return;
		
		curAction.start();
	}
	
	final void exec(long curTick)
	{
		if(null == curAction)
			return;
		
		if(curAction.checkEnd())
		{
			if(actions.isEmpty())
			{
				curAction = null;
				onOver();
				return;
			}	
			curAction = actions.remove(0);
			curAction.start();
		}
		else
		{
			curAction.exec(curTick);
		}
	}
	
	final boolean checkOver()
	{
		return curAction == null;
	}
	
	final void onOver()
	{
		MNpcAIOver snd = new MNpcAIOver();
		snd.npckey = npc.getNpcKey();
		
		GsClient.pSendWhileCommit(snd);
	}
	
	final long getNpcKey()
	{
		return npc.getNpcKey();
	}
	
	 private final NPC npc;
	 private Action curAction;
	 private java.util.List<Action> actions = new java.util.LinkedList<Action>();
}

