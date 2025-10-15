package fire.pb.activity.winner;

import fire.pb.map.SceneNpcManager;


public class CReqStartWinner {
	private final long roleid;
	private final long npckey;

	public CReqStartWinner(long roleid, long npckey) {
		super();
		this.roleid = roleid;
		this.npckey = npckey;
	}

	public void process() {
		int npccode = SceneNpcManager.selectNpcByKey(npckey).getNpcID();
		// 判断是否在活动时间内
		if (!WinnerManager.getInstance().isInWinnerActiveTime(System.currentTimeMillis())) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 144888, npccode, null);
			return;
		}
		new PReqStartWinner(roleid, npccode).submit();
	}
}

