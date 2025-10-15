
package fire.pb.mission;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SDefineTeam__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SDefineTeam extends __SDefineTeam__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 805547;

	public int getType() {
		return 805547;
	}

	public int instid; // 副本id
	public int tlstep; // 队长进度
	public int mystep; // 我的进度

	public SDefineTeam() {
	}

	public SDefineTeam(int _instid_, int _tlstep_, int _mystep_) {
		this.instid = _instid_;
		this.tlstep = _tlstep_;
		this.mystep = _mystep_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(instid);
		_os_.marshal(tlstep);
		_os_.marshal(mystep);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		instid = _os_.unmarshal_int();
		tlstep = _os_.unmarshal_int();
		mystep = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SDefineTeam) {
			SDefineTeam _o_ = (SDefineTeam)_o1_;
			if (instid != _o_.instid) return false;
			if (tlstep != _o_.tlstep) return false;
			if (mystep != _o_.mystep) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += instid;
		_h_ += tlstep;
		_h_ += mystep;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(instid).append(",");
		_sb_.append(tlstep).append(",");
		_sb_.append(mystep).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SDefineTeam _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = instid - _o_.instid;
		if (0 != _c_) return _c_;
		_c_ = tlstep - _o_.tlstep;
		if (0 != _c_) return _c_;
		_c_ = mystep - _o_.mystep;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

