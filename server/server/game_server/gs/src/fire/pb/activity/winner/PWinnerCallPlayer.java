package fire.pb.activity.winner;

import org.apache.log4j.Logger;

import fire.pb.map.Npc;
import fire.pb.map.SceneNpcManager;
import fire.pb.map.Transfer;
import fire.pb.move.SRoleEnterScene;
import fire.pb.npc.TransmitTypes;
import mkdb.Procedure;

public class PWinnerCallPlayer extends Procedure {
	public static final Logger logger = Logger.getLogger("ACTIVE");
	
	private final long roleid;
	
	public PWinnerCallPlayer(long roleid) {
		this.roleid = roleid;
	}

	@Override
	protected boolean process() throws Exception {
//		if(!WinnerManager.getInstance().isInWinnerActiveTime(System.currentTimeMillis())){
//			logger.error(roleid+"错误，冠军试炼的时候拉人,活动没有开启");
//			return false;
//		}
		if(WinnerManager.npc_keys==null){
			logger.error(roleid+"错误，冠军试炼的时候拉人,目标npc不存在1");
			return false;
		}
		if(WinnerManager.npc_keys.size()==0){
			logger.error(roleid+"错误，冠军试炼的时候拉人,目标npc不存在2");
			return false;
		}
		
		final Npc npc=SceneNpcManager.selectNpcByKey(WinnerManager.npc_keys.get(0));
		if(null == npc){
			logger.error(roleid+"错误，冠军试炼的时候拉人,目标npc不存在");
			return false;
		}
		if (!WinnerManager.canTransform(roleid,TransmitTypes.winnercall)){
			logger.error(roleid+"错误，冠军试炼的时候拉人,玩家所在地图不能传送");
			return false;
		}
		final fire.pb.map.GridPos gridPos = npc.getPos().toGridPos();
		Transfer.justGoto(roleid, npc.getScene(), gridPos.getX(), gridPos.getY(),SRoleEnterScene.SYSTEM_DRAG);
		return true;
	}

}
