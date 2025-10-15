
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CGetMulDayLoginGift__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CGetMulDayLoginGift extends __CGetMulDayLoginGift__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787732;

	public int getType() {
		return 787732;
	}

	public byte rewardid; // 七日登录礼包ID

	public CGetMulDayLoginGift() {
	}

	public CGetMulDayLoginGift(byte _rewardid_) {
		this.rewardid = _rewardid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(rewardid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		rewardid = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CGetMulDayLoginGift) {
			CGetMulDayLoginGift _o_ = (CGetMulDayLoginGift)_o1_;
			if (rewardid != _o_.rewardid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)rewardid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(rewardid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CGetMulDayLoginGift _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = rewardid - _o_.rewardid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

