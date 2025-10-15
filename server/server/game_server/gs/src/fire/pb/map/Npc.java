package fire.pb.map;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import fire.msp.npc.GSetNpcBattleState;
import fire.pb.GsClient;
import fire.pb.main.ConfigManager;
import fire.pb.npc.SNpcShare;
import fire.pb.timer.NpcLifeTask;


/**
 * NPC 
 * @author nobody
 *
 */
public class Npc extends Body {
	public static final int NOT_INBATTLE = 0;
	public static final int INBATTLE = 1;
	public static final int INBATTLE_AND_TIMEOUT = 2;
	
	private int npcbaseid;
	private String name;
	private int shape;
	private ScheduledFuture<?> future = null;//如果是时限npc，有时间
	
	private xbean.NpcInfo npcinfo;
	private boolean readonly;
	
	public xbean.NpcInfo getNpcinfo()
	{
		return npcinfo;
	}

	Npc(long npckey,boolean readonly)
	{
		super(npckey,Body.TYPE_NPC);
		this.readonly = readonly;
	}
	
	public Npc copy()
	{
		Npc copy = new Npc(getUniqueID(), readonly);
		copy.setScene(this.getScene());
		copy.setPos(this.getPos().copy());
		copy.setNpcId(this.getNpcID());
		copy.setName(this.getName());
		copy.setShape(this.getShape());
		copy.setFuture(this.getFuture());
		return copy;
	}
	public long getNpcKey()
	{
		return getUniqueID();
	}
	
	public int getNpcID(){
		return npcbaseid;
	}
	
	public void setNpcinfo(xbean.NpcInfo npcinfo)
	{
		this.npcinfo = npcinfo;
	}

	/**
	 * 设置NPC的战斗状态
	 * @param inBattle
	 */
	public void setInBattle(int inBattle )
	{
		if(readonly)
			throw new UnsupportedOperationException("不能在只读模式下修改NPC的战斗状态");
		npcinfo.setIsinbattle(inBattle);
		//地图模块的npc需要记录这个字段，此处需要用psendwhilecommit向地图模块同步这个字段
		GsClient.pSendWhileCommit(new GSetNpcBattleState(getNpcKey(), inBattle));
	}
	
	public int getInBattle(){
    	return npcinfo.getIsinbattle();
    }
	
	public void setName(String name)
	{
		this.name = name;;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getShape()
	{
		return shape;
	}
	
	public void setShape(int shape)
	{
		this.shape = shape;
	}

	public ScheduledFuture<?> getFuture() {
		return future;
	}

	public void setNpcId(int npcbaseid)
	{
		this.npcbaseid = npcbaseid;
	}


	public void setFuture(ScheduledFuture<?> future) {
		this.future = future;
	}
    
	public void updateNpcInfo(fire.msp.npc.NpcInfo npcinfo)
	{
		synchronized (this)
		{
			updateMapInfo(npcinfo.sceneid, npcinfo.posx, npcinfo.posy, npcinfo.posz);
			this.setNpcId(npcinfo.npcbaseid);
			this.setName(npcinfo.name);
			this.setShape(npcinfo.shape);
			if (npcinfo.time > 0)
			{
				if(future != null)
					future.cancel(false);
				future = mkdb.Executor.getInstance().schedule(new NpcLifeTask(npcinfo.npckey,npcinfo.npcbaseid), npcinfo.time, TimeUnit.MILLISECONDS);
			}
		}
	}
	public SNpcShare getNpcShare()
	{
		return ConfigManager.getInstance().getConf(SNpcShare.class).get(getNpcID());
	}
}
