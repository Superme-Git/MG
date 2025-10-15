
package fire.pb.battle.livedie;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SAcceptLiveDieBattleFirst__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SAcceptLiveDieBattleFirst extends __SAcceptLiveDieBattleFirst__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793849;

	public int getType() {
		return 793849;
	}

	public long hostroleid; // 0表示没有发战书的人，大于0表示有发战书的人
	public java.lang.String hostrolename; // 名称

	public SAcceptLiveDieBattleFirst() {
		hostrolename = "";
	}

	public SAcceptLiveDieBattleFirst(long _hostroleid_, java.lang.String _hostrolename_) {
		this.hostroleid = _hostroleid_;
		this.hostrolename = _hostrolename_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(hostroleid);
		_os_.marshal(hostrolename, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		hostroleid = _os_.unmarshal_long();
		hostrolename = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SAcceptLiveDieBattleFirst) {
			SAcceptLiveDieBattleFirst _o_ = (SAcceptLiveDieBattleFirst)_o1_;
			if (hostroleid != _o_.hostroleid) return false;
			if (!hostrolename.equals(_o_.hostrolename)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)hostroleid;
		_h_ += hostrolename.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(hostroleid).append(",");
		_sb_.append("T").append(hostrolename.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

