
package fire.pb.team.teammelon;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __STeamRollMelonInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class STeamRollMelonInfo extends __STeamRollMelonInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794524;

	public int getType() {
		return 794524;
	}

	public long melonid;
	public java.util.LinkedList<fire.pb.team.teammelon.RoleRollInfo> rollinfolist; // 每人的ROLL点信息 by changhao
	public long grabroleid; // 谁获得了道具如果是0就是大家都放弃了 by changhao
	public java.lang.String grabrolename; // 名字 by changhao
	public java.util.LinkedList<fire.pb.team.teammelon.MelonItemBagInfo> melonitemlist; // 战力品详细信息 by changaho

	public STeamRollMelonInfo() {
		rollinfolist = new java.util.LinkedList<fire.pb.team.teammelon.RoleRollInfo>();
		grabrolename = "";
		melonitemlist = new java.util.LinkedList<fire.pb.team.teammelon.MelonItemBagInfo>();
	}

	public STeamRollMelonInfo(long _melonid_, java.util.LinkedList<fire.pb.team.teammelon.RoleRollInfo> _rollinfolist_, long _grabroleid_, java.lang.String _grabrolename_, java.util.LinkedList<fire.pb.team.teammelon.MelonItemBagInfo> _melonitemlist_) {
		this.melonid = _melonid_;
		this.rollinfolist = _rollinfolist_;
		this.grabroleid = _grabroleid_;
		this.grabrolename = _grabrolename_;
		this.melonitemlist = _melonitemlist_;
	}

	public final boolean _validator_() {
		for (fire.pb.team.teammelon.RoleRollInfo _v_ : rollinfolist)
			if (!_v_._validator_()) return false;
		for (fire.pb.team.teammelon.MelonItemBagInfo _v_ : melonitemlist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(melonid);
		_os_.compact_uint32(rollinfolist.size());
		for (fire.pb.team.teammelon.RoleRollInfo _v_ : rollinfolist) {
			_os_.marshal(_v_);
		}
		_os_.marshal(grabroleid);
		_os_.marshal(grabrolename, "UTF-16LE");
		_os_.compact_uint32(melonitemlist.size());
		for (fire.pb.team.teammelon.MelonItemBagInfo _v_ : melonitemlist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		melonid = _os_.unmarshal_long();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.team.teammelon.RoleRollInfo _v_ = new fire.pb.team.teammelon.RoleRollInfo();
			_v_.unmarshal(_os_);
			rollinfolist.add(_v_);
		}
		grabroleid = _os_.unmarshal_long();
		grabrolename = _os_.unmarshal_String("UTF-16LE");
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.team.teammelon.MelonItemBagInfo _v_ = new fire.pb.team.teammelon.MelonItemBagInfo();
			_v_.unmarshal(_os_);
			melonitemlist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof STeamRollMelonInfo) {
			STeamRollMelonInfo _o_ = (STeamRollMelonInfo)_o1_;
			if (melonid != _o_.melonid) return false;
			if (!rollinfolist.equals(_o_.rollinfolist)) return false;
			if (grabroleid != _o_.grabroleid) return false;
			if (!grabrolename.equals(_o_.grabrolename)) return false;
			if (!melonitemlist.equals(_o_.melonitemlist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)melonid;
		_h_ += rollinfolist.hashCode();
		_h_ += (int)grabroleid;
		_h_ += grabrolename.hashCode();
		_h_ += melonitemlist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(melonid).append(",");
		_sb_.append(rollinfolist).append(",");
		_sb_.append(grabroleid).append(",");
		_sb_.append("T").append(grabrolename.length()).append(",");
		_sb_.append(melonitemlist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

