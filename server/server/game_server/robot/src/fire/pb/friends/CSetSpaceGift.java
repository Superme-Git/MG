
package fire.pb.friends;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CSetSpaceGift__ extends mkio.Protocol { }

/** 放置角色空间礼物
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CSetSpaceGift extends __CSetSpaceGift__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 806641;

	public int getType() {
		return 806641;
	}

	public int giftnum; // 放置的礼物数量

	public CSetSpaceGift() {
	}

	public CSetSpaceGift(int _giftnum_) {
		this.giftnum = _giftnum_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(giftnum);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		giftnum = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CSetSpaceGift) {
			CSetSpaceGift _o_ = (CSetSpaceGift)_o1_;
			if (giftnum != _o_.giftnum) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += giftnum;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(giftnum).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CSetSpaceGift _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = giftnum - _o_.giftnum;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

