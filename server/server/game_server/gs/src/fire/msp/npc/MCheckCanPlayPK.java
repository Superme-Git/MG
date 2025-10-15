
package fire.msp.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MCheckCanPlayPK__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MCheckCanPlayPK extends __MCheckCanPlayPK__ {
	@Override
	protected void process() {
		new fire.pb.battle.PPlayPKBattle(hostid,guestid).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730414;

	public int getType() {
		return 730414;
	}

	public long hostid;
	public long guestid;

	public MCheckCanPlayPK() {
	}

	public MCheckCanPlayPK(long _hostid_, long _guestid_) {
		this.hostid = _hostid_;
		this.guestid = _guestid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(hostid);
		_os_.marshal(guestid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		hostid = _os_.unmarshal_long();
		guestid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MCheckCanPlayPK) {
			MCheckCanPlayPK _o_ = (MCheckCanPlayPK)_o1_;
			if (hostid != _o_.hostid) return false;
			if (guestid != _o_.guestid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)hostid;
		_h_ += (int)guestid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(hostid).append(",");
		_sb_.append(guestid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(MCheckCanPlayPK _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(hostid - _o_.hostid);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(guestid - _o_.guestid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

