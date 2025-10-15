
package fire.pb.friends;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRequestSpaceRoleInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRequestSpaceRoleInfo extends __SRequestSpaceRoleInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 806539;

	public int getType() {
		return 806539;
	}

	public fire.pb.friends.InfoBean friendinfobean;
	public int title;
	public int reqtype;
	public java.util.HashMap<Byte,Integer> components;

	public SRequestSpaceRoleInfo() {
		friendinfobean = new fire.pb.friends.InfoBean();
		components = new java.util.HashMap<Byte,Integer>();
	}

	public SRequestSpaceRoleInfo(fire.pb.friends.InfoBean _friendinfobean_, int _title_, int _reqtype_, java.util.HashMap<Byte,Integer> _components_) {
		this.friendinfobean = _friendinfobean_;
		this.title = _title_;
		this.reqtype = _reqtype_;
		this.components = _components_;
	}

	public final boolean _validator_() {
		if (!friendinfobean._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(friendinfobean);
		_os_.marshal(title);
		_os_.marshal(reqtype);
		_os_.compact_uint32(components.size());
		for (java.util.Map.Entry<Byte, Integer> _e_ : components.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		friendinfobean.unmarshal(_os_);
		title = _os_.unmarshal_int();
		reqtype = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			byte _k_;
			_k_ = _os_.unmarshal_byte();
			int _v_;
			_v_ = _os_.unmarshal_int();
			components.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRequestSpaceRoleInfo) {
			SRequestSpaceRoleInfo _o_ = (SRequestSpaceRoleInfo)_o1_;
			if (!friendinfobean.equals(_o_.friendinfobean)) return false;
			if (title != _o_.title) return false;
			if (reqtype != _o_.reqtype) return false;
			if (!components.equals(_o_.components)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += friendinfobean.hashCode();
		_h_ += title;
		_h_ += reqtype;
		_h_ += components.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(friendinfobean).append(",");
		_sb_.append(title).append(",");
		_sb_.append(reqtype).append(",");
		_sb_.append(components).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

