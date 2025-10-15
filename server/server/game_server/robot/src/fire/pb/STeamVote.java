
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __STeamVote__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class STeamVote extends __STeamVote__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786524;

	public int getType() {
		return 786524;
	}

	public int flag; // 2=pvp地图提醒
	public java.util.LinkedList<java.lang.String> parms;

	public STeamVote() {
		parms = new java.util.LinkedList<java.lang.String>();
	}

	public STeamVote(int _flag_, java.util.LinkedList<java.lang.String> _parms_) {
		this.flag = _flag_;
		this.parms = _parms_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(flag);
		_os_.compact_uint32(parms.size());
		for (java.lang.String _v_ : parms) {
			_os_.marshal(_v_, "UTF-16LE");
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		flag = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			java.lang.String _v_;
			_v_ = _os_.unmarshal_String("UTF-16LE");
			parms.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof STeamVote) {
			STeamVote _o_ = (STeamVote)_o1_;
			if (flag != _o_.flag) return false;
			if (!parms.equals(_o_.parms)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += flag;
		_h_ += parms.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(flag).append(",");
		_sb_.append(parms).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

