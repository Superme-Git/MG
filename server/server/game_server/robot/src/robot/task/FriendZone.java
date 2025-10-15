package robot.task;

import fire.pb.friends.CRequestAddFriend;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import mkio.Protocol;
import robot.LoginRole;
import robot.LoginRoleMgr;
import robot.Misc;
import robot.friend.FriendThread;

public class FriendZone extends Task_RoleBase
{
	private BlockingQueue<mkio.Protocol> protocols = new LinkedBlockingQueue<mkio.Protocol>();
	private FriendThread friendT;
	
	private long op_time = 0L;
	private boolean op_state = false;
	
	private static final long OP_INTERVAL = 1000;
	private static final int FRIEND_MAX = 50;

	public FriendZone(LoginRole role) {
		super(role);
		init();
	}
	
	private void init() {
		if (friendT != null)
			return;
		
		friendT = new FriendThread(this);
		friendT.start();
	}

	@Override
	public void run(){
		Global.DoGlobalTask(this);
		
		long now = System.currentTimeMillis();
		if ((op_time != 0L) && (op_time + OP_INTERVAL > now)) {
			return;
		}
		
		addFriendRandom();
		if (op_state) {
			return;
		}
		
		op_time = now;
		
		op_state = true;
		friendT.addOpNum();

	}
	
	public void setOpState() {
		op_state = false;
	}

	private void addFriendRandom() {
		if (this.role.isFriendFull(FRIEND_MAX)) {
			return;
		}
		Set<Long> roleSet = LoginRoleMgr.getInstance().getRoles().keySet();
		if (roleSet.size() < FRIEND_MAX) {
			return;
			}
		
		int i = 1;
		while (i != 0) {
			long role_id = ((Long)Misc.getRandom(roleSet)).longValue();
			if (this.role.hasFriend(role_id)) {
				continue;
				}
			
			i = 0;	
			role.sendProtocol(new CRequestAddFriend(role_id));
		}
	}

	@Override
	public void start()
	{
	}

	@Override
	public void stop()
	{
	}

	@Override
	public void processProtocol(Protocol p)
	{
		synchronized (protocols) {
			protocols.offer(p);
		}
	}
}