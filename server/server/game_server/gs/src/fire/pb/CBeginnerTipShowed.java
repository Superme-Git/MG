
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CBeginnerTipShowed__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CBeginnerTipShowed extends __CBeginnerTipShowed__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleid = gnet.link.Onlines.getInstance().findRoleid( this );
		if ( roleid < 0 )
			return;
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				xbean.BeginnerTip bt = xtable.Beginnertip.get( roleid );
				if ( bt == null ) {
					bt = xbean.Pod.newBeginnerTip();
					xtable.Beginnertip.insert( roleid, bt );
				}
				bt.getTips().put( tipid, 1 );
				return true;
			}
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786459;

	public int getType() {
		return 786459;
	}

	public int tipid; // id为BeginnerTipType

	public CBeginnerTipShowed() {
	}

	public CBeginnerTipShowed(int _tipid_) {
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
		if (_o1_ instanceof CBeginnerTipShowed) {
			CBeginnerTipShowed _o_ = (CBeginnerTipShowed)_o1_;
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

	public int compareTo(CBeginnerTipShowed _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = tipid - _o_.tipid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

