
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRequestRolePos__ extends mkio.Protocol { }

/** 客户端使用千里寻踪蝶请求玩家坐标
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CRequestRolePos extends __CRequestRolePos__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786487;

	public int getType() {
		return 786487;
	}

	public final static int BYNAME = 1; // 按名字查找
	public final static int BYID = 2; // 按id查找

	public long roleid; // 玩家id
	public java.lang.String rolename; // 玩家名
	public byte searchtype; // 查找类型

	public CRequestRolePos() {
		rolename = "";
	}

	public CRequestRolePos(long _roleid_, java.lang.String _rolename_, byte _searchtype_) {
		this.roleid = _roleid_;
		this.rolename = _rolename_;
		this.searchtype = _searchtype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(searchtype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String("UTF-16LE");
		searchtype = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRequestRolePos) {
			CRequestRolePos _o_ = (CRequestRolePos)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (searchtype != _o_.searchtype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += rolename.hashCode();
		_h_ += (int)searchtype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(searchtype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

