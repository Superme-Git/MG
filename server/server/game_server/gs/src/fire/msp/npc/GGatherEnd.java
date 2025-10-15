
package fire.msp.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GGatherEnd__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GGatherEnd extends __GGatherEnd__ {
	@Override
	protected void process() {
		// protocol handle
		fire.pb.scene.manager.GatherManager.getInstance().onGatherEnd(roleid, gatherkey, result );
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730420;

	public int getType() {
		return 730420;
	}

	public long roleid;
	public long gatherkey;
	public int result;

	public GGatherEnd() {
	}

	public GGatherEnd(long _roleid_, long _gatherkey_, int _result_) {
		this.roleid = _roleid_;
		this.gatherkey = _gatherkey_;
		this.result = _result_;
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
		_os_.marshal(result);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		gatherkey = _os_.unmarshal_long();
		result = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GGatherEnd) {
			GGatherEnd _o_ = (GGatherEnd)_o1_;
			if (roleid != _o_.roleid) return false;
			if (gatherkey != _o_.gatherkey) return false;
			if (result != _o_.result) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += (int)gatherkey;
		_h_ += result;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(gatherkey).append(",");
		_sb_.append(result).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GGatherEnd _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(gatherkey - _o_.gatherkey);
		if (0 != _c_) return _c_;
		_c_ = result - _o_.result;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

