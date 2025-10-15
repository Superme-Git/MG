
package fire.pb.gm;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CCheckGM__ extends mkio.Protocol { }

/** 客户端请求，是不是GM帐号
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CCheckGM extends __CCheckGM__ {
	@Override
	protected void process() {
		// protocol handle
		final long gmroleid=gnet.link.Onlines.getInstance().findRoleid(this);
		if (gmroleid < 0)
			return;
		final int userID=((gnet.link.Dispatch)this.getContext()).userid;
		if (!GMInteface.gmPriv(userID)) return;
		final SCheckGM send = new SCheckGM();
		gnet.link.Onlines.getInstance().send(gmroleid, send);
		//GMInteface.doGmOperateLog(userID, "CheckGM");
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 791436;

	public int getType() {
		return 791436;
	}


	public CCheckGM() {
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
		if (_o1_ instanceof CCheckGM) {
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

	public int compareTo(CCheckGM _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

