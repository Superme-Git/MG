
package fire.msp.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MAddRobberNpc__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MAddRobberNpc extends __MAddRobberNpc__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730408;

	public int getType() {
		return 730408;
	}

	public fire.msp.npc.FixedPosNpcInfo npc;
	public long roleid;

	public MAddRobberNpc() {
		npc = new fire.msp.npc.FixedPosNpcInfo();
	}

	public MAddRobberNpc(fire.msp.npc.FixedPosNpcInfo _npc_, long _roleid_) {
		this.npc = _npc_;
		this.roleid = _roleid_;
	}

	public final boolean _validator_() {
		if (!npc._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(npc);
		_os_.marshal(roleid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npc.unmarshal(_os_);
		roleid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MAddRobberNpc) {
			MAddRobberNpc _o_ = (MAddRobberNpc)_o1_;
			if (!npc.equals(_o_.npc)) return false;
			if (roleid != _o_.roleid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += npc.hashCode();
		_h_ += (int)roleid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npc).append(",");
		_sb_.append(roleid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(MAddRobberNpc _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = npc.compareTo(_o_.npc);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

