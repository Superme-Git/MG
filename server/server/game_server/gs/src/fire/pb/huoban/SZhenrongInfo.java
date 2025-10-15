
package fire.pb.huoban;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SZhenrongInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SZhenrongInfo extends __SZhenrongInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 818836;

	public int getType() {
		return 818836;
	}

	public int dangqianzhenrong; // 当前阵容编号
	public java.util.HashMap<Integer,fire.pb.huoban.ZhenrongInfo> zhenrongxinxi; // 阵容列表信息

	public SZhenrongInfo() {
		zhenrongxinxi = new java.util.HashMap<Integer,fire.pb.huoban.ZhenrongInfo>();
	}

	public SZhenrongInfo(int _dangqianzhenrong_, java.util.HashMap<Integer,fire.pb.huoban.ZhenrongInfo> _zhenrongxinxi_) {
		this.dangqianzhenrong = _dangqianzhenrong_;
		this.zhenrongxinxi = _zhenrongxinxi_;
	}

	public final boolean _validator_() {
		for (java.util.Map.Entry<Integer, fire.pb.huoban.ZhenrongInfo> _e_ : zhenrongxinxi.entrySet()) {
			if (!_e_.getValue()._validator_()) return false;
		}
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(dangqianzhenrong);
		_os_.compact_uint32(zhenrongxinxi.size());
		for (java.util.Map.Entry<Integer, fire.pb.huoban.ZhenrongInfo> _e_ : zhenrongxinxi.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		dangqianzhenrong = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			fire.pb.huoban.ZhenrongInfo _v_ = new fire.pb.huoban.ZhenrongInfo();
			_v_.unmarshal(_os_);
			zhenrongxinxi.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SZhenrongInfo) {
			SZhenrongInfo _o_ = (SZhenrongInfo)_o1_;
			if (dangqianzhenrong != _o_.dangqianzhenrong) return false;
			if (!zhenrongxinxi.equals(_o_.zhenrongxinxi)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += dangqianzhenrong;
		_h_ += zhenrongxinxi.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(dangqianzhenrong).append(",");
		_sb_.append(zhenrongxinxi).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

