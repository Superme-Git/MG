
package fire.pb.fushi.redpack;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SNoticeRedPackList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SNoticeRedPackList extends __SNoticeRedPackList__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 812543;

	public int getType() {
		return 812543;
	}

	public java.util.ArrayList<fire.pb.fushi.redpack.RedPackRoleTip> redpackroletiplist; // 红包信息

	public SNoticeRedPackList() {
		redpackroletiplist = new java.util.ArrayList<fire.pb.fushi.redpack.RedPackRoleTip>();
	}

	public SNoticeRedPackList(java.util.ArrayList<fire.pb.fushi.redpack.RedPackRoleTip> _redpackroletiplist_) {
		this.redpackroletiplist = _redpackroletiplist_;
	}

	public final boolean _validator_() {
		for (fire.pb.fushi.redpack.RedPackRoleTip _v_ : redpackroletiplist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(redpackroletiplist.size());
		for (fire.pb.fushi.redpack.RedPackRoleTip _v_ : redpackroletiplist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.fushi.redpack.RedPackRoleTip _v_ = new fire.pb.fushi.redpack.RedPackRoleTip();
			_v_.unmarshal(_os_);
			redpackroletiplist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SNoticeRedPackList) {
			SNoticeRedPackList _o_ = (SNoticeRedPackList)_o1_;
			if (!redpackroletiplist.equals(_o_.redpackroletiplist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += redpackroletiplist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(redpackroletiplist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

