
package gnet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __QueryUserid2Rep__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class QueryUserid2Rep extends __QueryUserid2Rep__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 213;

	public int getType() {
		return 213;
	}

	public int xid;
	public int result;
	public int userid;
	public long roleid;
	public int level;

	public QueryUserid2Rep() {
		result = 0;
		userid = 0;
		roleid = 0;
		level = 0;
	}

	public QueryUserid2Rep(int _xid_, int _result_, int _userid_, long _roleid_, int _level_) {
		this.xid = _xid_;
		this.result = _result_;
		this.userid = _userid_;
		this.roleid = _roleid_;
		this.level = _level_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(xid);
		_os_.marshal(result);
		_os_.marshal(userid);
		_os_.marshal(roleid);
		_os_.marshal(level);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		xid = _os_.unmarshal_int();
		result = _os_.unmarshal_int();
		userid = _os_.unmarshal_int();
		roleid = _os_.unmarshal_long();
		level = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof QueryUserid2Rep) {
			QueryUserid2Rep _o_ = (QueryUserid2Rep)_o1_;
			if (xid != _o_.xid) return false;
			if (result != _o_.result) return false;
			if (userid != _o_.userid) return false;
			if (roleid != _o_.roleid) return false;
			if (level != _o_.level) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += xid;
		_h_ += result;
		_h_ += userid;
		_h_ += (int)roleid;
		_h_ += level;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(xid).append(",");
		_sb_.append(result).append(",");
		_sb_.append(userid).append(",");
		_sb_.append(roleid).append(",");
		_sb_.append(level).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(QueryUserid2Rep _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = xid - _o_.xid;
		if (0 != _c_) return _c_;
		_c_ = result - _o_.result;
		if (0 != _c_) return _c_;
		_c_ = userid - _o_.userid;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = level - _o_.level;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

