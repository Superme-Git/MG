
package fire.pb.friends;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRequestSpaceRoleInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CRequestSpaceRoleInfo extends __CRequestSpaceRoleInfo__ {
	@Override
	protected void process() {
		// protocol handle
		final long currentRoleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (currentRoleId < 0)
			return;

		int curtype = reqtype;
		mkdb.Procedure proc = new mkdb.Procedure() {
			@Override
			public boolean process() {
				if (xtable.Properties.selectRolename(roleid) == null) {
					// 这里存在的原因是:服务器删档了,而客户端本地的最近联系人列表未删除
					Module.logger.warn("[CRequestSpaceRoleInfo] find non-existent role info, roleId:" + currentRoleId
							+ " targetRoleId:" + roleid);
					return false;
				}

				SRequestSpaceRoleInfo send = new SRequestSpaceRoleInfo();
				send.friendinfobean = FriendHelper.toInfoBean(currentRoleId, roleid);
				if (send.friendinfobean.factionid > 0) {
					String clanname = xtable.Clans.selectClanname(send.friendinfobean.factionid);
					if (clanname != null) {
						send.friendinfobean.factionname = clanname;
					}
				}
				send.title = xtable.Properties.selectTitle(roleid);
				send.reqtype = curtype;
				fire.pb.map.Role.getPlayerComponents(roleid, send.components);
				mkdb.Procedure.psendWhileCommit(currentRoleId, send);
				return true;
			}
		};
		proc.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 806538;

	public int getType() {
		return 806538;
	}

	public long roleid;
	public int reqtype;

	public CRequestSpaceRoleInfo() {
	}

	public CRequestSpaceRoleInfo(long _roleid_, int _reqtype_) {
		this.roleid = _roleid_;
		this.reqtype = _reqtype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(reqtype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		reqtype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRequestSpaceRoleInfo) {
			CRequestSpaceRoleInfo _o_ = (CRequestSpaceRoleInfo)_o1_;
			if (roleid != _o_.roleid) return false;
			if (reqtype != _o_.reqtype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += reqtype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(reqtype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CRequestSpaceRoleInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = reqtype - _o_.reqtype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

