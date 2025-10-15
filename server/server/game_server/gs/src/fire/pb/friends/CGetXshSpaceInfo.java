package fire.pb.friends;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CGetXshSpaceInfo__ extends mkio.Protocol { }

/** 获取说不得大师空间数据
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}
public class CGetXshSpaceInfo extends __CGetXshSpaceInfo__ {

	@Override
	protected void process() {
		// protocol handle
		final long cur_roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (cur_roleid < 0)
			return;
		xbean.RoleSpace rs = xtable.Rolespaces.select(XSHUtils.SXH_ROLE_ID);
		SGetXshSpaceInfo snd = new SGetXshSpaceInfo();
		if (rs != null) {
			snd.giftnum = rs.getGift();
			snd.popularity = rs.getPopularity();
			snd.revnum = rs.getRecvgift();
		} else {
			snd.giftnum = 0;
			snd.popularity = 0;
			snd.revnum = 0;
		}
		gnet.link.Onlines.getInstance().send(cur_roleid, snd);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 806651;

	public int getType() {
		return 806651;
	}


	public CGetXshSpaceInfo() {
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
		if (_o1_ instanceof CGetXshSpaceInfo) {
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

	public int compareTo(CGetXshSpaceInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}
}
