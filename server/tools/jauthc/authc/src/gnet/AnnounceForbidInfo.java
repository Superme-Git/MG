
package gnet;
import mkio.LoginManager;


// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __AnnounceForbidInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class AnnounceForbidInfo extends __AnnounceForbidInfo__ {
	@Override
	protected void process() {
		// protocol handle
	
		try {
			LoginManager.getInstance().getLoginBySession(this.getConnection()).
				getLoginUI().onAnnounceForbidInfo((char)(this.forbid.ltype), this.forbid.time, this.forbid.createtime, this.forbid.reason.getString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 119;

	public int getType() {
		return 119;
	}

	public int userid;
	public int localsid;
	public gnet.RoleForbid forbid;

	public AnnounceForbidInfo() {
		forbid = new gnet.RoleForbid();
	}

	public AnnounceForbidInfo(int _userid_, int _localsid_, gnet.RoleForbid _forbid_) {
		this.userid = _userid_;
		this.localsid = _localsid_;
		this.forbid = _forbid_;
	}

	public final boolean _validator_() {
		if (!forbid._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(userid);
		_os_.marshal(localsid);
		_os_.marshal(forbid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		userid = _os_.unmarshal_int();
		localsid = _os_.unmarshal_int();
		forbid.unmarshal(_os_);
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof AnnounceForbidInfo) {
			AnnounceForbidInfo _o_ = (AnnounceForbidInfo)_o1_;
			if (userid != _o_.userid) return false;
			if (localsid != _o_.localsid) return false;
			if (!forbid.equals(_o_.forbid)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += userid;
		_h_ += localsid;
		_h_ += forbid.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(userid).append(",");
		_sb_.append(localsid).append(",");
		_sb_.append(forbid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

