
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRequestApply__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRequestApply extends __SRequestApply__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808456;

	public int getType() {
		return 808456;
	}

	public java.util.ArrayList<fire.pb.clan.RoleBaseInfo> applylist; // 申请列表

	public SRequestApply() {
		applylist = new java.util.ArrayList<fire.pb.clan.RoleBaseInfo>();
	}

	public SRequestApply(java.util.ArrayList<fire.pb.clan.RoleBaseInfo> _applylist_) {
		this.applylist = _applylist_;
	}

	public final boolean _validator_() {
		for (fire.pb.clan.RoleBaseInfo _v_ : applylist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(applylist.size());
		for (fire.pb.clan.RoleBaseInfo _v_ : applylist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.clan.RoleBaseInfo _v_ = new fire.pb.clan.RoleBaseInfo();
			_v_.unmarshal(_os_);
			applylist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRequestApply) {
			SRequestApply _o_ = (SRequestApply)_o1_;
			if (!applylist.equals(_o_.applylist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += applylist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(applylist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

