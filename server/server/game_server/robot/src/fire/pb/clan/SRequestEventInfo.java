
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRequestEventInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRequestEventInfo extends __SRequestEventInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808501;

	public int getType() {
		return 808501;
	}

	public java.util.ArrayList<fire.pb.clan.ClanEventInfo> eventlist;

	public SRequestEventInfo() {
		eventlist = new java.util.ArrayList<fire.pb.clan.ClanEventInfo>();
	}

	public SRequestEventInfo(java.util.ArrayList<fire.pb.clan.ClanEventInfo> _eventlist_) {
		this.eventlist = _eventlist_;
	}

	public final boolean _validator_() {
		for (fire.pb.clan.ClanEventInfo _v_ : eventlist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(eventlist.size());
		for (fire.pb.clan.ClanEventInfo _v_ : eventlist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.clan.ClanEventInfo _v_ = new fire.pb.clan.ClanEventInfo();
			_v_.unmarshal(_os_);
			eventlist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRequestEventInfo) {
			SRequestEventInfo _o_ = (SRequestEventInfo)_o1_;
			if (!eventlist.equals(_o_.eventlist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += eventlist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(eventlist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

