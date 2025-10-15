package fire.pb.friends;

import java.util.ArrayList;
import java.util.List;

import fire.log.Module;
import mkdb.Procedure;

public class PClearTimeOutProtocol extends Procedure {

	@Override
	public boolean process() {
		Module.logger.info("Server start clear timeout offline protocol.");
		try {
			return doClear();
		} catch (Exception e) {
			Module.logger.error("Server start clear timeout offline protocol error:", e);
			return false;
		}
	}

	private boolean doClear() {
		// 从表里清除消息队列为空的角色
		final List<Long> needClearAllRole = new ArrayList<Long>();
		xtable.Offlinemsg.getTable().browse(
				new mkdb.TTable.IWalk<Long, xbean.OfflineMsgList>() {
					@Override
					public boolean onRecord(Long roleId, xbean.OfflineMsgList msgList) {
						// 遍历离线协议
						List<xbean.OfflineMsgProtocol> protocolList = msgList.getProtocollist();
						List<xbean.OfflineMsgProtocol> removeObjects = new ArrayList<xbean.OfflineMsgProtocol>();
						for (xbean.OfflineMsgProtocol protocol : protocolList) {
							// 检测超时
							if (isTimeOut(protocol.getTick())) {
								removeObjects.add(protocol);
							}
						}

						protocolList.removeAll(removeObjects);

						if (protocolList.size() == 0)
							needClearAllRole.add(roleId);
						return true;
					}
				});
		// 清除没有消息的角色
		for (Long roleId : needClearAllRole) {
			xtable.Offlinemsg.remove(roleId);
		}
		return true;
	}

	protected boolean isTimeOut(long tick) {
		int daySpace = (int) ((System.currentTimeMillis() - tick) / (24 * 60 * 60 * 1000));
		return daySpace >= Constant.OFFLINE_MSG_TIME_OUT_DAY;
	}

}
