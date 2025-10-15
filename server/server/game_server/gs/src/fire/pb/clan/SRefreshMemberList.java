
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRefreshMemberList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRefreshMemberList extends __SRefreshMemberList__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808493;

	public int getType() {
		return 808493;
	}

	public java.util.ArrayList<fire.pb.clan.ClanMember> memberlist; // 公会成员列表

	public SRefreshMemberList() {
		memberlist = new java.util.ArrayList<fire.pb.clan.ClanMember>();
	}

	public SRefreshMemberList(java.util.ArrayList<fire.pb.clan.ClanMember> _memberlist_) {
		this.memberlist = _memberlist_;
	}

	public final boolean _validator_() {
		for (fire.pb.clan.ClanMember _v_ : memberlist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(memberlist.size());
		for (fire.pb.clan.ClanMember _v_ : memberlist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.clan.ClanMember _v_ = new fire.pb.clan.ClanMember();
			_v_.unmarshal(_os_);
			memberlist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRefreshMemberList) {
			SRefreshMemberList _o_ = (SRefreshMemberList)_o1_;
			if (!memberlist.equals(_o_.memberlist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += memberlist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(memberlist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

