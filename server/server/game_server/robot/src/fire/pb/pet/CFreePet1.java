
package fire.pb.pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CFreePet1__ extends mkio.Protocol { }

/** 客户端请求放生宠物  (放生成功，服务器会返回客户端删除宠物的消息
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CFreePet1 extends __CFreePet1__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788497;

	public int getType() {
		return 788497;
	}

	public java.util.ArrayList<Integer> petkeys; // 放生的宠物的key

	public CFreePet1() {
		petkeys = new java.util.ArrayList<Integer>();
	}

	public CFreePet1(java.util.ArrayList<Integer> _petkeys_) {
		this.petkeys = _petkeys_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(petkeys.size());
		for (Integer _v_ : petkeys) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			petkeys.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CFreePet1) {
			CFreePet1 _o_ = (CFreePet1)_o1_;
			if (!petkeys.equals(_o_.petkeys)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += petkeys.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(petkeys).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

