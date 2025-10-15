
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SReturnRoleList__ extends mkio.Protocol { }

/** 服务器发给客户端，允许返回角色选择界面，并发送已有角色信息列表，同SRoleList
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SReturnRoleList extends __SReturnRoleList__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786478;

	public int getType() {
		return 786478;
	}

	public long prevloginroleid;
	public byte prevroleinbattle; // 1 = 在战斗托管中；0 = 不在游戏中
	public java.util.LinkedList<fire.pb.RoleInfo> roles;
	public int gacdon; // gacd是否开启,1为开启,0为关闭

	public SReturnRoleList() {
		roles = new java.util.LinkedList<fire.pb.RoleInfo>();
	}

	public SReturnRoleList(long _prevloginroleid_, byte _prevroleinbattle_, java.util.LinkedList<fire.pb.RoleInfo> _roles_, int _gacdon_) {
		this.prevloginroleid = _prevloginroleid_;
		this.prevroleinbattle = _prevroleinbattle_;
		this.roles = _roles_;
		this.gacdon = _gacdon_;
	}

	public final boolean _validator_() {
		for (fire.pb.RoleInfo _v_ : roles)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(prevloginroleid);
		_os_.marshal(prevroleinbattle);
		_os_.compact_uint32(roles.size());
		for (fire.pb.RoleInfo _v_ : roles) {
			_os_.marshal(_v_);
		}
		_os_.marshal(gacdon);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		prevloginroleid = _os_.unmarshal_long();
		prevroleinbattle = _os_.unmarshal_byte();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.RoleInfo _v_ = new fire.pb.RoleInfo();
			_v_.unmarshal(_os_);
			roles.add(_v_);
		}
		gacdon = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SReturnRoleList) {
			SReturnRoleList _o_ = (SReturnRoleList)_o1_;
			if (prevloginroleid != _o_.prevloginroleid) return false;
			if (prevroleinbattle != _o_.prevroleinbattle) return false;
			if (!roles.equals(_o_.roles)) return false;
			if (gacdon != _o_.gacdon) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)prevloginroleid;
		_h_ += (int)prevroleinbattle;
		_h_ += roles.hashCode();
		_h_ += gacdon;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(prevloginroleid).append(",");
		_sb_.append(prevroleinbattle).append(",");
		_sb_.append(roles).append(",");
		_sb_.append(gacdon).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

