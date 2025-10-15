package fire.pb.friends;

import fire.log.Module;
import fire.pb.StateCommon;
import fire.pb.exception.CodecException;
import fire.pb.map.RoleManager;
import mkio.Protocol;

/**
 * 好友的离线消息
 */
public class OffLineMessageManager {

	private static OffLineMessageManager offLineMsgManager;

	private OffLineMessageManager() {
	}

	public synchronized static OffLineMessageManager getOffLineMsgManager() {
		if (null == offLineMsgManager) {
			offLineMsgManager = new OffLineMessageManager();
		}
		return offLineMsgManager;
	}

	/**
	 * 角色是否在线
	 */
	public boolean isOnline(long roleid) {
		return StateCommon.isOnline(roleid);
	}

	public boolean isOnlineUnsafe(long roleid) {
		return RoleManager.getInstance().getRoleByID(roleid) != null;
	}

	/**
	 * 角色是否在线
	 */
	public int isOnlineIntValue(long roleid) {
		return StateCommon.isOnline(roleid) == true ? 1 : 0;
	}

	/**
	 * 离线消息
	 */
	public void inserteOfflineMsgById(final long roleId, final mkio.Protocol msg) {
		mkdb.Procedure proc = new mkdb.Procedure() {
			protected boolean process() throws Exception {
				xbean.OfflineMsgProtocol pro = xbean.Pod.newOfflineMsgProtocol();
				pro.setProclassname(getProtocolClassName(msg));
				pro.setProtype(msg.getType());
				pro.setContent(msg);
				pro.setTick(System.currentTimeMillis());

				xbean.OfflineMsgList list = xtable.Offlinemsg.get(roleId);
				if (null == list) {
					list = xbean.Pod.newOfflineMsgList();
					list.getProtocollist().add(pro);
					xtable.Offlinemsg.insert(roleId, list);
					return true;
				}
				list.getProtocollist().add(0, pro);
				if (list.getProtocollist().size() > 20) {
					for (int i = list.getProtocollist().size() - 1; i >= 20; i--) {
						list.getProtocollist().remove(i);
					}
				}
				return true;
			};
		};

		if (mkdb.Transaction.current() != null)
			mkdb.Procedure.pexecuteWhileCommit(proc);
		else
			proc.submit();
	}

	private String getProtocolClassName(mkio.Protocol msg) throws CodecException {
		String str = msg.getClass().toString();
		String[] strArray = str.split(" ");
		if (strArray.length < 2) {
			throw new CodecException("保存离线消息的class name出错！！！");
		}
		return strArray[1];
	}

	/**
	 * 上线后执行离线消息
	 */
	@SuppressWarnings("unchecked")
	public boolean executeOfflineMsgById(long roleId) {
		xbean.OfflineMsgList list = xtable.Offlinemsg.get(roleId);
		if (null == list) {
			list = xbean.Pod.newOfflineMsgList();
			xtable.Offlinemsg.insert(roleId, list);
			return true;
		}

		java.util.List<xbean.OfflineMsgProtocol> msgs = list.getProtocollist();
		if (null == msgs || msgs.size() == 0)
			return true;

		for (xbean.OfflineMsgProtocol msg : msgs) {
			Class<mkio.Protocol> protocolCls;
			mkio.Protocol content = null;
			try {
				protocolCls = (Class<Protocol>) Class.forName(msg.getProclassname());
				mkio.Protocol protocol = protocolCls.newInstance();
				content = msg.getContent(protocol);
			} catch (Exception e) {
				Module.logger.error("Recv offline protocol Class reflection error!", e);
				Module.logger.debug("Recv offline protocol:" + msg.getProclassname());
				continue;
			} catch (Error e) {
				Module.logger.debug("Recv offline protocol:" + msg.getProclassname());
				Module.logger.error("Recv offline protocol error! may now protocol != db save protocol", e);
				continue;
			}

			mkdb.Procedure.psendWhileCommit(roleId, content);
		}
		msgs.clear();
		return true;
	}

}
