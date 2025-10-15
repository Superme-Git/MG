package gnet.link;

import java.util.concurrent.atomic.AtomicReference;

import fire.pb.PSendRoleList;
import mkdb.Transaction;

import com.locojoy.base.Marshal.OctetsStream;

public class User {
	private final int userid;
	private final AtomicReference<Link.Session> linkSession = new AtomicReference<Link.Session>();
	private int state = 0;//
	private boolean linkBroken = false;

	public static int STATE_FAILED = 0;// 登入失败，直接返回提示
	public static int STATE_LOGIN = 1;// 登入状态
	public static int STATE_IN_QUEUE = 2;// 排队状态
	public static int STATE_ZERO_QUEUE = 3;// 0排位状态
	public static int STATE_SLOW_QUEUE = 4;// 缓慢进入状态
	private int ip;
	private long enterTime = 0;// 缓慢进入到期时间
	private int slowQueueIndex = -1;// 缓慢进入队列位置索引

	public User(int userid) {
		this.userid = userid;
	}

	public int getUserid() {
		return userid;
	}

	public Link.Session getLinkSession() {
		return linkSession.get();
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public boolean isLinkBroken() {
		return linkBroken;
	}

	public void setLinkBroken(boolean linkBroken) {
		this.linkBroken = linkBroken;
	}

	void linkAttach(Link link, int linksid) {
		linkSession.set(link.attach(linksid, this));
		this.setLogin(SetLogin.eLogin);
	}

	public boolean kick(int error) {
		Link.Session ls = linkSession.get();
		if (null != ls) {
			Kick p1 = new Kick();
			p1.linksid = ls.getSid();
			p1.action = Kick.A_QUICK_CLOSE;
			p1.error = error;
			return p1.send(ls.getXio());
		}
		return false;
	}

	boolean setLogin(int login) {
		Link.Session ls = linkSession.get();
		if (null != ls) {
			return new SetLogin(ls.getSid(), login, -1).send(ls.getXio());
		}
		return false;
	}

	public boolean send(mkio.Protocol p2) {
		final Link.Session ls = linkSession.get();
		if (null != ls) {
			final Send p1 = new Send();
			p1.linksids.add(ls.getSid());
			p1.ptype = p2.getType();
			p1.pdata = new OctetsStream().marshal(p2);
			ls.getXio().getCreator().getManager().getCoder().checkSend(p2, p1.pdata.size());
			Onlines.sendSend(p1, ls.getXio());
			// p1.send(ls.getXio());
			return true;
		}
		return false;
	}

	public void sendRoleList(boolean isReturnList) {
		if (Transaction.current() == null)
			new PSendRoleList(this, isReturnList).submit();
		else
			mkdb.Procedure.pexecuteWhileCommit(new PSendRoleList(this, isReturnList));
	}

	public void setIp(int ip) {
		if (ip == 0)
			return;// 0不设置
		this.ip = ip;
	}

	public int getIp() {
		return this.ip;
	}

	public long getEnterTime() {
		return enterTime;
	}

	public void setEnterTime(long enterTime) {
		this.enterTime = enterTime;
	}

	public int getSlowQueueIndex() {
		return slowQueueIndex;
	}

	public void setSlowQueueIndex(int slowQueueIndex) {
		this.slowQueueIndex = slowQueueIndex;
	}

}
