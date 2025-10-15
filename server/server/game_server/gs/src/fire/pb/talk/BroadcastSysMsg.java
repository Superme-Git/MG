package fire.pb.talk;

import gnet.link.Onlines;
import mkio.Protocol.Coder;
import mkio.Protocol.Stub;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import fire.pb.PropRole;
import fire.pb.friends.SSendSystemMessageToRole;
import fire.pb.map.SceneManager;

import com.locojoy.base.Octets;

/**
 * 存储服务器发出的系统消息，当系统消息生成时，在线角色可立即收到，离线角色下次上线收到
 */
public class BroadcastSysMsg {
	private boolean readonly;
	private xbean.BroadcastMsgList msgList;

	public BroadcastSysMsg() {
		this(false);
	}

	public BroadcastSysMsg(boolean readonly) {
		this.readonly = readonly;
		if (!readonly)
			msgList = xtable.Broadcastmsgtab.get(1);
		else
			msgList = xtable.Broadcastmsgtab.select(1);
		if (msgList == null) {
			if (readonly)
				msgList = xbean.Pod.newBroadcastMsgListData();
			else {
				msgList = xbean.Pod.newBroadcastMsgList();
				xtable.Broadcastmsgtab.insert(1, msgList);
			}
		}
	}

	public void broadcastSysMsg(String msg) {
		List<String> param = new LinkedList<String>();
		param.add(msg);
		broadcastSysMsg(141661, param);
	}

	public void broadcastSysMsg(final int msgId, final List<String> param) {
		ArrayList<Octets> octetsList = new ArrayList<Octets>();
		if (param != null) {
			for (String s : param) {
				octetsList.add(MessageMgr.convertString2Octets(s));
			}
		}

		SSendSystemMessageToRole msg = new SSendSystemMessageToRole();
		msg.systemroleid = 0;
		msg.contentid = msgId;
		msg.contentparam = octetsList;
		SceneManager.sendAll(msg);

		long now = System.currentTimeMillis();
		for (Iterator<xbean.BroadcastMsg> it = msgList.getMsglist().iterator(); it.hasNext();) {
			xbean.BroadcastMsg tempmsg = it.next();
			if (tempmsg.getBroadendtime() < now) {
				it.remove();
			}
		}

		// 存在硬盘
		msg.time = MessageMgr.getCurrTime(true);
		xbean.BroadcastMsg broadmsg = xbean.Pod.newBroadcastMsg();
		broadmsg.getMsgprotocol().setProclassname("");
		broadmsg.getMsgprotocol().setProtype(msg.getType());
		broadmsg.getMsgprotocol().setContent(msg);
		broadmsg.setBroadtime(now);
		broadmsg.setBroadendtime(now + 7 * 24 * 60 * 60 * 1000);// 保存一个星期
		msgList.getMsglist().add(broadmsg);
	}

	private mkio.Protocol getProtocol(xbean.BroadcastMsg broadmsg) {
		try {
			Stub stub = ((Coder) (Onlines.getInstance().getCoder())).getStub(broadmsg.getMsgprotocol().getProtype());
			mkio.Protocol p = stub.newInstance();
			p = broadmsg.getMsgprotocol().getContent(p);
			return p;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void psendSysMsgWhileOnline(long roleId, long lastOfflineTime) {
		long now = System.currentTimeMillis();
		for (Iterator<xbean.BroadcastMsg> it = msgList.getMsglist().iterator(); it.hasNext();) {
			xbean.BroadcastMsg broadmsg = it.next();
			if (now > broadmsg.getBroadendtime()) {
				if (!readonly)
					it.remove();
				continue;
			}

			if (broadmsg.getBroadtime() >= lastOfflineTime) {
				mkio.Protocol p = getProtocol(broadmsg);
				mkdb.Procedure.psendWhileCommit(roleId, p);
			}
		}
	}

	/**
	 * 角色上线发送系统广播消息
	 */
	public static class PSendSysMsgWhileOnline extends mkdb.Procedure {
		final private long roleId;

		public PSendSysMsgWhileOnline(long roleId) {
			this.roleId = roleId;
		}

		@Override
		protected boolean process() throws Exception {
			PropRole pRole = new PropRole(roleId, true);
			new BroadcastSysMsg().psendSysMsgWhileOnline(roleId, pRole.getLastOfflineTime());
			return true;
		}
	}
}
