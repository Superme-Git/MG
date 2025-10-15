
package gnet;


import fire.pb.AU2GameFunDispatch;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __AU2Game__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class AU2Game extends __AU2Game__ {
	@Override
	protected void process() {
		
		new AU2GameFunDispatch(userid, qtype, info, retcode, reserved).doDispatch();
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 8038;

	public int getType() {
		return 8038;
	}

	public int userid;
	public int qtype;
	public com.locojoy.base.Octets info;
	public int retcode;
	public int reserved;

	public AU2Game() {
		info = new com.locojoy.base.Octets();
	}

	public AU2Game(int _userid_, int _qtype_, com.locojoy.base.Octets _info_, int _retcode_, int _reserved_) {
		this.userid = _userid_;
		this.qtype = _qtype_;
		this.info = _info_;
		this.retcode = _retcode_;
		this.reserved = _reserved_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(userid);
		_os_.marshal(qtype);
		_os_.marshal(info);
		_os_.marshal(retcode);
		_os_.marshal(reserved);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		userid = _os_.unmarshal_int();
		qtype = _os_.unmarshal_int();
		info = _os_.unmarshal_Octets();
		retcode = _os_.unmarshal_int();
		reserved = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof AU2Game) {
			AU2Game _o_ = (AU2Game)_o1_;
			if (userid != _o_.userid) return false;
			if (qtype != _o_.qtype) return false;
			if (!info.equals(_o_.info)) return false;
			if (retcode != _o_.retcode) return false;
			if (reserved != _o_.reserved) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += userid;
		_h_ += qtype;
		_h_ += info.hashCode();
		_h_ += retcode;
		_h_ += reserved;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(userid).append(",");
		_sb_.append(qtype).append(",");
		_sb_.append("B").append(info.size()).append(",");
		_sb_.append(retcode).append(",");
		_sb_.append(reserved).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

