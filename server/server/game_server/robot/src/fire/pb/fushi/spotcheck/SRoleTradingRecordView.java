
package fire.pb.fushi.spotcheck;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRoleTradingRecordView__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRoleTradingRecordView extends __SRoleTradingRecordView__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 812642;

	public int getType() {
		return 812642;
	}

	public java.util.ArrayList<fire.pb.fushi.spotcheck.RoleTradingRecord> roletradingrecordlist; // 记录信息

	public SRoleTradingRecordView() {
		roletradingrecordlist = new java.util.ArrayList<fire.pb.fushi.spotcheck.RoleTradingRecord>();
	}

	public SRoleTradingRecordView(java.util.ArrayList<fire.pb.fushi.spotcheck.RoleTradingRecord> _roletradingrecordlist_) {
		this.roletradingrecordlist = _roletradingrecordlist_;
	}

	public final boolean _validator_() {
		for (fire.pb.fushi.spotcheck.RoleTradingRecord _v_ : roletradingrecordlist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(roletradingrecordlist.size());
		for (fire.pb.fushi.spotcheck.RoleTradingRecord _v_ : roletradingrecordlist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.fushi.spotcheck.RoleTradingRecord _v_ = new fire.pb.fushi.spotcheck.RoleTradingRecord();
			_v_.unmarshal(_os_);
			roletradingrecordlist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRoleTradingRecordView) {
			SRoleTradingRecordView _o_ = (SRoleTradingRecordView)_o1_;
			if (!roletradingrecordlist.equals(_o_.roletradingrecordlist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += roletradingrecordlist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roletradingrecordlist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

