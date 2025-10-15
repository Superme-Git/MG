
package gnet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __QueryUserid2Req__ extends mkio.Protocol { }

/** 根据角色名查角色信息,主要用于网站或客服发奖，首先通过角色名查询角色及账号是否存在，查询成功才发奖
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class QueryUserid2Req extends __QueryUserid2Req__ {
	@Override
	protected void process() {
		// protocol handle
		Long roleId = xtable.Rolename2key.select(rolename.getString("UTF-16LE"));
		if(roleId == null)
		{
			new QueryUserid2Rep(xid, 43, 0, 0, 0).send(getManager());
			return;
		}
		xbean.Properties prop = xtable.Properties.select(roleId);
		if(prop == null)
		{
			new QueryUserid2Rep(xid, 43, 0, 0, 0).send(getManager());
			return;
		}
		new QueryUserid2Rep(xid, 0, prop.getUserid(), roleId, prop.getLevel()).send(getManager());
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 212;

	public int getType() {
		return 212;
	}

	public int xid;
	public com.locojoy.base.Octets rolename;

	public QueryUserid2Req() {
		rolename = new com.locojoy.base.Octets();
	}

	public QueryUserid2Req(int _xid_, com.locojoy.base.Octets _rolename_) {
		this.xid = _xid_;
		this.rolename = _rolename_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(xid);
		_os_.marshal(rolename);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		xid = _os_.unmarshal_int();
		rolename = _os_.unmarshal_Octets();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof QueryUserid2Req) {
			QueryUserid2Req _o_ = (QueryUserid2Req)_o1_;
			if (xid != _o_.xid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += xid;
		_h_ += rolename.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(xid).append(",");
		_sb_.append("B").append(rolename.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

