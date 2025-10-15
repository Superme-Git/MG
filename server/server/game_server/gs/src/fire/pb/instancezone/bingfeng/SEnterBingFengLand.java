package fire.pb.instancezone.bingfeng;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SEnterBingFengLand__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SEnterBingFengLand extends __SEnterBingFengLand__ {
	@Override
	protected void process() {
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 804559;

	public int getType() {
		return 804559;
	}

	public int landid;
	public int stage;
	public int autogo;
	public int finishstage;

	public SEnterBingFengLand() {
	}

	public SEnterBingFengLand(int _landid_, int _stage_, int _autogo_, int _finishstage_) {
		this.landid = _landid_;
		this.stage = _stage_;
		this.autogo = _autogo_;
		this.finishstage = _finishstage_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(landid);
		_os_.marshal(stage);
		_os_.marshal(autogo);
		_os_.marshal(finishstage);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		landid = _os_.unmarshal_int();
		stage = _os_.unmarshal_int();
		autogo = _os_.unmarshal_int();
		finishstage = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SEnterBingFengLand) {
			SEnterBingFengLand _o_ = (SEnterBingFengLand)_o1_;
			if (landid != _o_.landid) return false;
			if (stage != _o_.stage) return false;
			if (autogo != _o_.autogo) return false;
			if (finishstage != _o_.finishstage) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += landid;
		_h_ += stage;
		_h_ += autogo;
		_h_ += finishstage;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(landid).append(",");
		_sb_.append(stage).append(",");
		_sb_.append(autogo).append(",");
		_sb_.append(finishstage).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SEnterBingFengLand _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = landid - _o_.landid;
		if (0 != _c_) return _c_;
		_c_ = stage - _o_.stage;
		if (0 != _c_) return _c_;
		_c_ = autogo - _o_.autogo;
		if (0 != _c_) return _c_;
		_c_ = finishstage - _o_.finishstage;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

