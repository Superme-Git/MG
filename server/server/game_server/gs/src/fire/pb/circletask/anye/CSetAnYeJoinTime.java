
package fire.pb.circletask.anye;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CSetAnYeJoinTime__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CSetAnYeJoinTime extends __CSetAnYeJoinTime__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				RoleAnYeTask rayt = new RoleAnYeTask(roleid);
				rayt.setAnYeJoinTime();
				rayt.refreshBasicAnYeData();
				return true;
			}
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 807457;

	public int getType() {
		return 807457;
	}

	public long jointime; // 首次参加时间

	public CSetAnYeJoinTime() {
	}

	public CSetAnYeJoinTime(long _jointime_) {
		this.jointime = _jointime_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(jointime);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		jointime = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CSetAnYeJoinTime) {
			CSetAnYeJoinTime _o_ = (CSetAnYeJoinTime)_o1_;
			if (jointime != _o_.jointime) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)jointime;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(jointime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CSetAnYeJoinTime _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(jointime - _o_.jointime);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

