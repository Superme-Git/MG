
package fire.pb.school.change;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SOldSchoolList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SOldSchoolList extends __SOldSchoolList__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810484;

	public int getType() {
		return 810484;
	}

	public java.util.ArrayList<Integer> oldshapelist; // 旧造型列表
	public java.util.ArrayList<Integer> oldschoollist; // 旧职业列表

	public SOldSchoolList() {
		oldshapelist = new java.util.ArrayList<Integer>();
		oldschoollist = new java.util.ArrayList<Integer>();
	}

	public SOldSchoolList(java.util.ArrayList<Integer> _oldshapelist_, java.util.ArrayList<Integer> _oldschoollist_) {
		this.oldshapelist = _oldshapelist_;
		this.oldschoollist = _oldschoollist_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(oldshapelist.size());
		for (Integer _v_ : oldshapelist) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(oldschoollist.size());
		for (Integer _v_ : oldschoollist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			oldshapelist.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			oldschoollist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SOldSchoolList) {
			SOldSchoolList _o_ = (SOldSchoolList)_o1_;
			if (!oldshapelist.equals(_o_.oldshapelist)) return false;
			if (!oldschoollist.equals(_o_.oldschoollist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += oldshapelist.hashCode();
		_h_ += oldschoollist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(oldshapelist).append(",");
		_sb_.append(oldschoollist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

