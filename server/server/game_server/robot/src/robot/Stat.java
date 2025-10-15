package robot;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import robot.team.TeamMgr;

public class Stat {

	private volatile int userauthc = 0;
	private volatile int enterworld = 0;
	private volatile int moving = 0;
	private volatile int authcerror = 0;
	private volatile int forbid = 0;
	private volatile int session = 0;

	static public enum Status {
		ST_NULL, ST_AUTHCOK, ST_ENTERWORLD, ST_MOVING, ST_STOPPING,
	}

	final public void onAddSession() {
		synchronized (this) {
			session++;
		}
	}

	final public void onDelSession() {
		synchronized (this) {
			session--;
		}
	}

	final public Status userAuthcOK() {
		synchronized (this) {
			userauthc++;
		}
		return Status.ST_AUTHCOK;
	}

	final public void userAuthcError() {
		synchronized (this) {
			authcerror++;
		}
	}

	final public void userAuthcForbid() {
		synchronized (this) {
			forbid++;
		}
	}

	final public Status roleEnterWorld() {
		synchronized (this) {
			enterworld++;
		}
		return Status.ST_ENTERWORLD;
	}

	final public Status roleStartMove() {
		synchronized (this) {
			moving++;
		}
		return Status.ST_MOVING;
	}

	final public Status roleStopMove() {
		synchronized (this) {
			moving--;
		}
		return Status.ST_STOPPING;
	}

	final public Status userDisconnect(Status s) {
		synchronized (this) {
			switch (s) {
			case ST_MOVING:
				moving--;
				enterworld--;
				userauthc--;
				break;
			case ST_ENTERWORLD:
				enterworld--;
				userauthc--;
				break;
			case ST_AUTHCOK:
				userauthc--;
				break;
			case ST_STOPPING:
				enterworld--;
				userauthc--;
				break;
			default:
				break;
			}
		}
		return Status.ST_NULL;
	}

	static private class ActiveCheck {
		static private class Record {
			public long key = -1;
			public final Set<Long> ids = new HashSet<Long>();
		}

		private Record last = new Record();
		private Record current = new Record();

		private void swapRecord() {
			Record temp = last;
			last = current;
			current = temp;
		}

		public void active(long roleid, long t) {
			long key = t / (long) (1000 * 5);
			if (key != current.key) {
				swapRecord();
				current.key = key;
				current.ids.clear();
			}
			current.ids.add(roleid);
		}

		public int getCount() {
			return current.ids.size();
		}
	}

	private final ActiveCheck activecheck = new ActiveCheck();

	final public void roleCheckMove(long roleid) {
		long t = System.currentTimeMillis();
		synchronized (this) {
			activecheck.active(roleid, t);
		}
	}

	final public int getActiveCount() {
		synchronized (this) {
			return activecheck.getCount();
		}
	}

	static public interface RoleMoveStateMXBean {
		int getUserAuthc();

		int getUserAuthcError();

		int getUserForbid();

		int getRoleEnterWorld();

		int getRoleMoving();

		int getRoleActive();

		int getSessionCount();

		int getTeamCount();
	}

	static public class RoleMoveState implements RoleMoveStateMXBean {

		@Override
		public int getUserAuthc() {
			return Stat.getInstance().userauthc;
		}

		@Override
		public int getRoleEnterWorld() {
			return Stat.getInstance().enterworld;
		}

		@Override
		public int getRoleMoving() {
			return Stat.getInstance().moving;
		}

		@Override
		public int getRoleActive() {
			return Stat.getInstance().getActiveCount();
		}

		@Override
		public int getUserAuthcError() {
			return Stat.getInstance().authcerror;
		}

		@Override
		public int getUserForbid() {
			return Stat.getInstance().forbid;
		}

		@Override
		public int getSessionCount() {
			return Stat.getInstance().session;
		}

		@Override
		public int getTeamCount() {
			return TeamMgr.getInstance().getTeams().size();
		}
	}

	private Stat() {
		try {
			javax.management.MBeanServer server = java.lang.management.ManagementFactory
					.getPlatformMBeanServer();
			server.registerMBean(new RoleMoveState(),
					javax.management.ObjectName
							.getInstance("robot.counter:type=RoleMoveState"));
		} catch (Exception e) {
			mkdb.Trace.error("registerMBean Stat.RoleMoveState", e);
		}
	}

	private static final Stat instance = new Stat();

	public static Stat getInstance() {
		return instance;
	}

	void dump(PrintStream ps) {
		ps.println("=======================================");
		ps.println("authc      " + userauthc);
		ps.println("authcerror " + authcerror);
		ps.println("forbid     " + forbid);
		ps.println("enterworld " + enterworld);
		ps.println("moving     " + moving);
		ps.println("active     " + activecheck.getCount());
		ps.println("session    " + session);
		ps.println();
	}
}
