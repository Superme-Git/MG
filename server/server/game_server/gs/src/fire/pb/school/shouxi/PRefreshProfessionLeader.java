package fire.pb.school.shouxi;

/***
 * 挑战结束时刷新 by changhao
 */
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fire.msp.npc.GChangeShouxiShape;
import fire.pb.GsClient;
import fire.pb.PropRole;
import fire.pb.StateCommon;
import fire.pb.battle.Monster;
import fire.pb.map.SceneManager;
import fire.pb.scene.manager.SceneNpcManager;
import fire.pb.scene.movable.NPC;
import fire.pb.school.School;
import fire.pb.school.SchoolConst;
import fire.pb.talk.MessageMgr;
import fire.pb.talk.STransChatMessageNotify2Client;
import fire.pb.title.Title;
import fire.pb.util.MessageUtil;
import mkdb.Procedure;

import org.apache.log4j.Logger;

public class PRefreshProfessionLeader extends Procedure {
 
	@Override
	protected boolean process() throws Exception
	{		
		STransChatMessageNotify2Client ssmg = MessageMgr.getMsgNotify(170035, 0, null);
		SceneManager.sendAll(ssmg);
		
		return true;
	} 
}
