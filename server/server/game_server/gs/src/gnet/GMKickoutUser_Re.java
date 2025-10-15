
package gnet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GMKickoutUser_Re__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GMKickoutUser_Re extends __GMKickoutUser_Re__ {
	@Override
	protected void process() {
		// protocol handle
		if (retcode == 0){
			//Role role = gnet.link.Onlines.getInstance().find(io, localsid);
			//gnet.link.Onlines.getInstance().kick(dstroleid, fire.pb.KickErrConst.ERR_GM_KICKOUT);
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 355;

	public int getType() {
		return 355;
	}

	public int retcode;
	public int gmuserid;
	public int localsid;
	public int kickuserid;

	public GMKickoutUser_Re() {
	}

	public GMKickoutUser_Re(int _retcode_, int _gmuserid_, int _localsid_, int _kickuserid_) {
		this.retcode = _retcode_;
		this.gmuserid = _gmuserid_;
		this.localsid = _localsid_;
		this.kickuserid = _kickuserid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(retcode);
		_os_.marshal(gmuserid);
		_os_.marshal(localsid);
		_os_.marshal(kickuserid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		retcode = _os_.unmarshal_int();
		gmuserid = _os_.unmarshal_int();
		localsid = _os_.unmarshal_int();
		kickuserid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GMKickoutUser_Re) {
			GMKickoutUser_Re _o_ = (GMKickoutUser_Re)_o1_;
			if (retcode != _o_.retcode) return false;
			if (gmuserid != _o_.gmuserid) return false;
			if (localsid != _o_.localsid) return false;
			if (kickuserid != _o_.kickuserid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += retcode;
		_h_ += gmuserid;
		_h_ += localsid;
		_h_ += kickuserid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(retcode).append(",");
		_sb_.append(gmuserid).append(",");
		_sb_.append(localsid).append(",");
		_sb_.append(kickuserid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GMKickoutUser_Re _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = retcode - _o_.retcode;
		if (0 != _c_) return _c_;
		_c_ = gmuserid - _o_.gmuserid;
		if (0 != _c_) return _c_;
		_c_ = localsid - _o_.localsid;
		if (0 != _c_) return _c_;
		_c_ = kickuserid - _o_.kickuserid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

