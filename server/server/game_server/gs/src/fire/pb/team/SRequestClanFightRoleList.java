
package fire.pb.team;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRequestClanFightRoleList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRequestClanFightRoleList extends __SRequestClanFightRoleList__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794560;

	public int getType() {
		return 794560;
	}

	public int isfresh; // 客户端用 0刷新 1不刷新  by qyl
	public java.util.LinkedList<fire.pb.team.RoleSimapleInfo> rolelist; // 一个个人简单信息 by changhao
	public int ret; // 如果是0正常-1就是没数据了 b changhao

	public SRequestClanFightRoleList() {
		rolelist = new java.util.LinkedList<fire.pb.team.RoleSimapleInfo>();
	}

	public SRequestClanFightRoleList(int _isfresh_, java.util.LinkedList<fire.pb.team.RoleSimapleInfo> _rolelist_, int _ret_) {
		this.isfresh = _isfresh_;
		this.rolelist = _rolelist_;
		this.ret = _ret_;
	}

	public final boolean _validator_() {
		for (fire.pb.team.RoleSimapleInfo _v_ : rolelist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(isfresh);
		_os_.compact_uint32(rolelist.size());
		for (fire.pb.team.RoleSimapleInfo _v_ : rolelist) {
			_os_.marshal(_v_);
		}
		_os_.marshal(ret);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		isfresh = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.team.RoleSimapleInfo _v_ = new fire.pb.team.RoleSimapleInfo();
			_v_.unmarshal(_os_);
			rolelist.add(_v_);
		}
		ret = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRequestClanFightRoleList) {
			SRequestClanFightRoleList _o_ = (SRequestClanFightRoleList)_o1_;
			if (isfresh != _o_.isfresh) return false;
			if (!rolelist.equals(_o_.rolelist)) return false;
			if (ret != _o_.ret) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += isfresh;
		_h_ += rolelist.hashCode();
		_h_ += ret;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(isfresh).append(",");
		_sb_.append(rolelist).append(",");
		_sb_.append(ret).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

