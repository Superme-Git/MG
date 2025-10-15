
package fire.pb.mission.activelist;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CShareActivity__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CShareActivity extends __CShareActivity__ {
	@Override
	protected void process() {
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				RoleLiveness actrole = RoleLiveness.getRoleLiveness(roleid);
				if (actrole != null){
					if(activityid == RoleLiveness.SHARE)
						actrole.handleActiveLivenessData(RoleLiveness.SHARE);
					else if(activityid == RoleLiveness.SHARE2)
						actrole.handleActiveLivenessData(RoleLiveness.SHARE2);
					else {
						RoleLivenessManager.logger.error("角色[" + roleid + "]准备处理分享活动[" + activityid + "]没有配置！当分享1活动处理.");
						if(activityid > 0)
							actrole.handleActiveLivenessData(activityid);
					}
				}
				return true;
			}
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 805489;

	public int getType() {
		return 805489;
	}

	public int activityid;

	public CShareActivity() {
	}

	public CShareActivity(int _activityid_) {
		this.activityid = _activityid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(activityid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		activityid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CShareActivity) {
			CShareActivity _o_ = (CShareActivity)_o1_;
			if (activityid != _o_.activityid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += activityid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(activityid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CShareActivity _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = activityid - _o_.activityid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

