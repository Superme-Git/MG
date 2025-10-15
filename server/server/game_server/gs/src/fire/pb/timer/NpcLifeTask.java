package fire.pb.timer;

import java.util.TimerTask;

import fire.pb.npc.PRemoveNpcFromGS;

public class NpcLifeTask extends TimerTask{

	private final long npckey;
	private final int npcid;
	public NpcLifeTask(final long npckey,final int npcid)
	{
		this.npckey = npckey;
		this.npcid = npcid;
	}
	
	@Override
	public void run() {
		String trace = "NpcLifeTask.npcid:"+npcid;
		//添加对摇钱树Npc的处理
		if (npcid == fire.pb.item.groceries.YaoQianShuItem.YAOQIANSHUNPC) {
			new fire.pb.npc.PRemoveYaoQian(npckey).submit();
		}

		new  PRemoveNpcFromGS(npckey,trace).submit();
	}

}
