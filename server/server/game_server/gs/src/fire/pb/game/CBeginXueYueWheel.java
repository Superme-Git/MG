
package fire.pb.game;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CBeginXueYueWheel__ extends mkio.Protocol { }

/** 血月商人
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CBeginXueYueWheel extends __CBeginXueYueWheel__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid <= 0) return;
		new PBeginOpenTraderBox(roleid, boxtype, npckey).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810366;

	public int getType() {
		return 810366;
	}

	public long npckey; // npckey为npc的唯一ID
	public int boxtype;

	public CBeginXueYueWheel() {
	}

	public CBeginXueYueWheel(long _npckey_, int _boxtype_) {
		this.npckey = _npckey_;
		this.boxtype = _boxtype_;
	}

	public final boolean _validator_() {
		if (npckey < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(npckey);
		_os_.marshal(boxtype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npckey = _os_.unmarshal_long();
		boxtype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CBeginXueYueWheel) {
			CBeginXueYueWheel _o_ = (CBeginXueYueWheel)_o1_;
			if (npckey != _o_.npckey) return false;
			if (boxtype != _o_.boxtype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)npckey;
		_h_ += boxtype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npckey).append(",");
		_sb_.append(boxtype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CBeginXueYueWheel _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		_c_ = boxtype - _o_.boxtype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

