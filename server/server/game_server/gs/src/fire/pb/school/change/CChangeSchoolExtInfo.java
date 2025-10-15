
package fire.pb.school.change;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CChangeSchoolExtInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CChangeSchoolExtInfo extends __CChangeSchoolExtInfo__ {
	@Override
	protected void process() {
		final long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId < 0)
			return;

		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				// 发送转职信息
				SChangeSchoolExtInfo send = new SChangeSchoolExtInfo();
				xbean.ChangeSchoolInfo info = xtable.Changeschool.select(roleId);
				if (info != null) {
					int maxChangeWeaponCount = ChangeSchoolUtils.getMaxChangeWeaponCount();
					int maxChangeGemCount = ChangeSchoolUtils.getMaxChangeGemCount();
					send.remainchangeweaponcount = Math.max(maxChangeWeaponCount - info.getChangeweaponcount(), 0);
					send.remainchangegemcount = Math.max(maxChangeGemCount - info.getChangeweaponcount(), 0);
				}
				mkdb.Procedure.psendWhileCommit(roleId, send);
				return true;
			}
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810487;

	public int getType() {
		return 810487;
	}


	public CChangeSchoolExtInfo() {
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
		if (_o1_ instanceof CChangeSchoolExtInfo) {
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

	public int compareTo(CChangeSchoolExtInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

