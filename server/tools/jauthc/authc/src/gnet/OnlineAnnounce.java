
package gnet;


import mkio.LoginIns;
import mkio.LoginManager;


// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __OnlineAnnounce__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class OnlineAnnounce extends __OnlineAnnounce__ {
	@Override
	protected void process() {
		// protocol handle
		//System.out.println("Get the OnlineAnnounce");
		LoginIns login =  LoginManager.getInstance().getLoginBySession(this.getConnection());
		mkdb.Trace.debug(this.getClass().getName() + ": " + login.getParam().username );
		login.getLoginUI().onAuthOk(this.userid);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 110;

	public int getType() {
		return 110;
	}

	public int userid;
	public int localsid;
	public int remain_time;
	public int zoneid;
	public int aid;
	public int algorithm; // 赋值为algorithm，用于在client显示帐号安全等级

	public OnlineAnnounce() {
		remain_time = 0;
		zoneid = 0;
		aid = 0;
		algorithm = 0;
	}

	public OnlineAnnounce(int _userid_, int _localsid_, int _remain_time_, int _zoneid_, int _aid_, int _algorithm_) {
		this.userid = _userid_;
		this.localsid = _localsid_;
		this.remain_time = _remain_time_;
		this.zoneid = _zoneid_;
		this.aid = _aid_;
		this.algorithm = _algorithm_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(userid);
		_os_.marshal(localsid);
		_os_.marshal(remain_time);
		_os_.marshal(zoneid);
		_os_.marshal(aid);
		_os_.marshal(algorithm);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		userid = _os_.unmarshal_int();
		localsid = _os_.unmarshal_int();
		remain_time = _os_.unmarshal_int();
		zoneid = _os_.unmarshal_int();
		aid = _os_.unmarshal_int();
		algorithm = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof OnlineAnnounce) {
			OnlineAnnounce _o_ = (OnlineAnnounce)_o1_;
			if (userid != _o_.userid) return false;
			if (localsid != _o_.localsid) return false;
			if (remain_time != _o_.remain_time) return false;
			if (zoneid != _o_.zoneid) return false;
			if (aid != _o_.aid) return false;
			if (algorithm != _o_.algorithm) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += userid;
		_h_ += localsid;
		_h_ += remain_time;
		_h_ += zoneid;
		_h_ += aid;
		_h_ += algorithm;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(userid).append(",");
		_sb_.append(localsid).append(",");
		_sb_.append(remain_time).append(",");
		_sb_.append(zoneid).append(",");
		_sb_.append(aid).append(",");
		_sb_.append(algorithm).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(OnlineAnnounce _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = localsid - _o_.localsid;
		if (0 != _c_) return _c_;
		_c_ = remain_time - _o_.remain_time;
		if (0 != _c_) return _c_;
		_c_ = zoneid - _o_.zoneid;
		if (0 != _c_) return _c_;
		_c_ = aid - _o_.aid;
		if (0 != _c_) return _c_;
		_c_ = algorithm - _o_.algorithm;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

