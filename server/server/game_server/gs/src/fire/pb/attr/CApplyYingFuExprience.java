
package fire.pb.attr;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CApplyYingFuExprience__ extends mkio.Protocol { }

/** 客户端申请角色盈福经验
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CApplyYingFuExprience extends __CApplyYingFuExprience__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId <= 0)
			return;
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				fire.pb.mission.activelist.RoleLiveness activeRole = fire.pb.mission.activelist.RoleLiveness.getRoleLiveness(roleId, false);
				SApplyYingFuExprience send = new SApplyYingFuExprience(activeRole.getYingFuExp());
				mkdb.Procedure.psendWhileCommit(roleId, send);
				return true;
			}
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 799438;

	public int getType() {
		return 799438;
	}


	public CApplyYingFuExprience() {
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CApplyYingFuExprience) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CApplyYingFuExprience _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

