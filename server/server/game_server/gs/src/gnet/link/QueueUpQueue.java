package gnet.link;

import java.util.Iterator;
import java.util.LinkedHashMap;

import fire.pb.SSendQueueInfo;
import fire.pb.SSendSlowQueueInfo;

/**
 * 进入游戏排队queue
 * 
 * @author liangyanpeng
 *
 */
public class QueueUpQueue extends LinkedHashMap<Integer, User> {

	private static final long serialVersionUID = -7290627688877735735L;

	private final int state;

	public QueueUpQueue(int state) {
		super();
		this.state = state;
	}

	@Override
	public synchronized User put(Integer userid, User user) {
		user.setState(state);
		User u = super.put(userid, user);
		transferQueueInformation(user, size());
		return u;
	}

	private void transferQueueInformation(User user, int sequence) {
		int size = Onlines.getInstance().getConnectedUsers().getUserQueueSize(User.STATE_IN_QUEUE);
		if (state == User.STATE_IN_QUEUE) {
			user.send(new SSendQueueInfo(sequence, size, (
					(sequence / Onlines.getInstance().getConnectedUsers().avgLoginSpeed) + 1) * 60));// 以前是分钟现在改成发送秒数, 所以乘以60秒
		} else if (state == User.STATE_SLOW_QUEUE) {
			long now = System.currentTimeMillis();
			int enterTime = (int) ((user.getEnterTime() - now) / 1000);
			if (enterTime < 0)
				enterTime = 1;
			user.send(new SSendSlowQueueInfo(sequence, this.size(), enterTime));
		} else {
			user.send(new SSendQueueInfo(0, size, 0));
		}
	}

	public void transferQueueInformation(User user) {
		if (state == User.STATE_ZERO_QUEUE) {
			// 找到某个用户的排队信息
			int sequence = 1;
			Iterator<User> it = this.values().iterator();
			while (it.hasNext()) {
				User tmpuser = it.next();
				if (user.getUserid() == tmpuser.getUserid())
					break;
				sequence++;
			}
			transferQueueInformation(user, sequence);
		} else if (state == User.STATE_SLOW_QUEUE) {
			transferQueueInformation(user, user.getSlowQueueIndex());
		} else {
			transferQueueInformation(user, 0);
		}
	}

	@Override
	public synchronized User remove(Object key) {
		return super.remove(key);
	}

}
