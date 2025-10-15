
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CBeginnerTip__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CBeginnerTip extends __CBeginnerTip__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleid = gnet.link.Onlines.getInstance().findRoleid( this );
		if ( roleid < 0 )
			return;
		SBeginnerTip send = new SBeginnerTip();
		send.tipid = tipid;
		xbean.BeginnerTip tip = xtable.Beginnertip.select( roleid );
		if ( tip == null ) {
			send.tipvalue = 0;
		} else {
			final Integer value = tip.getTips().get( tipid );
			if ( value == null )
				send.tipvalue = 0;
			else
				send.tipvalue = value.intValue();
		}
		gnet.link.Onlines.getInstance().send( roleid, send );
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786457;

	public int getType() {
		return 786457;
	}

	public int tipid; // id为BeginnerTipType

	public CBeginnerTip() {
	}

	public CBeginnerTip(int _tipid_) {
		this.tipid = _tipid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(tipid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		tipid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CBeginnerTip) {
			CBeginnerTip _o_ = (CBeginnerTip)_o1_;
			if (tipid != _o_.tipid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += tipid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(tipid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CBeginnerTip _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = tipid - _o_.tipid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

