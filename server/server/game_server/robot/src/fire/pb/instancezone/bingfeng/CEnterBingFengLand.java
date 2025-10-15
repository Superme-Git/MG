
package fire.pb.instancezone.bingfeng;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CEnterBingFengLand__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CEnterBingFengLand extends __CEnterBingFengLand__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 804553;

	public int getType() {
		return 804553;
	}

	public int landid; // landId,根据配置表里的数据,比如40级冰封王座是1,50级冰封王座是2
	public int stage; // 第几步

	public CEnterBingFengLand() {
	}

	public CEnterBingFengLand(int _landid_, int _stage_) {
		this.landid = _landid_;
		this.stage = _stage_;
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
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		landid = _os_.unmarshal_int();
		stage = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CEnterBingFengLand) {
			CEnterBingFengLand _o_ = (CEnterBingFengLand)_o1_;
			if (landid != _o_.landid) return false;
			if (stage != _o_.stage) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += landid;
		_h_ += stage;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(landid).append(",");
		_sb_.append(stage).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CEnterBingFengLand _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = landid - _o_.landid;
		if (0 != _c_) return _c_;
		_c_ = stage - _o_.stage;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

