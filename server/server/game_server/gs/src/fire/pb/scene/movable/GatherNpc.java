package fire.pb.scene.movable;

import java.util.Calendar;
import java.util.Map;

import fire.pb.main.ConfigManager;
import fire.pb.npc.SGatherConfig;
import fire.pb.scene.Scene;
import fire.pb.scene.manager.GatherManager;
import fire.pb.scene.manager.RoleManager;
import fire.pb.scene.sPos.Position;

/**
 * 采集npc
 * 
 * 
 */
public class GatherNpc extends NPC {
	private volatile int lefttime = -1;
	private volatile Map<Long, Long> gatherplayers = new java.util.concurrent.ConcurrentHashMap<Long, Long>();
	private boolean canRemove = true;
	private final int cooldown;

	GatherNpc(long npckey, int npcid, String name, Scene scene, Position pos,int dir) {
		
		super(npckey, npcid, name, scene, pos, dir);
		SGatherConfig conf = ConfigManager.getInstance().getConf(fire.pb.npc.SGatherConfig.class).get(this.getNpcID());
		if (conf != null) {
			if (conf.可采集次数 != 0)
				lefttime = conf.可采集次数;
			canRemove = conf.是否消失 == 1;
			cooldown = conf.coolingtime;
		} else {
			cooldown = 0;
		}

	}

	public synchronized void onGatherEnd(long roleid, int result) {
		if (cooldown != 0) {
			Calendar now = Calendar.getInstance();
			now.add(Calendar.SECOND, cooldown);
			gatherplayers.put(roleid, now.getTimeInMillis());
		}
		if (lefttime != -1) {
			lefttime--;
			if (lefttime == 0 && canRemove) {
				// 删除采集物
				GatherManager.getInstance().removeGathernpc(
						getScene().getMapID(), this.getUniqueID());
				return;
			}
		}
	}

	public boolean inCooldown(final long roleid) {
		Long cangathertime = gatherplayers.get(roleid);
		if (cangathertime != null
				&& Calendar.getInstance().getTimeInMillis() < cangathertime) {
			return true;
		}
		return false;
	}

	public boolean canGather(final long roleid) {
		// 判断有没有其他人在采集
		if (inCooldown(roleid))
			return false;

		Role player = RoleManager.getInstance().getRoleByID(roleid);
		if (player == null) {
			return true;
		}
		return player.checkDistance(this, 400);

	}

	@Override
	public void setScene(Scene scene) {
		super.setScene(scene);

		Map<Long, GatherNpc> sceneGathers = GatherManager.getInstance().getGathersByMapID(scene.getMapID());
		if (sceneGathers == null) {
			sceneGathers = new java.util.concurrent.ConcurrentHashMap<Long, GatherNpc>();
			GatherManager.getInstance().getGatherMap().put(scene.getMapID(), sceneGathers);
		}
		sceneGathers.put(this.getUniqueID(), this);
	}
}