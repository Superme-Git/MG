
package fire.msp.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MStartGather__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MStartGather extends __MStartGather__ {
	@Override
	protected void process() {
		// protocol handle
		//new fire.pb.npc.PGatherProc( roleid, gatherkey ).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730419;

	public int getType() {
		return 730419;
	}

	public long roleid;
	public long gatherkey;
	public int gatherid;

	public MStartGather() {
	}

	public MStartGather(long _roleid_, long _gatherkey_, int _gatherid_) {
		this.roleid = _roleid_;
		this.gatherkey = _gatherkey_;
		this.gatherid = _gatherid_;
	}

	public final boolean _validator_() {
		if (gatherkey < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(gatherkey);
		_os_.marshal(gatherid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		gatherkey = _os_.unmarshal_long();
		gatherid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MStartGather) {
			MStartGather _o_ = (MStartGather)_o1_;
			if (roleid != _o_.roleid) return false;
			if (gatherkey != _o_.gatherkey) return false;
			if (gatherid != _o_.gatherid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += (int)gatherkey;
		_h_ += gatherid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(gatherkey).append(",");
		_sb_.append(gatherid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(MStartGather _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(gatherkey - _o_.gatherkey);
		if (0 != _c_) return _c_;
		_c_ = gatherid - _o_.gatherid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

