
package fire.pb.master;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SInitPrenticeList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SInitPrenticeList extends __SInitPrenticeList__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 816461;

	public int getType() {
		return 816461;
	}

	public java.util.ArrayList<fire.pb.master.RoleBaseData> prenticelist;

	public SInitPrenticeList() {
		prenticelist = new java.util.ArrayList<fire.pb.master.RoleBaseData>();
	}

	public SInitPrenticeList(java.util.ArrayList<fire.pb.master.RoleBaseData> _prenticelist_) {
		this.prenticelist = _prenticelist_;
	}

	public final boolean _validator_() {
		for (fire.pb.master.RoleBaseData _v_ : prenticelist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(prenticelist.size());
		for (fire.pb.master.RoleBaseData _v_ : prenticelist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.master.RoleBaseData _v_ = new fire.pb.master.RoleBaseData();
			_v_.unmarshal(_os_);
			prenticelist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SInitPrenticeList) {
			SInitPrenticeList _o_ = (SInitPrenticeList)_o1_;
			if (!prenticelist.equals(_o_.prenticelist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += prenticelist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(prenticelist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

