
package fire.pb.team;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSetMyFormation__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSetMyFormation extends __SSetMyFormation__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794475;

	public int getType() {
		return 794475;
	}

	public int formation; // 上线,玩家改变阵型（为0表示不启用任何阵型）
	public int entersend; // 1是上线发，0是平时刷新

	public SSetMyFormation() {
	}

	public SSetMyFormation(int _formation_, int _entersend_) {
		this.formation = _formation_;
		this.entersend = _entersend_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(formation);
		_os_.marshal(entersend);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		formation = _os_.unmarshal_int();
		entersend = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSetMyFormation) {
			SSetMyFormation _o_ = (SSetMyFormation)_o1_;
			if (formation != _o_.formation) return false;
			if (entersend != _o_.entersend) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += formation;
		_h_ += entersend;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(formation).append(",");
		_sb_.append(entersend).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SSetMyFormation _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = formation - _o_.formation;
		if (0 != _c_) return _c_;
		_c_ = entersend - _o_.entersend;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

