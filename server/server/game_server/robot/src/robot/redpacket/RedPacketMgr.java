package robot.redpacket;

/**
 * 红包管理器
 * 
 * @author yt
 */
public class RedPacketMgr {

	public boolean isSendPacket = false;

	public long num = 1;

	public static RedPacketMgr _instance = new RedPacketMgr();

	public static RedPacketMgr getInstance() {
		return _instance;
	}

	public long sendRedPacketTime = System.currentTimeMillis();

	/**
	 * 设置是否發送
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月11日 下午8:01:51
	 * @version 1.0
	 */
	public void setIsSendPacket() {
		if (isSendPacket && System.currentTimeMillis() - sendRedPacketTime > 5000) {
			RedPacketMgr.getInstance().isSendPacket = false;
		}
	}

	/**
	 * 设置状态
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月11日 下午8:19:42
	 * @version 1.0
	 */
	public void setNumAndSendPacketStatus() {
		RedPacketMgr.getInstance().isSendPacket = true;
		RedPacketMgr.getInstance().num++;
		RedPacketMgr.getInstance().sendRedPacketTime = System.currentTimeMillis();
	}
}
