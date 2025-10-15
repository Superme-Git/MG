package gnet.link;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

import java.util.concurrent.atomic.AtomicReference;


public class Role {
	private final int userid;
	private final long roleid;
	private final AtomicReference<Link.Session> linkSession = new AtomicReference<Link.Session>();

	public Role(int userid, long roleid) {
		this.userid = userid;
		this.roleid = roleid;
	}

	public long getRoleid() {
		return roleid;
	}

	public int getUserid() {
		return userid;
	}

	public Link.Session getLinkSession() {
		return linkSession.get();
	}

	public boolean isOnline() {
		return null != linkSession.get();
	}

	// package private
	void linkBroken() {
		Link.Session ls = linkSession.getAndSet(null);
		if (null != ls)
			ls.close();
	}

	void linkAttach(Link link, int linksid) {
		linkSession.set(link.attach(linksid, this));
		// careful! under synchronized (roles)
		//this.setLogin(SetLogin.eLogin);  SetLogin挪到User登录时，不然在角色选择和排队时GS收不到LinkBroken
	}

	public boolean sendOctets(int ptype, Octets poctets) {
		final Link.Session ls = linkSession.get();
		if (null != ls) {
			final Send p1 = new Send();
			p1.linksids.add(ls.getSid());
			p1.ptype = ptype;
			p1.pdata = poctets;
			
			Onlines.sendSend(p1, ls.getXio());
			//p1.send(ls.getXio());
			return true;
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
			//p1.send(ls.getXio());
			return true;
		}
		return false;
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

	public boolean bind(int pvid) {
		Link.Session ls = linkSession.get();
		if (null != ls) {
			Bind p1 = new Bind();
			p1.pvid = pvid;
			p1.linksids.add(ls.getSid());
			return p1.send(ls.getXio());
		}
		return false;
	}

	public boolean unbind(int pvid) {
		Link.Session ls = linkSession.get();
		if (null != ls) {
			UnBind p1 = new UnBind();
			p1.pvid = pvid;
			p1.linksids.add(ls.getSid());
			return p1.send(ls.getXio());
		}
		return false;
	}

	
	

	@Override
	public String toString() {
		return "userid=" + userid + " roleid=" + roleid + linkSession.get();
	}
}
