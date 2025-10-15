
package fire.pb.shop;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SMarketPetTips__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SMarketPetTips extends __SMarketPetTips__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810650;

	public int getType() {
		return 810650;
	}

	public fire.pb.Pet pettips; // 宠物信息
	public int tipstype; // 显示tips类型

	public SMarketPetTips() {
		pettips = new fire.pb.Pet();
	}

	public SMarketPetTips(fire.pb.Pet _pettips_, int _tipstype_) {
		this.pettips = _pettips_;
		this.tipstype = _tipstype_;
	}

	public final boolean _validator_() {
		if (!pettips._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(pettips);
		_os_.marshal(tipstype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		pettips.unmarshal(_os_);
		tipstype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SMarketPetTips) {
			SMarketPetTips _o_ = (SMarketPetTips)_o1_;
			if (!pettips.equals(_o_.pettips)) return false;
			if (tipstype != _o_.tipstype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += pettips.hashCode();
		_h_ += tipstype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(pettips).append(",");
		_sb_.append(tipstype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

