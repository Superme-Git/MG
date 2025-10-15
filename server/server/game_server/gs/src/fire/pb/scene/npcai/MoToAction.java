package fire.pb.scene.npcai;

import fire.pb.move.SNPCMoveTo;
import fire.pb.scene.manager.RoleManager;
import fire.pb.scene.movable.NPC;
import fire.pb.scene.movable.Role;
import fire.pb.scene.sPos.Position;

public final class MoToAction extends Action 
{
	MoToAction(NPC n, Config_moveto cm)
	{
		super(n);
		to = cm.getDest();
		speed = cm.getSpeed();
	}
	
	@Override
	boolean checkEnd() {
		final NPC  npc = getNPC();
		if(null == npc)
			return true;
		
		final Position where = getNPC().getPos();
		return where.getDirectDistance(to) < 2;
	}
	
	@Override
	void start()
	{
		from = getNPC().getPos();
		startTick = System.currentTimeMillis();
		getNPC().setCurDestPos(to.getX(), to.getY());
		getNPC().setSpeed(speed);
		
		final SNPCMoveTo snd = new SNPCMoveTo();
		snd.npckey = getNPC().getNpcKey();
		snd.destpos = to.toProtocolPos();
		snd.speed = speed;
		getNPC().sendAround(snd);
	}
	
	final private float getSpeedPerMilliSec()
	{
		return (float) speed / 1000;
	}
	
	/**
	 * 
	 * 根据当前的时刻确定NPC所处的位置
	 * 可以扩展到 加速移动等
	 * 
	 */
	final private Position getNowLocation(long tick)
	{
		final int len = (int)( (tick - startTick) * getSpeedPerMilliSec());
		
		return from.getPositionBetween(to, len);
	}
	
	@Override
	void exec(long curTick)
	{
		final NPC npc  = getNPC();
		if(null == npc)
			return;
		
		final Position loc = getNowLocation(curTick);
		npc.updatePosition(loc);
		//Scene.logger.info(getNPC().getName() + "\t now "+ loc.toGridPos());
		
		if(null == npc.getRelatives())
			return;
		
		for(long a : npc.getRelatives())
		{
			final Role r = RoleManager.getInstance().getRoleByID(a);
			if(null == r)
				continue;
			
			r.updatePosition(loc);
		}
	}
	
	private Position from;
	private Position to;
	private int speed;
	long startTick;

}

