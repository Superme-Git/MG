
package fire.pb.master;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SReceiveNewPrentice__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SReceiveNewPrentice extends __SReceiveNewPrentice__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 816457;

	public int getType() {
		return 816457;
	}

	public long prenticeid; // 徒弟id
	public java.lang.String prenticename; // 徒弟名字

	public SReceiveNewPrentice() {
		prenticename = "";
	}

	public SReceiveNewPrentice(long _prenticeid_, java.lang.String _prenticename_) {
		this.prenticeid = _prenticeid_;
		this.prenticename = _prenticename_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(prenticeid);
		_os_.marshal(prenticename, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		prenticeid = _os_.unmarshal_long();
		prenticename = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SReceiveNewPrentice) {
			SReceiveNewPrentice _o_ = (SReceiveNewPrentice)_o1_;
			if (prenticeid != _o_.prenticeid) return false;
			if (!prenticename.equals(_o_.prenticename)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)prenticeid;
		_h_ += prenticename.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(prenticeid).append(",");
		_sb_.append("T").append(prenticename.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

