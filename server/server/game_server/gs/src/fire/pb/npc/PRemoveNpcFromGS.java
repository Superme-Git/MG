package fire.pb.npc;

import java.util.LinkedList;
import java.util.List;

import fire.msp.npc.GRemoveNpcFromScene;
import fire.pb.GsClient;
import fire.pb.map.Npc;
import fire.pb.map.SceneNpcManager;

public class PRemoveNpcFromGS extends mkdb.Procedure
{
	
	private List<Long> npckeys;
	boolean notifyscene = true;
	public String trace;
	public PRemoveNpcFromGS(long npckey,String trace)
	{
		npckeys = new LinkedList<Long>();
		npckeys.add(npckey);
		this.trace = trace;
	}
	public PRemoveNpcFromGS(List<Long> npckeys,boolean notifyscene)
	{
		this.npckeys = npckeys;
		this.notifyscene =notifyscene; 
	}
	
	
	@Override
	protected boolean process()
	{
		for(long npckey : npckeys)
		{
			Npc npc = SceneNpcManager.getInstance().getNpcs().remove(npckey);
			if (npc != null)
			{
				xtable.Npcs.remove(npckey);
				if (npc.getFuture() != null)
					npc.getFuture().cancel(false);
				if(notifyscene)
					GsClient.pSendWhileCommit(new GRemoveNpcFromScene(npckey,trace));
			}
		}		
		return true;
	}
	
}
