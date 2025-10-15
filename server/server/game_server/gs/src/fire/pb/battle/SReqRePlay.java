
package fire.pb.battle;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SReqRePlay__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SReqRePlay extends __SReqRePlay__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793736;

	public int getType() {
		return 793736;
	}

	public byte candoit; // 0无, 1有
	public java.lang.String battlecameraurl;

	public SReqRePlay() {
		battlecameraurl = "";
	}

	public SReqRePlay(byte _candoit_, java.lang.String _battlecameraurl_) {
		this.candoit = _candoit_;
		this.battlecameraurl = _battlecameraurl_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(candoit);
		_os_.marshal(battlecameraurl, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		candoit = _os_.unmarshal_byte();
		battlecameraurl = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SReqRePlay) {
			SReqRePlay _o_ = (SReqRePlay)_o1_;
			if (candoit != _o_.candoit) return false;
			if (!battlecameraurl.equals(_o_.battlecameraurl)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)candoit;
		_h_ += battlecameraurl.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(candoit).append(",");
		_sb_.append("T").append(battlecameraurl.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

