package fire.pb.npc.NpcServiceCond;

import fire.pb.scene.manager.SceneNpcManager;
import fire.pb.scene.movable.NPC;

public class NpcSceneState implements Condition {

	public NpcSceneState() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean CheckCond(long roleid, int args1, int args2) {
		//roleid 用做 npcid
		NPC npc = SceneNpcManager.getInstance().getNpcByKey(roleid);
		if (npc == null)
			return false;
		
		
		
		return false;
	}

}
