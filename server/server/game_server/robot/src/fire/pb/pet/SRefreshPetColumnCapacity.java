
package fire.pb.pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRefreshPetColumnCapacity__ extends mkio.Protocol { }

/** 服务器向客户端刷新宠物栏的最大容量
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRefreshPetColumnCapacity extends __SRefreshPetColumnCapacity__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788458;

	public int getType() {
		return 788458;
	}

	public int columnid;
	public int capacity;

	public SRefreshPetColumnCapacity() {
	}

	public SRefreshPetColumnCapacity(int _columnid_, int _capacity_) {
		this.columnid = _columnid_;
		this.capacity = _capacity_;
	}

	public final boolean _validator_() {
		if (columnid < 1 || columnid > 2) return false;
		if (capacity < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(columnid);
		_os_.marshal(capacity);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		columnid = _os_.unmarshal_int();
		capacity = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRefreshPetColumnCapacity) {
			SRefreshPetColumnCapacity _o_ = (SRefreshPetColumnCapacity)_o1_;
			if (columnid != _o_.columnid) return false;
			if (capacity != _o_.capacity) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += columnid;
		_h_ += capacity;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(columnid).append(",");
		_sb_.append(capacity).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SRefreshPetColumnCapacity _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = columnid - _o_.columnid;
		if (0 != _c_) return _c_;
		_c_ = capacity - _o_.capacity;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

