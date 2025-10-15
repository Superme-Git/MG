
package fire.pb.fushi;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CConfirmCharge__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CConfirmCharge extends __CConfirmCharge__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 812456;

	public int getType() {
		return 812456;
	}

	public int goodid;
	public int goodnum;
	public java.lang.String extra;

	public CConfirmCharge() {
		extra = "";
	}

	public CConfirmCharge(int _goodid_, int _goodnum_, java.lang.String _extra_) {
		this.goodid = _goodid_;
		this.goodnum = _goodnum_;
		this.extra = _extra_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(goodid);
		_os_.marshal(goodnum);
		_os_.marshal(extra, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		goodid = _os_.unmarshal_int();
		goodnum = _os_.unmarshal_int();
		extra = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CConfirmCharge) {
			CConfirmCharge _o_ = (CConfirmCharge)_o1_;
			if (goodid != _o_.goodid) return false;
			if (goodnum != _o_.goodnum) return false;
			if (!extra.equals(_o_.extra)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += goodid;
		_h_ += goodnum;
		_h_ += extra.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(goodid).append(",");
		_sb_.append(goodnum).append(",");
		_sb_.append("T").append(extra.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

