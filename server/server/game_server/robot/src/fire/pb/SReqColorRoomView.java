
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SReqColorRoomView__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SReqColorRoomView extends __SReqColorRoomView__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786535;

	public int getType() {
		return 786535;
	}

	public int nummax; // 最大空间
	public java.util.LinkedList<fire.pb.RoleColorType> rolecolortypelist; // 染色方案列表

	public SReqColorRoomView() {
		rolecolortypelist = new java.util.LinkedList<fire.pb.RoleColorType>();
	}

	public SReqColorRoomView(int _nummax_, java.util.LinkedList<fire.pb.RoleColorType> _rolecolortypelist_) {
		this.nummax = _nummax_;
		this.rolecolortypelist = _rolecolortypelist_;
	}

	public final boolean _validator_() {
		for (fire.pb.RoleColorType _v_ : rolecolortypelist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(nummax);
		_os_.compact_uint32(rolecolortypelist.size());
		for (fire.pb.RoleColorType _v_ : rolecolortypelist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		nummax = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.RoleColorType _v_ = new fire.pb.RoleColorType();
			_v_.unmarshal(_os_);
			rolecolortypelist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SReqColorRoomView) {
			SReqColorRoomView _o_ = (SReqColorRoomView)_o1_;
			if (nummax != _o_.nummax) return false;
			if (!rolecolortypelist.equals(_o_.rolecolortypelist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += nummax;
		_h_ += rolecolortypelist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(nummax).append(",");
		_sb_.append(rolecolortypelist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

