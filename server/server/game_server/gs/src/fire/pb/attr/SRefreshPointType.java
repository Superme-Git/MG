
package fire.pb.attr;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRefreshPointType__ extends mkio.Protocol { }

/** 刷新人物加点后的加点面板数值
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRefreshPointType extends __SRefreshPointType__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 799435;

	public int getType() {
		return 799435;
	}

	public fire.pb.attr.RoleBasicFightProperties bfp;
	public java.util.HashMap<Integer,Integer> point; // 潜能
	public int pointscheme; // 当前加点方案
	public int schemechanges; // 方案切换次数

	public SRefreshPointType() {
		bfp = new fire.pb.attr.RoleBasicFightProperties();
		point = new java.util.HashMap<Integer,Integer>();
	}

	public SRefreshPointType(fire.pb.attr.RoleBasicFightProperties _bfp_, java.util.HashMap<Integer,Integer> _point_, int _pointscheme_, int _schemechanges_) {
		this.bfp = _bfp_;
		this.point = _point_;
		this.pointscheme = _pointscheme_;
		this.schemechanges = _schemechanges_;
	}

	public final boolean _validator_() {
		if (!bfp._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(bfp);
		_os_.compact_uint32(point.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : point.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(pointscheme);
		_os_.marshal(schemechanges);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		bfp.unmarshal(_os_);
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			point.put(_k_, _v_);
		}
		pointscheme = _os_.unmarshal_int();
		schemechanges = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRefreshPointType) {
			SRefreshPointType _o_ = (SRefreshPointType)_o1_;
			if (!bfp.equals(_o_.bfp)) return false;
			if (!point.equals(_o_.point)) return false;
			if (pointscheme != _o_.pointscheme) return false;
			if (schemechanges != _o_.schemechanges) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += bfp.hashCode();
		_h_ += point.hashCode();
		_h_ += pointscheme;
		_h_ += schemechanges;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(bfp).append(",");
		_sb_.append(point).append(",");
		_sb_.append(pointscheme).append(",");
		_sb_.append(schemechanges).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

