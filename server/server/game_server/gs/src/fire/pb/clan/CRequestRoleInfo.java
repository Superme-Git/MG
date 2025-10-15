
package fire.pb.clan;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRequestRoleInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CRequestRoleInfo extends __CRequestRoleInfo__ {
	@Override
	protected void process() {
		// protocol handle
		final long selfid = gnet.link.Onlines.getInstance().findRoleid(this);
		if(selfid < 0) {
			return;
		}
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				return fire.pb.clan.srv.ClanBaseManager.getInstance().getRoleInfoDes(selfid,roleid,moduletype);
			}
		}.submit();
		
		
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808502;

	public int getType() {
		return 808502;
	}

	public long roleid; // 角色id
	public int moduletype; // 模块类型

	public CRequestRoleInfo() {
	}

	public CRequestRoleInfo(long _roleid_, int _moduletype_) {
		this.roleid = _roleid_;
		this.moduletype = _moduletype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(moduletype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		moduletype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRequestRoleInfo) {
			CRequestRoleInfo _o_ = (CRequestRoleInfo)_o1_;
			if (roleid != _o_.roleid) return false;
			if (moduletype != _o_.moduletype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += moduletype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(moduletype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CRequestRoleInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = moduletype - _o_.moduletype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

