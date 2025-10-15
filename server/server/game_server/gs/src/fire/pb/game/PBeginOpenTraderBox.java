package fire.pb.game;

import fire.pb.activity.timernpc.TimerNpcData;
import fire.pb.activity.timernpc.TimerNpcService;
import fire.pb.map.Npc;
import fire.pb.map.SceneNpcManager;
import fire.pb.talk.MessageMgr;
import mkdb.Procedure;

public class PBeginOpenTraderBox extends Procedure {
	
	private final long roleid;
	private final int boxtype;
	private final long npckey;
	
	public PBeginOpenTraderBox(final long roleid, final int boxtype, final long npckey) {
		this.roleid = roleid;
		this.boxtype = boxtype;
		this.npckey = npckey;
	}
	
	@Override
	protected boolean process() throws Exception {
		Npc npc = SceneNpcManager.selectNpcByKey(npckey);
		if (npc == null) {
			MessageMgr.psendMsgNotifyWhileRollback(roleid, 170021, null);
			return false;
		}
		
		int actId = TimerNpcService.getInstance().getActId(npc.getNpcID());
		//活动错误
		if (actId == -1) {
			return false;
		}
		
		TimerNpcData timerData = TimerNpcService.getInstance().getNpcData(actId);
		if (timerData == null)
			return false;
		
		xbean.timerNpcInfoCol npcInfo = xtable.Timernpcinfotable.get(actId);
		if (npcInfo == null) {
			return false;
		}
		
		xbean.TraderWheel traderWheel = xtable.Opentraderwheeltable.get(roleid);
		if (traderWheel == null) {
			traderWheel = xbean.Pod.newTraderWheel();
			xtable.Opentraderwheeltable.insert(roleid, traderWheel);
		}
		
		int itemIndex = GameManager.getInstance().getAwardItemIndex(boxtype);
		if (itemIndex == -1) {
			return false;
		}
		
		traderWheel.setBoxtype(boxtype);
		traderWheel.setItemindex(itemIndex);
		
		switch (boxtype) {
		case GameManager.SILVER_BOX: {
			//扣除钥匙
			fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
			long ret = bag.removeItemById(GameManager.SILVER_BOX_KEY, 1, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_shenmixiaofan, GameManager.SILVER_BOX_KEY, "TraderWheel");
			if (ret == 0)
				return false;
			break;
		}
		case GameManager.GOLD_BOX: {
			//扣除钥匙
			fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
			long ret = bag.removeItemById(GameManager.GOLD_BOX_KEY, 1, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_shenmishangren, GameManager.GOLD_BOX_KEY, "TraderWheel");
			if (ret == 0)
				return false;
			break;
		}
		default:
			break;
		}
		
		TimerNpcService.getInstance().removeMonster(npcInfo, npckey);
		if (timerData.addnpctype == 1)
			TimerNpcService.getInstance().addTimerNpc(actId, 1, npc.getMapId());
		
		SBeginXueYueWheel bsend = new SBeginXueYueWheel();
		bsend.wheelindex = itemIndex;
		mkdb.Procedure.psendWhileCommit(roleid, bsend);
		return true;
	}

}
